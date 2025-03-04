package com.kep.portal.model.dto.customerGroup.response;

import com.kep.core.model.dto.ResponseDto;
import com.kep.core.model.dto.common.ResponseCode;
import com.kep.core.model.dto.common.ResponseMessage;
import com.kep.portal.model.dto.customerGroup.CustomerGroupDto;
import com.kep.portal.model.entity.customer.CustomerGroup;
import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.stream.Collectors;

@Getter
public class GetCustomerGroupListResponseDto extends ResponseDto {
    private List<CustomerGroupDto> customerGroupResponseDtoList;

    private GetCustomerGroupListResponseDto(List<CustomerGroup> customerGroupList) {
        super(ResponseCode.SUCCESS, ResponseMessage.SUCCESS);
        this.customerGroupResponseDtoList =
                customerGroupList.stream().map(CustomerGroupDto::of).collect(Collectors.toList());
    }

    public static ResponseEntity<ResponseDto> noExistCustomerGroupList() {
        ResponseDto result = new ResponseDto(ResponseCode.NOT_EXISTED_DATA, ResponseMessage.NOT_EXISTED_DATA);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(result);
    }

    public static ResponseEntity<? super GetCustomerGroupListResponseDto> success(List<CustomerGroup> customerGroupList) {
        GetCustomerGroupListResponseDto result = new GetCustomerGroupListResponseDto(customerGroupList);
        return ResponseEntity.status(HttpStatus.OK).body(result);
    }
}
