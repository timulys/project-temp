package com.kep.core.model.dto.branch;

import com.kep.core.model.dto.privilege.RoleDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Positive;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BranchRoleDto {
    @Positive
    private Long branchId;
    private List<Long> roleList;
    private List<RoleDto> roles;
}
