package com.kep.portal.controller.customer;

import com.kep.core.model.dto.ApiResult;
import com.kep.core.model.dto.ApiResultCode;
import com.kep.core.model.dto.customer.CustomerDto;
import com.kep.core.model.dto.customer.CustomerMemberDto;
import com.kep.core.model.dto.customer.GuestDto;
import com.kep.core.model.dto.legacy.LegacyCustomerDto;
import com.kep.portal.model.dto.customer.GuestMemoDto;
import com.kep.portal.service.customer.CustomerServiceImpl;
import com.kep.portal.service.customer.GuestMemoService;
import com.kep.portal.util.SecurityUtils;
import com.mysema.commons.lang.Assert;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.persistence.EntityNotFoundException;
import javax.validation.constraints.NotNull;
import java.util.Collection;
import java.util.List;

@RestController
@RequestMapping("/api/v1/customer")
@Slf4j
public class CustomerController {

    @Resource
    private CustomerServiceImpl customerService;

    @Resource
    private SecurityUtils securityUtils;

    @Resource
    private GuestMemoService guestMemoService;

    /**
     * 전체 고객 목록 (접근 권한 룰은 업체마다)
     * @param pageable
     * @return
     */
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
     * 회원(상담원) 기념일 고객 목록
     * @return
     */
    @GetMapping(value = "/anniversarie")
    public ResponseEntity<ApiResult<List<CustomerDto>>> anniversaries() {
//        Long memberId = securityUtils.getMemberId();
        List<CustomerDto> entities = customerService.anniversaries();
        ApiResult<List<CustomerDto>> response = ApiResult.<List<CustomerDto>>builder()
                .code(ApiResultCode.succeed)
                .payload(entities)
                .build();
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    /**
     * 고객 정보
     * @param id
     * @return
     */
//    @GetMapping(value = "/{id}")
//    public ResponseEntity<ApiResult<CustomerDto>> show(@PathVariable("id") String idString) {
//        log.info("CUSTOMER, SEARCH ID :{}", idString);
//        try {
//            if(idString == null || idString.trim().isEmpty() || !idString.matches("\\d+")) {
//                // id가 null, 빈 문자열이거나 숫자가 아닌 경우
//                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
//            }
//            Long id = Long.parseLong(idString);
//
//            CustomerDto entity = customerService.show(id);
//            ApiResult<CustomerDto> response = ApiResult.<CustomerDto>builder()
//                    .code(ApiResultCode.succeed)
//                    .payload(entity)
//                    .build();
//            return new ResponseEntity<>(response, HttpStatus.OK);
//        } catch (EntityNotFoundException e) {
//            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
//        } catch (NumberFormatException e) {
//            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
//        }
//    }

    /**
     * 고객 정보
     * @param id
     * @return
     */
    @GetMapping(value = "/{id}")
    public ResponseEntity<ApiResult<CustomerDto>> show(@PathVariable("id") Long id
            , @RequestParam(required = false) Long issueId) {

        CustomerDto entity = customerService.show(id,issueId);
        log.info("[customerId]:{}",entity);
        ApiResult<CustomerDto> response = ApiResult.<CustomerDto>builder()
                .code(ApiResultCode.succeed)
                .payload(entity)
                .build();
        return new ResponseEntity<>(response, HttpStatus.OK);
    }



    /**
     * 선택된 고객의 계약 정보 조회
     * @param cntrtNum
     * @return
     */
    @GetMapping(value="/contract/{cntrtNum}")
    public ResponseEntity<ApiResult<LegacyCustomerDto.CresData>> getContractInfo(@PathVariable("cntrtNum") String cntrtNum) {
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
    @GetMapping(value = "/issue/latest")
    public ResponseEntity<ApiResult<List<CustomerDto>>> latest () {
        List<CustomerDto> entities = customerService.issueLatest();
        ApiResult<List<CustomerDto>> response = ApiResult.<List<CustomerDto>>builder()
                .code(ApiResultCode.succeed)
                .payload(entities)
                .build();
        return new ResponseEntity<>(response, HttpStatus.OK);
    }


    /**
     * 즐겨찾기 고객 목록
     * @return
     */
    @GetMapping(value = "/favorite")
    public ResponseEntity<ApiResult<List<CustomerDto>>> favorites () {
        List<CustomerDto> entities = customerService.favorites();
        ApiResult<List<CustomerDto>> response = ApiResult.<List<CustomerDto>>builder()
                .code(ApiResultCode.succeed)
                .payload(entities)
                .build();
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    /**
     * 상담원 고객 즐겨찾기
     * @param dto
     * @return
     */
    @PostMapping(value = "/favorite")
    public ResponseEntity<ApiResult<CustomerMemberDto>> favoriteStore (
            @RequestBody @NotNull CustomerMemberDto dto) {
        CustomerMemberDto entity = customerService.favoritesStore(dto);
        ApiResultCode code = ApiResultCode.failed;
        if(entity != null){
            code = ApiResultCode.succeed;
        }
        ApiResult<CustomerMemberDto> response = ApiResult.<CustomerMemberDto>builder()
                .code(code)
                .payload(entity)
                .build();
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    /**
     * 상담원 고객 즐겨찾기 삭제
     * @param dto
     * @return
     */
    @DeleteMapping(value = "/favorite")
    public ResponseEntity<ApiResult<CustomerMemberDto>> favoriteDelete (
            @RequestBody @NotNull CustomerMemberDto dto) {
        CustomerMemberDto entity = customerService.favoritesStore(dto);
        ApiResultCode code = ApiResultCode.failed;
        if(entity != null){
            code = ApiResultCode.succeed;
        }
        ApiResult<CustomerMemberDto> response = ApiResult.<CustomerMemberDto>builder()
                .code(code)
                .payload(entity)
                .build();
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    /**
     * TODO: DELETEME, 고객 정보 저장 (시연용)
     */
    @PostMapping
    public ResponseEntity<ApiResult<CustomerDto>> post(
            @RequestBody CustomerDto customer) {

        log.info("CUSTOMER, POST, BODY: {}", customer);

        customer = customerService.store(customer);
        ApiResult<CustomerDto> response = ApiResult.<CustomerDto>builder()
                .code(ApiResultCode.succeed)
                .payload(customer)
                .build();

        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @GetMapping("/memo/{id}")
    public ResponseEntity<ApiResult<GuestMemoDto>> getCustomerMemo(@PathVariable Long id){


        GuestMemoDto customerMemoDto = guestMemoService.findCustomerMemo(id);

        ApiResult< GuestMemoDto> response = ApiResult.<GuestMemoDto>builder()
                .code(ApiResultCode.succeed)
                .payload(customerMemoDto)
                .build();
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping(value="/memo/save")
    public ResponseEntity<ApiResult<GuestMemoDto>> manageCustomerMemo(@RequestBody GuestMemoDto dto) {

        log.info("NOTICE MANAGER SAVE, POST, BODY guestId: {}", dto.getCustomerId());

        Assert.notNull(dto,"dto is null");

        GuestMemoDto resultDto = guestMemoService.saveCustomerMemo(dto);


        ApiResult<GuestMemoDto> response = ApiResult.<GuestMemoDto>builder()
                .code(ApiResultCode.succeed)
                .payload(resultDto)
                .build();
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
