package com.kep.portal.controller.issue;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kep.core.model.dto.ApiResult;
import com.kep.core.model.dto.ApiResultCode;
import com.kep.core.model.dto.customer.*;
import com.kep.core.model.dto.issue.IssueDto;
import com.kep.core.model.dto.issue.IssueLogStatus;
import com.kep.core.model.dto.issue.payload.IssuePayload;
import com.kep.core.model.dto.platform.AuthorizeType;
import com.kep.core.model.dto.platform.PlatformType;
import com.kep.core.model.dto.platform.kakao.profile.KakaoCustomerDto;
import com.kep.core.model.exception.NotFoundIssueException;
import com.kep.portal.client.LegacyClient;
import com.kep.portal.model.entity.customer.Customer;
import com.kep.portal.model.entity.customer.CustomerMapper;
import com.kep.portal.model.entity.customer.Guest;
import com.kep.portal.model.entity.issue.Issue;
import com.kep.portal.repository.customer.CustomerAuthorizedRepository;
import com.kep.portal.repository.customer.CustomerRepository;
import com.kep.portal.repository.customer.GuestRepository;
import com.kep.portal.service.customer.CustomerServiceImpl;
import com.kep.portal.service.issue.IssueService;
import com.kep.portal.service.issue.event.EventByPlatformService;
import com.kep.portal.service.platform.BizTalkHistoryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 플랫폼에서 전달된 고객 이벤트
 * <p>
 * 카카오 상담톡, `message`이벤트 전에 `open`이벤트가 먼저 도착 (없을 경우 생성, platform 모듈에서 보장)
 * <p>
 * <li>X-Platform-Type, X-Service-Key, X-User-Key
 * <li>카카오 상담톡, 채널 (SenderKey), 유저 (UserKey)
 * <li>솔루션 웹, 채널, 유저 (쿠키)
 */
@Tag(name = "플랫폼 전달 고객 이벤트 API", description = "/api/v1/event-by-platform")
@RestController
@RequestMapping("/api/v1/event-by-platform")
@Slf4j
public class EventByPlatformController {

    @Resource
    private EventByPlatformService eventByPlatformService;

    @Resource
    private ObjectMapper objectMapper;

    @Resource
    private LegacyClient legacyClient;

    @Resource
    private CustomerServiceImpl customerService;

    @Resource
    private IssueService issueService;
    @Resource
    private GuestRepository guestRepository;

    @Resource
    private CustomerMapper customerMapper;

    @Resource
    private BizTalkHistoryService bizTalkHistoryService;

    @Resource
    private CustomerAuthorizedRepository customerAuthorizedRepository;
    
    @Resource
    private CustomerRepository customerRepository;
    
    /**
     * 상담 요청 이벤트
     */
    @Tag(name = "플랫폼 전달 고객 이벤트 API")
    @Operation(summary = "상담 요청 이벤트")
    @PostMapping(value = "/open")
    public ResponseEntity<ApiResult<IssueDto>> open(
            @Parameter(description = "플랫폼 타입(solution_web, kakao_counsel_talk, kakao_alert_talk, kakao_friend_talk, kakao_template, legacy_web, legacy_app , kakao_counsel_center)", in = ParameterIn.HEADER, required = true)
            @RequestHeader(value = "X-Platform-Type") PlatformType platformType
            ,@Parameter(description = "서비스 키", in = ParameterIn.HEADER, required = true)
            @RequestHeader(value = "X-Service-Key") String serviceKey
            ,@Parameter(description = "유저 키", in = ParameterIn.HEADER, required = true)
            @RequestHeader(value = "X-User-Key") String userKey
            ,@Parameter(description = "트랙 키", in = ParameterIn.HEADER, required = true)
            @RequestHeader(value = "X-Track-Key") Long trackKey
            , @RequestBody(required = false) Map<String, Object> options) {

        log.info("EVENT BY PLATFORM, OPEN, TRACK KEY: {}, PLATFORM: {}, SERVICE: {}, USER: {}, OPTIONS: {}",
                trackKey, platformType, serviceKey, userKey, options);

        if (options == null)
            options = new HashMap<>();

        if (!ObjectUtils.isEmpty(options)
                && !ObjectUtils.isEmpty(options.get("mocked"))) { // 솔루션에서 생성한 오픈 이벤트
            log.warn("RECEIVE MOCK REFERENCE EVENT, TRACK KEY: {}", trackKey);
            // TODO: 기존 상담 정보를 사용해야하는 요건이 있을 경우, 기존 파라미터 세팅 가능
        }

        IssueDto issueDto = eventByPlatformService.open(platformType, serviceKey, userKey, options, trackKey);

        return new ResponseEntity<>(ApiResult.<IssueDto>builder()
                .code(ApiResultCode.succeed)
                .payload(issueDto)
                .build(), HttpStatus.CREATED);
    }

