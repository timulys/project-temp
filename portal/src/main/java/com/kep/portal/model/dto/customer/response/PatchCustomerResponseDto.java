package com.kep.portal.model.dto.customer.response;

import com.kep.core.model.dto.ResponseDto;
import com.kep.core.model.dto.common.ResponseCode;
import com.kep.core.model.dto.common.ResponseMessage;
import lombok.Getter;
import lombok.ToString;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

/**
 * 고객 정보 업데이트 Response DTO
 */
@Getter
@ToString
public class PatchCustomerResponseDto extends ResponseDto {
    private PatchCustomerResponseDto(String message) {
        super(ResponseCode.SUCCESS, message);
    }

    public static ResponseEntity<PatchCustomerResponseDto> success(String message) {
        PatchCustomerResponseDto result = new PatchCustomerResponseDto(message);
        return ResponseEntity.status(HttpStatus.OK).body(result);
    }
}
