/**
 * Guest Memo Dto 신규
 *
 *  @생성일자      / 만든사람      / 수정내용
 *  2023.04.06 / asher.shin   / 신규
 */
package com.kep.portal.model.dto.customer;

import com.kep.portal.model.entity.customer.Customer;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.ZonedDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class GuestMemoDto {

    //PK
    private Long id;

    //고객(게스트) PK
    private Long guestId;

    //직원 PK
    private Long memberId;

    //고객 PK
    private Long customerId;

    //메모내용
    private String content;

    //등록시간
    private ZonedDateTime created;

    //수정시간
    private ZonedDateTime modified;
}
