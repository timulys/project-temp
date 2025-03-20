package com.kep.portal.model.dto.customerGroup;

import com.kep.portal.model.entity.customer.CustomerGroup;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CustomerGroupDto {
    @Schema(description = "고객 그룹 ID")
    private Long id;
    @Schema(description = "고객 그룹명")
    private String groupName;
    @Schema(description = "고객 그룹 관리 상담원 ID")
    private Long memberId;

    public static CustomerGroupDto of(CustomerGroup customerGroup) {
        return new CustomerGroupDto(customerGroup.getId(), customerGroup.getGroupName(), customerGroup.getMember().getId());
    }
}
