package com.kep.core.model.dto.platform.kakao.bizTalk.response;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.kep.core.model.dto.ResponseDto;
import com.kep.core.model.dto.platform.kakao.KakaoBizMessageTemplatePayload;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.http.ResponseEntity;

import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class BizTalkResponseDto<T> extends ResponseDto {
    private String code;
    private String message;
    private Integer totalCount;
    private String image;
    /* V2와 V3를 모두 대응하기 위해 Alias 추가 */
    @JsonAlias(value = {"list", "data"})
    private T data;
}
