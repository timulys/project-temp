package com.kep.portal.scheduler;

import com.kep.core.model.dto.platform.PlatformTemplateStatus;
import com.kep.core.model.dto.platform.kakao.KakaoBizMessageTemplatePayload;
import com.kep.core.model.dto.platform.kakao.KakaoBizTemplateResponse;
import com.kep.core.model.dto.platform.kakao.vo.Comment;
import com.kep.portal.client.PlatformClient;
import com.kep.portal.config.property.PortalProperty;
import com.kep.portal.model.entity.platform.PlatformTemplate;
import com.kep.portal.model.entity.platform.PlatformTemplateRejectHistory;
import com.kep.portal.repository.platform.PlatformTemplateRejectHistoryRepository;
import com.kep.portal.repository.platform.PlatformTemplateRepository;
import lombok.extern.slf4j.Slf4j;
import net.javacrumbs.shedlock.spring.annotation.SchedulerLock;
import org.springframework.context.annotation.Profile;
import org.springframework.data.domain.*;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;

import javax.annotation.Resource;
import java.time.ZonedDateTime;
import java.util.Collections;
import java.util.List;
import java.util.stream.Stream;

/**
 * 플랫폼 템플릿 상태 동기화 배치(템플릿 상태 변경 처리)
 */
@Service
@Transactional
@Slf4j
@Profile(value = {"stg", "dev", "cc-dev", "kc-dev","kc-dev-bnk","kc-dev-cluster","local"})
public class SyncPlatformTemplateStatusJob {
	private static final String STATUS_APPROVE = "APR";
	private static final String RESPONSE_SUCCESS_CODE = "200";
	private static final String RESPONSE_DELETE_CODE = "508"; // 데이터 없음
	private static final String JOB_NAME = "SYNC PLATFORM TEMPLATE STATUS";

	@Resource
	private PlatformTemplateRepository platformTemplateRepository;

	@Resource
	private PlatformTemplateRejectHistoryRepository platformTemplateRejectHistoryRepository;

	@Resource
	private PlatformClient platformClient;

	@Resource
	private PortalProperty property;

	// NOTE : biz톡 관련 사항으로 스케줄러 임시 주석처리
//	@Scheduled(initialDelay = 1000 * 10, fixedDelay = 1000 * 60) // 스케쥴 시간 단축(jhchoi)
	@Scheduled(initialDelay = 1000 * 60, fixedDelay = 1000 * 60 * 60)
	@SchedulerLock(name = "SYNC_PLATFORM_TEMPLATE_STATUS"
			, lockAtLeastFor = "PT5S"
			, lockAtMostFor = "PT20S")
	public void run() {
		final String jobName = JOB_NAME;
		log.info(">>> SCHEDULER: {}, START", jobName);

		try {
			processTemplates(jobName);
		} catch (Exception e) {
			log.error(">>> SCHEDULER: {}, FAILED, {}", jobName, e.getLocalizedMessage(), e);
		}

		log.info(">>> SCHEDULER: {}, END", jobName);
	}

