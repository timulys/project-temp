package com.kep.portal.model.dto.customer.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class PatchFavoriteCustomerRequestDto {
    @Schema(description = "상담원 ID")
    private Long memberId;
    @Schema(description = "고객 ID")
    private Long customerId;
}
