package com.kep.core.model.dto.customer;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.kep.core.model.dto.platform.AuthorizeType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import java.time.ZonedDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CustomerAuthorizedDto {

    @Positive
    private Long id;

    @Positive
    @JsonIgnore
    private Long customerId;

    private AuthorizeType type;

    private String platformUserId;

    private ZonedDateTime created;

}
