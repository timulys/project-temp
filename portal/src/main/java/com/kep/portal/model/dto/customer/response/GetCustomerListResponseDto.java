package com.kep.portal.model.dto.customer.response;

import com.kep.core.model.dto.ResponseDto;
import com.kep.core.model.dto.common.ResponseCode;
import com.kep.core.model.dto.customer.CustomerDto;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;

@Getter
public class GetCustomerListResponseDto extends ResponseDto {
    @Schema(description = "고객 목록")
    private List<CustomerDto> customerList;

    private GetCustomerListResponseDto(List<CustomerDto> customerList, String message) {
        super(ResponseCode.SUCCESS, message);
        this.customerList = customerList;
    }

    public static ResponseEntity<GetCustomerListResponseDto> success(List<CustomerDto> customerList, String message) {
        GetCustomerListResponseDto result = new GetCustomerListResponseDto(customerList, message);
        return ResponseEntity.status(HttpStatus.OK).body(result);
    }
}
