package com.kep.portal.model.dto.platform;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.kep.core.model.dto.platform.PlatformType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class BizTalkHistoryStatisticsDto {

    private LocalDate from;
    private LocalDate to;
    private PlatformType type;

    private List<BizTalkHistoryStatisticsSuccessFailDto> successFails;
    private List<BizTalkHistoryStatisticsTemplateDto> templates;

    private BizTalkHistoryStatisticsSuccessFailDto successFail;
    private BizTalkHistoryStatisticsTemplateDto template;
}
