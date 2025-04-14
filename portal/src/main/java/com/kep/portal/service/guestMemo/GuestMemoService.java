package com.kep.portal.service.guestMemo;

import com.kep.portal.model.dto.guestMemo.request.PostGuestMemoRequestDto;
import com.kep.portal.model.dto.guestMemo.response.GetGuestMemoResponseDto;
import com.kep.portal.model.dto.guestMemo.response.PostGuestMemoResponseDto;
import org.springframework.http.ResponseEntity;

public interface GuestMemoService {
    // 고객(게스트) 메모 등록
    ResponseEntity<? super PostGuestMemoResponseDto> postGuestMemo(PostGuestMemoRequestDto dto);

    // 고객(게스트) 메모 조회
    ResponseEntity<? super GetGuestMemoResponseDto> findGuestMemo(Long guestId, Long customerId);

    // 고객(게스트) 메모 수정

    // 고객(게스트) 메모 삭제
}
