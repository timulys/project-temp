package com.kep.portal.model.dto.customer.response;

import com.kep.core.model.dto.ResponseDto;
import com.kep.core.model.dto.common.ResponseCode;
import com.kep.core.model.dto.customer.CustomerDto;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@Getter
public class GetCustomerResponseDto extends ResponseDto {
    @Schema(description = "고객 정보")
    private CustomerDto customer;

    private GetCustomerResponseDto(CustomerDto customer, String message) {
        super(ResponseCode.SUCCESS, message);
        this.customer = customer;
    }

    public static ResponseEntity<GetCustomerResponseDto> success(CustomerDto customerDto, String message) {
        GetCustomerResponseDto result = new GetCustomerResponseDto(customerDto, message);
        return ResponseEntity.status(HttpStatus.OK).body(result);
    }
}
