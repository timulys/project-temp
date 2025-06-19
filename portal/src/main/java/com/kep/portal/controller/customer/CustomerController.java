package com.kep.portal.controller.customer;

import com.kep.core.model.dto.ApiResult;
import com.kep.core.model.dto.ApiResultCode;
import com.kep.core.model.dto.customer.CustomerDto;
import com.kep.core.model.dto.legacy.LegacyCustomerDto;
import com.kep.portal.model.dto.customer.request.PatchCustomerRequestDto;
import com.kep.portal.model.dto.customer.request.PatchFavoriteCustomerRequestDto;
import com.kep.portal.model.dto.customer.request.PostCustomerRequestDto;
import com.kep.portal.model.dto.customer.response.*;
import com.kep.portal.service.customer.CustomerServiceImpl;
import com.kep.portal.util.SecurityUtils;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.util.List;

@Tag(name = "고객 API", description = "/api/v1/customer")
@RestController
@RequestMapping("/api/v1/customer")
@Slf4j
public class CustomerController {

    @Resource
    private CustomerServiceImpl customerService;

    @Resource
    private SecurityUtils securityUtils;

    /**
     * 전체 고객 목록 (접근 권한 룰은 업체마다)
     * @param pageable
     * @return
     */
    @Tag(name = "고객 API")
    @Operation(summary = "전체 고객 목록 조회", description = "접근 권한 룰 :: 업체 별")
    @GetMapping
    public ResponseEntity<ApiResult<List<CustomerDto>>> index(Pageable pageable) {
        List<CustomerDto> entities = customerService.getAll(pageable,null , false);
        ApiResult<List<CustomerDto>> response = ApiResult.<List<CustomerDto>>builder()
                .code(ApiResultCode.succeed)
                .payload(entities)
                .build();
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    /**
     * 회원(상담원) 고객 목록
     * @param pageable
     * @return
     */
    @Tag(name = "고객 API")
    @Operation(summary = "회원(상담원) 고객 목록 조회", description = "회원(상담원) 고객 목록 조회")
    @GetMapping(value = "/member")
    public ResponseEntity<ApiResult<List<CustomerDto>>> member(Pageable pageable) {

        Long memberId = securityUtils.getMemberId();
        List<CustomerDto> entities = customerService.getAll(pageable , memberId , false);
        ApiResult<List<CustomerDto>> response = ApiResult.<List<CustomerDto>>builder()
                .code(ApiResultCode.succeed)
                .payload(entities)
                .build();
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    /**
     * 선택된 고객의 계약 정보 조회
     * @param cntrtNum
     * @return
     */
    @Tag(name = "고객 API")
    @Operation(summary = "고객 계약 정보 조회", description = "선택된 고객의 계약 정보 조회")
    @GetMapping(value="/contract/{cntrtNum}")
    public ResponseEntity<ApiResult<LegacyCustomerDto.CresData>> getContractInfo(
            @Parameter(description = "계약 아이디", in = ParameterIn.PATH, required = true)
            @PathVariable("cntrtNum") String cntrtNum
    ) {
        log.info("cntrtNum[계약실행번호] :{}",cntrtNum);
        try {
            LegacyCustomerDto.CresData contractData = customerService.getContractByCntrtNum(cntrtNum);
            ApiResult<LegacyCustomerDto.CresData> response = ApiResult.<LegacyCustomerDto.CresData>builder()
                    .code(ApiResultCode.succeed)
                    .payload(contractData)
                    .build();

            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            // // 오류 발생 시 실패 응답 반환
            log.error("Error occurred while fetching [계약정보] information:", e);
            ApiResult<LegacyCustomerDto.CresData> errorResponse = ApiResult.<LegacyCustomerDto.CresData>builder()
                    .code(ApiResultCode.failed)
                    .message(e.getMessage())
                    .build();

            return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * 최근대화 고객 목록
     * @return
     */
    @Tag(name = "고객 API")
    @Operation(summary = "최근대화 고객 목록")
    @GetMapping(value = "/issue/latest")
    public ResponseEntity<ApiResult<List<CustomerDto>>> latest () {
        List<CustomerDto> entities = customerService.issueLatest();
        ApiResult<List<CustomerDto>> response = ApiResult.<List<CustomerDto>>builder()
                .code(ApiResultCode.succeed)
                .payload(entities)
                .build();
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    /** V2 **/
    // Create APIs
    /**
     * TODO: 해당 기능은 원래 기획에 없던 기능임, 이 부분은 추후 상황을 고려하여 삭제되어야 함
     * TODO: 정규 기능으로 유지해야 하는지에 대한 판단 필요 - by, tim.c
     * 고객 정보 저장(수동)
     */
    @Tag(name = "고객 API")
    @Operation(summary = "(시연용) 고객 정보 저장")
    @ApiResponse(responseCode = "200", description = "성공",
            content = @Content(schema = @Schema(implementation = PostCustomerResponseDto.class)))
    @PostMapping
    public ResponseEntity<? super PostCustomerResponseDto> postCustomer(@RequestBody @Valid PostCustomerRequestDto requestBody) {
        log.info("Customer Create, Body : {}", requestBody);
        ResponseEntity<? super PostCustomerResponseDto> response = customerService.createCustomer(requestBody);
        log.info("Customer Create, Response : {}", response);
        return response;
    }


    // Retrieve APIs
    /**
     * 고객 정보 전체 조회
     * @param memberId
     * @return
     */
    @Tag(name = "고객 API")
    @Operation(summary = "고객 목록 조회(V2)", description = "고객 목록 조회(V2)")
    @ApiResponse(responseCode = "200", description = "성공",
            content = @Content(schema = @Schema(implementation = GetCustomerListResponseDto.class)))
    @GetMapping("/all/{memberId}")
    public ResponseEntity<? super GetCustomerListResponseDto> getCustomerList(@PathVariable("memberId") Long memberId) {
        log.info("Get Customer All List, Member ID : {}", memberId);
        ResponseEntity<? super GetCustomerListResponseDto> response = customerService.findAllCustomer(memberId);
        log.info("Get Customer All List, Response : {}", response);
        return response;
    }

    /**
     * 고객 정보 단건 조회
     * @param customerId
     * @return
     */
    @Tag(name = "고객 API")
    @Operation(summary = "고객 정보 단건 조회(V2)", description = "고객 정보 단건 조회(V2)")
    @ApiResponse(responseCode = "200", description = "성공",
            content = @Content(schema = @Schema(implementation = GetCustomerResponseDto.class)))
    @GetMapping("/{customerId}")
    public ResponseEntity<? super GetCustomerResponseDto> getCustomer(@PathVariable("customerId") Long customerId) {
        log.info("Get One Customer, Customer ID : {}", customerId);
        ResponseEntity<? super GetCustomerResponseDto> response = customerService.findCustomer(customerId);
        log.info("Get One Customer, Response : {}", response);
        return response;
    }

    /**
     * 즐겨찾기 고객 조회
     * @param memberId
     * @return
     */
    @Tag(name = "고객 API")
    @Operation(summary = "즐겨찾기 고객 조회(V2)", description = "즐겨찾기 고객 조회(V2)")
    @ApiResponse(responseCode = "200", description = "성공",
            content = @Content(schema = @Schema(implementation = GetFavoriteCustomerListResponseDto.class)))
    @GetMapping("/favorite/{memberId}")
    public ResponseEntity<? super GetFavoriteCustomerListResponseDto> getFavoriteCustomer(@PathVariable("memberId") Long memberId) {
        log.info("Get Favorite Customer List, Member ID : {}", memberId);
        ResponseEntity<? super GetFavoriteCustomerListResponseDto> response = customerService.findAllFavoriteCustomerList(memberId);
        log.info("Get Favorite Customer List, Response : {}", response);
        return response;
    }

    /**
     * 기념일 고객 목록 조회(V2)
     * @param memberId
     * @return
     */
    @Tag(name = "고객 API")
    @Operation(summary = "기념일 고객 목록 조회(V2)", description = "기념일 고객 목록 조회(V2)")
    @ApiResponse(responseCode = "200", description = "성공",
            content = @Content(schema = @Schema(implementation = GetAnniversariesCustomerListResponseDto.class)))
    @GetMapping("/anniversaries/{memberId}")
    public ResponseEntity<? super GetAnniversariesCustomerListResponseDto> getAnniversariesCustomerList(@PathVariable("memberId") Long memberId) {
        log.info("Get Anniversary Customer List, Member ID : {}", memberId);
        ResponseEntity<? super GetAnniversariesCustomerListResponseDto> response = customerService.findAllAnniversariesCustomerList(memberId);
        log.info("Get Anniversary Customer List, Response : {}", response);
        return response;
    }



    // Update Methods
    /**
     * 고객 정보 수정
     */
    @Tag(name = "고객 API")
    @Operation(summary = "고객 정보 수정")
    @ApiResponse(responseCode = "200", description = "성공",
            content = @Content(schema = @Schema(implementation = PatchCustomerResponseDto.class)))
    @PatchMapping
    public ResponseEntity<? super PatchCustomerResponseDto> patchCustomer(@RequestBody @Valid PatchCustomerRequestDto requestBody) {
        log.info("Update Customer, Body : {}", requestBody);
        ResponseEntity<? super PatchCustomerResponseDto> response = customerService.updateCustomer(requestBody);
        log.info("Update Customer, Response : {}", response);
        return response;
    }

    /**
     * 즐겨찾기 고객 수정
     * @param requestBody
     * @return
     */
    @Tag(name = "고객 API")
    @Operation(summary = "고객 즐겨찾기 수정(V2)", description = "고객 즐겨찾기 수정(V2)")
    @ApiResponse(responseCode = "200", description = "성공",
            content = @Content(schema = @Schema(implementation = PatchFavoriteCustomerResponseDto.class)))
    @PatchMapping("/favorite")
    public ResponseEntity<? super PatchFavoriteCustomerResponseDto> patchFavoriteCustomer(@RequestBody @Valid PatchFavoriteCustomerRequestDto requestBody) {
        log.info("Patch Favorite Customer, Body : {}", requestBody);
        ResponseEntity<? super PatchFavoriteCustomerResponseDto> response = customerService.patchFavoriteCustomer(requestBody);
        log.info("Patch Favorite Customer, Response : {}", response);
        return response;
    }


    // Delete Methods
    /**
     * 고객 정보 삭제
     * @param customerId
     * @return
     */
    @Tag(name = "고객 API")
    @Operation(summary = "고객 정보 삭제(V2)", description = "고객 정보 삭제(V2)")
    @ApiResponse(responseCode = "200", description = "성공",
            content = @Content(schema = @Schema(implementation = DeleteCustomerResponseDto.class)))
    @DeleteMapping("/{customerId}")
    public ResponseEntity<? super DeleteCustomerResponseDto> deleteCustomer(@PathVariable Long customerId) {
        log.info("Customer Delete, Customer ID : {}", customerId);
        ResponseEntity<? super DeleteCustomerResponseDto> response = customerService.deleteCustomer(customerId);
        log.info("Customer Delete, Response : {}", response);
        return response;
    }
}
