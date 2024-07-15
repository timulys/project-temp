package com.kep.portal.client;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

import javax.annotation.Resource;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.server.ResponseStatusException;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.kep.core.model.dto.customer.CustomerDto;
import com.kep.core.model.dto.customer.GuestDto;
import com.kep.core.model.dto.issue.IssueExtraDto;
import com.kep.core.model.dto.legacy.LegacyBnkCategoryDto;
import com.kep.core.model.dto.legacy.LegacyCustomerDto;
import com.kep.portal.config.property.CoreProperty;
import com.kep.portal.model.dto.subject.IssueCategoryChildrenDto;
import com.kep.portal.model.entity.customer.CustomerAuthorized;
import com.kep.portal.model.entity.customer.Guest;
import com.kep.portal.model.entity.issue.Issue;
import com.kep.portal.repository.customer.CustomerAuthorizedRepository;
import com.kep.portal.repository.customer.GuestRepository;
import com.kep.portal.repository.issue.IssueRepository;
import com.kep.portal.repository.member.MemberRepository;
import com.kep.portal.util.SecurityUtils;

import lombok.extern.slf4j.Slf4j;

/**
 * Legacy API 클라아언트 => BNK API 연동
 * @생성일자	   / 생성자	  / 생성내용
 * 2023.10.04  / YO       / 고객사 API를 연동하고 응답 데이터를 IssueController에 전송
 */
@Service
@Slf4j
public class LegacyClient {

    @Resource
    private CustomerAuthorizedRepository customerAuthorizedRepository;

    @Resource
    private MemberRepository memberRepository;

    @Resource
    private CoreProperty coreProperty;

    @Resource
    private SecurityUtils securityUtils;

    @Resource
    private ObjectMapper objectMapper;

    @Resource
    private IssueRepository issueRepository;

    @Resource
    private GuestRepository guestRepository;

    //고객정보 API
    private static final String BNK_CUST_URL = "/api/ckchat/cust";
    //계약번호 API
    private static final String BNK_CONTRACT_URL = "/api/ckchat/cntrt";
    //카테고리 API (조회구분,현업구분,현업이관부서)
    private static final String BNK_CATEGORY_URL = "/api/ckchat/trnfcode";
    //BNK 인입결과 API
    private static final String BNK_RESULT_URL = "/api/ckchat/consult";

    /**
     * BNK 고객정보 API 연동
     * @param customerDto,memberId
     * @return legacyCustomerDto
     *
     */
    public LegacyCustomerDto getCustomerInfo(CustomerDto customerDto, String sendFlag) {
        log.info("▶▶▶::고객정보에 대한 BNK API 호출을 시작합니다");
        log.info("BNK API 호출 sendFlag::{}",sendFlag);
        String apiCustInfoUrl = coreProperty.getLegacyServiceUri() + BNK_CUST_URL;
        log.info("고객정보에 대한 BNK API 호출 :{}", apiCustInfoUrl);
        CustomerAuthorized customerAuthorized = new CustomerAuthorized();

        List<CustomerAuthorized> customerAuthorizedList = customerAuthorizedRepository.findAllByCustomerId(customerDto.getId());

        if(!customerAuthorizedList.isEmpty()) {
            customerAuthorized = customerAuthorizedList.stream().findFirst().get();
        } else {
            throw new RuntimeException("고객 인증 정보를 찾을 수 없습니다. 카카오 싱크 데이터가 존재하지 않습니다.");
        }
        //BNK 추가요구사항 => vndrCustNo가아닌 유저네임으로 ex)member1
        // 데이터 유효성 검사: identifier, platformUserId, vndrCustNo 가 null 인지 체크
        if (ObjectUtils.isEmpty(customerDto.getIdentifier()) || ObjectUtils.isEmpty(customerAuthorized.getPlatformUserId())) {
            throw new RuntimeException("필수 데이터가 데이터베이스에서 누락되었습니다. identifier, platformUserId, vndrCustNo를 확인해주세요.");
        }

        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("ci", customerDto.getIdentifier());
        requestBody.put("suid", customerAuthorized.getPlatformUserId());
//        requestBody.put("vndr_cust_no", securityUtils.getVndrCustNo());
        //member 테이블에 username 필드로 변경됨
        requestBody.put("vndr_cust_no", securityUtils.getVndrCustNo());
        requestBody.put("send_flag", sendFlag);
        log.info("[BNK 고객정보 :::: Reuqest Data:]:{}",requestBody);

        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<Map<String, Object>> entity = new HttpEntity<>(requestBody, headers);

        try {
            ResponseEntity<String> response = restTemplate.postForEntity(apiCustInfoUrl, entity, String.class);
            if (response.getStatusCode() == HttpStatus.OK) {
                log.info("고객 정보 요청에 성공했습니다. Status Code: {}", HttpStatus.OK);
                LegacyCustomerDto legacyCustomerDto = new ObjectMapper().readValue(response.getBody(), LegacyCustomerDto.class);
                if (legacyCustomerDto != null && legacyCustomerDto.getCustNo() != null && !legacyCustomerDto.getCustNo().isEmpty()) {
                    log.info("BNK API 요청 성공, 고객 정보: {}", legacyCustomerDto);
                    return legacyCustomerDto;
                } else {
                    log.error("BNK API 응답 ▶▶▶::[BNK 유효한 고객 데이터가 없습니다.]");
                    return new LegacyCustomerDto();
                }
            } else {
                log.error("BNK API 요청 실패, 상태 코드: {}", response.getStatusCode());
                throw new RuntimeException("BNK API request failed with status code: " + response.getStatusCode());
            }
        } catch (Exception e) {
            log.error("BNK API 요청 중 오류 발생: {}", e.getMessage());
            throw new RuntimeException("Error occurred during BNK API request: " + e.getMessage(), e);
        }
    }

