package com.kep.portal.service.member;

import com.kep.portal.model.dto.member.response.GetMemberResponseDto;
import org.springframework.http.ResponseEntity;

public interface MemberServiceV2 {
    // Retrieve Methods
    ResponseEntity<? super GetMemberResponseDto> getMember(Long id);
}
