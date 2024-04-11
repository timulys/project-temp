package com.kep.core.model.dto.platform.kakao;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class KakaoBizSearchSendEvent {
    private String clientId;
    private String startDate;
    private String endDate;
    private String statusCode;
    private String kepStatus;
    private Integer rows;
    private String etc1;
    private String etc2;
    private Integer size;
    private String lastUid;
    private Integer page;
    private String sendProfileKey;
}
