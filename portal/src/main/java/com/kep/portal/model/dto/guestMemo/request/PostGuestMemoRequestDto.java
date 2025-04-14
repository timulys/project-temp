package com.kep.portal.model.dto.guestMemo.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class PostGuestMemoRequestDto {
    @Schema(description = "Guest ID")
    private Long guestId;
    @Schema(description = "고객 ID")
    private Long customerId;
    @Schema(description = "고객(게스트) 메모 내용")
    private String content;

}
