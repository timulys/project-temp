package com.kep.portal.controller.customer;

import com.kep.portal.model.dto.customerGroup.request.PostCustomerGroupRequestDto;
import com.kep.portal.model.dto.customerGroup.request.PutCustomerGroupRequestDto;
import com.kep.portal.model.dto.customerGroup.response.DeleteCustomerGroupResponseDto;
import com.kep.portal.model.dto.customerGroup.response.GetCustomerGroupListResponseDto;
import com.kep.portal.model.dto.customerGroup.response.PostCustomerGroupResponseDto;
import com.kep.portal.model.dto.customerGroup.response.PutCustomerGroupResponseDto;
import com.kep.portal.service.customer.CustomerGroupService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Tag(name = "고객 그룹 관리 API")
@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/customer-group")
public class CustomerGroupController {
    // Auwotied Components
    private final CustomerGroupService customerGroupService;

    // Create APIs
    @Operation(summary = "고객 그룹 등록")
    @ApiResponse(responseCode = "200", description = "성공",
            content = @Content(schema = @Schema(implementation = PostCustomerGroupResponseDto.class)))
    @PostMapping
    public ResponseEntity<? super PostCustomerGroupResponseDto> postCustomerGroup(
            @RequestBody @Valid PostCustomerGroupRequestDto requestBody) {
        log.info("Create Customer Group, Body : {}", requestBody);
        ResponseEntity<? super PostCustomerGroupResponseDto> response = customerGroupService.createCustomerGroup(requestBody);
        log.info("Create Customer Group, Response : {}", response);
        return response;
    }

    // Retrieve APIs
    @Operation(summary = "고객 그룹 목록 전체 조회")
    @ApiResponse(responseCode = "200", description = "성공",
            content = @Content(schema = @Schema(implementation = GetCustomerGroupListResponseDto.class)))
    @GetMapping("/{memberId}")
    public ResponseEntity<? super GetCustomerGroupListResponseDto> getCustomerGroupList(
            @PathVariable("memberId") Long memberId) {
        log.info("Find All Group By Member ID : {}", memberId);
        ResponseEntity<? super GetCustomerGroupListResponseDto> response = customerGroupService.findAllCustomerGroupByMemberId(memberId);
        log.info("Find All Group, Response: {}", response);
        return response;
    }

    // Update APIs
    @Operation(summary = "고객 그룹 수정")
    @ApiResponse(responseCode = "200", description = "성공",
            content = @Content(schema = @Schema(implementation = PutCustomerGroupResponseDto.class)))
    @PatchMapping
    public ResponseEntity<? super PutCustomerGroupResponseDto> patchCustomerGroup(
            @RequestBody @Valid PutCustomerGroupRequestDto requestBody) {
        log.info("Put Customer Group, Body : {}", requestBody);
        ResponseEntity<? super PutCustomerGroupResponseDto> response = customerGroupService.updateCustomerGroup(requestBody);
        log.info("Put Customer Group, Response : {}", response);
        return response;
    }

    // Delete APIs
    @Operation(summary = "고객 그룹 삭제")
    @ApiResponse(responseCode = "200", description = "성공",
            content = @Content(schema = @Schema(implementation = DeleteCustomerGroupResponseDto.class)))
    @DeleteMapping("/{customerGroupId}")
    public ResponseEntity<? super DeleteCustomerGroupResponseDto> deleteCustomerGroup (
            @PathVariable("customerGroupId") Long customerGroupId) {
        log.info("Delete Customer Group, Customer Group Id : {}", customerGroupId);
        ResponseEntity<? super DeleteCustomerGroupResponseDto> response = customerGroupService.deleteCustomerGroup(customerGroupId);
        log.info("Delete Customer Group, Response : {}", response);
        return response;
    }
}