    /**
     * 메세지 이벤트
     */
    @Tag(name = "플랫폼 전달 고객 이벤트 API")
    @Operation(summary = "메시지")
    @PostMapping(value = "/message")
    public ResponseEntity<ApiResult<IssueDto>> message(
            @Parameter(description = "플랫폼 타입(solution_web, kakao_counsel_talk, kakao_alert_talk, kakao_friend_talk, kakao_template, legacy_web, legacy_app , kakao_counsel_center)", in = ParameterIn.HEADER, required = true)
            @RequestHeader(value = "X-Platform-Type") PlatformType platformType,
            @Parameter(description = "서비스 키", in = ParameterIn.HEADER, required = true)
            @RequestHeader(value = "X-Service-Key") String serviceKey,
            @Parameter(description = "유저 키", in = ParameterIn.HEADER, required = true)
            @RequestHeader(value = "X-User-Key") String userKey,
            @Parameter(description = "트랙 키", in = ParameterIn.HEADER, required = true)
            @RequestHeader(value = "X-Track-Key") Long trackKey,
            @RequestBody IssuePayload issuePayload) throws Exception {

        log.info("EVENT BY PLATFORM, MESSAGE, TRACK KEY: {}, PLATFORM: {}, SERVICE: {}, USER: {}, BODY: {}",
                trackKey, platformType, serviceKey, userKey, issuePayload);

        try {
            IssueDto issueDto = eventByPlatformService.message(platformType, serviceKey, userKey, issuePayload);

            return new ResponseEntity<>(ApiResult.<IssueDto>builder()
                    .code(ApiResultCode.succeed)
                    .payload(issueDto)
                    .build(), HttpStatus.CREATED);

        } catch (NotFoundIssueException e) {
            log.error("{}, PLATFORM: {}, SERVICE: {}, USER: {}", e.getLocalizedMessage()
                    , platformType, serviceKey, userKey);
            ApiResult<IssueDto> response = ApiResult.<IssueDto>builder()
                    .code(ApiResultCode.failed)
                    .message(e.getLocalizedMessage())
                    .build();
            // TODO: 에러 코드
            // response.setError("<<QWEQE>>");
            return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
        }
    }

    /**
     * 메세지 콜백 이벤트
     */
    @Tag(name = "플랫폼 전달 고객 이벤트 API")
    @Operation(summary = "메시지 콜백")
    @PutMapping(value = "/message/callback")
    public ResponseEntity<ApiResult<String>> messageCallback(
            @Parameter(description = "이벤트 키(이슈 로그 아이디)", in = ParameterIn.HEADER, required = true)
            @RequestHeader(value = "X-Event-Key") Long issueLogId,
            @RequestBody boolean result) throws Exception {

        log.info("EVENT BY PLATFORM, MESSAGE CALLBACK, EVENT ID: {}, RESULT: {}",
                issueLogId, result);

        eventByPlatformService.callback(issueLogId, result ? IssueLogStatus.receive : IssueLogStatus.fail);

        return new ResponseEntity<>(ApiResult.<String>builder()
                .code(ApiResultCode.succeed)
                .build(), HttpStatus.OK);
    }