	/********** Private Methods **********/
	// 템플릿 목록 페이징 처리 및 템플릿 단건 처리
	private void processTemplates(String jobName) {
		boolean hasNext = true;
		int page = 0;
		final int pageSize = 10;

		while (hasNext) {
			log.info(">>> SCHEDULER: {}, FETCHING PAGE: {}", jobName, page);
			Slice<PlatformTemplate> platformTemplates = fetchTemplateList(page++, pageSize);
			hasNext = platformTemplates.hasNext();

			for (PlatformTemplate platformTemplate : platformTemplates) {
				PlatformTemplate updatedTemplate;
				try {
					// 템플릿 단건 처리
					KakaoBizTemplateResponse<KakaoBizMessageTemplatePayload> response =
							platformClient.getKakaoBizTemplateInfo(platformTemplate.getSenderProfileKey(), platformTemplate.getCode());

					// 업데이트 대상이 없는 경우
					if ((response == null || response.getData() == null) &&
							!RESPONSE_SUCCESS_CODE.equals(response.getCode()) && !RESPONSE_DELETE_CODE.equals(response.getCode())) {
						log.info(">>> SCHEDULER: {}, SKIP ALREADY PROCESSED, TEMPLATE_CODE: {}, ERROR CODE: {}, ERROR MESSAGE: {}",
								jobName, platformTemplate.getCode(), response != null ? response.getCode() : "N/A", response != null ? response.getMessage() : "N/A");
						return;
					} else if (Stream.of("REG", "REQ").anyMatch(status -> status.equals(response.getData().getInspectionStatus()))) { // 등록, 요청인 상태에서 변경이 없을 경우
						log.info(">>> SCHEDULER: {}, SKIP ALREADY PROCESSED, TEMPLATE_CODE: {}, CURRENT INSPECTION STATUS: {}",
								jobName, platformTemplate.getCode(), response != null ? response.getData().getInspectionStatus() : "N/A");
						return;
					} else {
						// 템플릿 상태 업데이트
						updatedTemplate = RESPONSE_SUCCESS_CODE.equals(response.getCode()) ?
								updateTemplateStatus(platformTemplate, response.getData(), jobName) : deleteTemplateStatus(platformTemplate, jobName);
					}

					if (updatedTemplate != null && PlatformTemplateStatus.reject.equals(updatedTemplate.getStatus())) {
						List<Comment> comments = response.getData().getComments() != null ?
								response.getData().getComments() : Collections.emptyList();
						updateTemplateRejectHistory(updatedTemplate, comments, jobName);
					}
				} catch (Exception e) {
					log.error(">>> SCHEDULER: {}, TEMPLATE PROCESSING FAILED, TEMPLATE_CODE: {}, ERROR: {}",
							jobName, platformTemplate.getCode(), e.getMessage(), e);
				}
			}
		}
	}

	// 검수요청 중인 템플릿 목록 조회
	private Slice<PlatformTemplate> fetchTemplateList(int page, int pageSize) {
		PageRequest pageable = PageRequest.of(page, pageSize, Sort.Direction.ASC, "created");
		// request : 검수요청 중 템플릿
		return platformTemplateRepository.findAll(
				Example.of(PlatformTemplate.builder().status(PlatformTemplateStatus.request).build()), pageable);
	}

	// 검수요청 중인 템플릿의 결과 상태 업데이트
	private PlatformTemplate updateTemplateStatus(PlatformTemplate platformTemplate, KakaoBizMessageTemplatePayload data, String jobName) {
		if (!platformTemplate.getStatus().equals("REG")) { // 등록만 해놓은 상태일 경우 상태값 업데이트 방지
			platformTemplate.setStatus(STATUS_APPROVE.equals(data.getInspectionStatus()) ? PlatformTemplateStatus.approve : PlatformTemplateStatus.reject);
		}
		platformTemplate.setModified(ZonedDateTime.now());
		platformTemplate.setModifier(property.getSystemMemberId());

		return platformTemplateRepository.save(platformTemplate);
	}

	// 검수요청 중 삭제된 템플릿 결과 상태 업데이트
	private PlatformTemplate deleteTemplateStatus(PlatformTemplate platformTemplate, String jobName) {
		platformTemplate.setStatus(PlatformTemplateStatus.delete);
		platformTemplate.setModified(ZonedDateTime.now());
		platformTemplate.setModifier(property.getSystemMemberId());

        return platformTemplateRepository.save(platformTemplate);
	}

	// 템플릿 검수 반려 이력 업데이트
	private void updateTemplateRejectHistory(PlatformTemplate updatedTemplate, List<Comment> comments, String jobName) {
		for (Comment comment : comments) {
			try {
				PlatformTemplateRejectHistory existingHistory = platformTemplateRejectHistoryRepository.findByCommentSeqno(comment.getId());
				if (existingHistory == null) {
					PlatformTemplateRejectHistory newHistory = PlatformTemplateRejectHistory.builder()
							.platformTemplateId(updatedTemplate.getId())
							.commentSeqno(comment.getId())
							.commentContent(comment.getContent())
							.commentUserName(comment.getUserName())
							.commentCreateAt(comment.getCreateAt())
							.commentStatus(comment.getStatus())
							.created(ZonedDateTime.now())
							.creator(property.getSystemMemberId())
							.build();

					platformTemplateRejectHistoryRepository.save(newHistory);
				}
			} catch (Exception e) {
				log.error(">>> SCHEDULER: {}, FAILED TO SAVE REJECT HISTORY, TEMPLATE_CODE: {}, COMMENT_ID: {}, ERROR: {}",
						jobName, updatedTemplate.getCode(), comment.getId(), e.getMessage(), e);
			}
		}
	}
}
