package com.kep.portal.model.dto.customerGroup.request;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
public class PutCustomerGroupRequestDto {
    @NotNull
    private Long id;
    @NotNull
    private Long memberId;
    @NotBlank
    private String groupName;
}
