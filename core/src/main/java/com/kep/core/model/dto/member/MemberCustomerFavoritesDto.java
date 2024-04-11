package com.kep.core.model.dto.member;

import com.kep.core.model.dto.customer.CustomerDto;
import lombok.*;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import java.time.ZonedDateTime;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class MemberCustomerFavoritesDto {

    @Positive
    private Long id;
    @Positive
    private Long memberId;
    @Positive
    @NotNull
    private Long customerId;

    private CustomerDto customer;

    private ZonedDateTime created;
}
