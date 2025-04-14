/**
 * Guest Memo Dto 신규
 *
 *  @생성일자      / 만든사람      / 수정내용
 *  2023.04.06 / asher.shin   / 신규
 */
package com.kep.portal.model.dto.guestMemo;

import com.kep.portal.model.entity.customer.GuestMemo;
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
    @Schema(description = "고객 메모 아이디")
    private Long id;
    @Schema(description = "고객(게스트) 아이디")
    private Long guestId;
    @Schema(description = "직원 아이디")
    private Long memberId;
    @Schema(description = "고객 아이디")
    private Long customerId;
    @Schema(description = "메모내용")
    private String content;
    @Schema(description = "등록시간")
    private ZonedDateTime created;
    @Schema(description = "수정시간")
    private ZonedDateTime modified;

    public static GuestMemoDto of(GuestMemo guestMemo) {
        GuestMemoDto dto = new GuestMemoDto();
        dto.setId(guestMemo.getId());
        dto.setGuestId(guestMemo.getGuestId());
        dto.setCustomerId(guestMemo.getCustomerId());
        dto.setMemberId(guestMemo.getMemberId());
        dto.setContent(guestMemo.getContent());
        dto.setCreated(guestMemo.getCreated());
        dto.setModified(guestMemo.getModified());
        return dto;
    }
}
