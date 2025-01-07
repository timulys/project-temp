package com.kep.core.model.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.kep.core.model.dto.common.ResponseCode;
import com.kep.core.model.dto.common.ResponseMessage;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.http.ResponseEntity;

@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class ResponseDto {
    /**
     * 결과 코드
     */
    private String code;
    /**
     * 결과 메시지
     */
    private String message;

    /**
     * Database Error 공통 Response
     * @return
     */
    public static ResponseEntity<ResponseDto> databaseErrorMessage() {
        return ResponseEntity.internalServerError()
                .body(new ResponseDto(ResponseCode.DATABASE_ERROR, ResponseMessage.DATABASE_ERROR));
    }

    /**
     * Validation Failed 공통 Response
     * @return
     */
    public static ResponseEntity<ResponseDto> validationFailedMessage() {
        return ResponseEntity.badRequest()
                .body(new ResponseDto(ResponseCode.VALIDATION_FAILED, ResponseMessage.VALIDATION_FAILED));
    }

    /**
     * Biz Message Center API Call Failed Response
     * @return
     */
    public static ResponseEntity<ResponseDto> bizCenterCallFailedMessage() {
        return ResponseEntity.badRequest()
                .body(new ResponseDto(ResponseCode.BZM_CALL_FAILED, ResponseMessage.BZM_CALL_FAILED));
    }

    /**
     * AlimTalk Service API Call Failed Response
     * @return
     */
    public static ResponseEntity<ResponseDto> alimTalkFailedMessage() {
        return ResponseEntity.badRequest()
                .body(new ResponseDto(ResponseCode.ALIM_TALK_CALL_FAILED, ResponseMessage.ALIM_TALK_CALL_FAILED));
    }

    /**
     * Biz Message Center API Processing Failed Response
     *
     * @return
     */
    public static ResponseEntity<ResponseDto> customFailedMessage(String code, String message) {
        return ResponseEntity.ok(new ResponseDto(code, message));
    }
}
