package com.kep.portal.model.dto.customerGroup.request;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
public class PostCustomerGroupRequestDto {
    @NotBlank
    private String groupName;
    @NotNull
    private Long memberId;
}
