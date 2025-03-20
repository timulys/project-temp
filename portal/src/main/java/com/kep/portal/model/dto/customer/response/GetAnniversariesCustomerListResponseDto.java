package com.kep.portal.model.dto.customer.response;

import com.kep.core.model.dto.ResponseDto;
import com.kep.core.model.dto.common.ResponseCode;
import com.kep.core.model.dto.customer.CustomerDto;
import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;

@Getter
public class GetAnniversariesCustomerListResponseDto extends ResponseDto {
    private List<CustomerDto> customerList;

    private GetAnniversariesCustomerListResponseDto(List<CustomerDto> customerList, String message) {
        super(ResponseCode.SUCCESS, message);
        this.customerList = customerList;
    }

    public static ResponseEntity<GetAnniversariesCustomerListResponseDto> success(List<CustomerDto> customerList, String message) {
        GetAnniversariesCustomerListResponseDto result = new GetAnniversariesCustomerListResponseDto(customerList, message);
        return ResponseEntity.status(HttpStatus.OK).body(result);
    }
}
