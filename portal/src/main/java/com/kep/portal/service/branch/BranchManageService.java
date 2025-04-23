package com.kep.portal.service.branch;

import com.kep.core.model.dto.branch.BranchDto;
import com.kep.portal.model.dto.branch.request.PatchBranchRequestDto;
import com.kep.portal.model.dto.branch.response.PatchBranchResponseDto;
import com.kep.portal.model.entity.branch.Branch;
import org.springframework.http.ResponseEntity;

public interface BranchManageService {
    // Response Entity Methods
    ResponseEntity<? super PatchBranchResponseDto> patchBranch(PatchBranchRequestDto dto);

    // Aggregation Methods
    BranchDto saveBranch(BranchDto dto, Branch headQuarters);
}
