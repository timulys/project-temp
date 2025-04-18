package com.kep.portal.service.member;

import com.kep.portal.model.dto.member.response.GetMemberListResponseDto;
import org.springframework.http.ResponseEntity;

public interface MemberServiceV2 {
    // Retrieve Methods
    ResponseEntity<? super GetMemberListResponseDto> getTeamMember(Long teamId);
    ResponseEntity<? super GetMemberListResponseDto> getGroupManagerMember(String levelType, Long branchId);
}
