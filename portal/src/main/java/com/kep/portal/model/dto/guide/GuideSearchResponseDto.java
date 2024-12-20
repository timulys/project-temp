package com.kep.portal.model.dto.guide;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.kep.core.model.dto.guide.GuideDto;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class GuideSearchResponseDto {

    @Schema(description = "카테고리에 속한 가이드 중 이름에 키워드가 포함된 가이드 갯수")
    private Long nameCount;
    @Schema(description = "카테고리에 속한 가이드 중 메세지에 키워드가 포함된 가이드 갯수")
    private Long messageCount;
    private Long fileCount;

    private List<GuideDto> nameSearch;
    private List<GuideDto> messageSearch;
    private List<GuideDto> fileSearch;
}
