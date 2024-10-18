package com.kep.portal.service.member;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.kep.core.model.dto.env.CounselEnvDto;
import com.kep.core.model.dto.env.CounselInflowEnvDto;
import com.kep.core.model.dto.issue.payload.IssuePayload;
import com.kep.core.model.dto.member.MemberDto;
import com.kep.core.model.dto.system.SystemEventHistoryActionType;
import com.kep.core.model.dto.work.OfficeHoursDto;
import com.kep.core.model.dto.work.OfficeWorkDto;
import com.kep.core.model.dto.work.WorkType;
import com.kep.core.model.exception.BizException;
import com.kep.portal.config.property.CoreProperty;
import com.kep.portal.config.property.PortalProperty;
import com.kep.portal.config.property.SystemMessageProperty;
import com.kep.portal.model.dto.member.MemberAssignDto;
import com.kep.portal.model.dto.member.MemberPassDto;
import com.kep.portal.model.dto.member.MemberSearchCondition;
import com.kep.portal.model.dto.member.MemberStatusSyncDto;
import com.kep.portal.model.entity.branch.Branch;
import com.kep.portal.model.entity.branch.BranchTeam;
import com.kep.portal.model.entity.member.Member;
import com.kep.portal.model.entity.member.MemberMapper;
import com.kep.portal.model.entity.member.MemberRole;
import com.kep.portal.model.entity.privilege.Level;
import com.kep.portal.model.entity.privilege.Role;
import com.kep.portal.model.entity.team.Team;
import com.kep.portal.model.entity.team.TeamMember;
import com.kep.portal.model.entity.work.*;
import com.kep.portal.repository.assign.BranchOfficeHoursRepository;
import com.kep.portal.repository.assign.MemberOfficeHoursRepository;
import com.kep.portal.repository.branch.BranchRepository;
import com.kep.portal.repository.branch.BranchTeamRepository;
import com.kep.portal.repository.member.MemberRepository;
import com.kep.portal.repository.member.MemberRoleRepository;
import com.kep.portal.repository.privilege.RoleRepository;
import com.kep.portal.repository.team.TeamMemberRepository;
import com.kep.portal.repository.team.TeamRepository;
import com.kep.portal.repository.work.BranchOffDutyHoursRepository;
import com.kep.portal.service.branch.BranchService;
import com.kep.portal.service.env.CounselEnvService;
import com.kep.portal.service.issue.IssueService;
import com.kep.portal.service.privilege.RoleService;
import com.kep.portal.service.system.SystemEventService;
import com.kep.portal.service.team.TeamMemberService;
import com.kep.portal.service.team.TeamService;
import com.kep.portal.service.work.BreakTimeService;
import com.kep.portal.service.work.OfficeHoursService;
import com.kep.portal.service.work.WorkService;
import com.kep.portal.util.CommonUtils;
import com.kep.portal.util.OfficeHoursTimeUtils;
import com.kep.portal.util.SecurityUtils;
import com.kep.portal.util.ZonedDateTimeUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.util.ObjectUtils;
import org.thymeleaf.util.StringUtils;

import javax.annotation.Resource;
import javax.transaction.Transactional;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Service
@Transactional
@Slf4j
public class MemberService {

	@Resource
	private MemberRepository memberRepository;
	@Resource
	private SecurityUtils securityUtils;
	@Resource
	private MemberMapper memberMapper;
	@Resource
	private PasswordEncoder passwordEncoder;
	@Resource
	private MemberRoleRepository memberRoleRepository;
	@Resource
	private RoleService roleService;
	@Resource
	private TeamRepository teamRepository;
	@Resource
	private OfficeHoursService officeHoursService;
	@Resource
	private MemberOfficeHoursRepository memberOfficeHoursRepository;
	@Resource
	private TeamService teamService;
	@Resource
	private TeamMemberRepository teamMemberRepository;
	@Resource
	private TeamMemberService teamMemberService;
	@Resource
	private BranchTeamRepository branchTeamRepository;
	@Resource
	private BranchService branchService;
	@Resource
	private RoleRepository roleRepository;

	@Resource
	private OfficeHoursMapper officeHoursMapper;
	@Resource
	private IssueService issueService;
	@Resource
	private BranchRepository branchRepository;
	@Resource
	private CounselEnvService counselEnvService;

	@Resource
	private BranchOfficeHoursRepository branchOfficeHoursRepository;

	@Resource
	private BranchOffDutyHoursRepository branchOffDutyHoursRepository;
	
	@Resource
	private CoreProperty coreProperty;

	@Resource
	private SystemMessageProperty systemMessageProperty;

	@Resource
	private WorkService workService;

	@Resource
	private BreakTimeService breakTimeService;

	@Resource
	private SystemEventService systemEventService;

	@Resource
	private ObjectMapper objectMapper;

	@Resource
	private PortalProperty portalProperty;

	private static final Pattern pattern = Pattern.compile("\\s+");
	
	public Member findById(@NotNull @Positive Long id) {
		return memberRepository.findById(id).orElse(null);
	}

	public List<Member> findAll(@NotNull Example<Member> example) {
		return memberRepository.findAll(example);
	}

	public long count(@NotNull Example<Member> example) {
		return memberRepository.count(example);
	}
	
	/**
	 * 상담원 개별설정 JSON 으로 저장
	 *
	 * @param setting
	 * @return
	 */
	public Map<String, Object> setting(Map<String, Object> setting) {
		Member member = memberRepository.findById(securityUtils.getMemberId()).orElse(null);
		Assert.notNull(member, "member null id:" + securityUtils.getMemberId());
		member.setSetting(setting);
		memberRepository.save(member);
		return setting;
	}

	/**
	 * 상담가능 수정
	 *
	 * @param id
	 * @param status
	 * @return
	 */
	public boolean status(Long id, WorkType.OfficeHoursStatusType status) throws Exception {
		Member member = this.findById(id);
		Member beforeMember = new Member();
		BeanUtils.copyProperties(member , beforeMember );
		Assert.notNull(member, "member is null");
		member.setStatus(status);
		memberRepository.save(member);
		this.systemEventStore(SystemEventHistoryActionType.member_update , beforeMember , member);
		return true;
		/* TODO : (tim.c) 내재화 QA 이후 아래 코드로 치환(상담원 상담 상태 브랜치와 연동 로직 추가)
		boolean result = true;
		Member member = memberRepository.findById(id).orElseThrow(() -> new BizException("Not Existed Member"));
		Branch branch = branchRepository.findById(member.getBranchId()).orElseThrow(() -> new BizException("Not existed Branch"));

		if (branch.getAssign().equals(WorkType.Cases.branch)) {
			if (workService.offDutyHours(branch)) {
				BranchOfficeHours branchOfficeHours = branchOfficeHoursRepository.findByBranchId(branch.getId());
				result = officeHoursService.isOfficeHours(
						branchOfficeHours.getStartCounselTime(),
						branchOfficeHours.getEndCounselTime(),
						branchOfficeHours.getDayOfWeek());
			}
		}

		if (result) {
			Member beforeMember = new Member();
			BeanUtils.copyProperties(member, beforeMember);
			member.setStatus(status);
			memberRepository.save(member);
			this.systemEventStore(SystemEventHistoryActionType.member_update, beforeMember, member);
		}
		return result;*/
	}

