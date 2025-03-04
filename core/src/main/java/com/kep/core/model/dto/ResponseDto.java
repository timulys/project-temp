package com.kep.core.model.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.kep.core.model.dto.common.ResponseCode;
import com.kep.core.model.dto.common.ResponseMessage;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class ResponseDto {
    /**
     * 결과 코드
     */
    private String responseCode;
    /**
     * 결과 메시지
     */
    private String message;

    /**
     * Database Error Response
     * @return
     */
    public static ResponseEntity<ResponseDto> databaseErrorMessage() {
        ResponseDto result = new ResponseDto(ResponseCode.DATABASE_ERROR, ResponseMessage.DATABASE_ERROR);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(result);
    }

    /**
     * Validation Failed Response
     * @return
     */
    public static ResponseEntity<ResponseDto> validationFailedMessage() {
        ResponseDto result = new ResponseDto(ResponseCode.VALIDATION_FAILED, ResponseMessage.VALIDATION_FAILED);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(result);
    }

    /**
     * Biz Message Center API Call Failed Response
     * @return
     */
    public static ResponseEntity<ResponseDto> bizCenterCallFailedMessage() {
        ResponseDto result = new ResponseDto(ResponseCode.BZM_CALL_FAILED, ResponseMessage.BZM_CALL_FAILED);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(result);
    }

    /**
     * AlimTalk Service API Call Failed Response
     * @return
     */
    public static ResponseEntity<ResponseDto> alimTalkFailedMessage() {
        ResponseDto result = new ResponseDto(ResponseCode.ALIM_TALK_CALL_FAILED, ResponseMessage.ALIM_TALK_CALL_FAILED);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(result);
    }

    /**
     * FriendTalk Service APi Call Failed Response
     * @return
     */
    public static ResponseEntity<ResponseDto> friendTalkFailedMessage() {
        ResponseDto result = new ResponseDto(ResponseCode.FRIEND_TALK_CALL_FAILED, ResponseMessage.FRIEND_TALK_CALL_FAILED);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(result);
    }

    /**
     * Biz Message Center API Processing Failed Response
     * @return
     */
    public static ResponseEntity<ResponseDto> customFailedMessage(String code, String message) {
        ResponseDto result = new ResponseDto(code, message);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(result);
    }

    /**
     * Member Not Existed Response
     * @return
     */
    public static ResponseEntity<ResponseDto> notExistedMember() {
        ResponseDto result = new ResponseDto(ResponseCode.NOT_EXISTED_MEMBER, ResponseMessage.NOT_EXISTED_MEMBER);
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(result);
    }

    /**
     * Customer Not Existed Response
     * @return
     */
    public static ResponseEntity<ResponseDto> notExistedCustomer() {
        ResponseDto result = new ResponseDto(ResponseCode.NOT_EXISTED_CUSTOMER, ResponseMessage.NOT_EXISTED_CUSTOMER);
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(result);
    }

    /**
     * Customer Group Not Existed Response
     * @return
     */
    public static ResponseEntity<ResponseDto> notExistedCustomerGroup() {
        ResponseDto result = new ResponseDto(ResponseCode.NOT_EXISTED_CUSTOMER_GROUP, ResponseMessage.NOT_EXISTED_CUSTOMER_GROUP);
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(result);
    }

    /**
     * No Permission Response
     * @return
     */
    public static ResponseEntity<ResponseDto> noPermission() {
        ResponseDto result = new ResponseDto(ResponseCode.NO_PERMISSION, ResponseMessage.NO_PERMISSION);
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(result);
    }
}
