package com.kep.portal.service.issue;

import com.kep.core.model.dto.issue.IssueExtraDto;
import com.kep.core.model.exception.BizException;
import com.kep.portal.client.LegacyClient;
import com.kep.portal.config.property.SocketProperty;
import com.kep.portal.model.dto.subject.IssueCategoryChildrenDto;
import com.kep.portal.model.dto.summary.IssueExtraSummaryDetailDto;
import com.kep.portal.model.dto.summary.IssueExtraSummaryDto;
import com.kep.portal.model.entity.channel.Channel;
import com.kep.portal.model.entity.channel.ChannelEnv;
import com.kep.portal.model.entity.issue.*;
import com.kep.portal.model.entity.subject.IssueCategory;
import com.kep.portal.repository.channel.ChannelEnvRepository;
import com.kep.portal.repository.issue.IssueExtraRepository;
import com.kep.portal.repository.issue.IssueRepository;
import com.kep.portal.repository.subject.IssueCategoryRepository;
import com.kep.portal.service.subject.IssueCategoryService;
import com.kep.portal.util.CommonUtils;
import com.kep.portal.util.SecurityUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.util.ObjectUtils;

import javax.annotation.Resource;
import javax.transaction.Transactional;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import java.time.ZonedDateTime;
import java.util.List;

@Service
@Transactional
@Slf4j
public class IssueExtraService {

	@Resource
	private IssueExtraRepository issueExtraRepository;

	@Resource
	private IssueExtraMapper issueExtraMapper;

	@Resource
	private SocketProperty socketProperty;

	@Resource
	private SimpMessagingTemplate simpMessagingTemplate;

	@Resource
	private IssueCategoryRepository issueCategoryRepository;

	@Resource
	private IssueService issueService;

	@Resource
	private IssueMapper issueMapper;

	@Resource
	private IssueCategoryService issueCategoryService;

	@Resource
	private SecurityUtils securityUtils;

	@Resource
	private IssueRepository issueRepository;

	@Resource
	private LegacyClient legacyClient;

	@Resource
    private ChannelEnvRepository channelEnvRepository;

	public IssueExtraDto getByIssueId(@NotNull @Positive Long issueId) {

		Issue issue = issueService.findById(issueId);

		Assert.notNull(issue, "Not Found by IssueId");

		IssueExtra issueExtra = (issue.getIssueExtra() != null) ? issue.getIssueExtra() : null;
		log.info("ISSUE_EXTRA : {}" , issueExtra);

		if(issueExtra != null){
			if (issueExtra.getIssueCategoryId() != null) {
				issueExtra.setIssueCategory(issueCategoryService.findById(issueExtra.getIssueCategoryId()));
			}
			IssueExtraDto issueExtraDto = issueExtraMapper.map(issueExtra);

			// TODO: 정책, 후처리 분류 (IssueExtra.issueCategoryId) 세팅, 이슈 분류 정보를 (Issue.issueCategory) 기본값으로 사용
			if (issueExtraDto.getIssueCategoryId() == null && issue.getIssueCategory() != null) {
				// TODO: 분류 단계 설정
				// 소분류 카테고리 정보
				issueExtraDto.setIssueCategoryId(issue.getIssueCategory().getId());
			}

			return issueExtraDto;
		}

		return null;
	}

	/**
	 * 유입경로
	 */
	public Page<IssueExtraDto> getAllByInflow(@NotNull Long guestId, @NotNull Pageable pageable) {
		// 유입 경로 정보 조회
		Page<IssueExtra> issueExtras =  issueExtraRepository
				.findAllByGuestIdAndInflowNotNull(guestId , pageable);
		return new PageImpl<>(issueExtraMapper.map(issueExtras.getContent()), issueExtras.getPageable(), issueExtras.getTotalElements());
	}

	/**
	 * 메모
	 */
	public Page<IssueExtraDto> getAllByMemo(@NotNull Long guestId, @NotNull Pageable pageable) {
		// 메모 정보 조회
		Page<IssueExtraMemo> issueExtras =  issueExtraRepository
				.findAllByGuestIdAndMemoNotNull(guestId , pageable);
		return new PageImpl<>(issueExtraMapper.mapMemo(issueExtras.getContent()), issueExtras.getPageable(), issueExtras.getTotalElements());
	}

	public IssueExtra save(@NotNull @Valid IssueExtra entity) {
		return issueExtraRepository.save(entity);
	}