    /**
     * 상담 종료 이벤트
     */
    @Tag(name = "플랫폼 전달 고객 이벤트 API")
    @Operation(summary = "상담 종료")
    @PostMapping(value = "/close")
    public ResponseEntity<ApiResult<IssueDto>> close(
            @Parameter(description = "플랫폼 타입(solution_web, kakao_counsel_talk, kakao_alert_talk, kakao_friend_talk, kakao_template, legacy_web, legacy_app , kakao_counsel_center)", in = ParameterIn.HEADER, required = true)
            @RequestHeader(value = "X-Platform-Type") PlatformType platformType,
            @Parameter(description = "서비스 키", in = ParameterIn.HEADER, required = true)
            @RequestHeader(value = "X-Service-Key") String serviceKey,
            @Parameter(description = "유저 키", in = ParameterIn.HEADER, required = true)
            @RequestHeader(value = "X-User-Key") String userKey,
            @Parameter(description = "트랙 키", in = ParameterIn.HEADER, required = true)
            @RequestHeader(value = "X-Track-Key") Long trackKey,
            @RequestBody(required = false) Map<String, Object> options) {

        log.info("EVENT BY PLATFORM, CLOSE, TRACK KEY: {}, PLATFORM: {}, SERVICE: {}, USER: {}, OPTIONS: {}",
                trackKey, platformType, serviceKey, userKey, options);

        IssueDto issueDto = eventByPlatformService.close(platformType, serviceKey, userKey, options, trackKey);

        return new ResponseEntity<>(ApiResult.<IssueDto>builder()
                .code(ApiResultCode.succeed)
                .payload(issueDto)
                .build(), HttpStatus.CREATED);
    }

