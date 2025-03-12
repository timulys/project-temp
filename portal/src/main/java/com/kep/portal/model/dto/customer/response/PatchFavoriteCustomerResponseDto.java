package com.kep.portal.model.dto.customer.response;

import com.kep.core.model.dto.ResponseDto;
import com.kep.core.model.dto.common.ResponseCode;
import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@Getter
public class PatchFavoriteCustomerResponseDto extends ResponseDto {

    private PatchFavoriteCustomerResponseDto(String message) {
        super(ResponseCode.SUCCESS, message);
    }

    public static ResponseEntity<PatchFavoriteCustomerResponseDto> success(String message) {
        PatchFavoriteCustomerResponseDto result = new PatchFavoriteCustomerResponseDto(message);
        return ResponseEntity.status(HttpStatus.OK).body(result);
    }

    public static ResponseEntity<ResponseDto> notExistedCustomerMember(String message) {
        ResponseDto result = new ResponseDto(ResponseCode.NOT_EXISTED_DATA, message);
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(result);
    }
}
