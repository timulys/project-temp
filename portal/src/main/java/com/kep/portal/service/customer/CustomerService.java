package com.kep.portal.service.customer;

import java.util.List;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

import com.kep.portal.model.dto.customer.request.PatchCustomerRequestDto;
import com.kep.portal.model.dto.customer.request.PatchFavoriteCustomerRequestDto;
import com.kep.portal.model.dto.customer.response.*;
import com.kep.portal.model.dto.customer.request.PostCustomerRequestDto;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.kep.portal.model.entity.customer.Customer;

@Service
/*
 *  @생성일자      / 만든사람      / 수정내용
 *  2023.04.12 / asher.shin   / 카테고리별 조회 및 즐겨찾기 변경 추가
 *  2023.05.23 / asher.shin   / 관심상품 리스트 가져오기 추가
 *  2023.06.12 / asher.shin    / getCustomerCategoryList name 검색을 위해 파라미터 추가
 *  
 */
public interface CustomerService {
	Customer findById(@NotNull @Positive Long id);
	List<Customer> search(@NotNull String subject, @NotNull String query);

	/** Create Methods **/
	ResponseEntity<? super PostCustomerResponseDto> createCustomer(PostCustomerRequestDto requestDto);
	ResponseEntity<? super PatchFavoriteCustomerResponseDto> patchFavoriteCustomer(PatchFavoriteCustomerRequestDto requestDto);
	/** Retrieve Methods **/
	ResponseEntity<? super GetCustomerResponseDto> findCustomer(Long customerId);
	ResponseEntity<? super GetCustomerListResponseDto> findAllCustomer(Long memberId);
	ResponseEntity<? super GetFavoriteCustomerListResponseDto> findAllFavoriteCustomerList(Long memberId);
	/** Update Methods **/
	ResponseEntity<? super PatchCustomerResponseDto> updateCustomer(PatchCustomerRequestDto requestDto);
	/** Delete Methods **/
	ResponseEntity<? super DeleteCustomerResponseDto> deleteCustomer(Long customerId);
}
