package com.kep.portal.model.dto.customer.response;

import com.kep.core.model.dto.ResponseDto;
import com.kep.core.model.dto.common.ResponseCode;
import com.kep.core.model.dto.common.ResponseMessage;
import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

/**
 * 고객 정보 업데이트 Response DTO
 */
@Getter
public class PatchCustomerResponseDto extends ResponseDto {
    private PatchCustomerResponseDto() {
        super(ResponseCode.SUCCESS, ResponseMessage.SUCCESS);
    }

    public static ResponseEntity<PatchCustomerResponseDto> success() {
        PatchCustomerResponseDto result = new PatchCustomerResponseDto();
        return ResponseEntity.status(HttpStatus.OK).body(result);
    }
}
