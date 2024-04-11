/**
 * 고객 카테고리 리스트 조회 dto
 *  @생성일자      / 만든사람		 	/ 수정내용
 * 	 2023.04.12 / asher.shin   / 신규
 */
package com.kep.portal.model.dto.customer;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CustomerListDto {

    private List<CustomerCommonResponseDto> FavoritesCustomers;

    private List<CustomerCommonResponseDto> customerAnniversarys;

    private List<CustomerCommonResponseDto> customerContracts;

    private List<CustomerCommonResponseDto> customerPromises;

    private List<CustomerCommonResponseDto> customers;
}
