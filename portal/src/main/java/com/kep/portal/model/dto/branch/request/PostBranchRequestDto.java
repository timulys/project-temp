package com.kep.portal.model.dto.branch.request;

import com.kep.core.model.dto.branch.BranchDto;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.ToString;

import java.util.List;

@Data
@ToString
public class PostBranchRequestDto {
    @Schema(description = "브랜치 저장 목록")
    private List<BranchDto> branchList;
}
