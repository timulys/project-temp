package com.kep.portal.controller.guide;

import com.kep.core.model.dto.ApiResult;
import com.kep.core.model.dto.ApiResultCode;
import com.kep.core.model.dto.guide.GuideDto;
import com.kep.core.model.dto.guide.GuideLogDto;
import com.kep.core.model.dto.guide.GuidePayload;
import com.kep.core.model.type.QueryParam;
import com.kep.portal.model.dto.guide.GuideSearchDto;
import com.kep.portal.model.dto.guide.GuideSearchResponseDto;
import com.kep.portal.service.guide.GuideLogService;
import com.kep.portal.service.guide.GuideService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.springdoc.api.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.SortDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.IOException;
import java.util.List;
import java.util.Map;

@Tag(name = "가이드 API", description = "/api/v1/guide")
@Slf4j
@RestController
@RequestMapping("/api/v1/guide")
public class GuideController {

    @Resource
    private GuideService guideService;

    @Resource
    private GuideLogService guideLogService;

    /**
     * 가이드 검색
     * SB-CP-T03
     *
     * @param categoryId
     * @param pageable
     * @return
     */
    @GetMapping
    @PreAuthorize("hasAnyAuthority('READ_GUIDE')")
    @Tag(name = "가이드 API")
    @Operation(summary = "가이드 목록 조회", description = "가이드 목록 조회(SB-CP-T03)")
    public ResponseEntity<ApiResult<List<GuideDto>>> get(
            @Parameter(description = "카테고리 아이디")
            @RequestParam(value = "category_id", required = false) Long categoryId,
            @SortDefault.SortDefaults({
                    @SortDefault(sort = {"name"}, direction = Sort.Direction.ASC)}) Pageable pageable) {

        try {
            Page<GuideDto> items;
            if (categoryId == null) {
                items = guideService.getAllSubCategory(pageable, null);
            } else {
                items = guideService.getAllSubCategory(pageable, categoryId);
            }

            ApiResult<List<GuideDto>> response = ApiResult.<List<GuideDto>>builder()
                    .code(ApiResultCode.succeed)
                    .payload(items.getContent())
                    .totalElement(items.getTotalElements())
                    .totalPage(items.getTotalPages())
                    .currentPage(items.getNumber())
                    .build();
            return new ResponseEntity<>(response, HttpStatus.OK);

        } catch (Exception e) {
            log.error(e.getLocalizedMessage());
            ApiResult<List<GuideDto>> response = ApiResult.<List<GuideDto>>builder()
                    .code(ApiResultCode.failed)
                    .message(e.getLocalizedMessage())
                    .build();
            response.setError("<<SB-CP-T03>>");
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * 가이드 검색
     * SB-FM-010
     *
     * @return
     */
    @GetMapping("/m")
    @PreAuthorize("hasAnyAuthority('READ_GUIDE')")
    @Tag(name = "가이드 API")
    @Operation(summary = "가이드 검색", description = "가이드 검색(SB-FM-010)")
    public ResponseEntity<ApiResult<List<GuideDto>>> get(
            @Parameter(description = "카테고리 아이디")
            @RequestParam(value = "category_id") Long categoryId
    ) {
        try {

            List<GuideDto> items = guideService.getAllGuide(categoryId);


            ApiResult<List<GuideDto>> response = ApiResult.<List<GuideDto>>builder()
                    .code(ApiResultCode.succeed)
                    .payload(items)
                    .build();
            return new ResponseEntity<>(response, HttpStatus.OK);

        } catch (Exception e) {
            ApiResult<List<GuideDto>> response = ApiResult.<List<GuideDto>>builder()
                    .code(ApiResultCode.failed)
                    .message(e.getLocalizedMessage())
                    .build();
            response.setError("<<SB-FM-010>>");
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * TODO :: 최대 자리수 요건 정의 필요 20240717 volka
     *
     * 상담가이드 생성, 수정
     * SB-CA-006
     *
     * @param guidePayload
     * @param enabled
     * @return
     * @throws Exception
     */
    @PostMapping
    @PreAuthorize("hasAnyAuthority('WRITE_GUIDE')")
    @Tag(name = "가이드 API")
    @Operation(summary = "상담가이드 저장", description = "상담가이드 저장(SB-CA-006)")
    public ResponseEntity<ApiResult<GuideDto>> create(
            @Valid @RequestBody GuidePayload guidePayload
            , @Parameter(description = "저장 여부 (임시저장 시 false)")
            @RequestParam(value = "enabled", required = false, defaultValue = "true") Boolean enabled
    ) throws Exception {

        try {
            GuideDto guideDto = guideService.store(guidePayload, enabled);

            ApiResult<GuideDto> response = ApiResult.<GuideDto>builder()
                    .code(ApiResultCode.succeed)
                    .payload(guideDto)
                    .build();
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            ApiResult<GuideDto> response = ApiResult.<GuideDto>builder()
                    .code(ApiResultCode.failed)
                    .message(e.getLocalizedMessage())
                    .build();
            response.setError("<<SB-CA-006-CUD>>");
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

    /**
     * 가이드 검색
     * SB-CP-T07
     *
     * @param searchDto
     * @return
     * @throws Exception
     */
    @GetMapping("/search")
    @PreAuthorize("hasAnyAuthority('READ_GUIDE')")
    @Tag(name = "가이드 API")
    @Operation(summary = "가이드 검색", description = "가이드 검색 (SB-CP-T07)")
    public ResponseEntity<ApiResult<GuideSearchResponseDto>> search(
            @ParameterObject @QueryParam @Valid GuideSearchDto searchDto
    ) throws Exception {
        try {
            GuideSearchResponseDto searchResult = guideService.getSearchDto(searchDto);

            ApiResult<GuideSearchResponseDto> response = ApiResult.<GuideSearchResponseDto>builder()
                    .code(ApiResultCode.succeed)
                    .payload(searchResult)
                    .build();
            return new ResponseEntity<>(response, HttpStatus.OK);

        } catch (Exception e) {
            ApiResult<GuideSearchResponseDto> response = ApiResult.<GuideSearchResponseDto>builder()
                    .code(ApiResultCode.failed)
                    .message(e.getLocalizedMessage())
                    .build();
            response.setError("<<SB-CP-T07>>");
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * 가이드 검색
     * SB-CA-006
     *
     * @param searchDto
     * @param pageable
     * @return
     */
    @GetMapping("/manage-search")
    @PreAuthorize("hasAnyAuthority('WRITE_GUIDE')")
    @Tag(name = "가이드 API")
    @Operation(summary = "가이드 검색", description = "가이드 검색(SB-CA-006)")
    public ResponseEntity<ApiResult<List<GuideDto>>> manageSearch(
            @ParameterObject @QueryParam @Valid GuideSearchDto searchDto,
            @ParameterObject @SortDefault.SortDefaults({
                    @SortDefault(sort = {"name"}, direction = Sort.Direction.ASC)}) Pageable pageable
    ) {
        try {
            Page<GuideDto> searchGuide = guideService.getSearchGuide(searchDto, pageable);

            ApiResult<List<GuideDto>> response = ApiResult.<List<GuideDto>>builder()
                    .code(ApiResultCode.succeed)
                    .payload(searchGuide.getContent())
                    .totalPage(searchGuide.getTotalPages())
                    .totalElement(searchGuide.getTotalElements())
                    .currentPage(searchGuide.getNumber())
                    .build();
            return new ResponseEntity<>(response, HttpStatus.OK);

        } catch (Exception e) {
            log.info("MANAGER SEARCH ERROR: {}", e.getLocalizedMessage(),e);
            ApiResult<List<GuideDto>> response = ApiResult.<List<GuideDto>>builder()
                    .code(ApiResultCode.failed)
                    .message(e.getLocalizedMessage())
                    .build();
            response.setError("<<SB-CA-006>>");
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }



    @Tag(name = "가이드 API")
    @Operation(summary = "가이드 검색", description = "가이드 검색(SB-CA-006)")
    @GetMapping("/manage-search-dtl/{guideId}")
    @PreAuthorize("hasAnyAuthority('WRITE_GUIDE')")
    public ResponseEntity<ApiResult<GuideDto>> manageSearchDetail(
            @Parameter(description = "가이드 아이디", in = ParameterIn.PATH, required = true)
            @PathVariable("guideId") Long guideId
    ) {
        try {
            GuideDto guideDto = guideService.getGuide(guideId);

            ApiResult<GuideDto> response = ApiResult.<GuideDto>builder()
                    .code(ApiResultCode.succeed)
                    .payload(guideDto)
                    .build();
            return new ResponseEntity<>(response, HttpStatus.OK);

        } catch (Exception e) {
            log.info("MANAGER SEARCH ERROR: {}", e.getLocalizedMessage(),e);
            ApiResult<GuideDto> response = ApiResult.<GuideDto>builder()
                    .code(ApiResultCode.failed)
                    .message(e.getLocalizedMessage())
                    .build();
            response.setError("<<SB-CA-006>>");
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/search/more")
    @PreAuthorize("hasAnyAuthority('READ_GUIDE')")
    @Tag(name = "가이드 API")
    @Operation(summary = "가이드 목록 검색")
    public ResponseEntity<ApiResult<List<GuideDto>>> moreSearch(
            @ParameterObject @QueryParam @Valid GuideSearchDto searchDto,
            @ParameterObject @SortDefault.SortDefaults({
                    @SortDefault(sort = {"name"}, direction = Sort.Direction.ASC)}) Pageable pageable
    ) {
        try {

            Page<GuideDto> searchResponse = null;
            switch (searchDto.getType()) {
                case name:
                    searchResponse = guideService.getNameSearch(searchDto, pageable);
                    break;
                case file:
                    searchResponse = guideService.getFileSearch(searchDto, pageable);
                    break;
                case message:
                    searchResponse = guideService.getMessageSearch(searchDto, pageable);
                    break;
            }

            if (searchResponse == null) {
                ApiResult<List<GuideDto>> response = ApiResult.<List<GuideDto>>builder()
                        .code(ApiResultCode.succeed)
                        .totalPage(0)
                        .totalElement(0L)
                        .currentPage(0)
                        .build();
                return new ResponseEntity<>(response, HttpStatus.OK);
            }

            ApiResult<List<GuideDto>> response = ApiResult.<List<GuideDto>>builder()
                    .code(ApiResultCode.succeed)
                    .payload(searchResponse.getContent())
                    .totalPage(searchResponse.getTotalPages())
                    .totalElement(searchResponse.getTotalElements())
                    .currentPage(searchResponse.getNumber())
                    .build();
            return new ResponseEntity<>(response, HttpStatus.OK);

        } catch (Exception e) {
            log.info("SEARCH MORE ERROR: {}", e.getLocalizedMessage(),e);
            ApiResult<List<GuideDto>> response = ApiResult.<List<GuideDto>>builder()
                    .code(ApiResultCode.failed)
                    .message(e.getLocalizedMessage())
                    .build();
            response.setError("<<SB-CA-006>> name-search");
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * 가이드 로그 적재
     *
     * @return
     */
    @PostMapping("/log")
    @Tag(name = "가이드 API")
    @Operation(summary = "가이드 로그 저장")
    public ResponseEntity<ApiResult<GuideLogDto>> saveLog(
            @ParameterObject @QueryParam GuideLogDto dto
    ) {
        try {
            GuideLogDto log = guideLogService.store(dto);


            ApiResult<GuideLogDto> response = ApiResult.<GuideLogDto>builder()
                    .code(ApiResultCode.succeed)
                    .payload(log)
                    .build();

            return new ResponseEntity<>(response, HttpStatus.OK);

        } catch (Exception e) {
            ApiResult<GuideLogDto> response = ApiResult.<GuideLogDto>builder()
                    .code(ApiResultCode.failed)
                    .message(e.getLocalizedMessage())
                    .build();
            response.setError("<<SB-CP-SAVELOG>>");
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * 가이드 필수블록 전송했는지 확인
     *
     * @param guideLogDto
     * @return
     */
    @GetMapping("/check-require")
    @Tag(name = "가이드 API")
    @Operation(summary = "가이드 필수요소 체크", description = "가이드 필수블록 전송했는지 확인")
    public ResponseEntity<ApiResult> checkRequire(
            @ParameterObject @QueryParam GuideLogDto guideLogDto
    ) {

        try {
            Map<Integer, Boolean> requireLog = guideLogService.getRequireLog(guideLogDto);

            ApiResult response = ApiResult.builder()
                    .payload(requireLog)
                    .code(ApiResultCode.succeed)
                    .build();

            return new ResponseEntity<>(response, HttpStatus.OK);

        } catch (Exception e) {
            ApiResult response = ApiResult.builder()
                    .code(ApiResultCode.failed)
                    .message(e.getLocalizedMessage())
                    .build();
            response.setError("<<SB-CP-T03-REQUIRE>>");
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping
    @PreAuthorize("hasAnyAuthority('WRITE_GUIDE')")
    @Tag(name = "가이드 API")
    @Operation(summary = "가이드 삭제")
    public ResponseEntity<ApiResult<Map<String, String>>> delete(
            @RequestParam List<Long> ids
    ) {
        try {
            Map<String, String> resultMap = guideService.deleteAllById(ids);

            ApiResult<Map<String, String>> response = ApiResult.<Map<String, String>>builder()
                    .payload(resultMap)
                    .code(ApiResultCode.succeed)
                    .build();

            return new ResponseEntity<>(response, HttpStatus.OK);

        } catch (Exception e) {
            ApiResult<Map<String, String>> response = ApiResult.<Map<String, String>>builder()
                    .code(ApiResultCode.failed)
                    .message(e.getLocalizedMessage())
                    .build();
            response.setError("<<SB-CA-006-DELETE>>");
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PatchMapping("/branch")
    @PreAuthorize("hasRole('ADMIN')")
    @Tag(name = "가이드 API")
    @Operation(summary = "가이드 브랜치 수정")
    public ResponseEntity<ApiResult> patchBranch(
            @Parameter(description = "가이드 아이디")
            @RequestParam("guide_id") Long guideId,
            @Parameter(description = "브랜치 아이디")
            @RequestParam("branch_id") Long branchId
    ) {
        try {
            guideService.patchBranch(guideId, branchId);

            ApiResult response = ApiResult.builder()
                    .code(ApiResultCode.succeed)
                    .build();
            return new ResponseEntity<>(response, HttpStatus.OK);

        } catch (Exception e) {
            ApiResult response = ApiResult.builder()
                    .code(ApiResultCode.failed)
                    .message(e.getLocalizedMessage())
                    .build();
            response.setError("<<SB-CA-006-BRANCH>>");
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PatchMapping("/team")
    @PreAuthorize("hasRole('ADMIN')")
    @Tag(name = "가이드 API")
    @Operation(summary = "가이드 팀 수정")
    public ResponseEntity<ApiResult> patchTeam(
            @Parameter(description = "가이드 아이디")
            @RequestParam("guide_id") Long guideId,
            @Parameter(description = "팀 아이디")
            @RequestParam("team_id") Long teamId
    ) {

        try {
            guideService.patchTeam(guideId, teamId);

            ApiResult response = ApiResult.builder()
                    .code(ApiResultCode.succeed)
                    .build();
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            ApiResult response = ApiResult.builder()
                    .code(ApiResultCode.failed)
                    .message(e.getLocalizedMessage())
                    .build();
            response.setError("<<SB-CA-006-TEAM>>");
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

    @PostMapping("/upload")
    @Tag(name = "가이드 API")
    @Operation(summary = "엑셀 업로드")
    public ResponseEntity<ApiResult<String>> excelUpload(
            @RequestParam("file") MultipartFile file
    ) throws IOException, InvalidFormatException {

        guideService.saveExcelGuide(file);


        ApiResult<String> response = ApiResult.<String>builder()
                .code(ApiResultCode.succeed)
                .build();
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @Tag(name = "가이드 API")
    @Operation(summary = "엑셀 다운로드")
    @GetMapping("/download")
    public void download(HttpServletResponse res){
        guideService.download(res);
    }

}
