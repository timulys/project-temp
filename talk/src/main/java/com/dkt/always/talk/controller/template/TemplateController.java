package com.dkt.always.talk.controller.template;

import com.dkt.always.talk.service.template.TemplateExternalService;
import com.dkt.always.talk.service.template.TemplateSearchService;
import com.kep.core.model.dto.ApiResult;
import com.kep.core.model.dto.platform.PlatformTemplateStatus;
import com.kep.core.model.dto.platform.PlatformType;
import com.kep.core.model.dto.platform.kakao.KakaoBizMessageTemplatePayload;
import com.kep.core.model.dto.platform.kakao.bizTalk.request.TemplateSearchRequestDto;
import com.kep.core.model.dto.platform.kakao.bizTalk.response.BizTalkResponseDto;
import com.kep.core.model.dto.platform.kakao.bizTalk.response.TemplateResponseDto;
import com.kep.core.model.dto.platform.kakao.bizTalk.response.TemplateSearchResponseDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.SortDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v2/template")
@RequiredArgsConstructor
@Slf4j
public class TemplateController {
    /** Autowired Components **/
    private final TemplateExternalService templateExternalService;
    private final TemplateSearchService templateSearchService;

    /** Create APIs **/
    @Tag(name = "템플릿 등록")
    @Operation(summary = "템플릿 등록")
    @PostMapping
    public ResponseEntity<? super BizTalkResponseDto> createTemplate(@RequestBody @Valid KakaoBizMessageTemplatePayload requestDto) {
        ResponseEntity<? super BizTalkResponseDto> response = templateExternalService.createTemplate(requestDto);
        return response;
    }

    /** Retrieve APIs **/
    @Tag(name = "템플릿 조회")
    @Operation(summary = "템플릿 조회")
    @GetMapping
    // TODO : @PreAuthorize, 추후 Security&JWT 인증 로직 추가 예정
    public ResponseEntity<? super ApiResult<List<TemplateSearchResponseDto>>> getTemplateList(
            @RequestParam(name = "platform") PlatformType platform,
            @RequestParam(name = "branch_id", required = false) Long branchId,
            @RequestParam(name = "status", required = false) List<PlatformTemplateStatus> status,
            @RequestParam(name = "template_name", required = false) String templateName,
            @SortDefault.SortDefaults({
                    @SortDefault(sort = {"created"}, direction = Sort.Direction.DESC)}) Pageable pageable) {
        log.info("Kakao Biz Message Template, Platform Type : %s, Branch ID : %s, Status : %s, Template Name : %s"
                .formatted(platform, branchId, status, templateName));
        TemplateSearchRequestDto requestDto = TemplateSearchRequestDto.of(platform, branchId, status, templateName);
        ResponseEntity<? super ApiResult<List<TemplateSearchResponseDto>>> response =
                templateSearchService.getTemplateList(requestDto, pageable);
        return response;
    }

    @Tag(name = "템플릿 상세 조회")
    @Operation(summary = "템플릿 상세 조회")
    @GetMapping("/{id}")
    public ResponseEntity<? super ApiResult<TemplateResponseDto>> getTemplate(@NotNull @PathVariable("id") Long id) throws Exception {
        ResponseEntity<? super ApiResult<TemplateResponseDto>> response = templateSearchService.getTemplate(id);
        return response;
    }

    @Tag(name = "템플릿 카테고리 전체 조회")
    @Operation(summary = "템플릿 카테고리 전체 조회")
    @GetMapping("/category/all")
    public ResponseEntity<? super BizTalkResponseDto> getCategory() {
        ResponseEntity<? super BizTalkResponseDto> response = templateExternalService.getCategoryList();
        return response;
    }

    @Tag(name = "템플릿 ClientId 조회")
    @Operation(summary = "템플릿 ClientId 조회")
    @GetMapping("/clientId")
    public ResponseEntity<String> getClientId() {
        ResponseEntity<String> response = templateExternalService.getClientId();
        return response;
    }

    /** Update APIs **/
    @Tag(name = "템플릿 수정")
    @Operation(summary = "템플릿 수정")
    @PutMapping
    public ResponseEntity<? super BizTalkResponseDto> updateTemplate(@RequestBody @Valid KakaoBizMessageTemplatePayload requestDto) {
        ResponseEntity<? super BizTalkResponseDto> response = templateExternalService.updateTemplate(requestDto);
        return response;
    }

    /** Delete APIs **/
    @Tag(name = "템플릿 검수요청 취소")
    @Operation(summary = "템플릿 검수요청 취소")
    @PutMapping("/cancel/request")
    public ResponseEntity<? super BizTalkResponseDto> cancelTemplate(@RequestParam(name = "profileKey") String profileKey,
                                                               @RequestParam(name = "templateCode") String templateCode) {
        ResponseEntity<? super BizTalkResponseDto> response = templateExternalService.cancelTemplate(profileKey, templateCode);
        return response;
    }
}
