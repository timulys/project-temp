package com.kep.core.model.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.kep.core.model.dto.common.ResponseCode;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@Getter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class ResponseDto {
    /**
     * 결과 코드
     */
    @Schema(description = "응답 코드", defaultValue = "SUC")
    private String responseCode;
    /**
     * 결과 메시지
     */
    @Schema(description = "응답 메시지")
    private String message;

    /**
     * Biz Message Center API Processing Failed Response
     * @return
     */
    public static ResponseEntity<ResponseDto> customFailedMessage(String code, String message) {
        ResponseDto result = new ResponseDto(code, message);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(result);
    }

    /**
     * Internal Server Error Response
     * @param message
     * @return
     */
    public static ResponseEntity<ResponseDto> serverErrorMessage(String message) {
        ResponseDto result = new ResponseDto(ResponseCode.SERVER_ERROR, message);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(result);
    }

    /**
     * Database Error Response
     * @return
     */
    public static ResponseEntity<ResponseDto> databaseErrorMessage(String message) {
        ResponseDto result = new ResponseDto(ResponseCode.DATABASE_ERROR, message);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(result);
    }

    /**
     * No Search Data
     * @param message
     * @return
     */
    public static ResponseEntity<ResponseDto> noSearchData(String message) {
        ResponseDto result = new ResponseDto(ResponseCode.SUCCESS, message);
        return ResponseEntity.status(HttpStatus.OK).body(result);
    }

    /**
     * Validation Failed Response
     * @return
     */
    public static ResponseEntity<ResponseDto> validationFailedMessage(String message) {
        ResponseDto result = new ResponseDto(ResponseCode.VALIDATION_FAILED, message);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(result);
    }

    /**
     * Biz Message Center API Call Failed Response
     * @return
     */
    public static ResponseEntity<ResponseDto> bizCenterCallFailedMessage(String message) {
        ResponseDto result = new ResponseDto(ResponseCode.BZM_CALL_FAILED, message);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(result);
    }

    /**
     * AlimTalk Service API Call Failed Response
     * @return
     */
    public static ResponseEntity<ResponseDto> alimTalkFailedMessage(String message) {
        ResponseDto result = new ResponseDto(ResponseCode.ALIM_TALK_CALL_FAILED, message);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(result);
    }

    /**
     * FriendTalk Service APi Call Failed Response
     * @return
     */
    public static ResponseEntity<ResponseDto> friendTalkFailedMessage(String message) {
        ResponseDto result = new ResponseDto(ResponseCode.FRIEND_TALK_CALL_FAILED, message);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(result);
    }

    /**
     * Member Not Existed Response
     * @return
     */
    public static ResponseEntity<ResponseDto> notExistedMember(String message) {
        ResponseDto result = new ResponseDto(ResponseCode.NOT_EXISTED_MEMBER, message);
        return ResponseEntity.status(HttpStatus.OK).body(result);
    }

    /**
     * Customer Not Existed Response
     * @return
     */
    public static ResponseEntity<ResponseDto> notExistedCustomer(String message) {
        ResponseDto result = new ResponseDto(ResponseCode.NOT_EXISTED_CUSTOMER, message);
        return ResponseEntity.status(HttpStatus.OK).body(result);
    }

    /**
     * Customer Group Not Existed Response
     * @return
     */
    public static ResponseEntity<ResponseDto> notExistedCustomerGroup(String message) {
        ResponseDto result = new ResponseDto(ResponseCode.NOT_EXISTED_CUSTOMER_GROUP, message);
        return ResponseEntity.status(HttpStatus.OK).body(result);
    }

    /**
     * No Permission Response
     * @return
     */
    public static ResponseEntity<ResponseDto> noPermission(String message) {
        ResponseDto result = new ResponseDto(ResponseCode.NO_PERMISSION, message);
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(result);
    }
}
