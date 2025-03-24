package com.kep.portal.controller.talk;

import com.kep.core.model.dto.ApiResult;
import com.kep.core.model.dto.platform.PlatformTemplateStatus;
import com.kep.core.model.dto.platform.PlatformType;
import com.kep.core.model.dto.platform.kakao.KakaoBizMessageTemplatePayload;
import com.kep.core.model.dto.platform.kakao.bizTalk.response.BizTalkResponseDto;
import com.kep.core.model.dto.platform.kakao.bizTalk.response.TemplateResponseDto;
import com.kep.core.model.dto.platform.kakao.bizTalk.response.TemplateSearchResponseDto;
import com.kep.portal.client.talk.TemplateServiceClient;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

/**
 * Biz Talk Module 우회 컨트롤러
 * By-Pass
 */
@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/template")
public class TemplateController {
    /** Autowired Components **/
    private final TemplateServiceClient templateServiceClient;


    /** Create APIs **/
    @Tag(name = "템플릿 등록")
    @Operation(summary = "템플릿 등록(임시 우회용")
    @PostMapping
    public ResponseEntity<? super BizTalkResponseDto> createTemplate(@RequestBody @Valid KakaoBizMessageTemplatePayload requestDto) {
        log.info("Kakao Biz Message Template, Payload : {}", requestDto);
        ResponseEntity<? super BizTalkResponseDto> response = templateServiceClient.createTemplate(requestDto);
        return response;
    }


    /** Retrieve APIs **/
    @Tag(name = "템플릿 조회")
    @Operation(summary = "템플릿 조회(임시 우회용)")
    @GetMapping
    public ResponseEntity<? super ApiResult<List<TemplateSearchResponseDto>>> getTemplateList(
            @RequestParam(name = "platform") PlatformType platform,
            @RequestParam(name = "branch_id", required = false) Long branchId,
            @RequestParam(name = "status", required = false) List<PlatformTemplateStatus> status,
            @RequestParam(name = "template_name", required = false) String templateName) {
        log.info("Kakao Biz Message Template, Template List, platform : {}", platform);
        log.info("Kakao Biz Message Template, Template List, branch_id : {}", branchId);
        log.info("Kakao Biz Message Template, Template List, status : {}", status);
        log.info("Kakao Biz Message Template, Template List, template_name : {}", templateName);
        ResponseEntity<? super ApiResult<List<TemplateSearchResponseDto>>> response =
                templateServiceClient.getTemplateList(platform, branchId, templateName);
        return response;
    }

    @Tag(name = "템플릿 상세 조회")
    @Operation(summary = "템플릿 상세 조회(임시 우회용)")
    @GetMapping("/{id}")
    public ResponseEntity<? super ApiResult<TemplateResponseDto>> getTemplate(@PathVariable("id") Long id) {
        log.info("Kakao Biz Message Template, Template ID : {}", id);
        ResponseEntity<? super ApiResult<TemplateResponseDto>> response = templateServiceClient.getTemplate(id);
        return response;
    }

    @Tag(name = "템플릿 카테고리 전체 조회")
    @Operation(summary = "템플릿 카테고리 전체 조회(임시 우회용)")
    @GetMapping("/category/all")
    public ResponseEntity<? super BizTalkResponseDto> getCategory() {
        log.info("Kakao Biz Message Template, Find All Category");
        ResponseEntity<? super BizTalkResponseDto> response = templateServiceClient.getCategory();
        return response;
    }

    @Tag(name = "템플릿 ClientId 조회")
    @Operation(summary = "템플릿 ClientId 조회(임시 우회용)")
    @GetMapping("/clientId")
    public ResponseEntity<String> getClientId() {
        log.info("Kakao Biz Message Template, Find Client ID");
        ResponseEntity<String> response = templateServiceClient.getClientId();
        return response;
    }


    /** Update APIs **/
    @Tag(name = "템플릿 수정")
    @Operation(summary = "템플릿 수정")
    @PutMapping
    public ResponseEntity<? super BizTalkResponseDto> updateTemplate(@RequestBody @Valid KakaoBizMessageTemplatePayload requestDto) {
        log.info("Kakao Biz Message Template, Update Template : {}", requestDto);
        ResponseEntity<? super BizTalkResponseDto> response = templateServiceClient.updateTemplate(requestDto);
        return response;
    }


    /** Delete APIs **/
    @Tag(name = "템플릿 검수요청 취소")
    @Operation(summary = "템플릿 검수요청 취소")
    @PutMapping("/cancel/request")
    public ResponseEntity<? super BizTalkResponseDto> cancelTemplate(@RequestParam(name = "profileKey") String profileKey,
                                                                     @RequestParam(name = "templateCode") String templateCode) {
        log.info("Kakao Biz Message Template, Cancel Template : {}", templateCode);
        ResponseEntity<? super BizTalkResponseDto> response = templateServiceClient.cancelTemplate(profileKey, templateCode);
        return response;
    }
}
