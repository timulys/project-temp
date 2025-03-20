package com.kep.portal.model.dto.customer.response;

import com.kep.core.model.dto.ResponseDto;
import com.kep.core.model.dto.common.ResponseCode;
import com.kep.core.model.dto.common.ResponseMessage;
import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@Getter
public class PostCustomerResponseDto extends ResponseDto {
    private PostCustomerResponseDto(String message) {
        super(ResponseCode.SUCCESS, message);
    }

    public static ResponseEntity<PostCustomerResponseDto> success(String message) {
        PostCustomerResponseDto result = new PostCustomerResponseDto(message);
        return ResponseEntity.status(HttpStatus.OK).body(result);
    }
}
