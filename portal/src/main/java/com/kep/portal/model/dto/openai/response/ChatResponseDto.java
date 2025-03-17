package com.kep.portal.model.dto.openai.response;

import com.kep.portal.model.dto.openai.ChoiceDto;
import com.kep.portal.model.dto.openai.UsageDto;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.List;

@Data
public class ChatResponseDto implements Serializable {
    private String id;
    private String object;
    private LocalDate created;
    private String model;
    private UsageDto usage;
    private List<ChoiceDto> choices;
}
