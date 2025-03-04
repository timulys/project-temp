package com.kep.portal.model.dto.customerGroup;

import com.kep.portal.model.entity.customer.CustomerGroup;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CustomerGroupDto {
    private Long id;
    private String groupName;
    private Long memberId;

    public static CustomerGroupDto of(CustomerGroup customerGroup) {
        return new CustomerGroupDto(customerGroup.getId(), customerGroup.getGroupName(), customerGroup.getMember().getId());
    }
}