	/**
	 * 브랜치에 소속된 팀들에 소속된 유저
	 */
	public List<Member> findByTeamBelongBranch(@NotNull @Positive Long branchId) {
		List<Long> teamIds = branchTeamRepository.findAllByBranchIdOrderByIdDesc(branchId).stream().map(item -> item.getTeam().getId()).collect(Collectors.toList());

		log.info("BRANCH TEAM ID: {}", teamIds);
		List<Long> memberIds = teamMemberRepository.findAllByTeamIdIn(teamIds).stream().map(TeamMember::getMemberId).distinct().collect(Collectors.toList());
		log.info("BRANCH TEAM MEMBER ID: {}", memberIds);
		return memberRepository.findAllByIdInAndEnabled(memberIds, true);
	}

	/**
	 * 회원 목록
	 */
	public Page<MemberDto> getAll(@NotNull Pageable pageable) {
		Page<Member> items = memberRepository.findAll(pageable);
		List<MemberDto> data = memberMapper.map(items.getContent());
		return new PageImpl<>(data, items.getPageable(), items.getTotalElements());
	}
	
	/**
	 * 총 멤버수 카운트
	 */
	public long getAccount() {
		return memberRepository.count();
	}

	/**
	 * 인사말 검증
	 * @param issuePayload
	 */
	private void validFirstMessage(IssuePayload issuePayload) {

		List<IssuePayload.Chapter> chapters = issuePayload.getChapters();

		if (chapters == null || chapters.isEmpty()) throw new IllegalArgumentException("chapters can not be empty");

		List<IssuePayload.Section> sections = null;

		for (IssuePayload.Chapter chapter : chapters) {
			sections = chapter.getSections();
			if (sections == null || sections.isEmpty()) throw new IllegalArgumentException("sections can not be empty");

			for (IssuePayload.Section section : sections) {
				if (section.getType() == null) throw new IllegalArgumentException("section type can not be null");

				//인사말에 파일 첨부는 이미지파일만 가능. 따라서 섹션 속성이 버튼은 없음
				if (section.getActions() != null && !section.getActions().isEmpty()) {
					for (IssuePayload.Action action : section.getActions()) {
						if (action.getType() == null || action.getData() == null || action.getData().isEmpty()) throw new IllegalArgumentException("action's type and data can not be empty");
					}
				}
			}
		}
	}

	/**
	 * @수정일자		/ 수정자		 	/ 수정내용
	 * 2023.05.31	/ asher.shin	/ 상담원 생성/수정시 근무 외 상담가능 여부 추가
	 * 2024.05.27	/ tim.c			/ 첫 인사말 사용 유무 체크 기능 추가
	 * @param dto
	 * @return
	 */
	public MemberDto store(@NotNull MemberDto dto) throws Exception {

		// todo 고도화로.. 계정 수정 버그로 인하여 주석처리 validation 체크는
		// if (pattern.matcher(dto.getUsername()).matches() || dto.getUsername().contains(" ")) throw new IllegalArgumentException("username must not contain space");

		// Member 저장
		Member member;
		Member beforeMember = new Member();
		Branch branch = branchService.findById(dto.getBranchId());
		Assert.notNull(branch, "not null branch id:" + dto.getBranchId());

		boolean branchHasTeam = true;
		List<Team> teams = new ArrayList<>();
		if (!ObjectUtils.isEmpty(dto.getTeamIds())) {
			teams = teamService.findAllById(dto.getTeamIds());
			for (Team team : teams) {
				branchHasTeam = branchService.branchHasTeam(branch, team);
				if (!branchHasTeam) {
					break;
				}
			}
		}

		// branch team 이 안맞기 때문에 저장을 안한다
		if (!branchHasTeam) {
			return null;
		}

		if (dto.getId() == null) {
			/**
			 * FIXME :: vndrCustNo BNK 로직 volka
			 */
			//신규 멤버 등록 시 vndrCustNo 중복체크
//			Optional<Member> checkVndrCustNo = memberRepository.findByVndrCustNo(dto.getVndrCustNo());
//			log.info("#####상담직원번호: {}", checkVndrCustNo);
//			if (checkVndrCustNo.isPresent()) {
//				// 'vndrCustNo'이 이미 사용중인 경우 예외처리
//				log.error("VndrCustNo '" + dto.getVndrCustNo() + "' is already in use.");
//			}

			//vndrCustNo == userName으로 수정 (Member.getVndrCustNo() :: username 리턴으로 되어있음 20241017 기준)
			Optional<Member> checkUsername = Optional.ofNullable(memberRepository.findByUsername(dto.getUsername()));
			if (checkUsername.isPresent()) throw new BizException("this username already exists");

			member = memberMapper.map(dto);
			member.setPassword(passwordEncoder.encode(dto.getUsername()));
			member.setCreator(securityUtils.getMemberId());
			member.setCreated(ZonedDateTime.now());
			//240101/ YO / 상담원 직원 번호 추가
			member.setVndrCustNo(dto.getVndrCustNo());
			member.setOutsourcing(dto.getOutsourcing());
			// default 최대 상담건수

			int maxCounsel = branch.getMaxCounsel() != null ? branch.getMaxCounsel() : 0;

			if(WorkType.MaxCounselType.individual.equals(branch.getMaxCounselType())) {
				maxCounsel = branch.getMaxMemberCounsel() != null ? branch.getMaxMemberCounsel() : 0;
			}

			member.setMaxCounsel(maxCounsel);
			// 상담 가능 상태
			member.setStatus(getStatusType(dto));
		} else {
			member = memberRepository.findById(dto.getId()).orElse(null);
			BeanUtils.copyProperties(member , beforeMember );
			Assert.notNull(member, "member is null");
			CommonUtils.copyNotEmptyProperties(dto, member);
		}

		member.setModifier(securityUtils.getMemberId());
		member.setModified(ZonedDateTime.now());

		// 회원 첫 인사말
		// TODO: 첫 인사말 안넘어오는 경우, 삭제 필요 (GNB > 내 정보 수정)
		// TODO: 그 외 첫 인사말이 화면에 아에 없는 경우 (계정 관리 등) 예외 필요 (URL 분리 필요)
		if (dto.getUsedMessage() == null || !dto.getUsedMessage()) {
			member.setUsedMessage(false);
			member.setFirstMessage(null);
		} else {
			validFirstMessage(dto.getFirstMessage());

			member.setUsedMessage(true);
			member.setFirstMessage(dto.getFirstMessage());
		}

		member = memberRepository.save(member);

		// Member Role 매칭 저장
		if (!ObjectUtils.isEmpty(dto.getRoleList())) {
			List<Role> roles = roleService.findAllByIdIn(dto.getRoleList());
			List<MemberRole> memberRoles = new ArrayList<>();
			for (Role role : roles) {
				memberRoles.add(MemberRole.builder().roleId(role.getId()).memberId(member.getId()).modifier(securityUtils.getMemberId()).modified(ZonedDateTime.now()).build());
			}
			memberRoleRepository.deleteByMemberId(member.getId());
			memberRoleRepository.flush();
			memberRoleRepository.saveAll(memberRoles);
			member.setRoles(roles);
		}

		// Team Member 매칭 저장
		if (!ObjectUtils.isEmpty(dto.getTeamIds())) {
			List<TeamMember> teamMembers = new ArrayList<>();
			for (Team team : teams) {
				teamMembers.add(TeamMember.builder().team(team).memberId(member.getId()).modifier(securityUtils.getMemberId()).modified(ZonedDateTime.now()).build());
			}
			teamMemberRepository.deleteByMemberId(member.getId());
			teamMemberRepository.flush();
			teamMemberRepository.saveAll(teamMembers);
			member.setTeams(teams);
		} else {
			teamMemberRepository.deleteByMemberId(member.getId());
			teamMemberRepository.flush();
		}

		// 상담원 생성시 브랜치 근무시간으로 초기 세팅
		// 등록시 근무시간 외 상담값 넣기
		if (dto.getId() == null) {
			boolean yn = false;
			if(dto.getOfficeHours() != null){
				yn = dto.getOfficeHours().getOffDutyCounselYn() != null ? dto.getOfficeHours().getOffDutyCounselYn() : false;
			}
			dto.setOfficeHours(officeHoursService.branch(securityUtils.getBranchId()));
			dto.getOfficeHours().setOffDutyCounselYn(yn);
		}


		// 근무시간
		if (!ObjectUtils.isEmpty(dto.getOfficeHours())) {
			OfficeHours officeHours = officeHoursService.member(dto.getOfficeHours(), member.getId());
			member.setOfficeHours(officeHours);

			boolean isOfficeHour = officeHoursService.isOfficeHours(officeHours.getStartCounselTime(), officeHours.getEndCounselTime(), officeHours.getDayOfWeek() );
			WorkType.OfficeHoursStatusType status = isOfficeHour ? WorkType.OfficeHoursStatusType.on : WorkType.OfficeHoursStatusType.off;
			member.setStatus(status);
		}
		member.setBranch(branch);
		memberRepository.save(member);
		// SystemEventHistory 저장 (update의 경우 MemberEventListener 사용 X )
		if(Objects.nonNull(dto.getId())){
			this.systemEventStore(SystemEventHistoryActionType.member_update , beforeMember , member);
		}
		return memberMapper.map(member);
	}

