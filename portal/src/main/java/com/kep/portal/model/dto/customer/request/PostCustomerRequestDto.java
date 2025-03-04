package com.kep.portal.model.dto.customer.request;

import com.kep.core.model.dto.customer.CustomerContactDto;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;

@Data
public class PostCustomerRequestDto {
    @NotBlank
    private String name;
    @NotNull
    private Long customerGroupId;

    private List<CustomerContactDto> contacts;
}
