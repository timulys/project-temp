package com.kep.portal.controller.platform;

import com.kep.core.model.dto.ApiResult;
import com.kep.core.model.dto.ApiResultCode;
import com.kep.core.model.dto.platform.BizTalkHistoryDto;
import com.kep.core.model.type.QueryParam;
import com.kep.portal.model.dto.platform.BizTalkHistoryCondition;
import com.kep.portal.service.platform.BizTalkHistoryService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.SortDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/v1/platform/biztalk-history")
public class BizTalkHistoryController {

    @Resource
    private BizTalkHistoryService bizTalkHistoryService;

    /**
     * 상담관리에서 톡 이력 조회
     */
    @GetMapping
    @PreAuthorize("hasAnyAuthority('READ_TALK_HISTORY')")
    public ResponseEntity<ApiResult<List<BizTalkHistoryDto>>> search(
            @QueryParam BizTalkHistoryCondition condition,
            @SortDefault.SortDefaults({
                    @SortDefault(sort = {"sendDate"}, direction = Sort.Direction.DESC)}) Pageable pageable
    ) {

        Page<BizTalkHistoryDto> page = bizTalkHistoryService.search(condition, pageable);

        ApiResult<List<BizTalkHistoryDto>> result = ApiResult.<List<BizTalkHistoryDto>>builder()
                .payload(page.getContent())
                .code(ApiResultCode.succeed)
                .currentPage(page.getNumber())
                .totalElement(page.getTotalElements())
                .totalPage(page.getTotalPages())
                .build();

        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    /**
     * 상담원이 톡 전송 이력 조회
     */
    @GetMapping("/search")
    public ResponseEntity<ApiResult<List<BizTalkHistoryDto>>> memberSearch(
            @QueryParam BizTalkHistoryCondition condition,
            @SortDefault.SortDefaults({
                    @SortDefault(sort = {"sendDate"}, direction = Sort.Direction.DESC)}) Pageable pageable
    ) {

        Page<BizTalkHistoryDto> page = bizTalkHistoryService.search(condition, pageable);

        ApiResult<List<BizTalkHistoryDto>> result = ApiResult.<List<BizTalkHistoryDto>>builder()
                .payload(page.getContent())
                .code(ApiResultCode.succeed)
                .currentPage(page.getNumber())
                .totalElement(page.getTotalElements())
                .totalPage(page.getTotalPages())
                .build();

        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @GetMapping("/portal/download")
    public void portalDownload(HttpServletResponse res,
                         @QueryParam BizTalkHistoryCondition condition){
        bizTalkHistoryService.portalHistoryDownload(res, condition);
    }
    @GetMapping("/management/download")
    public void managementDownload(HttpServletResponse res,
                         @QueryParam BizTalkHistoryCondition condition){
        bizTalkHistoryService.managementHistoryDownload(res, condition);
    }

}
