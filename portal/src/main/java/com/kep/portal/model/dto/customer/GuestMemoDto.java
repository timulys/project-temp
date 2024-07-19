/**
 * Guest Memo Dto 신규
 *
 *  @생성일자      / 만든사람      / 수정내용
 *  2023.04.06 / asher.shin   / 신규
 */
package com.kep.portal.model.dto.customer;

import com.kep.portal.model.entity.customer.Customer;
import io.swagger.v3.oas.annotations.media.Schema;
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
    @Schema(description = "고객 메모 아이디")
    private Long id;

    //고객(게스트) PK
    @Schema(description = "고객(게스트) 아이디")
    private Long guestId;

    //직원 PK
    @Schema(description = "직원 아이디")
    private Long memberId;

    //고객 PK
    @Schema(description = "고객 아이디")
    private Long customerId;

    //메모내용
    @Schema(description = "메모내용")
    private String content;

    //등록시간
    @Schema(description = "등록시간")
    private ZonedDateTime created;

    //수정시간
    @Schema(description = "수정시간")
    private ZonedDateTime modified;
}
