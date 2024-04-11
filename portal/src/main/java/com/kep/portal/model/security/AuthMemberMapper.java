package com.kep.portal.model.security;

import com.kep.portal.model.dto.member.AuthMemberDto;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring"
		, unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface AuthMemberMapper {

	AuthMember map(AuthMemberDto dto);

	// TODO: 역할만 필터링, 권한은 노출할 필요없음
	AuthMemberDto map(AuthMember entity);
}
