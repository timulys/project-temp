/**
 * Hotkey Controller 신규
 *
 *  @생성일자      / 만든사람      / 수정내용
 *  2023.03.28 / asher.shin   / 신규
 */
package com.kep.portal.controller.hotkey;

import com.kep.core.model.dto.ApiResult;
import com.kep.core.model.dto.ApiResultCode;
import com.kep.portal.model.dto.hotkey.HotkeyDto;
import com.kep.portal.service.hotkey.HotkeyService;
import com.mysema.commons.lang.Assert;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "핫키 (자주 사용하는 문구) API", description = "/api/v1/hotkey")
@Slf4j
@RestController
@RequestMapping("/api/v1/hotkey")
public class HotkeyController {
    

    @Autowired
    private HotkeyService hotkeyservice;
    /**
     * 직원 자주사용하는 문구 리스트 
     * @return
     */
    @Tag(name = "핫키 (자주 사용하는 문구) API")
    @Operation(summary = "자주 사용하는 문구 목록 조회")
    @GetMapping("/list")
    public ResponseEntity<ApiResult<List<HotkeyDto>>> get(Pageable pageable){
        Page<HotkeyDto> items = hotkeyservice.getListHotkeyByMember(pageable);
        //직원의 자주사용하는 문구 리스트 받기

        ApiResult<List<HotkeyDto>> response = ApiResult.<List<HotkeyDto>>builder()
            .code(ApiResultCode.succeed)
            .payload(items.getContent())
            .totalPage(items.getTotalPages())
            .totalElement(items.getTotalElements())
            .currentPage(items.getNumber())
            .build();
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
    
    /**
     * 자주 사용하는 문구 저장/수정
     * @param hotkeyDto
     * @return
     */
    @Tag(name = "핫키 (자주 사용하는 문구) API")
    @Operation(summary = "자주 사용하는 문구 저장/수정")
    @PostMapping("/manage")
    public ResponseEntity<ApiResult<List<HotkeyDto>>> overwrite(@RequestBody HotkeyDto hotkeyDto){
        log.info("MEMBER, POST, HOTKEY : {}", hotkeyDto);

        Assert.notNull(hotkeyDto,"Dto is null");
        List<HotkeyDto> items = hotkeyservice.overwriteStore(hotkeyDto);
        return new ResponseEntity<>(ApiResult.<List<HotkeyDto>>builder()
                .code(ApiResultCode.succeed)
                .payload(items)
                .build(), HttpStatus.OK);

    }

    /**
     * 자주 사용하는 문구 저장/수정
     * @param hotkeyDto
     * @return
     */
    @Tag(name = "핫키 (자주 사용하는 문구) API")
    @Operation(summary = "자주 사용하는 문구 저장")
    @PostMapping("/manage/append")
    public ResponseEntity<ApiResult<List<HotkeyDto>>> append(@RequestBody HotkeyDto hotkeyDto){
        log.info("MEMBER, POST, HOTKEY : {}", hotkeyDto);

        Assert.notNull(hotkeyDto,"Dto is null");
        List<HotkeyDto> items = hotkeyservice.appendStore(hotkeyDto);
        return new ResponseEntity<>(ApiResult.<List<HotkeyDto>>builder()
            .code(ApiResultCode.succeed)
            .payload(items)
            .build(), HttpStatus.OK);
    }

    /**
     * 자주 사용하는 문구 저장/수정
     * @param hotkeyDto
     * @return
     */
    @Tag(name = "핫키 (자주 사용하는 문구) API")
    @Operation(summary = "자주 사용하는 문구 수정")
    @PostMapping("/manage/modify")
    public ResponseEntity<ApiResult> modify(@RequestBody HotkeyDto hotkeyDto){
        log.info("MEMBER, POST, HOTKEY : {}", hotkeyDto);

        Assert.notNull(hotkeyDto,"Dto is null");
        hotkeyservice.modifyStore(hotkeyDto);
        return new ResponseEntity<>(ApiResult.builder()
            .code(ApiResultCode.succeed)
            .build(), HttpStatus.OK);
    }

    @Tag(name = "핫키 (자주 사용하는 문구) API")
    @Operation(summary = "자주 사용하는 문구 삭제")
    @DeleteMapping("/manage/{id}")
    public ResponseEntity<ApiResult> delete(@PathVariable Long id){
        hotkeyservice.deleteStore(id);
        return new ResponseEntity<>(ApiResult.builder()
            .code(ApiResultCode.succeed)
            .build(), HttpStatus.OK);
    }
}