	/**
	 * 상담원 역할을 가지고 있는 경우, 상담 가능 상태 활성화
	 */
	private WorkType.OfficeHoursStatusType getStatusType(@NotNull MemberDto memberDto) {

		if (memberDto.getRoleList() != null) {
			if (roleService.hasLevelByRoleId(memberDto.getRoleList(), Level.ROLE_TYPE_OPERATOR)) {
				return WorkType.OfficeHoursStatusType.on;
			}
		}
		return WorkType.OfficeHoursStatusType.off;
	}

	/**
	 * 비밀번호 초기화
	 *
	 * @param memberId
	 * @return
	 */
	public boolean resetPassword(Long memberId) throws Exception {
		Member member = memberRepository.findById(memberId).orElse(null);
		Member beforeMember = new Member();
		if (member != null) {
			BeanUtils.copyProperties(member , beforeMember );
			member.setPassword(passwordEncoder.encode(member.getUsername()));
			memberRepository.save(member);
			this.systemEventStore(SystemEventHistoryActionType.member_password , beforeMember , member);
			return true;
		}
		return false;
	}

	/**
	 * 계정 username 중복검사
	 *
	 * @param userName
	 * @return
	 */
	public boolean duplicationUserName(@NotEmpty String userName) {
		Member member = memberRepository.findByUsername(userName);
		if (member == null)
			return true;
		return false;
	}
	/**
	 * 상담직원번호 vndrCustNo 중복검사
	 *
	 * @param vndrCustNo
	 * @return
	 */
	public boolean duplicationVndrCustNo(@NotEmpty String vndrCustNo){
		Optional<Member> member = memberRepository.findByVndrCustNo(vndrCustNo);
		//상담직원번호가 존재하지 않으면 ture, 존재하면 false
        return !member.isPresent();
    }

	/**
	 * 회원 정보
	 */
	public MemberDto get(@NotNull Long id) {
		Member entity = memberRepository.findById(id).orElse(null);
		if (entity != null) {

			// 브랜치
			Branch branch = branchService.findById(entity.getBranchId());
			entity.setBranch(branch);

			Set<Long> teamIds = teamMemberRepository.findAllByMemberId(id)
					.stream()
					.map(item -> item.getTeam().getId())
					.collect(Collectors.toSet());

			// 소속
			List<Team> teams = teamRepository.findAllById(teamIds);
			List<BranchTeam> branchTeams = branchTeamRepository.findAllByBranchAndTeamIn(branch, teams);

			for (Team team : teams){
				for (BranchTeam branchTeam : branchTeams){
					log.info("TEAM ID {}, BRANCH TEAM ID :{}",team.getId() , branchTeam.getTeam().getId());
					if(team.getId().equals(branchTeam.getTeam().getId())){
						team.setOwnerId(branchTeam.getMember().getId());
					}
				}
			}

			entity.setTeams(teams);

			// 근무시간
			OfficeHours officeHours = officeHoursService.member(entity.getId());
			entity.setOfficeHours(officeHours);

			/**
			 * TODO :: 유입경로 sync-client-id 사용
			 */
			// 상담 유입경로
			List<CounselInflowEnvDto> inflowEnvs = counselEnvService.findAllAndEnabled(entity.getBranchId());
			inflowEnvs.stream().forEach(item -> item.setValue(StringUtils.replace(item.getValue(), "{{sync_key}}", coreProperty.getSyncClientId())));
			entity.setInflowEnvs(inflowEnvs);
			
			// 역할
			List<Long> roleIds = memberRoleRepository.findAllByMemberId(entity.getId()).stream().map(MemberRole::getRoleId).collect(Collectors.toList());

			List<Role> roles = roleService.findAllByIdIn(roleIds);
			entity.setRoles(roles);
			return memberMapper.map(entity);
		}
		return null;
	}




