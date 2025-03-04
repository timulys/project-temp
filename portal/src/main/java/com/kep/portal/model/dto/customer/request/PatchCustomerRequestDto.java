package com.kep.portal.model.dto.customer.request;

import com.kep.core.model.dto.customer.CustomerContactDto;
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
    private Long id;
    @NotBlank
    private String name;
    @NotNull
    private Long customerGroupId;

    private List<CustomerContactDto> contacts;
}
