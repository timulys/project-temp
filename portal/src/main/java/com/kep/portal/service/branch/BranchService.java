package com.kep.portal.service.branch;

import com.kep.core.model.dto.branch.BranchChannelDto;
import com.kep.core.model.dto.branch.BranchDto;
import com.kep.core.model.dto.branch.BranchRoleDto;
import com.kep.core.model.dto.env.CounselEnvDto;
import com.kep.core.model.dto.privilege.RoleDto;
import com.kep.core.model.dto.system.SystemEnvDto;
import com.kep.core.model.dto.system.SystemEnvEnum;
import com.kep.core.model.dto.work.OfficeHoursDto;
import com.kep.core.model.dto.work.OfficeWorkDto;
import com.kep.core.model.dto.work.WorkType;
import com.kep.portal.model.dto.branch.BranchDtoWithRole;
import com.kep.portal.model.entity.branch.*;
import com.kep.portal.model.entity.channel.Channel;
import com.kep.portal.model.entity.member.Member;
import com.kep.portal.model.entity.privilege.Role;
import com.kep.portal.model.entity.privilege.RoleMapper;
import com.kep.portal.model.entity.team.Team;
import com.kep.portal.repository.branch.BranchChannelRepository;
import com.kep.portal.repository.branch.BranchRepository;
import com.kep.portal.repository.branch.BranchRoleRepository;
import com.kep.portal.repository.branch.BranchTeamRepository;
import com.kep.portal.repository.member.MemberRepository;
import com.kep.portal.repository.team.TeamRepository;
import com.kep.portal.service.channel.ChannelService;
import com.kep.portal.service.env.CounselEnvService;
import com.kep.portal.service.privilege.RoleService;
import com.kep.portal.service.work.OfficeHoursService;
import com.kep.portal.util.CommonUtils;
import com.kep.portal.util.SecurityUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.util.ObjectUtils;

import javax.annotation.Resource;
import javax.transaction.Transactional;
import javax.validation.constraints.NotNull;
import java.time.ZonedDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Transactional
@Slf4j
public class BranchService {

	@Resource
	private BranchRepository branchRepository;
	@Resource
	private BranchMapper branchMapper;

	@Resource
	private TeamRepository teamRepository;
	@Resource
	private BranchTeamRepository branchTeamRepository;
	@Resource
	private BranchChannelRepository branchChannelRepository;
	@Resource
	private BranchChannelMapper branchChannelMapper;
	@Resource
	private SecurityUtils securityUtils;

	@Resource
	private BranchRoleRepository branchRoleRepository;

	@Resource
	private RoleService roleService;

	@Resource
	private CounselEnvService counselEnvService;

	@Resource
	private MemberRepository memberRepository;

	@Resource
	private BranchRoleService branchRoleService;

	@Resource
	private RoleMapper roleMapper;
	@Resource
	private OfficeHoursService officeHoursService;
	@Resource
	private BranchChannelService branchChannelService;

	public Branch findById(@NotNull Long id) {

		return branchRepository.findById(id).orElse(null);
	}

	public List<Branch> findAllById(@NotNull Collection<Long> ids) {

		return branchRepository.findAllById(ids);
	}

	/**
	 * 신규직원 최대상담 건수 default
	 * @param id
	 * @param maxMemberCounsel
	 * @return
	 */
	public Branch maxMemberCounsel(Long id , Integer maxMemberCounsel){
		Branch branch = this.findById(id);
		Assert.notNull(branch,"not found branch , id:"+id);
		branch.setMaxMemberCounsel(maxMemberCounsel);
		branch.setModifier(securityUtils.getMemberId());
		branch.setModified(ZonedDateTime.now());
		return branchRepository.save(branch);
	}

	/**
	 * branch 사용중지
	 * @param id
	 * @param enabled
	 * @return
	 */
	public BranchDto enabled(@NotNull Long id , boolean enabled){
		Branch entity = this.findById(id);
		Assert.notNull(entity,"not found branch , id:"+id);

		//본사면 사용중지를 못함
		if(entity.getHeadQuarters()){
			return branchMapper.map(entity);
		}

		if(!enabled){
			List<Member> members = memberRepository.findAllByBranchIdOrderByBranchIdDesc(id);
			if(!ObjectUtils.isEmpty(members)){
				return branchMapper.map(entity);
			}
		}
		entity.setEnabled(enabled);
		entity.setModified(ZonedDateTime.now());
		entity.setModifier(securityUtils.getMemberId());
		return branchMapper.map(branchRepository.save(entity));
	}

