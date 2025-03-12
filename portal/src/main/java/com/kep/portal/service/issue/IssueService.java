package com.kep.portal.service.issue;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.kep.core.model.dto.issue.*;
import com.kep.portal.model.dto.issue.IssueSearchCondition;
import com.kep.portal.model.entity.channel.Channel;
import com.kep.portal.model.entity.customer.CustomerMapper;
import com.kep.portal.model.entity.customer.Guest;
import com.kep.portal.model.entity.issue.*;
import com.kep.portal.model.entity.member.Member;
import com.kep.portal.model.entity.team.Team;
import com.kep.portal.model.entity.team.TeamMember;
import com.kep.portal.repository.issue.IssueRepository;
import com.kep.portal.repository.issue.IssueSupportRepository;
import com.kep.portal.repository.member.MemberRepository;
import com.kep.portal.service.channel.ChannelService;
import com.kep.portal.service.customer.CustomerService;
import com.kep.portal.service.customer.GuestService;
import com.kep.portal.service.subject.IssueCategoryService;
import com.kep.portal.service.team.TeamMemberService;
import com.kep.portal.util.CommonUtils;
import com.kep.portal.util.SecurityUtils;
import com.querydsl.core.Tuple;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.IncorrectResultSizeDataAccessException;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

import javax.annotation.Nullable;
import javax.annotation.Resource;
import javax.transaction.Transactional;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

import static java.util.Comparator.comparingLong;
import static java.util.stream.Collectors.maxBy;
import static java.util.stream.Collectors.toList;

@Service
@Transactional
@Slf4j
public class IssueService {
    private final MemberRepository memberRepository;

