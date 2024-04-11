package com.kep.portal.controller.issue;


import com.kep.core.model.dto.ApiResult;
import com.kep.core.model.dto.ApiResultCode;
import com.kep.core.model.dto.issue.IssueHighlightDto;
import com.kep.portal.service.issue.IssueHighlightService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.constraints.NotNull;
import java.util.List;

@RestController
@RequestMapping("/api/v1/issue/highlight")
@Slf4j
public class HighlightController {


    @Resource
    private IssueHighlightService issueHighlightService;

    @GetMapping
    public ResponseEntity<ApiResult<List<IssueHighlightDto>>> index(){

        List<IssueHighlightDto> issueHighlightDtos = issueHighlightService.index();
        ApiResult<List<IssueHighlightDto>> response = ApiResult.<List<IssueHighlightDto>>builder()
                .code(ApiResultCode.succeed)
                .payload(issueHighlightDtos)
                .build();
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<ApiResult<IssueHighlightDto>> show(
            @PathVariable("id") @NotNull Long id){

        IssueHighlightDto issueHighlightDto = issueHighlightService.show(id);
        ApiResult<IssueHighlightDto> response = ApiResult.<IssueHighlightDto>builder()
                .code(ApiResultCode.succeed)
                .payload(issueHighlightDto)
                .build();
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

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

    @PutMapping(value = "/{id}")
    public ResponseEntity<ApiResult<IssueHighlightDto>> update(
            @RequestBody IssueHighlightDto dto
            , @PathVariable("id") @NotNull Long id){

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

    @DeleteMapping(value = "/{id}")
    public ResponseEntity<ApiResult<IssueHighlightDto>> destroy(@PathVariable("id") @NotNull Long id){

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
