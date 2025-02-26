package com.dkt.always.talk.controller.exceptions;

import com.fasterxml.jackson.core.JsonParseException;
import com.kep.core.model.dto.ResponseDto;
import com.kep.core.model.dto.common.ResponseCode;
import com.kep.core.model.exception.BizException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice(annotations = RestController.class)
@Slf4j
public class TalkServiceExceptionHandler {

    /** 정형화된 에러 처리 (ex. BizException.class 등)*/
    @ExceptionHandler({
            BizException.class
    })
    protected final ResponseEntity<ResponseDto> handleBizException(BizException ex) {
        return ResponseDto.customFailedMessage(ResponseCode.SERVER_ERROR, ex.getLocalizedMessage());
    }

    @ExceptionHandler({
            JsonParseException.class
    })
    protected final ResponseEntity<ResponseDto> handleJsonParseException(JsonParseException ex) {
        return ResponseDto.customFailedMessage(ResponseCode.VALIDATION_FAILED, ex.getLocalizedMessage());
    }

    /** 정형화 되지 않은 에러 처리 (ex. Exception.class)*/
    @ExceptionHandler({
            Exception.class
    })
    protected final ResponseEntity<ResponseDto> handleException(Exception ex) {
        return ResponseDto.customFailedMessage(ResponseCode.SERVER_ERROR, ex.getLocalizedMessage());
    }
}