	public BranchDto getById(@NotNull Long id) {

		Branch entity = this.findById(id);
		if(entity != null){
			List<Role> roles = branchRoleRepository.findAllByBranchId(id)
					.stream().map(BranchRole::getRole).collect(Collectors.toList());
			entity.setRoles(roles);
		}

		return branchMapper.map(entity);
	}

	public Page<BranchDto> getAll(@NotNull Pageable pageable) {

		Page<Branch> items = branchRepository.findAll(pageable);

		List<Long> branchIds = items.getContent().stream()
				.map(Branch::getId).collect(Collectors.toList());

		List<BranchRole> branchRoles = branchRoleRepository.findByBranchIdIn(branchIds);
		List<Branch> branches = items.getContent().stream()
				.peek(item->{
					List<Role> branchRoleList = branchRoles.stream()
							.filter(q->q.getBranchId().equals(item.getId()))
							.map(BranchRole::getRole)
							.collect(Collectors.toList());
					if(!branchRoleList.isEmpty()){
						item.setRoles(branchRoleList);
					}
				}).collect(Collectors.toList());
		return new PageImpl<>(branchMapper.map(branches), items.getPageable(), items.getTotalElements());
	}

	/**
	 * 브랜치-채널 매칭 목록 조회 (대표 채널이 있으면 대표 채널을 선택)
	 */
	public List<BranchChannelDto> getAllBranchChannel(@NotNull Boolean enabled, @NotNull Sort sort) {

		List<Branch> branches = branchRepository.findAll(Example.of(Branch.builder()
				.enabled(enabled).build()), sort);
		if (!branches.isEmpty()) {
			List<BranchChannel> branchChannels = branchChannelRepository.findAllByBranchIdIn(branches.stream().map(Branch::getId).collect(Collectors.toList()));
			List<BranchChannel> finalBranchChannels = new ArrayList<>();

			// 브랜치별 대표 채널 검색, 없으면 브랜치가 가지고 있는 임의 채널 선택
			for (Branch branch : branches) { // 정렬 유지
				BranchChannel finalBranchChannel = null;
				for (BranchChannel branchChannel : branchChannels) { // 대포 채널 (or 없는 경우 임의 채널) 선택
					if (branch.getId().equals(branchChannel.getBranch().getId())) {
						if (finalBranchChannel == null || !finalBranchChannel.getOwned()) {
							finalBranchChannel = branchChannel;
						}
					}
				}
				if (finalBranchChannel != null) {
					finalBranchChannels.add(finalBranchChannel);
				}
			}

			return branchChannelMapper.map(finalBranchChannels);
		}

		return Collections.emptyList();
	}

	/**
	 * 본사 브랜치
	 */
	public Branch findHeadQuarters() {

		List<Branch> branches = branchRepository.findAll(Example.of(Branch.builder()
				.enabled(true)
				.headQuarters(true)
				.build()), Sort.by(Sort.Direction.ASC, "id"));
		if (branches.isEmpty()) {
			log.warn("HEAD QUARTERS IS NOT FOUND");
			branches = branchRepository.findAll(Example.of(Branch.builder()
					.enabled(true)
					.build()), Sort.by(Sort.Direction.ASC, "id"));
		}

		Assert.isTrue(!branches.isEmpty(), "head quarters is not found");
		return branches.get(0);
	}

	/**
	 * 브랜치 생성
	 * @param dto
	 * @return
	 * @throws Exception
	 */
	public BranchDto store(@NotNull BranchDto dto) {

		Branch branch;
		Branch headQuarters = this.findHeadQuarters();
		if (dto.getId() == null) {
			branch = branchMapper.map(dto);
			branch.setCreator(securityUtils.getMemberId());
			branch.setCreated(ZonedDateTime.now());
			branch.setMaxCounsel(headQuarters.getMaxCounsel());
			branch.setMaxCounselType(headQuarters.getMaxCounselType());
			branch.setMaxMemberCounsel(headQuarters.getMaxMemberCounsel());
			branch.setAssign(headQuarters.getAssign());
		} else {
			branch = this.findById(dto.getId());
			CommonUtils.copyNotEmptyProperties(branchMapper.map(dto), branch);
			branch.setMaxCounsel(dto.getMaxCounsel());
			branch.setMaxCounselType(dto.getMaxCounselType());
			branch.setAssign(dto.getAssign());
			branch.setOffDutyHours(dto.getOffDutyHours());
		}

		branch.setModifier(securityUtils.getMemberId());
		branch.setModified(ZonedDateTime.now());
		branch = branchRepository.save(branch);

		if (dto.getId() == null) {
			branch.setRoles(roleService.basic(branch.getId()));
			counselEnvService.store(CounselEnvDto.builder()
							.branchId(branch.getId())
							.requestBlockEnabled(false)
							.memberAutoTransformEnabled(true)
							.issueAutoCloseEnabled(true)
							.issueDelay(SystemEnvDto.EnabledMinute.builder()
									.enabled(true)
									.minute(60)
									.build())
							.issueFileMimeType(SystemEnvDto.EnableFileMimeType.builder()
									.enabled(true)
									.fileMimeType(SystemEnvEnum.FileMimeType.all)
									.build())
					.build());

			//신규 생성시 본사 브랜치 설정 복사
			headQuartersClone(headQuarters , branch);

		}

		return branchMapper.map(branch);
	}