    /**
     *
     * FIXME :: bnk 고객 번호 20240712 volka
     *
     * 고객 인증 완료
     * @param authorizeType 인증 타입 정보
     * @param trackKey 트래킹 키
     * @param authorizedInfo 인증 정보가 담긴 Map
     * @return ResponseEntity 인증 완료 응답
     * @throws Exception 예외 발생 시 처리
     */
    @Tag(name = "플랫폼 전달 고객 이벤트 API")
    @Operation(summary = "고객 인증 완료")
    @PostMapping(value = "/authorized")
    public ResponseEntity<ApiResult<CustomerDto>> authorized(
            @Parameter(description = "인증 타입 (kakao_sync)", in = ParameterIn.HEADER, required = true)
            @RequestHeader(value = "X-Authorize-Type") AuthorizeType authorizeType,
            @Parameter(description = "트랙 키", in = ParameterIn.HEADER, required = true)
            @RequestHeader(value = "X-Track-Key") Long trackKey,
            @RequestBody(required = false) Map<String, Object> authorizedInfo) throws Exception {

        log.info("EVENT BY PLATFORM, AUTHORIZED, TRACK KEY: {}, TYPE: {}, BODY: {}",
                trackKey, authorizeType, authorizedInfo);

        // API 결과 코드, 고객 CI, bnk 고객 번호 등 초기 변수설정
        ApiResultCode code = ApiResultCode.failed;
        String custCi = null;
        String platformUserId = null;
        String requestBody = objectMapper.writeValueAsString(authorizedInfo);

        // DTO 객체 생성 및 초기화
        // 고객 정보, 고객 연락처 정보, 고객 기념일 정보 등 DTO 객체를 생성
        CustomerDto customerDto = null;
        List<CustomerContactDto> customerContactDtos = new ArrayList<>();
        List<CustomerAnniversaryDto> customerAnniversaryDtos = new ArrayList<>();

        // 추가적인 고객 ID, 멤버 ID, 이슈 ID
        Long customerId = 0L;
        Long memberId = 0L;
        String vndrCustNo = null; 
        Long issueId = 0L;

        //kakao sync(카카오 동기화 처리)
        if (AuthorizeType.kakao_sync.equals(authorizeType)) {
        	//고객정보 추출
            KakaoCustomerDto dto = objectMapper.readValue(requestBody, KakaoCustomerDto.class);
            log.info("KakaoCustomerDto :::::{}",dto);
            custCi = dto.getKakaoAccount().getCi();
            platformUserId = dto.getId();
            
            //연락처 정보 설정:이메일,전화번호
            customerContactDtos.add(CustomerContactDto.builder()
                    .type(CustomerContactType.email)
                    .payload(dto.getKakaoAccount().getEmail())
                    .build());

            customerContactDtos.add(CustomerContactDto.builder()
                    .type(CustomerContactType.call)
                    .payload(dto.getKakaoAccount().getPhoneNumber())
                    .build());

            //TODO : 레거시 고객 데이터가 없을시 사용할지 말지 선택 우선 씽크 데이타 저장
            customerDto = CustomerDto.builder()
                    .identifier(custCi)
                    .age(dto.getKakaoAccount().getAgeRange())
                    .profile(dto.getKakaoAccount().getProfile().getProfileImageUrl())
                    .name(dto.getKakaoAccount().getName())
                    .contacts(customerContactDtos)
                    .build();
            //기념일 (생일)
            if (dto.getKakaoAccount().getBirthday() != null) {
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd");
                LocalDate anniversary = LocalDate.parse(dto.getKakaoAccount().getBirthyear() + dto.getKakaoAccount().getBirthday(), formatter);
                customerAnniversaryDtos.add(CustomerAnniversaryDto.builder()
                        .type(AnniversaryType.birthday)
                        .anniversary(anniversary.toString())
                        .build());
            }


            //기념일
            if (!customerAnniversaryDtos.isEmpty()) {
                customerDto.setAnniversaries(customerAnniversaryDtos);
            }
            
            if (dto.getExtra() != null) {
                customerId = (dto.getExtra().get("customer_id") != null)
                        ? Long.valueOf(String.valueOf(dto.getExtra().get("customer_id"))) : 0L;

                memberId = (dto.getExtra().get("member_id") != null)
                        ? Long.valueOf(String.valueOf(dto.getExtra().get("member_id"))) : 0L;
                
                // 문자열로 변환
                vndrCustNo = (dto.getExtra().get("vndr_cust_no") != null)
                        ? String.valueOf(dto.getExtra().get("vndr_cust_no")) : null;
            	
                issueId = (dto.getExtra().get("issue_id") != null)
                        ? Long.valueOf(String.valueOf(dto.getExtra().get("issue_id"))) : 0L;
            }

        }

        if (customerDto != null) {
        	log.info("Saving customerDto: {}", customerDto);
            Customer customer = customerService.save(customerDto);
            log.info("Saved customer: {}", customer);
            
            if (customer != null) {
                if (issueId != 0L) {
                    Issue issue = issueService.findById(issueId);
                    Guest guest = issue.getGuest();
                    log.info("guest = {}", guest.getId());
                    if (guest.getCustomer() == null) {
                        guest.setCustomer(customer);
                        guestRepository.save(guest);
                        guestRepository.flush();

                        log.info("guest.customer = {}", guest.getCustomer().getId());
                    }
                }


                //내고객 인증 저장
                log.info("Associating customer with member");
                customerService.customerMemberStore(customer, memberId);
                log.info("Customer associated with member");

                //인증 히스토리
                log.info("Storing authorization history");
                CustomerAuthorizedDto customerAuthorizedDto = CustomerAuthorizedDto.builder()
                        .platformUserId(platformUserId)
                        .type(authorizeType)
                        .build();
                customerService.authorizedStore(customer, customerAuthorizedDto);
                log.info("Stored authorization history");
            }
            code = ApiResultCode.succeed;
        }
        log.info("Ending authorized endpoint with customerDto: {}", customerDto);
        return new ResponseEntity<>(ApiResult.<CustomerDto>builder()
                .code(code)
                .payload(customerDto)
                .build(), HttpStatus.CREATED);
        
	}
}


