package com.kep.portal.model.dto.openai.request;

import com.kep.portal.model.dto.openai.MessageDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PostChatRequestDto implements Serializable {
    private String model;
    private List<MessageDto> messages;
}