	public Page<MemberDto> items(Pageable pageable) {
		Long branchId = securityUtils.getBranchId();
//        log.info("MEMBER LIST BRANCH ID:{}",branchId);

		Page<Member> members;
		if (branchId != null) {
			members = memberRepository.findAllByBranchIdOrderByIdDesc(branchId, pageable);

		} else {
			members = memberRepository.findAllByOrderByIdDesc(pageable);
		}

//        List<Long> branchIds = members.getContent().stream()
//                .map(Member::getBranchId).collect(Collectors.toList());
//        List<Branch> branches = branchService.findAllById(branchIds);

		Branch branch = branchService.findById(branchId);
		List<Long> memberIds = members.getContent().stream().map(Member::getId).collect(Collectors.toList());

		List<MemberRole> memberRoles = memberRoleRepository.findAllByMemberIdIn(memberIds);
		List<TeamMember> teamMembers = teamMemberRepository.findAllByMemberIdIn(memberIds);

		List<Member> membersList = members.getContent().stream().peek(item -> {
			List<Long> roleIds = memberRoles.stream().filter(q -> q.getMemberId().equals(item.getId())).map(MemberRole::getRoleId).collect(Collectors.toList());
			List<Role> roles = roleService.findAllByIdIn(roleIds);
			item.setRoles(roles);
			List<Team> teams = teamMembers.stream().filter(q -> q.getMemberId().equals(item.getId())).map(TeamMember::getTeam).collect(Collectors.toList());
			item.setTeams(teams);
			item.setBranch(branch);
		}).collect(Collectors.toList());
		return new PageImpl<>(memberMapper.map(membersList), members.getPageable(), members.getTotalElements());
	}

	/**
	 * 브랜치 별 회원 목록
	 *
	 * @param branchId
	 * @return
	 */
	public List<MemberDto> findByBranchId(Long branchId) {

		log.info("MEMBER LIST BRANCH ID:{}", branchId);

		List<Member> members = memberRepository.findAllByBranchIdOrderByBranchIdDesc(branchId);

		List<Long> memberIds = members.stream().map(Member::getId).collect(Collectors.toList());

		List<MemberRole> memberRoles = memberRoleRepository.findAllByMemberIdIn(memberIds);
		List<TeamMember> teamMembers = teamMemberRepository.findAllByMemberIdIn(memberIds);
		List<Member> items = members.stream().peek(item -> {
			List<Long> roleIds = memberRoles.stream().filter(q -> q.getMemberId().equals(item.getId())).map(MemberRole::getRoleId).collect(Collectors.toList());
			List<Role> roles = roleService.findAllByIdIn(roleIds);
			item.setRoles(roles);
			List<Team> teams = teamMembers.stream().filter(q -> q.getMemberId().equals(item.getId())).map(TeamMember::getTeam).collect(Collectors.toList());
			item.setTeams(teams);
		}).collect(Collectors.toList());
		return memberMapper.map(items);
	}

	/**
	 * 검색
	 */
	public Page<MemberDto> search(MemberSearchCondition condition, Pageable pageable) {

		// 파라미터 프로젝션, 팀 소속 유저 (teamId -> memberIds)
		if (!addMembersCondition(condition)) {
			return new PageImpl<>(Collections.emptyList());
		}

		Page<Member> memberPage = memberRepository.search(condition, pageable);
		List<Member> members = memberPage.getContent();

		return new PageImpl<>(memberMapper.map(members), memberPage.getPageable(), memberPage.getTotalElements());
	}

	/**
	 * 배정 가능 상담원 목록
	 *
	 * <li>상담 포탈 > 상담 직원 전환
	 * <li>상담 관리 > 상담 직원 전환
	 */
	public Page<MemberAssignDto> searchAssignable(MemberSearchCondition condition, Pageable pageable) {

		// 파라미터 프로젝션, 팀 소속 유저 (teamId -> memberIds)
		if (!addMembersCondition(condition)) {
			return new PageImpl<>(Collections.emptyList());
		}
		Set<Long> roles = new HashSet<>();
		roles.add(100L);
		condition.setRoleIds(roles);

		Page<Member> memberPage = memberRepository.search(condition, pageable);
		List<Member> members = memberPage.getContent();
		List<MemberAssignDto> memberAssignDtos = memberMapper.mapAssign(memberPage.getContent());

		log.info("MEMBER ASSIGN LIST {}", memberAssignDtos);

		// 브랜치 이름
		Set<Long> branchIds = members.stream().map(Member::getBranchId).collect(Collectors.toSet());
		List<BranchOfficeHours> branchOfficeHours = officeHoursService.getBranchOfficeHours(branchIds);
		List<Branch> branches = branchService.findAllById(branchIds).stream().peek(item -> {
			OfficeHours officeHours = branchOfficeHours.stream().filter(q -> item.getId().equals(q.getBranchId())).findFirst().orElse(null);
			item.setOfficeHours(officeHours);
		}).collect(Collectors.toList());

		CounselEnvDto counselEnvDto = counselEnvService.get(condition.getBranchId());


		// 근무시간체크
		Set<Long> memberIds = members.stream().map(Member::getId).collect(Collectors.toSet());
		List<MemberOfficeHours> memberOfficeHoursList = memberOfficeHoursRepository.findAllByMemberIdIn(memberIds);

		// 회원 소속
		List<Long> memberIdList = new ArrayList<>(memberIds);
		List<TeamMember> teamMembers = teamMemberService.findAllByMemberIdIn(memberIdList);

		boolean isWork = true;


		boolean isBreakTime = breakTimeService.inBreakTime();

		for (MemberAssignDto member : memberAssignDtos) {

			log.info("MEMBER ID:{} , ASSIGNABLE STATUS:{} ", member.getId(), member.getStatus());

			// 브랜치 구하기
			Branch branch = branches.stream().filter(item -> item.getId().equals(member.getBranchId())).findFirst().orElse(null);
			member.setBranchName(branch.getName());

			// 시스템 근무시간 / 개인 근무시간인지 여부 체크
			OfficeHours officeHours = this.getOfficeHoursUseWorkType(member, branch, memberOfficeHoursList);

			// 소속명 구하기
			Team team = teamMembers.stream().filter(q -> q.getMemberId().equals(member.getId())).map(TeamMember::getTeam).findFirst().orElse(null);
			if(Objects.nonNull(team)){
				member.setTeamName(team.getName());
			}

			member.setAssignable(true);
			// 오늘 휴무 인지 여부 체크 추가
			isWork = workService.offDutyHours(branch);
			if(!isWork){
				member.setAssignable(false);
				continue;
			}

			// 1. breakTime(휴식시간) 여부 체크
			if(isBreakTime){
				member.setAssignable(false);
				continue;
			}

			// 2. 온라인 / 오프라인 체크
			if (WorkType.OfficeHoursStatusType.off.equals(member.getStatus())) {
				member.setAssignable(false);
				continue;
			}

			// 3. 상담 인입 제한 체크
			if(Objects.nonNull(counselEnvDto) && counselEnvDto.getRequestBlockEnabled()){
				member.setAssignable(false);
				continue;
			}

			// 4. 근무시간 체크
			if (Objects.nonNull(officeHours)) {
				member.setAssignable(officeHoursService.isOfficeHours(officeHours));
			}
		}

		// 상담원별 상담중 카운트
		Map<Long, Long> ongoingGroupByMember = issueService.countOngoingGroupByMember(memberIds);
		for (MemberAssignDto member : memberAssignDtos) {
			member.setOngoing(ongoingGroupByMember.get(member.getId()));
		}

		// 상담원별 상담대기 카운트
		Map<Long, Long> assignedGroupByMember = issueService.countAssignedGroupByMember(memberIds);
		for (MemberAssignDto member : memberAssignDtos) {
			member.setAssigned(assignedGroupByMember.get(member.getId()));
		}

		return new PageImpl<>(memberAssignDtos, memberPage.getPageable(), memberPage.getTotalElements());
	}

