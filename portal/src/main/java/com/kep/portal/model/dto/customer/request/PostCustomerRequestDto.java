package com.kep.portal.model.dto.customer.request;

import com.kep.core.model.dto.customer.CustomerContactDto;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;

@Data
public class PostCustomerRequestDto {
    @NotBlank
    @Schema(description = "고객명")
    private String name;
    @NotNull
    @Schema(description = "고객 그룹 ID")
    private Long customerGroupId;
    @Schema(description = "고객 연락처 목록")
    private List<CustomerContactDto> contacts;
}
