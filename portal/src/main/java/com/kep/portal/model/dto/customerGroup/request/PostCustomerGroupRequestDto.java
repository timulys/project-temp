package com.kep.portal.model.dto.customerGroup.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
public class PostCustomerGroupRequestDto {
    @NotBlank
    @Schema(description = "고객 그룹명")
    private String groupName;
    @NotNull
    @Schema(description = "고객 그룹 관리 상담원 ID")
    private Long memberId;
}