	private boolean addMembersCondition(@NotNull MemberSearchCondition condition) {

		if (condition.getTeamId() != null) {
			List<TeamMember> teamMembers = teamMemberService.findAllByTeamId(condition.getTeamId());
			if (teamMembers.isEmpty()) {
				return false;
			}
			condition.setIds(teamMembers.stream().map(TeamMember::getMemberId).collect(Collectors.toSet()));
		}
		return true;
	}

	public Page<MemberDto> items(MemberSearchCondition condition, Pageable pageable) {

		// 파라미터 프로젝션, team -> members
		if (condition.getTeamId() != null) {
			List<TeamMember> teamMembers = teamMemberService.findAllByTeamId(condition.getTeamId());
			if (teamMembers.isEmpty()) {
				return new PageImpl<>(Collections.emptyList());
			}
			condition.setIds(teamMembers.stream().map(TeamMember::getMemberId).collect(Collectors.toSet()));
		}

		// 파라미터 프로젝션, levelType -> roles -> members
		if (!ObjectUtils.isEmpty(condition.getLevelType())) {
			List<Role> roles = roleService.findAllByLevelTypeIn(condition.getLevelType());
			if (!roles.isEmpty()) {
				List<MemberRole> memberRoles = memberRoleRepository.findAllByRoleIdIn(roles.stream().map(Role::getId).collect(Collectors.toSet()));
				Set<Long> memberIds = memberRoles.stream().map(MemberRole::getMemberId).collect(Collectors.toSet());
				if (ObjectUtils.isEmpty(condition.getIds())) {
					condition.setIds(memberIds);
				} else {
					condition.getIds().retainAll(memberIds);
				}
			}
		} else {
			if (securityUtils.hasRole("ROLE_MANAGER")) {
				List<TeamMember> teamMembers = teamMemberRepository.findAllByTeamId(securityUtils.getTeamId());
				condition.setIds(teamMembers.stream().map(TeamMember::getMemberId).collect(Collectors.toSet()));
			}
		}

		// 브랜치 세팅
		if (condition.getBranchId() == null) {
			// 마스터 계정이 아니면 소속 브랜치만 조회 가능
			// TODO: 정책, 모든 계정을 조회 가능한 계쩡은? 본사 브랜치의 어드민?
			if (!securityUtils.hasRole(Level.ROLE_MASTER)) {
				condition.setBranchId(securityUtils.getBranchId());
			}
		}

		// 시스템 계정 제외
		condition.setManaged(true);

		Page<Member> memberPage = memberRepository.search(condition, pageable);
		List<Member> members = memberPage.getContent();

		// member ids
		Set<Long> memberIds = members.stream().map(Member::getId).collect(Collectors.toSet());
		Set<Long> branchIds = members.stream().map(Member::getBranchId).collect(Collectors.toSet());

		List<Branch> branchs = branchService.findAllById(branchIds);

		// 회원 역할
		List<MemberRole> memberRoles = memberRoleRepository.findAllByMemberIdIn(memberIds);

		// 회원 소속
		List<Long> memberIdList = new ArrayList<>(memberIds);
		List<TeamMember> teamMembers = teamMemberService.findAllByMemberIdIn(memberIdList);

		List<Member> membersList = members.stream().peek(item -> {
			List<Long> roleIds = memberRoles.stream().filter(q -> q.getMemberId().equals(item.getId())).map(MemberRole::getRoleId).collect(Collectors.toList());
			List<Role> roles = roleService.findAllByIdIn(roleIds);
			item.setRoles(roles);
			List<Team> teams = teamMembers.stream().filter(q -> q.getMemberId().equals(item.getId())).map(TeamMember::getTeam).collect(Collectors.toList());
			item.setTeams(teams);

			Branch branch = branchs.stream().filter(q -> q.getId().equals(item.getBranchId())).findFirst().orElse(null);

			if (!ObjectUtils.isEmpty(branch)) {
				item.setBranch(branch);
			}
		}).collect(Collectors.toList());

		return new PageImpl<>(memberMapper.map(membersList), memberPage.getPageable(), memberPage.getTotalElements());
	}

	/**
	 * 상담원 목록 (PoC)
	 */
	public List<MemberDto> searchByRole(@NotEmpty String roleType) {

		Role role = roleService.findOne(Example.of(Role.builder().type(roleType).build()));
		if (role != null) {
			List<MemberRole> memberRoles = memberRoleRepository.findAll(Example.of(MemberRole.builder().roleId(role.getId()).build()));

			if (!memberRoles.isEmpty()) {
				List<Long> memberIds = memberRoles.stream().map(MemberRole::getMemberId).collect(Collectors.toList());
				List<Member> entities = memberRepository.findByIdIn(memberIds);
				return memberMapper.map(entities);
			}
		}

		return Collections.emptyList();
	}

	/**
	 * 상담원 목록 (PoC)
	 */
	public Page<MemberDto> search() {

		List<Member> entities = memberRepository.findAll();
		return new PageImpl<>(memberMapper.map(entities));
	}