    /**
     *BNK 고객 계약정보 API 연동
     *@param cntrtNum
     *@return legacyCustomerDto
     */
    public LegacyCustomerDto.CresData getContractInfo(String cntrtNum){
        //API 호출전  cntrt_num 유효성 검사 [계약번호 없으면 API 호출 방지]
        if(ObjectUtils.isEmpty(cntrtNum)) {
            log.error("▶▶▶::cntrtNum[계약정보가 undefined 이거나 null 입니다.]");
            throw new IllegalArgumentException("▶▶▶::cntrtNum이 undefined 비어 있습니다.");
        }
        log.info("▶▶▶::계약 정보에 대한 BNK API 호출을 시작합니다: {}", cntrtNum);

        String apiContractInfoUrl = coreProperty.getLegacyServiceUri() + BNK_CONTRACT_URL;
        log.info("계약 정보에 대한 BNK API 호출 :{}", apiContractInfoUrl);
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("cntrt_num", cntrtNum);
        log.info("[Request cntrt_num :]"+requestBody);

        HttpEntity<Map<String, Object>> contractEntity = new HttpEntity<>(requestBody, headers);

        try {
            ResponseEntity<String> contractResponse = restTemplate.postForEntity(apiContractInfoUrl, contractEntity, String.class);
            if (contractResponse.getStatusCode() == HttpStatus.OK) {
                log.info("계약 정보 요청에 성공했습니다. Status code: {}", HttpStatus.OK);
                ObjectMapper contractInfo = new ObjectMapper();
                return contractInfo.readValue(contractResponse.getBody(), LegacyCustomerDto.CresData.class);
            } else {
                log.error("계약 정보 BNK API 호출 실패: Status Code {}", contractResponse.getStatusCode());
                throw new RuntimeException("계약 정보 BNK API 호출 실패. 상태 코드: " + contractResponse.getStatusCode());
            }
        } catch (Exception e) {
            log.error("계약 정보 API 통신 중 오류 발생", e);
            throw new RuntimeException("계약 정보 API 통신 중 오류 발생", e);
        }
    }

