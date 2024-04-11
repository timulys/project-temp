package com.kep.core.model.dto.customer;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import java.time.ZonedDateTime;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class CustomerMemberDto {

    @Positive
    private Long id;
    @Positive
    private Long memberId;
    @Positive
    private Long customerId;
    private CustomerDto customer;
    private Boolean favorite;

}
