/**
 * Hotkey Controller 신규
 *
 *  @생성일자      / 만든사람      / 수정내용
 *  2023.03.28 / asher.shin   / 신규
 */
package com.kep.portal.controller.hotkey;

import java.util.List;

import com.mysema.commons.lang.Assert;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.kep.core.model.dto.ApiResult;
import com.kep.core.model.dto.ApiResultCode;
import com.kep.portal.model.dto.hotkey.HotkeyDto;
import com.kep.portal.model.entity.hotkey.Hotkey;
import com.kep.portal.service.hotkey.HotkeyService;

import lombok.extern.slf4j.Slf4j;

@Tag(name = "핫키 (자주 사용하는 문구) API", description = "/api/v1/hotkey")
@Slf4j
@RestController
@RequestMapping("/api/v1/hotkey")
public class HotkeyController {
    

    @Autowired
    private HotkeyService hotkeyservice;
    /**
     * 직원 자주사용하는 문구 리스트 
     * @param member
     * @return
     */
    @Tag(name = "핫키 (자주 사용하는 문구) API")
    @Operation(summary = "자주 사용하는 문구 목록 조회")
    @GetMapping("/list/{id}")
    public ResponseEntity<ApiResult<List<HotkeyDto>>> get(
            @Parameter(description = "사용자 아이디", in = ParameterIn.PATH, required = true)
            @PathVariable Long id
    ){
            
    	log.info("MEMBER, GET, MEMBER: {}",id);
    	List<HotkeyDto> items = hotkeyservice.getListHotkeyByMember(id);
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
    @PostMapping("/manage/{memberId}")
    public ResponseEntity<ApiResult<List<HotkeyDto>>> add(
            @Parameter(description = "사용자 아이디", in = ParameterIn.PATH, required = true)
            @PathVariable Long memberId
            ,@RequestBody HotkeyDto hotkeyDto
    ){
        log.info("MEMBER, POST, MEMBER : {}",memberId);

        Assert.notNull(hotkeyDto,"Dto is null");
        List<HotkeyDto> items = hotkeyservice.store(hotkeyDto,memberId);
        return new ResponseEntity<>(ApiResult.<List<HotkeyDto>>builder()
                .code(ApiResultCode.succeed)
                .payload(items)
                .build(), HttpStatus.OK);

    }
}
