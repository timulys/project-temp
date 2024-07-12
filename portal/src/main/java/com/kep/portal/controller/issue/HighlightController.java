package com.kep.portal.controller.issue;


import com.kep.core.model.dto.ApiResult;
import com.kep.core.model.dto.ApiResultCode;
import com.kep.core.model.dto.issue.IssueHighlightDto;
import com.kep.portal.service.issue.IssueHighlightService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.constraints.NotNull;
import java.util.List;

@Tag(name = "이슈 하이라이트 API", description = "/api/v1/issue/highlight")
@RestController
@RequestMapping("/api/v1/issue/highlight")
@Slf4j
public class HighlightController {


    @Resource
    private IssueHighlightService issueHighlightService;

    @Tag(name = "이슈 하이라이트 API")
    @Operation(summary = "이슈 하이라이트 전체 목록 조회")
    @GetMapping
    public ResponseEntity<ApiResult<List<IssueHighlightDto>>> index(){

        List<IssueHighlightDto> issueHighlightDtos = issueHighlightService.index();
        ApiResult<List<IssueHighlightDto>> response = ApiResult.<List<IssueHighlightDto>>builder()
                .code(ApiResultCode.succeed)
                .payload(issueHighlightDtos)
                .build();
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @Tag(name = "이슈 하이라이트 API")
    @Operation(summary = "이슈 하이라이트 단건 조회")
    @GetMapping(value = "/{id}")
    public ResponseEntity<ApiResult<IssueHighlightDto>> show(
            @Parameter(description = "이슈 하이라이트 아이디", in = ParameterIn.PATH, required = true)
            @PathVariable("id") @NotNull Long id){

        IssueHighlightDto issueHighlightDto = issueHighlightService.show(id);
        ApiResult<IssueHighlightDto> response = ApiResult.<IssueHighlightDto>builder()
                .code(ApiResultCode.succeed)
                .payload(issueHighlightDto)
                .build();
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @Tag(name = "이슈 하이라이트 API")
    @Operation(summary = "이슈 하이라이트 저장")
    @PostMapping(value = "/")
    public ResponseEntity<ApiResult<IssueHighlightDto>> store(@RequestBody IssueHighlightDto dto){
        IssueHighlightDto store = issueHighlightService.store(dto);
        if(store.getId() != null){
            ApiResult<IssueHighlightDto> response = ApiResult.<IssueHighlightDto>builder()
                    .code(ApiResultCode.succeed)
                    .payload(store)
                    .build();
            return new ResponseEntity<>(response, HttpStatus.CREATED);
        }

        ApiResult<IssueHighlightDto> response = ApiResult.<IssueHighlightDto>builder()
                .code(ApiResultCode.failed)
                .build();
        return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);

    }

    @Tag(name = "이슈 하이라이트 API")
    @Operation(summary = "이슈 하이라이트 수정")
    @PutMapping(value = "/{id}")
    public ResponseEntity<ApiResult<IssueHighlightDto>> update(
            @RequestBody IssueHighlightDto dto
            , @Parameter(description = "이슈 하이라이트 아이디", in = ParameterIn.PATH, required = true)
            @PathVariable("id") @NotNull Long id
    ){

        IssueHighlightDto issueHighlightDto = issueHighlightService.update(id , dto);

        if(issueHighlightDto != null){
            ApiResult<IssueHighlightDto> response = ApiResult.<IssueHighlightDto>builder()
                    .code(ApiResultCode.succeed)
                    .payload(issueHighlightDto)
                    .build();
            return new ResponseEntity<>(response, HttpStatus.CREATED);
        }

        ApiResult<IssueHighlightDto> response = ApiResult.<IssueHighlightDto>builder()
                .code(ApiResultCode.failed)
                .payload(null)
                .build();
        return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Tag(name = "이슈 하이라이트 API")
    @Operation(summary = "이슈 하이라이트 삭제")
    @DeleteMapping(value = "/{id}")
    public ResponseEntity<ApiResult<IssueHighlightDto>> destroy(
            @Parameter(description = "이슈 하이라이트 아이디", in = ParameterIn.PATH, required = true)
            @PathVariable("id") @NotNull Long id
    ){

        boolean delete = issueHighlightService.delete(id);
        if(delete){
            ApiResult<IssueHighlightDto> response = ApiResult.<IssueHighlightDto>builder()
                    .code(ApiResultCode.succeed)
                    .build();
            return new ResponseEntity<>(response, HttpStatus.CREATED);
        }
        ApiResult<IssueHighlightDto> response = ApiResult.<IssueHighlightDto>builder()
                .code(ApiResultCode.failed)
                .build();
        return new ResponseEntity<>(response, HttpStatus.OK);
    }


}
