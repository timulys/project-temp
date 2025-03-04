package com.kep.portal.service.customer;

import com.kep.portal.model.dto.customerGroup.request.PostCustomerGroupRequestDto;
import com.kep.portal.model.dto.customerGroup.request.PutCustomerGroupRequestDto;
import com.kep.portal.model.dto.customerGroup.response.DeleteCustomerGroupResponseDto;
import com.kep.portal.model.dto.customerGroup.response.GetCustomerGroupListResponseDto;
import com.kep.portal.model.dto.customerGroup.response.PostCustomerGroupResponseDto;
import com.kep.portal.model.dto.customerGroup.response.PutCustomerGroupResponseDto;
import org.springframework.http.ResponseEntity;

public interface CustomerGroupService {
    // 그룹 등록
    ResponseEntity<? super PostCustomerGroupResponseDto> createCustomerGroup(PostCustomerGroupRequestDto dto);
    // 상담사별 관리 그룹 목록
    ResponseEntity<? super GetCustomerGroupListResponseDto> findAllCustomerGroupByMemberId(Long memberId);
    // 그룹 수정
    ResponseEntity<? super PutCustomerGroupResponseDto> updateCustomerGroup(PutCustomerGroupRequestDto dto);
    // 그룹 삭제
    ResponseEntity<? super DeleteCustomerGroupResponseDto> deleteCustomerGroup(Long customerGroupId);
}
