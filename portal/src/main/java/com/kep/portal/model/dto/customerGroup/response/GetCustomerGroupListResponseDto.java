package com.kep.portal.model.dto.customerGroup.response;

import com.kep.core.model.dto.ResponseDto;
import com.kep.core.model.dto.common.ResponseCode;
import com.kep.portal.model.dto.customerGroup.CustomerGroupDto;
import com.kep.portal.model.entity.customer.CustomerGroup;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.stream.Collectors;

@Getter
public class GetCustomerGroupListResponseDto extends ResponseDto {
    @Schema(description = "고객 그룹 목록")
    private List<CustomerGroupDto> customerGroupResponseDtoList;

    private GetCustomerGroupListResponseDto(String message, List<CustomerGroup> customerGroupList) {
        super(ResponseCode.SUCCESS, message);
        this.customerGroupResponseDtoList =
                customerGroupList.stream().map(CustomerGroupDto::of).collect(Collectors.toList());
    }

    public static ResponseEntity<ResponseDto> noExistCustomerGroupList(String message) {
        ResponseDto result = new ResponseDto(ResponseCode.NOT_EXISTED_DATA, message);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(result);
    }

    public static ResponseEntity<? super GetCustomerGroupListResponseDto> success(String message, List<CustomerGroup> customerGroupList) {
        GetCustomerGroupListResponseDto result = new GetCustomerGroupListResponseDto(message, customerGroupList);
        return ResponseEntity.status(HttpStatus.OK).body(result);
    }
}