	/**
	 * 상담원이 상태 및 근무 시간 기준으로 상담 가능한가 여부를 확인함
	 *
	 * @param branchId - branch ID
	 * @param memberId - 상담사 ID
	 * @return 상담 가능 여부
	 */
	public boolean isMemberOnWorking(Long branchId, Long memberId) {
		boolean onWorking = true;
		Member member = memberRepository.findById(memberId).get();
		// status off 면 상담 안 함
		if (member == null || member.getStatus().equals(WorkType.OfficeHoursStatusType.off) || !member.getEnabled())
			return !onWorking;

		final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("E,H:m", Locale.ENGLISH);
		ZonedDateTime nowDT = ZonedDateTime.now();
		String[] temp = nowDT.format(FORMATTER).split(",");
		String currentDayOfWeek = temp[0].toUpperCase();
		String currentHourMinute = temp[1];

		log.info("currentDayOfWeek {} , currentHourMinute {}", currentDayOfWeek, currentHourMinute);
		OfficeHours officeHours = null;
		Branch branch = branchRepository.findById(branchId).get();

		if (branch.getAssign().equals(WorkType.Cases.branch)) {// 근무시간 설정이 시스텡으로 설정되어 있으면, office hour와 off-hour를 체크
			log.info("WorkType.Cases.branch {}", WorkType.Cases.branch);

			if (branch.getOffDutyHours()) {// 근무시간 예외가 true면
				List<OffDutyHours> offDutyHours = branchOffDutyHoursRepository.findAllByBranchId(branchId);

				for (OffDutyHours o : offDutyHours) {
					if (o.getEnabled() && nowDT.compareTo(o.getStartCreated()) > 0 && nowDT.compareTo(o.getEndCreated()) < 0)
						return !onWorking;
				}
			}

			officeHours = branchOfficeHoursRepository.findByBranchId(branchId);
		} else {// 근무시간 설정이 상담사로 설정되어 있으면, office hour만 체크
			officeHours = memberOfficeHoursRepository.findByMemberId(memberId);
		}

		if (officeHours != null) {
			log.info("officeHours.getDayOfWeek() {}, currentDayOfWeek {}", officeHours.getDayOfWeek(), currentDayOfWeek);

			if (!officeHours.getDayOfWeek().contains(currentDayOfWeek))
				return !onWorking;

			log.info("startCounselTime {}, endCounselTime {}, currentHourMinute {}", officeHours.getStartCounselTime(), officeHours.getEndCounselTime(), currentHourMinute);

			if (currentHourMinute.compareTo(toTimeFormat(officeHours.getStartCounselTime())) < 0 || currentHourMinute.compareTo(toTimeFormat(officeHours.getEndCounselTime())) > 0)
				return !onWorking;
		} //else
			//log.info("officeHours == null");

		return onWorking;
	}

	private static String toTimeFormat(String time) {
		String hour = time.substring(0, time.indexOf(":"));
		String minute = time.substring(time.indexOf(":") + 1);

		if (hour.length() == 1)
			hour = "0" + hour;

		if (minute.length() == 1)
			minute = "0" + minute;

		return hour + ":" + minute;
	}

	/**
	 * 비밀번호 변경
	 *
	 * @param dto
	 * @return boolean
	 * @throws
	 *
	 * @수정일자 / 수정자 / 수정내용
	 * 2023.04.04 / philip.lee7 / 함수추가
	 */
	public Map<String,Object> changePassword(MemberPassDto dto) throws Exception {
		Member member = memberRepository.findById(dto.getId()).orElse(null);
		Map<String,Object> map = new HashMap<String,Object>();
		if (member != null) {
			// 비밀번호 체크
			if (!passwordEncoder.matches(dto.getPassword(), member.getPassword())) {
				map.put("result",false);
				map.put("message",systemMessageProperty.getPortal().getPassword().getChangeMessage().getNotMatch());

			} else if(!confirmPasswordRule(dto)) {
				map.put("result",false);
				map.put("message",systemMessageProperty.getPortal().getPassword().getChangeMessage().getNotPasswordRule());
			}else if(!dto.getNewPassword().equals(dto.getConfirmNewPassword())) {
				//신규 비밀번호와 확인 비밀번호와 체크
				map.put("result",false);
				map.put("message",systemMessageProperty.getPortal().getPassword().getChangeMessage().getNotEqualConfirm());
			}else  {
				Member beforeMember = new Member();
				BeanUtils.copyProperties(member , beforeMember );
				member.setPassword(passwordEncoder.encode(dto.getNewPassword()));
				member.setModified(ZonedDateTime.now());
				member.setModifier(securityUtils.getMemberId());
				memberRepository.save(member);
				this.systemEventStore(SystemEventHistoryActionType.member_password , beforeMember , member);
				map.put("result", true);
			}
		}
		return map;

		 /*else if (confirmSafetyPassword(dto) ){
			map.put("result",false);
			map.put("message",systemMessageProperty.getPortal().getPassword().getChangeMessage().getNotSafety());
		}*/
	}


	public boolean confirmPasswordRule (MemberPassDto dto){
		boolean result = false;
		Pattern passPattern1 = Pattern.compile("^(?!((?:[A-Za-z]+)|(?:[~!@#$%^&*()_+=]+)|(?:[0-9]+))$)[A-Za-z\\d~!@#$%^&*()_+=]{10,16}$");

		Matcher matcher1 = passPattern1.matcher(dto.getNewPassword());

		return matcher1.find();

	}

	/**
	 * 근무 시간외 상담 가능 여부 변경
	 * @수정일자 / 수정자 /수정내용
	 *  2023.05.31 / asher.shin / 함수추가
	 */
	public MemberDto changeOffDuty(Long id){
		Member member = memberRepository.findById(id).orElse(null);

		Assert.notNull(member,"member is null");

		MemberOfficeHours officeHours = memberOfficeHoursRepository.findByMemberId(id);

		log.info("memberOfiiceHours : {}",officeHours);
		Boolean counselYn = officeHours.getOffDutyCounselYn();

		officeHours.setOffDutyCounselYn(counselYn ==null?false : !counselYn);
		officeHours.setModified(ZonedDateTime.now());
		memberOfficeHoursRepository.save(officeHours);

		member.setOfficeHours(officeHours);

		return memberMapper.map(member);
	}

	/**
	 * 상담 카테고리 변경
	 * @수정일자 / 수정자 / 수정내용
	 *  2023.05.31 / asher.shin / 함수추가
	 */
	public MemberDto changeCounselCateogry(Long id,String category){
		Member member = memberRepository.findById(id).orElse(null);

		Assert.notNull(member,"member is null");

		member.setCounselCategory(category);
		member.setModified(ZonedDateTime.now());

		memberRepository.save(member);

		return memberMapper.map(member);
	}

	/*public boolean confirmSafetyPassword (MemberPassDto dto) {
		Pattern passPattern = Pattern.compile("(012)|(123)|(234)|(345)|(456)|(567)|(678)|(789)|(890)");

		return passPattern.matcher(dto.getNewPassword()).find();

	}*/

