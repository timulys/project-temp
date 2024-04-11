/**
 *
 *
 * @수정일자	  / 수정자			 / 수정내용
 * 2023.04.12 / asher.shin	 / 고객검색 respository
 */

package com.kep.portal.repository.customer;

import com.kep.core.model.dto.customer.CustomerDto;
import com.kep.portal.model.dto.customer.*;

import java.util.List;

public interface CustomerSearchRepository {


    //계약고객
    List<CustomerCommonResponseDto> searchCustomerContractList(Long memberId,String name);

    //기념일고객
    List<CustomerCommonResponseDto> searchCustomerAnniversaryList(Long memberId,String name);

    //유망고객
    List<CustomerCommonResponseDto> searchCustomerPromiseList(Long memberId,String name);

    //미분류고객
    List<CustomerCommonResponseDto> searchUnclassfiedCustomerList(Long memberId,String name);

    List<CustomerCommonResponseDto> searchFavoritesCustomerList(Long memberId,String name);
    
    //고객번호 검색 고객

    CustomerResponseDto searchCustomerByGuestId(Long guestId);
}
