/**
 * Guest Memo Repository 추가
 *
 *  @생성일자      / 만든사람      / 수정내용
 *  2023.04.06 / asher.shin   / 신규
 */
package com.kep.portal.repository.customer;

import com.kep.portal.model.entity.customer.Customer;
import com.kep.portal.model.entity.customer.GuestMemo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GuestMemoRepository extends JpaRepository<GuestMemo, Long> {

    GuestMemo findByGuestIdAndMemberId(Long guestId, Long memberId);

    GuestMemo findByCustomerIdAndMemberId(Long customerId, Long memberId);
}