	/**
	 * 기본 설정값 본사 브랜치에서 복제
	 * @param headQuarters
	 * @param branch
	 */
	private void headQuartersClone(Branch headQuarters , Branch branch){

		//근무시간 설정
		OfficeHoursDto officeHoursDto = officeHoursService.branch(headQuarters.getId());
		OfficeWorkDto workDto = OfficeWorkDto.builder()
				.branchId(branch.getId())
				.memberId(securityUtils.getMemberId())
				.cases(WorkType.Cases.branch)
				.officeHours(officeHoursDto)
				.build();
		officeHoursService.branch(workDto , branch.getId());
		//채널 목록
//		branchChannelService.headQuartersClone(headQuarters , branch);
	}

	/**
	 * Branch hasMany Team
	 * @param branchId
	 * @return
	 * @throws Exception
	 */
	public List<Team> branchHasManyTeam(@NotNull Long branchId , Long teamId) {
		List<Long> teamIds;
		if(teamId != null && teamId > 0){
			teamIds = branchTeamRepository.findAllByBranchIdAndTeamIdOrderByIdDesc(branchId,teamId)
					.stream()
					.map(item->item.getTeam().getId()).collect(Collectors.toList());
		} else {
			teamIds = branchTeamRepository.findAllByBranchIdOrderByIdDesc(branchId)
					.stream()
					.map(item->item.getTeam().getId()).collect(Collectors.toList());
		}

		if(!teamIds.isEmpty()){
			return teamRepository.findAllById(teamIds);
		}
		return null;
	}

	/**
	 * 브랜치 소속 팀인지 체크
	 * @param branch
	 * @param team
	 * @return
	 */
	public boolean branchHasTeam(@NotNull Branch branch , @NotNull Team team){
		BranchTeam branchTeam = branchTeamRepository.findByBranchAndTeam(branch , team);
		return !ObjectUtils.isEmpty(branchTeam);
	}

	/**
	 * 브랜치 역할 설정
	 * @param dtos
	 * @return
	 */
	public List<BranchDtoWithRole> rolesDeleteSaveAll(List<BranchRoleDto> dtos){
		if(!ObjectUtils.isEmpty(dtos)){
			List<BranchDtoWithRole> branchDtoWithRoles = new ArrayList<>();
			for (BranchRoleDto branchRoleDto : dtos){
				Long branchId = branchRoleDto.getBranchId();
				Branch branch = this.findById(branchId);
				Assert.notNull(branch , "not found branch, id:"+branchId);
				List<RoleDto> roles = roleMapper.map(roleService.findAllByIdIn(branchRoleDto.getRoleList()));
				BranchDtoWithRole branchDtoWithRole = BranchDtoWithRole.builder()
						.id(branchId)
						.name(branch.getName())
						.headQuarters(branch.getHeadQuarters())
						.roles(roles)
						.build();

				branchDtoWithRoles.add(branchDtoWithRole);
				branchRoleService.store(branchDtoWithRole);
			}
			return branchDtoWithRoles;

		}
		return null;
	}
	/**
	 * todo 리팩토링 필요
	 * @param branchDto
	 * @return
	 */
	public BranchDto updateBranchMaxCounsel(BranchDto branchDto) {
		Branch branch = branchRepository.findById(branchDto.getId()).orElse(null);
		if(Objects.nonNull(branch)){
			branch.setMaxCounsel(branchDto.getMaxCounsel());
			branch.setMaxCounselType(branchDto.getMaxCounselType());
			branchRepository.save(branch);
		}
		return branchMapper.map(branch);
	}

}
