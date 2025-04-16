package com.kep.portal.model.dto.customer.response;

import com.kep.core.model.dto.ResponseDto;
import com.kep.core.model.dto.common.ResponseCode;
import com.kep.core.model.dto.customer.CustomerDto;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.ToString;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;

@Getter
@ToString
public class GetFavoriteCustomerListResponseDto extends ResponseDto {
    @Schema(description = "즐겨찾기 고객 목록")
    private List<CustomerDto> customerList;

    private GetFavoriteCustomerListResponseDto(List<CustomerDto> customerList, String message) {
        super(ResponseCode.SUCCESS, message);
        this.customerList = customerList;
    }

    public static ResponseEntity<GetFavoriteCustomerListResponseDto> success(List<CustomerDto> favoriteList, String message) {
        GetFavoriteCustomerListResponseDto result = new GetFavoriteCustomerListResponseDto(favoriteList, message);
        return ResponseEntity.status(HttpStatus.OK).body(result);
    }
}
