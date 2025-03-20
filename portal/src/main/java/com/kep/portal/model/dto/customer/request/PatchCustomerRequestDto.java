package com.kep.portal.model.dto.customer.request;

import com.kep.core.model.dto.customer.CustomerContactDto;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * 고객 정보 업데이트 Request DTO
 */
@Data
public class PatchCustomerRequestDto {
    @NotNull
    @Schema(description = "고객 ID")
    private Long id;
    @NotBlank
    @Schema(description = "고객 이름")
    private String name;
    @Schema(description = "고객 그룹 ID")
    private Long customerGroupId;
    @Schema(description = "고객 연락처 목록")
    private List<CustomerContactDto> contacts;
}
