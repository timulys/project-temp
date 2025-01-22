package com.kep.core.model.dto.platform.kakao;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.kep.core.model.dto.platform.kakao.vo.Result;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class KakaoBizTalkSendResponse {
    // 다중 발송을 위한 Results 값은 List로 담을 수 있도록..
    private List<Result> results;
}
