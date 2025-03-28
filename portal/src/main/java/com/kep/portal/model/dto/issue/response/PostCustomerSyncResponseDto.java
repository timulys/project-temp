package com.kep.portal.model.dto.issue.response;

import com.kep.core.model.dto.ResponseDto;
import com.kep.core.model.dto.common.ResponseCode;
import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@Getter
public class PostCustomerSyncResponseDto extends ResponseDto {
    private PostCustomerSyncResponseDto(String message) {
        super(ResponseCode.SUCCESS, message);
    }

    public static ResponseEntity<PostCustomerSyncResponseDto> success(String message) {
        PostCustomerSyncResponseDto result = new PostCustomerSyncResponseDto(message);
        return ResponseEntity.status(HttpStatus.OK).body(result);
    }

    public static ResponseEntity<? super PostCustomerSyncResponseDto> existedSync(String message) {
        ResponseDto result = new ResponseDto(ResponseCode.DUPLICATED_DATA, message);
        return ResponseEntity.status(HttpStatus.OK).body(result);
    }
}
