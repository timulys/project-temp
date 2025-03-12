package com.kep.portal.model.dto.customer.request;

import lombok.Data;

@Data
public class PatchFavoriteCustomerRequestDto {
    private Long memberId;
    private Long customerId;
}