	/**
	 * 이슈 상세 정보 저장
	 * @throws Exception
	 */
	public IssueExtraDto store(@NotNull @Valid IssueExtraDto issueExtraDto, @NotNull @Positive Long issueId)
			throws Exception
	{
		Issue issue = issueService.findById(issueId);
		Assert.notNull(issue, "issue not null id:" + issueId);
		IssueExtra issueExtra = issue.getIssueExtra() != null ? issue.getIssueExtra() : null;

		//	요약 정보 먼저 확인 및 저장
//		    if (issueExtraDto.getSummary() != null) {
//		        issueExtra.setSummary(issueExtraDto.getSummary());
//		        issueExtra.setSummaryModified(ZonedDateTime.now());
//		        issueExtra.setSummaryCompleted(true);
//		    }
//		    if (issueExtra == null) {
//		      issueExtra = issueExtraMapper.map(issueExtraDto);
//		      issueExtra.setGuestId(issue.getGuest().getId());
//		    } else {
//		      CommonUtils.copyNotEmptyProperties(issueExtraMapper.map(issueExtraDto), issueExtra);
//		    }
//
//		    if (issueExtraDto.getSummary() != null) {
//		      issueExtra.setSummaryModified(ZonedDateTime.now());
//		      if (!issueExtra.isSummaryCompleted()) {
//		        issueExtra.setSummary(issueExtraDto.getSummary());
//		        issueExtra.setSummaryCompleted(true);
//		      }
//
//		    }
		// 요약 정보 확인 및 저장
		String summary = issueExtraDto.getSummary();
		if (issueExtra == null) {
			issueExtra = issueExtraMapper.map(issueExtraDto);
			issueExtra.setGuestId(issue.getGuest().getId());
		} else {
			CommonUtils.copyNotEmptyProperties(issueExtraMapper.map(issueExtraDto), issueExtra);
		}

		issueExtra.setSummary(summary == null || summary.isEmpty() ? null : summary);
		issueExtra.setSummaryModified(ZonedDateTime.now());
		issueExtra.setSummaryCompleted(true);

		if (issueExtraDto.getMemo() != null) {
			issueExtra.setMemo(issueExtraDto.getMemo());
			issueExtra.setMemoModified(ZonedDateTime.now());
		}

		if (issueExtraDto.getInflow() != null) {
			issueExtra.setInflowModified(ZonedDateTime.now());
		}

		issue.setIssueExtra(issueExtra);

		issueExtra = (IssueExtra)issueExtraRepository.save(issueExtra);

		if (issue.getIssueExtra() == null) {
			issue.setIssueExtra(issueExtra);
			issueRepository.save(issue);
		}

		if ((!ObjectUtils.isEmpty(issueExtraDto.getIssueCategoryId())) && (!ObjectUtils.isEmpty(issueExtraDto.getSummary())))
		{
			issueService.joinIssueSupport(issue);

			simpMessagingTemplate.convertAndSend(socketProperty.getIssuePath(), issueMapper.map(issue));
		}

		/**
		 * FIXME :: bnk 비즈니스 로직. 제거 필요 20240715 volka
		 */
		// BNK API 전송 로직 (에러 발생해도 요약 정보 저장에 영향을 주지 않음!!)
			try {
				if (!ObjectUtils.isEmpty(issueExtraDto.getSummary()) || !ObjectUtils.isEmpty(issueExtraDto.getBnkSummary())) {
					List<IssueCategoryChildrenDto> issueCategoryInfoList = issueCategoryService.searchById(issueExtraDto.getIssueCategoryId(), issue.getChannel().getId());
					legacyClient.sendToBnkResultApi(issue, issueCategoryInfoList, issueExtraDto);
				}
			} catch (Exception e) {
				log.error("BNK API 호출 실패", "이슈 ID " + issueId + " 에 대한 BNK API 호출 중 오류 발생: " + e.getMessage());
			}
		return issueExtraMapper.map(issueExtra);
	}

	/**
	 * 요약 저장
	 * @param issueExtraSummaryDto
	 * @return
	 */
	public void saveExtraSummary(IssueExtraSummaryDto issueExtraSummaryDto) {
		Issue issue = issueRepository.findById(issueExtraSummaryDto.getIssueId())
				.orElseThrow(() -> new BizException("Not found issue"));

		Integer issueCategoryMaxDepth = channelEnvRepository.findByChannel(issue.getChannel()).getMaxIssueCategoryDepth();

		IssueCategory category = issueCategoryRepository.findById(issueExtraSummaryDto.getIssueCategoryId())
				.orElseThrow(() -> new BizException("Not found issueCategory"));
		Assert.isTrue(category.getDepth().equals(issueCategoryMaxDepth), "issue category only can be max depth category");

		String summary = issueExtraSummaryDto.getSummary();
		IssueExtra issueExtra = null;

		if (issue.getIssueExtra() == null) {
			issueExtra = IssueExtra.builder()
					.guestId(issue.getGuest().getId())
					.summary(summary == null || summary.isEmpty() ? null : summary)
					.summaryModified(ZonedDateTime.now())
					.summaryCompleted(issueExtraSummaryDto.getSummaryCompleted())
					.issueCategoryId(category.getId())
					.build();

		} else {
			issueExtra = issue.getIssueExtra();
		}

		if (issueExtraSummaryDto.getMemo() != null && !issueExtraSummaryDto.getMemo().isEmpty()) {
			issueExtra.setMemo(issueExtraSummaryDto.getMemo());
			issueExtra.setMemoModified(ZonedDateTime.now());
		}

		if (issueExtraSummaryDto.getInflow() != null && !issueExtraSummaryDto.getInflow().isEmpty()) {
			issueExtra.setInflowModified(ZonedDateTime.now());
		}

		issueExtra = issueExtraRepository.save(issueExtra);
		issue.setIssueExtra(issueExtra);

		issueService.joinIssueSupport(issue);
		simpMessagingTemplate.convertAndSend(socketProperty.getIssuePath(), issueMapper.map(issue));
	}

	public IssueExtraSummaryDetailDto getExtraSummary(Long issueId) {
		Issue issue = issueRepository.findById(issueId).orElseThrow(() -> new BizException("Not found issue"));
		IssueExtra issueExtra = issue.getIssueExtra();

		if (issueExtra == null) return null;

		IssueExtraSummaryDetailDto result = new IssueExtraSummaryDetailDto();
		Channel channel = issue.getChannel();
		ChannelEnv channelEnv = channelEnvRepository.findByChannel(channel);

		result.setChannelId(channel.getId());
		result.setChannelName(channel.getName());
		result.setMaxIssueCategoryDepth(channelEnv.getMaxIssueCategoryDepth());
		result.setIssueCategory(issueExtra.getIssueCategoryId() == null ? null : issueCategoryService.getIssueCategoryTreeByLowestOne(channel.getId(), issueExtra.getIssueCategoryId()));
		result.setIssueExtraSummary(IssueExtraSummaryDto.of(issueExtra));

		return result;
	}
}
