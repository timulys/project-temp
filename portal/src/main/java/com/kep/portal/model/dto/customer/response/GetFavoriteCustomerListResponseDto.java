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
public class GetFavoriteCustomerListResponseDto extends ResponseDto {
    @Schema(description = "즐겨찾기 고객 목록")
    private List<CustomerDto> favoriteList;

    private GetFavoriteCustomerListResponseDto(List<CustomerDto> favoriteList, String message) {
        super(ResponseCode.SUCCESS, message);
        this.favoriteList = favoriteList;
    }

    public static ResponseEntity<GetFavoriteCustomerListResponseDto> success(List<CustomerDto> favoriteList, String message) {
        GetFavoriteCustomerListResponseDto result = new GetFavoriteCustomerListResponseDto(favoriteList, message);
        return ResponseEntity.status(HttpStatus.OK).body(result);
    }
}
