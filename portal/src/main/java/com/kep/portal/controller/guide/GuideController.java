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
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
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
    public ResponseEntity<ApiResult<List<GuideDto>>> get(
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
    public ResponseEntity<ApiResult<List<GuideDto>>> get(
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
    public ResponseEntity<ApiResult<GuideDto>> create(
            @Valid @RequestBody GuidePayload guidePayload,
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
    public ResponseEntity<ApiResult<GuideSearchResponseDto>> search(
            @QueryParam @Valid GuideSearchDto searchDto
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
    public ResponseEntity<ApiResult<List<GuideDto>>> manageSearch(
            @QueryParam @Valid GuideSearchDto searchDto,
            @SortDefault.SortDefaults({
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

    @GetMapping("/search/more")
    @PreAuthorize("hasAnyAuthority('READ_GUIDE')")
    public ResponseEntity<ApiResult<List<GuideDto>>> moreSearch(
            @QueryParam @Valid GuideSearchDto searchDto,
            @SortDefault.SortDefaults({
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
    public ResponseEntity<ApiResult<GuideLogDto>> saveLog(
            @QueryParam GuideLogDto dto
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
    public ResponseEntity<ApiResult> checkRequire(
            @QueryParam GuideLogDto guideLogDto
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
    public ResponseEntity<ApiResult> patchBranch(
            @RequestParam("guide_id") Long guideId,
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
    public ResponseEntity<ApiResult> patchTeam(
            @RequestParam("guide_id") Long guideId,
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
    public ResponseEntity<ApiResult<String>> excelUpload(
            @RequestParam("file") MultipartFile file
    ) throws IOException, InvalidFormatException {

        guideService.saveExcelGuide(file);


        ApiResult<String> response = ApiResult.<String>builder()
                .code(ApiResultCode.succeed)
                .build();
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/download")
    public void download(HttpServletResponse res){
        guideService.download(res);
    }

}
