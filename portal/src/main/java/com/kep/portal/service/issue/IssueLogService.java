package com.kep.portal.service.issue;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kep.core.model.dto.issue.IssueLogDto;
import com.kep.core.model.dto.issue.payload.IssuePayload;
import com.kep.portal.config.property.ModeProperty;
import com.kep.portal.config.property.PortalProperty;
import com.kep.portal.model.dto.issue.IssueLogSearchCondition;
import com.kep.portal.model.entity.customer.Guest;
import com.kep.portal.model.entity.issue.Issue;
import com.kep.portal.model.entity.issue.IssueLog;
import com.kep.portal.model.entity.issue.IssueLogMapper;
import com.kep.portal.model.entity.member.Member;
import com.kep.portal.repository.issue.IssueLogRepository;
import com.kep.portal.repository.issue.IssueRepository;
import com.kep.portal.service.forbidden.ForbiddenService;
import com.kep.portal.util.SecurityUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import javax.annotation.Resource;
import javax.transaction.Transactional;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
@Slf4j
public class IssueLogService {

	@Resource
	private IssueLogRepository issueLogRepository;
	@Resource
	private IssueLogMapper issueLogMapper;

	@Resource
	private IssueRepository issueRepository;
	@Resource
	private ModeProperty modeProperty;
	@Resource
	private PortalProperty portalProperty;
	@Resource
	private SecurityUtils securityUtils;
	@Resource
	private ObjectMapper objectMapper;
	
	@Resource
	private ForbiddenService forbiddenService;

	public IssueLog findById(@NotNull @Positive Long id) {

		return issueLogRepository.findById(id).orElse(null);
	}

	public Page<IssueLogDto> getAll(@NotNull Example<IssueLogDto> example, @NotNull Pageable pageable) {

		IssueLog search = issueLogMapper.map(example.getProbe());
		Assert.notNull(search, "search is null");

		Page<IssueLog> entityPage = issueLogRepository.findAll(Example.of(search), pageable);
		List<IssueLogDto> result = issueLogMapper.map(entityPage.getContent());
		Assert.notNull(result, "result is null");
		
		this.changeMessageForbiddenWord(result);

		return new PageImpl<>(result, entityPage.getPageable(), entityPage.getTotalElements());
	}

	public List<IssueLogDto> search(@NotNull @Valid IssueLogSearchCondition condition, @NotNull Pageable pageable) throws Exception {

		Issue issue = issueRepository.findById(condition.getIssueId()).orElse(null);
		Assert.notNull(issue, "issue is null");

		// 파라미터 프로젝션, 고객 상담 이력 (issueId -> guest -> issueIds)
		if (condition.getIssueId() != null) {
			Guest guest = issue.getGuest();
			Issue example = Issue.builder().guest(guest).build();
			if (modeProperty.getExcludeRelayedIssueLog()) {
				example.setRelayed(false);
			}
			List<Issue> issues = issueRepository.findAll(Example.of(example),
					Sort.by(Sort.Direction.DESC, "created"));
			condition.setIssueIds(issues.stream().map(Issue::getId).collect(Collectors.toList()));
		}

		// 상담원별 상담 이력 (본인의 이력만 노출) 기능 사용 여부
		if (modeProperty.getUseAssignerOnIssueLog()) {
			condition.setAssigners(Arrays.asList(portalProperty.getSystemMemberId(), securityUtils.getMemberId()));
		}

		Sort.Direction direction = "newer".equals(condition.getDirection()) ? Sort.Direction.ASC : Sort.Direction.DESC;
		Page<IssueLog> issueLogPage = issueLogRepository.search(condition,
				PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), direction, "id"));
		log.info("SEARCH ISSUE LOG: {}", objectMapper.writeValueAsString(issueLogPage));
		
		return this.changeMessageForbiddenWord(issueLogMapper.map(issueLogPage.getContent()));
	}

	/**
	 * dateTime 이후 고객 응답 여부
	 */
	public boolean isGuestRepliedAfter(@NotNull Issue issue, @NotNull ZonedDateTime dateTime) {

		long count = issueLogRepository.countByIssueIdAndCreatorAndCreatedAfter(issue.getId(), issue.getGuest().getId(), dateTime);
		return count > 0;
	}

	public Page<IssueLogDto> getAllByIssueId(@NotNull Long issueId, @NotNull Pageable pageable) {

		Page<IssueLog> entityPage = issueLogRepository.findAll(Example.of(IssueLog.builder()
				.issueId(issueId)
				.build()), pageable);

		List<IssueLogDto> dtos = issueLogMapper.map(entityPage.getContent());
		return new PageImpl<>(dtos, entityPage.getPageable(), entityPage.getTotalElements());
	}

	public IssueLog save(@NotNull @Valid IssueLog entity) {

		return issueLogRepository.save(entity);
	}

	public IssueLog save(@NotNull @Valid IssueLog entity, Member assigner) {

		// 상담원별 상담 이력 (본인의 이력만 노출) 기능 사용 여부
		if (modeProperty.getUseAssignerOnIssueLog()) {
			entity.setAssigner(assigner != null ? assigner.getId() : portalProperty.getSystemMemberId());
		}
		return save(entity);
	}

	public IssueLogDto store(@NotNull @Positive Long id, @NotNull @Valid IssuePayload payload) throws Exception {

		IssueLog issueLog = findById(id);
		issueLog.setPayload(objectMapper.writeValueAsString(payload));
		// TODO: 수정 표시? (modifier, 메세지에 단순 표식)
		return issueLogMapper.map(issueLogRepository.save(issueLog));
	}

	public IssueLog findLastBotMessage(@NotNull @Positive Long issueId) {

		return issueLogRepository.findFirstByIssueIdOrderByCreatedDesc(issueId);
	}
	

	/**
	 * 채팅창 금지어 적용
	 * @param dtos
	 * @return
	 */
	public List<IssueLogDto> changeMessageForbiddenWord(List<IssueLogDto> dtos){
		//TODO : 금지어 적용 여부 체크
		for(IssueLogDto dto : dtos) {
			try {
				IssuePayload issuePayload = objectMapper.readValue(dto.getPayload(), IssuePayload.class);
//				log.info("PAYLOAD : {}", dto.getPayload());
				for (IssuePayload.Chapter chapter : issuePayload.getChapters()) {
					for (IssuePayload.Section section : chapter.getSections()){
						if(section.getType().equals(IssuePayload.SectionType.text)){
							String data = forbiddenService.changeForbiddenToMasking(section.getData());
							section.setData(data);
						}
					}
				}
				dto.setPayload(objectMapper.writeValueAsString(issuePayload));

			} catch (Exception e){
				log.error("FORBIDDEN ERROR : {}",e.getMessage());
			}

			dto.setPayload(dto.getPayload());
		}

		return dtos;
	}
}