    /**
     * BNK 카테고리 API 연동
     *
     * 조회 구분, 현업 이관 업무, 현업 이관 부서에 따라 BNK의 카테고리 정보를 조회합니다.
     * - 조회구분 (L): 사용자의 조회구분 선택값을 기반으로 IssueCategoryController에서 요청합니다.
     * - 조회구분 (M): 조회구분에서 선택된 값(value)을 fld_cd로 설정하여 API 호출합니다.
     * - 현업이관부서 (S): 조회구분에서 선택된 값(value)을 fld_cd로, 현업이관업무에서 선택된 값(value)을 wrk_seq로 설정하여 API 호출합니다.
     *
     * @param gubun 조회 구분값 (L, M, S 중 하나)
     * @param fld_cd 현업 이관 업무 코드값 (조회구분에서 반환된 value 값)
     * @param wrk_seq 현업 이관 부서 코드값 (조회구분, 현업이관업무에서 반환된 value 값)
     * @return LegacyBnkCategoryDto BNK 카테고리 정보
     */
    public LegacyBnkCategoryDto getBnkCategoryInfo(LegacyBnkCategoryDto dto) {
        log.info("▶▶▶::BNK 카테고리 API 호출합니다.: {}", dto.getGubun());

        String apiCategoryUrl = coreProperty.getLegacyServiceUri() + BNK_CATEGORY_URL;
        log.info("계약 정보에 대한 BNK API 호출 :{}", apiCategoryUrl);

        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("gubun", dto.getGubun());

        switch (dto.getGubun()) {
            // 조회구분 (L)에 대한 API 호출
            case "L":
                break;
            // 현업선택 (M)에 대한 API 호출
            case "M":
                requestBody.put("fld_cd", dto.getFldCd());
                break;
            // 현업이관부서 (S)에 대한 API 호출
            case "S":
                requestBody.put("fld_cd", dto.getFldCd());
                requestBody.put("wrk_seq", dto.getWrkSeq());
                break;
            default:
                throw new IllegalArgumentException("Invalid gubun value: " + dto.getGubun());
        }

        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<Map<String, Object>> entity = new HttpEntity<>(requestBody, headers);

        try {
            ResponseEntity<String> response = restTemplate.postForEntity(apiCategoryUrl, entity, String.class);
            if (response.getStatusCode() == HttpStatus.OK) {
                log.info("BNK 카테고리 API 요청에 성공했습니다. Status Code: {}", HttpStatus.OK);
                ObjectMapper objectMapper = new ObjectMapper();
                LegacyBnkCategoryDto legacyBnkCategoryDto = new LegacyBnkCategoryDto();
                HashMap map = objectMapper.readValue(response.getBody(), HashMap.class);
                if(null != map && !ObjectUtils.isEmpty(map.get("list"))) {
                    legacyBnkCategoryDto.setDataList(objectMapper.convertValue((ArrayList) map.get("list"), objectMapper.getTypeFactory().defaultInstance().constructCollectionType(List.class, LegacyBnkCategoryDto.GubunData.class)));
                }
                return legacyBnkCategoryDto;
            } else {
                log.error("BNK 카테고리 API 연결 실패: Status Code {}", response.getStatusCode());
                throw new RuntimeException("BNK 카테고리 API에 연결할 수 없습니다. 응답 코드: " + response.getStatusCode());
            }
        } catch (Exception e) {
            log.error("BNK 카테고리 API 통신 중 오류 발생", e);
            throw new RuntimeException("BNK 카테고리 API 통신 중 오류 발생", e);
        }
    }


    /**
     * BNK 상담유형 인입결과  API 전송
     * @param issueExtraDto BNK에 전송할 데이터를 포함하는 DTO
     * @throws Exception
     */
    public void sendToBnkResultApi(Issue issue, List<IssueCategoryChildrenDto> issueCategoryInfoList, IssueExtraDto issueExtraDto) throws Exception {
        log.info("▶▶▶::BNK 상담유형 인입결과 API 전송을 시작합니다.");
        String apiResultUrl = coreProperty.getLegacyServiceUri() + BNK_RESULT_URL;
        log.info("BNK 상담유형 인입결과 API 전송 :{}", apiResultUrl);
        HashMap extraParameter = ObjectUtils.isEmpty(issue.getIssueExtra().getParameter()) ? new HashMap() : objectMapper.readValue(issue.getIssueExtra().getParameter(), HashMap.class);

        DateTimeFormatter formatterGroup = DateTimeFormatter.ofPattern("yyyyMMdd");
        DateTimeFormatter formatterMinuteSecondGroup = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");

        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);


        Map<String, Object> requestBody = new HashMap<>();
        String cust_no = issueExtraDto.getCustNo();
        requestBody.put("deal_kind1", issueCategoryInfoList.get(0).getChildren().get(0).getBnkCode());                            //상담유형(대)
        requestBody.put("deal_kind2", issueCategoryInfoList.get(0).getChildren().get(0).getChildren().get(0).getBnkCode());        //상담유형(중)
//        requestBody.put("deal_ymd", issue.getCreated().format(formatterGroup)); 												// 상담일자를 오늘 날짜로 설정
        requestBody.put("deal_ymd", LocalDate.now().format(formatterGroup));                                                    // 상담일자를 오늘 날짜로 설정
        requestBody.put("deal_memo", issueExtraDto.getBnkSummary());                                                            // 이관메모 등록내용
        requestBody.put("deal_sum", issueExtraDto.getSummary());                                                                // 상담요약 등록내용
        requestBody.put("deal_gubun", issueExtraDto.getSelectedRadioData() != null ? issueExtraDto.getSelectedRadioData() : "");// 라디오박스 선택값
//        requestBody.put("deal_gubun", issueExtraDto.getSelectedRadioData());													// 라디오박스 선택값
        requestBody.put("rslt_clerk", securityUtils.getVndrCustNo());                                                            // 담당직원번호
        if (!ObjectUtils.isEmpty(cust_no)) {
            requestBody.put("cust_no", issueExtraDto.getCustNo());                                                                // 고객번호
        } else {
            requestBody.put("cust_no", "9999999");
        }
        requestBody.put("deal_start_time", issue.getCreated().format(formatterMinuteSecondGroup));                                // 상담연결시간
        requestBody.put("deal_end_time", issue.getClosed().format(formatterMinuteSecondGroup));                                // 상담종료시간
        requestBody.put("fld_cd", issueExtraDto.getFldCd());                                                                     // 현업분류카테고리(협업선택)
        requestBody.put("wrk_seq", issueExtraDto.getWrkSeq());                                                                    // 현업분류카테고리(현업이관부서)
        requestBody.put("fld_dept_cd", issueExtraDto.getFldDeptCd());                                                            // 현업분류카테고리(현업이관부서)
        requestBody.put("cntrt_num", issueExtraDto.getCntrtNum());                                                                // 계약실행번호
        requestBody.put("inout_gubun", "아웃바운드");                                                                            // 인/아웃바운드
//    	requestBody.put("chat_type", (ObjectUtils.isEmpty(inflowInfo.get("path")) ? "" : String.valueOf(inflowInfo.get("path"))));// 채팅유형 유입경로에서 설정[=>[카카오채널, 홈페이지, 고객상담, 리텐션, 승계, 사후관리]
        requestBody.put("chat_type", "03");    // 채팅유형 유입경로에서 설정[=>[카카오채널, 홈페이지, 고객상담, 리텐션, 승계, 사후관리]

