package com.kep.portal.model.dto.customerGroup.response;

import com.kep.core.model.dto.ResponseDto;
import com.kep.core.model.dto.common.ResponseCode;
import com.kep.core.model.dto.common.ResponseMessage;
import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@Getter
public class DeleteCustomerGroupResponseDto extends ResponseDto {
    private DeleteCustomerGroupResponseDto() {
        super(ResponseCode.SUCCESS, ResponseMessage.SUCCESS);
    }

    public static ResponseEntity<DeleteCustomerGroupResponseDto> success() {
        DeleteCustomerGroupResponseDto result = new DeleteCustomerGroupResponseDto();
        return ResponseEntity.status(HttpStatus.OK).body(result);
    }
}
