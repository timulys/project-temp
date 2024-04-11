package com.kep.portal.model.entity.member;

import com.kep.core.model.dto.member.MemberDto;
import com.kep.portal.model.dto.member.MemberAssignDto;
import com.kep.portal.model.entity.branch.BranchMapper;
import com.kep.portal.model.entity.privilege.RoleMapper;
import com.kep.portal.model.entity.work.OfficeHoursMapper;
import org.mapstruct.*;

import java.util.List;

@Mapper(componentModel = "spring"
		, uses = {BranchMapper.class, OfficeHoursMapper.class , RoleMapper.class}
		, unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface MemberMapper {

	@Mapping(target = "password", ignore = true)
	Member map(MemberDto dto);

	@Mapping(target = "password", ignore = true)
	MemberDto map(Member entity);

	List<MemberDto> map(List<Member> entities);

	@AfterMapping
	default void toAfter(Member entity , @MappingTarget MemberDto.MemberDtoBuilder dto){
		dto.branchId(null);
	}

	// ////////////////////////////////////////////////////////////////////////
	// MemberAssignDto
	MemberAssignDto mapAssign(Member entity);

	List<MemberAssignDto> mapAssign(List<Member> entities);
}
