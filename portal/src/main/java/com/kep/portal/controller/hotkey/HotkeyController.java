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
    public ResponseEntity<ApiResult<List<HotkeyDto>>> get(){
            
    	List<HotkeyDto> items = hotkeyservice.getListHotkeyByMember(null);
        //직원의 자주사용하는 문구 리스트 받기
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
    @Operation(summary = "자주 사용하는 문구 저장/수정")
    @PostMapping("/manage")
    public ResponseEntity<ApiResult<List<HotkeyDto>>> add(@RequestBody HotkeyDto hotkeyDto){
        log.info("MEMBER, POST, HOTKEY : {}", hotkeyDto);

        Assert.notNull(hotkeyDto,"Dto is null");
        List<HotkeyDto> items = hotkeyservice.store(hotkeyDto);
        return new ResponseEntity<>(ApiResult.<List<HotkeyDto>>builder()
                .code(ApiResultCode.succeed)
                .payload(items)
                .build(), HttpStatus.OK);

    }
}
