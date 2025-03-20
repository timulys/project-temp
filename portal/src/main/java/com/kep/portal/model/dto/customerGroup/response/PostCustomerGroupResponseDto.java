package com.kep.portal.model.dto.customerGroup.response;

import com.kep.core.model.dto.ResponseDto;
import com.kep.core.model.dto.common.ResponseCode;
import com.kep.core.model.dto.common.ResponseMessage;
import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@Getter
public class PostCustomerGroupResponseDto extends ResponseDto {
    private PostCustomerGroupResponseDto(String message) {
        super(ResponseCode.SUCCESS, message);
    }

    public static ResponseEntity<PostCustomerGroupResponseDto> success(String message) {
        PostCustomerGroupResponseDto result = new PostCustomerGroupResponseDto(message);
        return ResponseEntity.status(HttpStatus.OK).body(result);
    }

    public static ResponseEntity<ResponseDto> existedGroupName(String message) {
        ResponseDto result = new ResponseDto(ResponseCode.DUPLICATED_DATA, message);
        return ResponseEntity.status(HttpStatus.CONFLICT).body(result);
    }
}
