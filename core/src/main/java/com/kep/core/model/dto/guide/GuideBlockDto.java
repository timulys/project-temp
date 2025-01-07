package com.kep.core.model.dto.guide;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import nonapi.io.github.classgraph.json.Id;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

@Schema(description = "가이드 블록 정보")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class GuideBlockDto {

    @Positive
    @NotNull
    @Schema(description = "가이드 블록 아이디")
    private Long id;

    @Schema(description = "블록명")
    private String blockName;

    @Schema(description = "")
    private Long requireId;

    @NotEmpty
    @Schema(description = "")
    private String payload;

    @Schema(description = "블록에 포함 된 메세지 갯수")
    private Integer contentCount;

    @Schema(description = "")
    private String messageCondition;

    @Schema(description = "")
    private String fileCondition;
}
