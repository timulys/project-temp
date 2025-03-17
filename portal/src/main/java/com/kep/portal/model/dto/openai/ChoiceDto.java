package com.kep.portal.model.dto.openai;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.io.Serializable;

@Data
public class ChoiceDto implements Serializable {
    private Integer index;
    private MessageDto message;
    @JsonProperty("finish_reason")
    private String finishReason;
}
