package com.kep.core.model.dto.platform.kakao.bizTalk.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.kep.core.model.dto.ResponseDto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class TemplateCategoryResponseDto extends ResponseDto {
    private String code;
    private String name;
    private String groupName;
    private String inclusion;
    private String exclusion;
}
