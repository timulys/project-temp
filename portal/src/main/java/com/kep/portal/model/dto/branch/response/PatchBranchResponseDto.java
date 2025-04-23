package com.kep.portal.model.dto.branch.response;

import com.kep.core.model.dto.ResponseDto;
import com.kep.core.model.dto.branch.BranchDto;
import com.kep.core.model.dto.common.ResponseCode;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.ToString;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@Getter
@ToString
public class PatchBranchResponseDto extends ResponseDto {
    @Schema(description = "브랜치 정보")
    private final BranchDto branch;

    private PatchBranchResponseDto(BranchDto branch, String message) {
        super(ResponseCode.SUCCESS, message);
        this.branch = branch;
    }

    public static ResponseEntity<PatchBranchResponseDto> success(BranchDto branch, String message) {
        PatchBranchResponseDto result = new PatchBranchResponseDto(branch, message);
        return ResponseEntity.status(HttpStatus.OK).body(result);
    }
}
