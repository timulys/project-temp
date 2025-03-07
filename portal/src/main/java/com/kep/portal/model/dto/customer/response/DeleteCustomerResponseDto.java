package com.kep.portal.model.dto.customer.response;

import com.kep.core.model.dto.ResponseDto;
import com.kep.core.model.dto.common.ResponseCode;
import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@Getter
public class DeleteCustomerResponseDto extends ResponseDto {
    private DeleteCustomerResponseDto(String message) {
        super(ResponseCode.SUCCESS, message);
    }

    public static ResponseEntity<DeleteCustomerResponseDto> success(String message) {
        DeleteCustomerResponseDto result = new DeleteCustomerResponseDto(message);
        return ResponseEntity.status(HttpStatus.OK).body(result);
    }
}