	/**
	 * 그룹에 속하지 않은 멤버 and 팀장 아닌 멤버
	 * 	or
	 * 같은 그룹 and 팀장 아닌 멤버 and 무소속 매니저
	 */
	public Page<MemberDto> notIn(MemberSearchCondition condition, Pageable pageable) {
		Long teamId = condition.getTeamId();
//		if (condition.getTeamId() != null) {
//			List<TeamMember> teamMembers = teamMemberService.findAllByTeamId(condition.getTeamId());
//			if (teamMembers.isEmpty()) {
//				return new PageImpl<>(Collections.emptyList());
//			}
//			condition.setIds(teamMembers.stream().map(TeamMember::getMemberId).collect(Collectors.toSet()));
//		}

		if (securityUtils.isManager()) {
			List<TeamMember> teamMembers = teamMemberRepository.findAllByTeamId(securityUtils.getTeamId());
			condition.setIds(teamMembers.stream().map(TeamMember::getMemberId).collect(Collectors.toSet()));
		} else {
			condition.getLevelType().clear();
			condition.getLevelType().add("MANAGER"); //첫 조회 시 매니저 고정
			List<Role> roles = roleService.findAllByLevelTypeIn(condition.getLevelType());
			List<MemberRole> memberRoles = memberRoleRepository.findAllByRoleIdIn(roles.stream().map(Role::getId).collect(Collectors.toSet()));

			if (memberRoles == null || memberRoles.isEmpty()) {
				condition.getLevelType().clear();
				condition.getLevelType().add("ADMIN"); //매니저 미존재 시 어드민 조회
				roles = roleService.findAllByLevelTypeIn(condition.getLevelType());
				memberRoles = memberRoleRepository.findAllByRoleIdIn(roles.stream().map(Role::getId).collect(Collectors.toSet()));

				if (memberRoles == null || memberRoles.isEmpty()) return Page.empty();
			}

			Set<Long> memberIds = memberRoles.stream().map(MemberRole::getMemberId).collect(Collectors.toSet());
			condition.setIds(memberIds);
		}



		//기본 조회 매니저 -> 없을 때 어드민 조회
//		if (!ObjectUtils.isEmpty(condition.getLevelType())) {
//			List<Role> roles = roleService.findAllByLevelTypeIn(condition.getLevelType());
//			if (!roles.isEmpty()) {
//				List<MemberRole> memberRoles = memberRoleRepository.findAllByRoleIdIn(roles.stream().map(Role::getId).collect(Collectors.toSet()));
//				Set<Long> memberIds = memberRoles.stream().map(MemberRole::getMemberId).collect(Collectors.toSet());
//				condition.setIds(memberIds);
//			}
//		} else {
//			if (securityUtils.hasRole("ROLE_MANAGER")) {
//				List<TeamMember> teamMembers = teamMemberRepository.findAllByTeamId(securityUtils.getTeamId());
//				condition.setIds(teamMembers.stream().map(TeamMember::getMemberId).collect(Collectors.toSet()));
//			}
//		}
		if (condition.getBranchId() == null) {
			if (!securityUtils.hasRole(Level.ROLE_MASTER)) {
				condition.setBranchId(securityUtils.getBranchId());
			}
		}

		condition.setManaged(true);
		Page<Member> memberPage = memberRepository.search(condition, pageable);
		List<Member> members = memberPage.getContent();

		Set<Long> memberIds = members.stream().map(Member::getId).collect(Collectors.toSet());
		List<MemberRole> memberRoles = memberRoleRepository.findAllByMemberIdIn(memberIds);

		Long[] memberIdArray = memberIds.toArray(new Long[0]);

		List<BranchTeam> byMemberId = branchTeamRepository.findByMemberIdIn(memberIdArray);
		List<Long> branchTeamMemberId = byMemberId.stream()
				.map(BranchTeam::getMember)
				.map(Member::getId)
				.collect(Collectors.toList());

		List<Long> memberIdList = new ArrayList<>(memberIds);
		List<TeamMember> teamMembers = teamMemberService.findAllByMemberIdIn(memberIdList);

		if(teamId != null){
			List<Long> membersWithSameTeamIds = teamMembers.stream()
					.filter(member -> member.getTeam().getId().equals(teamId))
					.map(TeamMember::getMemberId)
					.collect(Collectors.toList());
			List<MemberRole> byRoleId = memberRoleRepository.findAll();
			List<Role> allRoles = roleRepository.findAll();
			List<String> levelType = condition.getLevelType();
			List<Long> allManagers = allRoles.stream()
					.filter(role -> role.getType().contains(levelType.get(0)))
					.map(Role::getId)
					.collect(Collectors.toList());

			List<MemberRole> matchingRoles = byRoleId.stream()
					.filter(memberRole -> allManagers.contains(memberRole.getRoleId()))
					.collect(Collectors.toList());

			List<Long> roleMemberIds = matchingRoles.stream().map(MemberRole::getMemberId).collect(Collectors.toList());
			List<TeamMember> teamMemberId = teamMemberService.findAllByMemberIdIn(roleMemberIds);
			List<Long> teamMemberIds = teamMemberId.stream().map(TeamMember::getMemberId).collect(Collectors.toList());

			Long[] idArray = byRoleId.stream().map(MemberRole::getMemberId).toArray(Long[]::new);

			List<BranchTeam> byMemberIdIn = branchTeamRepository.findByMemberIdIn(idArray);
			List<Long> branchTeamMember = byMemberIdIn.stream()
					.map(BranchTeam::getMember)
					.map(Member::getId)
					.collect(Collectors.toList());

			List<Member> allMembers = memberRepository.findAllByBranchIdOrderByBranchIdDesc(condition.getBranchId());

			List<Member> membersNotInTeamAndManager = allMembers.stream()
					.filter(member -> roleMemberIds.contains(member.getId()))
					.filter(member -> !teamMemberIds.contains(member.getId()))
					.filter(member -> !branchTeamMember.contains(member.getId()))
					.collect(Collectors.toList());

			List<Member> filteredMembers = members.stream()
					.filter(member -> !branchTeamMemberId.contains(member.getId()))
					.filter(member -> membersWithSameTeamIds.contains(member.getId()))
					.collect(Collectors.toList());

			filteredMembers.addAll(membersNotInTeamAndManager);

			List<Member> membersList = filteredMembers.stream().peek(item -> {
				List<Long> memberRole = memberRoles.stream().filter(q -> q.getMemberId().equals(item.getId())).map(MemberRole::getRoleId).collect(Collectors.toList());
				List<Role> roles = roleService.findAllByIdIn(memberRole);
				List<Long> noTeamRoles = byRoleId.stream().filter(q -> q.getMemberId().equals(item.getId())).map(MemberRole::getRoleId).collect(Collectors.toList());
				List<Role> noTeams = roleService.findAllByIdIn(noTeamRoles);
				item.setRoles(roles);
				item.setRoles(noTeams);
				List<Team> teams = teamMembers.stream().filter(q -> q.getMemberId().equals(item.getId())).map(TeamMember::getTeam).collect(Collectors.toList());
				item.setTeams(teams);

			}).collect(Collectors.toList());

			return new PageImpl<>(memberMapper.map(membersList), memberPage.getPageable(), membersList.size());
		} else {
			List<Member> memberList = members.stream()
					.map(item -> {
						List<Long> roleIds = memberRoles.stream()
								.filter(q -> q.getMemberId().equals(item.getId()))
								.map(MemberRole::getRoleId)
								.collect(Collectors.toList());

						List<Role> roles = roleService.findAllByIdIn(roleIds);
						item.setRoles(roles);
						return item;
					})
					.filter(item -> {
						if (!item.getEnabled())
							return false;
						List<Team> teams = teamMembers.stream()
								.filter(q -> q.getMemberId().equals(item.getId()))
								.map(TeamMember::getTeam)
								.collect(Collectors.toList());

						List<BranchTeam> branchTeams = branchTeamRepository.findByTeamId(item.getId());
						if (branchTeams.isEmpty()) {
							return true;
						}

						boolean memberIdPresent = branchTeams.stream()
								.anyMatch(bt -> bt.getMember().getId().equals(item.getId()));

						return teams.isEmpty() && !memberIdPresent;
					})
					.collect(Collectors.toList());

			return new PageImpl<>(memberMapper.map(memberList), memberPage.getPageable(), memberList.size());
		}
	}
	
