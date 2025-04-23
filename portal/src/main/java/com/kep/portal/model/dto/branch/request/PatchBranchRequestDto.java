package com.kep.portal.model.dto.branch.request;

import com.kep.core.model.dto.branch.BranchDto;
import com.kep.core.model.dto.privilege.RoleDto;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.ToString;

import java.util.List;

@Data
@ToString
public class PatchBranchRequestDto {
    @Schema(description = "브랜치 ID")
    private Long id;
    @Schema(description = "브랜치명")
    private String name;
    @Schema(description = "브랜치 배정 정책")
    private String assign;
    @Schema(description = "브랜치 사용 유무")
    private Boolean enabled;
    @Schema(description = "브랜치 근무 예외 여부")
    private Boolean offDutyHours;
    @Schema(description = "브랜치 최대 상담 건수 설정 타입(Batch: 일괄, Individual: 개별")
    private String maxCounselType;
    @Schema(description = "브랜치 최대 상담 건수")
    private Integer maxCounsel;
    @Schema(description = "역할목록")
    private List<RoleDto> roles;

    public static PatchBranchRequestDto of(BranchDto dto) {
        PatchBranchRequestDto requestDto = new PatchBranchRequestDto();
        requestDto.setId(dto.getId());
        requestDto.setName(dto.getName());
        requestDto.setAssign(dto.getAssign().name());
        requestDto.setEnabled(dto.getEnabled());
        requestDto.setOffDutyHours(dto.getOffDutyHours());
        requestDto.setMaxCounselType(dto.getMaxCounselType().name());
        requestDto.setMaxCounsel(dto.getMaxCounsel());
        requestDto.setRoles(dto.getRoles());
        return requestDto;
    }
}
