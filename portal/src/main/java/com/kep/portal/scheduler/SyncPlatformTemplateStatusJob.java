package com.kep.portal.scheduler;

import com.kep.core.model.dto.platform.PlatformTemplateStatus;
import com.kep.core.model.dto.platform.kakao.KakaoBizMessageTemplatePayload;
import com.kep.core.model.dto.platform.kakao.KakaoBizTemplateResponse;
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

/**
 * 플랫폼 템플릿 상태 동기화 배치(템플릿 상태 변경 처리)
 */
@Service
@Transactional
@Slf4j
@Profile(value = {"stg", "dev", "cc-dev", "kc-dev","kc-dev-bnk","kc-dev-cluster","local"})
public class SyncPlatformTemplateStatusJob {
	@Resource
	private PlatformTemplateRepository platformTemplateRepository;

	@Resource
	private PlatformTemplateRejectHistoryRepository platformTemplateRejectHistoryRepository;

	@Resource
	private PlatformClient platformClient;

	@Resource
	private PortalProperty property;

	// NOTE : biz톡 관련 사항으로 스케줄러 임시 주석처리
	/*
	@Scheduled(initialDelay = 1000 * 60, fixedDelay = 1000 * 60 * 60)
	@SchedulerLock(name = "SYNC_PLATFORM_TEMPLATE_STATUS"
			, lockAtLeastFor = "PT5S"
			, lockAtMostFor = "PT20S")
	 */
	public void run() {
		final String jobName = "SYNC PLATFORM TEMPLATE STATUS";
		log.info(">>> SCHEDULER: {}, START", jobName);

		boolean hasNext = true;
		final int pageSize = 10;
		try {
			int page = 0;
			while (hasNext) {
				// ////////////////////////////////////////////////////////////
				// 템플릿 검수중 상태 (PlatformTemplateStatus.request) 목록
				Pageable pageable = PageRequest.of(page++, pageSize, Sort.Direction.ASC, "created");
				Slice<PlatformTemplate> platformTemplatePage = platformTemplateRepository.findAll(Example.of(PlatformTemplate.builder().status(PlatformTemplateStatus.request).build()), pageable);
				hasNext = platformTemplatePage.hasNext();

				for (PlatformTemplate platformTemplate : platformTemplatePage.getContent()) {
					KakaoBizTemplateResponse<KakaoBizMessageTemplatePayload> res =  platformClient.getKakaoBizTemplateInfo(platformTemplate.getSenderProfileKey(), platformTemplate.getCode());

					// 검수완료 or 반려가 아닌 경우 or 비즈메시지센터에 정보 조회 시 오류인 경우(데이터가 없거나 조회가 안되는 경우) pass
					if((null == res.getData() && "API_200" != res.getCode()) || (!"O".equals(res.getData().getKepStatus()) && !"R".equals(res.getData().getKepStatus()))){
						if(null == res.getData() && "API_200" != res.getCode()){
							log.info(">>> SCHEDULER: {}, SKIP ALREADY PROCESSED, TEMPLATE_CODE: {}, ERROR CODE : {}, ERROR MESSAGE : {}", jobName, platformTemplate.getCode(), res.getCode(), res.getMessage());
						} else {
							log.info(">>> SCHEDULER: {}, SKIP ALREADY PROCESSED, TEMPLATE_CODE: {}", jobName, platformTemplate.getCode());
						}
						continue;
					}

					platformTemplate.setStatus(("O".equals(res.getData().getKepStatus()) ? PlatformTemplateStatus.approve : PlatformTemplateStatus.reject));
					platformTemplate.setModified(ZonedDateTime.now());
					platformTemplate.setModifier(property.getSystemMemberId());

					platformTemplate = platformTemplateRepository.save(platformTemplate);

					log.info(">>> SCHEDULER: {}, REJECT TEMPLATE HISTORY SAVE, PLATFORM : {}",
							jobName, platformTemplate);

					// 반려일 경우 이력을 저장함
					if (PlatformTemplateStatus.reject.equals(platformTemplate.getStatus())) {
						for(KakaoBizMessageTemplatePayload.TemplateComment templateComment : res.getData().getTemplateComments()){

							PlatformTemplateRejectHistory platformTemplateRejectHistory = platformTemplateRejectHistoryRepository.findByCommentSeqno(templateComment.getCommentSeqno());

							log.info(">>> SCHEDULER: {}, REJECT TEMPLATE HISTORY INFO: {}",
									jobName, platformTemplateRejectHistory);

							// 이력이 존재하지 않는 경우 반려이력 저장
							if(ObjectUtils.isEmpty(platformTemplateRejectHistory)){
								// 반려이력 저장
								platformTemplateRejectHistoryRepository.save(PlatformTemplateRejectHistory.builder()
										.platformTemplateId(platformTemplate.getId())
										.commentSeqno(templateComment.getCommentSeqno())
										.commentContent(templateComment.getCommentContent())
										.commentUserName(templateComment.getCommentUserName())
										.commentCreateAt(templateComment.getCommentCreateAt())
										.commentStatus(templateComment.getCommentStatus())
										.regBy(templateComment.getRegBy())
										.regDate(templateComment.getRegDate())
										.updateBy(templateComment.getUpdateBy())
										.updateDate(templateComment.getUpdateDate())
										.created(ZonedDateTime.now())
										.creator(property.getSystemMemberId())
										.build());
							}
						}
					}
				}
			}

		} catch (Exception e) {
			log.error(">>> SCHEDULER: {}, FAILED, {}", jobName, e.getLocalizedMessage(), e);
		}

		log.info(">>> SCHEDULER: {}, END", jobName);
	}
}