    public IssueService(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    @Resource
    private IssueRepository issueRepository;
    @Resource
    private IssueMapper issueMapper;

    @Resource
    private ChannelService channelService;
    @Resource
    private GuestService guestService;
    @Resource
    private CustomerService customerService;

    @Resource
    private CustomerMapper customerMapper;

    @Resource
    private SecurityUtils securityUtils;
    @Resource
    private TeamMemberService teamMemberService;

    @Resource
    private IssueSupportRepository issueSupportRepository;

    @Resource
    private IssueSupportMapper issueSupportMapper;

    @Resource
    private ObjectMapper objectMapper;

    @Resource
    private IssueLogMapper issueLogMapper;

    @Resource
    private IssueLogService issueLogService;

    @Resource
    private IssueCategoryService issueCategoryService;

    public Issue findById(@NotNull Long id) {

        return issueRepository.findById(id).orElse(null);
    }

    /**
     * @param id
     * @return
     * @수정일자     / 수정자		 	/ 수정내용
     * 2023.05.31 / asher.shin / 상담창 하나 조회시 고객 정보 가져오기
     */
    public IssueDto getById(@NotNull Long id) {

        Issue issue = this.findById(id);

        IssueDto issueDto = issueMapper.map(issue);

        if (issueDto.getCustomerId() != null) {
            issueDto.setCustomer(customerMapper.map(customerService.findById(issueDto.getCustomerId())));
        }

        // FIXME : 고객 대화 검토 요청 내용도 함께 조회 issueDto의 support에 데이터 추가

        return issueDto;
    }

    public Page<Issue> findAll(@NotNull Example<Issue> example, @NotNull Pageable pageable) {

        return issueRepository.findAll(example, pageable);
    }

    public Page<IssueDto> getAll(@NotNull Example<Issue> example, @NotNull Pageable pageable) {

        Page<Issue> entityPage = this.findAll(example, pageable);
        List<IssueDto> dtos = issueMapper.map(entityPage.getContent());
        Assert.notNull(dtos, "DTOs is null");

        return new PageImpl<>(dtos, entityPage.getPageable(), entityPage.getTotalElements());
    }

    public Issue findOne(@NotNull Example<Issue> example) {

        return issueRepository.findOne(example).orElse(null);
    }

    public Issue save(@NotNull @Valid Issue entity) {

        return issueRepository.save(entity);
    }

    public IssueDto store(@NotNull @Valid IssueDto dto) {

        Issue entity = this.save(issueMapper.map(dto));
        return issueMapper.map(entity);
    }

    public void delete(@NotNull @Valid Issue entity) {

        issueRepository.delete(entity);
    }

    @Nullable
    public IssueDto store(@NotNull @Valid IssueDto dto, @NotNull @Positive Long id) {

        Issue entity = issueRepository.findById(id).orElse(null);
        Assert.notNull(entity, "not found issue, id: " + id);

        CommonUtils.copyNotEmptyProperties(dto, entity);
        entity = this.save(entity);
        return issueMapper.map(entity);
    }

    // ////////////////////////////////////////////////////////////////////////
    //
    // ////////////////////////////////////////////////////////////////////////

    /**
     * 상담 포탈 > 상담 목록
     */
    public Page<IssueDto> search(@NotNull @Valid IssueSearchCondition condition, @NotNull Pageable pageable) throws JsonProcessingException {

        // 파라미터 프로젝션 (고객)
        List<Guest> guests = guestService.searchGuestAndCustomer(condition.getCustomerSubject(), condition.getCustomerQuery());

        // 유저 조건 추가
        Member member = Member.builder().id(securityUtils.getMemberId()).build();

        // 이슈 검색
        Page<Issue> issuePage;
        // 고객 검색어 있을 경우
        if (!ObjectUtils.isEmpty(condition.getCustomerSubject()) && !ObjectUtils.isEmpty(condition.getCustomerQuery())) {
            if (guests.isEmpty()) {
                return new PageImpl<>(Collections.emptyList());
            } else {
                if (condition.getType() != null) {
                    issuePage = issueRepository.findAllByMemberAndStatusInAndGuestInAndType(member, condition.getStatus(), guests, condition.getType(), pageable);
                } else {
                    issuePage = issueRepository.findAllByMemberAndStatusInAndGuestIn(member, condition.getStatus(), guests, pageable);
                }

            }
        }
        // 고객 검색어 없을 경우
        else {
            if (condition.getType() != null) {
                issuePage = issueRepository.findAllByMemberAndStatusInAndType(member, condition.getStatus(), condition.getType(), pageable);
            } else {
                issuePage = issueRepository.findAllByMemberAndStatusIn(member, condition.getStatus(), pageable);
            }
        }

        List<Issue> issues = issuePage.getContent()
                .stream()
                .peek(item -> {
                    if (item.getLastIssueLog() != null) {
                        List<IssueLogDto> issueLogs = issueLogService.changeMessageForbiddenWord(issueLogMapper.map(Collections.singletonList(item.getLastIssueLog())));
                        if (!issueLogs.isEmpty()) {
                            IssueLog issueLog = issueLogMapper.map(issueLogs.stream().findFirst()
                                    .orElse(null));

                            item.setLastIssueLog(issueLog);
                        }
                    }

                })
                .collect(toList());
        joinIssueSupport(issues);

        List<IssueDto> issueDtos = issueMapper.map(issues);
        return new PageImpl<>(issueDtos, issuePage.getPageable(), issuePage.getTotalElements());
    }

    /**
     * 상담 지원 요청 정보 조인
     */
    public void joinIssueSupport(Issue issue) {

        joinIssueSupport(Collections.singletonList(issue));
    }

    /**
     * 상담 지원 요청 정보 조인
     */
    public void joinIssueSupport(List<Issue> issues) {

        List<IssueSupport> issueSupportList = issueSupportRepository.findAllByIssueInOrderByIdDesc(issues);
        log.debug("ISSUE SUPPORT LIST:{} ", issueSupportMapper.map(issueSupportList));

        // '상담 전환' 요청
        Map<Long, Optional<IssueSupport>> issueSupportChanges = issueSupportList.stream()
                .filter(q -> IssueSupportType.change.equals(q.getType()))
                .collect(Collectors.groupingBy(q -> q.getIssue().getId(), maxBy(comparingLong(IssueSupport::getId))));

        // '상담 검토' 요청
        Map<Long, Optional<IssueSupport>> issueSupportQuestions = issueSupportList.stream()
                .filter(q -> IssueSupportType.question.equals(q.getType()))
                .collect(Collectors.groupingBy(q -> q.getIssue().getId(), maxBy(comparingLong(IssueSupport::getId))));

        for (Issue issue : issues) {
            Map<IssueSupportType, IssueSupportStatus> support = new HashMap<>();
            Optional<IssueSupport> issueSupportChange = issueSupportChanges.get(issue.getId());

            if (issueSupportChange != null && issueSupportChange.isPresent()) {
                IssueSupport issueSupport = issueSupportChange.get();
                support.put(IssueSupportType.change, issueSupport.getStatus());

                if (!IssueStatus.assign.equals(issue.getStatus())) {
                    if (support.get(IssueSupportType.change).equals(IssueSupportStatus.finish) || support.get(IssueSupportType.change).equals(IssueSupportStatus.auto)) {
                        support.remove(IssueSupportType.change);
                    }
                }
            }

            Optional<IssueSupport> issueSupportQuestion = issueSupportQuestions.get(issue.getId());

            if (issueSupportQuestion != null && issueSupportQuestion.isPresent()) {
                IssueSupport issueSupport = issueSupportQuestion.get();
                support.put(IssueSupportType.question, issueSupport.getStatus());
            }
            issue.setSupport(support);
        }
    }

    /**
     * 상담 관리 > 상담 진행 목록
     */
    public Page<IssueDto> searchOpen(@NotNull @Valid IssueSearchCondition condition, @NotNull Pageable pageable) throws Exception {

        // 파라미터 프로젝션
        if (!setGuestCondition(condition)) {
            return new PageImpl<>(Collections.emptyList());
        }

        if (!setMemberIdCondition(condition)) {
            return new PageImpl<>(Collections.emptyList());
        }

        /**
         * Admin : 브랜치 기준
         * Manager : 팀 기준
         */
        setConditionByRole(condition);


        if (condition.getChannelId() != null) {
            Channel channel = channelService.findById(condition.getChannelId());
            Assert.notNull(channel, "Not found Channel");
            condition.setChannels(Arrays.asList(channel));
        }

        // 카테고리 조회용 최하위 카테고리 in 조건 추가
        if (condition.getCategoryId() != null) {
            condition.setIssueCategoryIds(issueCategoryService.getLowestCategoriesById(condition.getChannelId(), condition.getCategoryId()));
        }

        Page<Issue> issuePage = issueRepository.search(condition, pageable);

        List<Long> memberIds = issuePage.getContent()
                .stream()
                .filter(item -> item.getMember() != null)
                .map(item -> item.getMember().getId())
                .collect(Collectors.toList());

        List<TeamMember> teamMembers = teamMemberService.findAllByMemberIdIn(memberIds);

        List<Issue> issues = issuePage.getContent().stream()
                .peek(issue -> {
                    if (issue.getMember() != null) {
                        List<Team> teams = teamMembers.stream()
                                .filter(teamMember -> teamMember.getMemberId().equals(issue.getMember().getId()))
                                .map(TeamMember::getTeam).collect(Collectors.toList());
                        issue.getMember().setTeams(teams);
                    }
                }).collect(Collectors.toList());

        log.debug("SEARCH ISSUE OPENED: {}", objectMapper.writeValueAsString(issuePage));

        return new PageImpl<>(issueMapper.map(issues), issuePage.getPageable(), issuePage.getTotalElements());
    }

    /**
     * 상담 관리 > 상담 이력
     * 상담 포털 > 상담 이력
     */
    public Page<IssueDto> searchHistory(@NotNull @Valid IssueSearchCondition condition, @NotNull Pageable pageable) throws Exception {

        // 파라미터 프로젝션
        if (!setGuestCondition(condition)) {
            return new PageImpl<>(Collections.emptyList());
        }

        if (!setMemberIdCondition(condition)) {
            return new PageImpl<>(Collections.emptyList());
        }

        if (condition.getPlatform() != null) {
            List<Channel> channels = channelService.findAll(Example.of(Channel.builder().platform(condition.getPlatform()).build()));
            if (channels.isEmpty()) {
                return new PageImpl<>(Collections.emptyList());
            }
            condition.setChannels(channels);
        }

        //2023.05.02 브랜치 기준으로 변경(공통)
        // condition.setBranchId(securityUtils.getBranchId());

        // 카테고리 조회용 최하위 카테고리 in 조건 추가
        if (condition.getCategoryId() != null) {
            condition.setIssueCategoryIds(issueCategoryService.getLowestCategoriesById(condition.getChannelId(), condition.getCategoryId()));
        }

        Page<Issue> issuePage;
        // 대화 이력에 대한 내용이 condition에 포함되어 있다면
        if (StringUtils.hasText(condition.getPayload())) {
            // 내용 검색 처리
            issuePage = issueRepository.searchWithLog(condition, pageable);
        } else {
            // 그 외에는 일반 search
            issuePage = issueRepository.search(condition, pageable);
        }

        log.debug("SEARCH ISSUE HISTORY: {}", objectMapper.writeValueAsString(issuePage));

        return new PageImpl<>(issueMapper.map(issuePage.getContent()), issuePage.getPageable(), issuePage.getTotalElements());
    }

    /**
     * 고객 (비식별 고객, 식별 고객) 조건으로, {@link customerId} 목록 검색 후 조건에 목록 추가
     */
    private boolean setGuestCondition(@NotNull IssueSearchCondition condition) {
    	
    	if (!ObjectUtils.isEmpty(condition.getCustomerSubject()) && !ObjectUtils.isEmpty(condition.getCustomerQuery())) {
			List<Guest> guests = guestService.searchGuestAndCustomer(condition.getCustomerSubject(), condition.getCustomerQuery());
			if (guests.isEmpty()) return false;

			condition.setGuests(guests);
		}

		return true;
	}

    /**
     * 유저 조건
     */
    private boolean setMemberIdCondition(@NotNull IssueSearchCondition condition) {

        if (!ObjectUtils.isEmpty(condition.getMemberId())) {
            List<Member> members = memberRepository.findByIdIn(condition.getMemberId());
            if (members.isEmpty()) {
                return false;
            }
            condition.setMembers(members);
        }
        return true;
    }

    /**
     * 역할별 조건 추가
     */
    private void setConditionByRole(@NotNull IssueSearchCondition condition) {

        // 역할별(레벨) 조건 추가
        if (securityUtils.isAdmin()) {
            condition.setBranchId(securityUtils.getBranchId());
        } else if (securityUtils.isManager()) {
            condition.setTeamId(securityUtils.getTeamId());
        }
    }

    /**
     * 플랫폼에서 상담 요청시, 종료되지 않은 이슈 조회
     * TODO: 서울 지점 브랜치에서 상담중인 고객이, 부산 지점 상담톡 URL을 통해 상담 요청을 했을 경우
     */
    @Nullable
    public Issue findOngoingByPlatform(@NotNull Long channelId, @NotNull Long guestId) {

        try {
            return issueRepository.findOneByChannelIdAndGuestIdAndStatusNot(
                    channelId, guestId, IssueStatus.close);
        } catch (IncorrectResultSizeDataAccessException e) {
            // 한 로우만 존재해야하지만, 장애 발생시, 종료 안되는 경우 발생할 수 있음
            // TODO: 배치잡으로 종료 필요
            List<Issue> issues = issueRepository.findAllByChannelIdAndGuestIdAndStatusNotOrderByModifiedDesc(
                    channelId, guestId, IssueStatus.close);
            log.warn("IncorrectResultSizeDataAccessException, CHANNEL: {}, GUEST: {}, ISSUES: {}",
                    channelId, guestId, issues.stream().map(Issue::getId).collect(Collectors.toList()));
            if (!issues.isEmpty()) {
                return issues.get(0);
            }
        }

        return null;
    }

    /**
     * 마지막 봇 이슈 (봇 대화 이력을 저장한 이슈 중 가장 최근)
     */
    @Nullable
    public Issue findLastRelayedIssueByPlatform(@NotNull Long channelId, @NotNull Long guestId) {

        return issueRepository.findFirstByChannelIdAndGuestIdAndStatusAndRelayedOrderByModifiedDesc(
                channelId, guestId, IssueStatus.close, true);
    }

    /**
     * 유저별, 진행중 이슈 카운트
     */
    public Map<Long, Long> countOngoingGroupByMember(@NotNull Collection<Long> memberIds) {

        return issueRepository.countByStatusInGroupByMember(Arrays.asList(IssueStatus.ask, IssueStatus.reply, IssueStatus.urgent), memberIds);
    }

    /**
     * 유저별, 배정 이슈 카운트
     */
    public Map<Long, Long> countAssignedGroupByMember(@NotNull Collection<Long> memberIds) {

        return issueRepository.countByStatusInGroupByMember(Arrays.asList(IssueStatus.assign), memberIds);
    }

    /**
     * 배정대기 ({@link IssueStatus#open}) 이슈 카운트 (by 브랜치)
     * TODO: 정책, 설정값이 채널별로 있는게 맞나? 서울지점에서 50명 다 쓰면 부산지점은 배정을 못받음
     */
    public Long countOpenedByBranchId(@NotNull @Positive Long branchId) {

        return issueRepository.count(Example.of(Issue.builder()
                .status(IssueStatus.open)
                .branchId(branchId)
                .build()));
    }

    /**
     * 진행중 이슈 카운트
     */
    public Long countOngoing(Long branchId, LocalDate start, LocalDate end) {
        return issueRepository.count(IssueSearchCondition.builder()
                .branchId(branchId)
                .notStatus(Collections.singletonList(IssueStatus.close))
                .dateSubject("created")
                .startDate(start)
                .endDate(end)
                .build());
    }

    public List<Tuple> findByStatusNotAndIssueAutoCloseEnabled(IssueStatus issueStatus, Boolean issueAutoCloseEnabled) {
        return issueRepository.findByStatusNotAndIssueAutoCloseEnabled(issueStatus , issueAutoCloseEnabled);
    }
}
