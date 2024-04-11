package com.kep.core.model.dto.customer;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CustomerAnniversaryDto {

    @Positive
    private Long id;

    @Positive
    @JsonIgnore
    private Long customerId;

    private AnniversaryType type;

    private String anniversary;

}