	//총 멤버수 
	public long getTotalmembers() {
		return memberRepository.countByEnabledAndUsernameNot(true , "master1");
	}


	/**
	 * 시스템 설정 > 근무 조건 설정에서 > 근무 시간 기준이 시스템 or 상담직원 기준인지 여부에 따른 OfficeHours GET
	 * @param member
	 * @param branch
	 * @param memberOfficeHoursList
	 * @return
	 */
	private OfficeHours getOfficeHoursUseWorkType(MemberAssignDto member, Branch branch, List<MemberOfficeHours> memberOfficeHoursList) {
		if(Objects.isNull(branch)){
			return null;
		}
		OfficeHours officeHours = null;

		if (branch.getAssign().equals(WorkType.Cases.branch)) {
			officeHours = branch.getOfficeHours();
		}
		if (branch.getAssign().equals(WorkType.Cases.member)) {
			officeHours = memberOfficeHoursList.stream().filter(item -> item.getMemberId().equals(member.getId())).findFirst().orElse(null);
		}
		log.info("ASSIGN TYPE:{} , ID:{} , OFFICE_HOURS:{}", branch.getAssign(), branch.getId(), officeHoursMapper.map(officeHours));
		return officeHours;
	}

	public OfficeHoursDto saveOfficeHours(@NotNull OfficeWorkDto officeWorkDto) {
		if(Objects.nonNull(officeWorkDto.getBranch()) && Objects.nonNull(officeWorkDto.getBranch().getAssign()) ){
			switch (officeWorkDto.getBranch().getAssign()) {
				case branch:
					this.updateMemberStatusUseOfficeWorkDto(officeWorkDto , securityUtils.getMemberId());
					break;
				case member:
					this.updateMemberStatusAssignMember(officeWorkDto.getBranch().getId() , securityUtils.getMemberId());
					break;
				default:
					break;
			}
		}
		OfficeHoursDto resultOfficeHoursDto = officeHoursService.branch(officeWorkDto , officeWorkDto.getBranch().getId() );
		return resultOfficeHoursDto;
	}

	public void updateMemberStatusAssignMember(Long branchId , Long modifier) {
		List<MemberStatusSyncDto> officeHoursDtoList = officeHoursService.getMemberAndMemberOfficeHoursListUseBranchId(branchId);
		this.updateMemberStatusUseOfficeHoursDtoList(officeHoursDtoList , modifier);
	}

	public void updateMemberStatusUseOfficeHoursDtoList(List<MemberStatusSyncDto> officeHoursDtoList , Long modifier) {
		List<Member> memberList = new ArrayList<>();
		for(MemberStatusSyncDto memberAndOfficeHourDto : officeHoursDtoList) {
			boolean isOfficeHour = officeHoursService.isOfficeHours ( memberAndOfficeHourDto.getStartCounselTime() , memberAndOfficeHourDto.getEndCounselTime() , memberAndOfficeHourDto.getDayOfWeek() );
			Member member = memberAndOfficeHourDto.toMemberEntity();
			this.setMemberStatusUseIsOfficeHour(member , isOfficeHour , modifier);
			memberList.add(member);
		}
		this.memberSaveAll(memberList);
	}

	public void updateMemberStatusUseOfficeWorkDto(OfficeWorkDto officeWorkDto , Long modifier){
		boolean isOfficeHour = officeHoursService.isOfficeHours( OfficeHoursTimeUtils.hours(officeWorkDto.getOfficeHours().getStartCounselHours() , officeWorkDto.getOfficeHours().getStartCounselMinutes())
																,OfficeHoursTimeUtils.hours(officeWorkDto.getOfficeHours().getEndCounselHours()   , officeWorkDto.getOfficeHours().getEndCounselMinutes()  )
																,OfficeHoursTimeUtils.dayOfWeek(officeWorkDto.getOfficeHours().getDayOfWeek())
															   );
		List<Member> memberList = memberRepository.findByEnabledAndBranchId(true, officeWorkDto.getBranch().getId());
		for(Member member : memberList){
			this.setMemberStatusUseIsOfficeHour(member, isOfficeHour , modifier);
		}
		this.memberSaveAll(memberList);
	}

	public void setMemberStatusUseIsOfficeHour(Member member, boolean isOfficeHour , Long modifier) {
		WorkType.OfficeHoursStatusType updateStatus = isOfficeHour ? WorkType.OfficeHoursStatusType.on : WorkType.OfficeHoursStatusType.off;
		WorkType.OfficeHoursStatusType currentStatus = member.getStatus();
		// 원래 엔티티에 어노테이션을 활용해야겠지만 영향도 파악이 어려워서 if문 사용
		if(currentStatus != updateStatus){
			member.setModified(ZonedDateTime.now());
			member.setModifier(modifier);
		}
		member.setStatus(updateStatus);
	}


	public Member getMemberUseMemberStatusSyncScheduler(MemberStatusSyncDto memberStatusSyncDto , Long modifier) {
		boolean isOfficeHour = ZonedDateTimeUtil.isMidNight() ? OfficeHoursTimeUtils.isDayOfWeek(memberStatusSyncDto.getDayOfWeek()) : officeHoursService.isOfficeHours (memberStatusSyncDto.getStartCounselTime(), memberStatusSyncDto.getEndCounselTime(), memberStatusSyncDto.getDayOfWeek() );
		Member member = memberStatusSyncDto.toMemberEntity();
		this.setMemberStatusUseIsOfficeHour( member, isOfficeHour , modifier);
		return member;
	}

	public void memberSaveAll(List<Member> memberList) {
		memberRepository.saveAll(memberList);
	}

	public void systemEventStore(SystemEventHistoryActionType action , Member beforeMember , Member member) throws JsonProcessingException {
		Member fromMember = this.findById(securityUtils.getMemberId());
		String beforePayload = objectMapper.writeValueAsString(beforeMember);
		String afterPayload = objectMapper.writeValueAsString(member);
		systemEventService.store( fromMember, member.getId(),  action,"Member",beforePayload , afterPayload , null , null , "UPDATE" , securityUtils.getTeamId());
	}

	public void memberStatusSyncJobSaveAll (List<Member> memberList) {
		this.memberSaveAll(memberList);
		Member fromMember = this.findById(portalProperty.getSystemMemberId());
		if(Objects.nonNull(fromMember)){
			systemEventService.store(fromMember , portalProperty.getSystemMemberId(), SystemEventHistoryActionType.schedule_member_status_sync , "Member" , null , null , null , null , "UPDATE",null);
		}
	}
}