        // "extraParameter"에서 "o"키에 대응하는 값을 가져옴
        String seqnoValue = (String) extraParameter.get("o");

        // "seqnoValue"가 null이 아닌 경우에만 "seqno"와 "mgt_ymd"를 requestBody에 추가
        if (seqnoValue != null) {
            // "seqno" 값을 requestBody에 추가
            requestBody.put("seqno", seqnoValue);

            // 현재 날짜를 "mgt_ymd"로 설정하여 requestBody에 추가
            String mgtYmd = LocalDate.now().format(formatterGroup);
            requestBody.put("mgt_ymd", mgtYmd);
        }
        log.info("[=========BNK 상담유형 API 인입결과 SETTING=========]");
        requestBody.forEach((key, value) -> log.info("{}: {}", key, value == null ? "null" : value.toString()));
        log.info("=================================================");

        //계약번호와 고객번호 검증
        if (issueExtraDto.getCustNo() == null) {
            log.error("계약번호가 누락되었습니다::cust_no - {}", issueExtraDto.getCustNo());
            throw new IllegalArgumentException("▶▶▶::직원번호가[cust_no] 누락되었습니다.");
        }
        HttpEntity<Map<String, Object>> bnkRequestEntity = new HttpEntity<>(requestBody, headers);
        try {
            ResponseEntity<String> bnkResultResponse = restTemplate.postForEntity(apiResultUrl, bnkRequestEntity, String.class);

            if (bnkResultResponse.getStatusCode() == HttpStatus.OK) {
                log.info("BNK 상담유형 인입결과 API 요청에 성공했습니다. Status Code: {}", HttpStatus.OK);
                ObjectMapper objectMapper = new ObjectMapper();
                Map<String, Object> responseMap = objectMapper.readValue(bnkResultResponse.getBody(), new TypeReference<Map<String, Object>>() {
                });
                String resultValue = (String) responseMap.get("result");

                log.info("BNK 상담유형 API 인입결과: " + resultValue);
                if (!"Y".equals(resultValue)) {
                    throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "BNK API 응답 결과가 'Y'가 아닙니다: " + resultValue);
                }
            } else {
                log.error("BNK 상담유형 인입결과 API 호출 실패: Status Code {}", bnkResultResponse.getStatusCode());
                throw new ResponseStatusException(bnkResultResponse.getStatusCode(), "BNK API 호출 실패");
            }
        } catch (HttpClientErrorException | HttpServerErrorException e) {
            log.error("HTTP 클라이언트 또는 서버 오류 발생:URL - {}, 오류 코드 - {}, 오류 메시지 - {}", apiResultUrl, e.getStatusCode(), e.getResponseBodyAsString());
            throw e; // 이 예외들은 이미 ResponseStatusException의 서브클래스입니다.
        } catch (RestClientException e) {
            log.error("RestTemplate 통신 중 오류 발생: URL - {}, 오류 메시지 - {}", apiResultUrl, e.getMessage());
            throw new ResponseStatusException(HttpStatus.SERVICE_UNAVAILABLE, "RestTemplate 통신 중 오류 발생", e);
        } catch (Exception e) {
            log.error("BNK 상담유형 인입결과 API 통신 중 오류 발생: URL - {}, 오류 메시지 - {}", apiResultUrl, e.getMessage());
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "내부 서버 오류 발생", e);
        }
    }

}

	
