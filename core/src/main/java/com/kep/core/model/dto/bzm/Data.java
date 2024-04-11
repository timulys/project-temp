
package com.kep.core.model.dto.bzm;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

import java.util.List;

@lombok.Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class Data {

    private List<Object> comments;
    private String createdAt;
    private Long id;
    private String inspectRequestAt;
    private String inspectStatus;
    private String inspectedAt;
    private List<Message> messages;
    private String modifiedAt;
    private String name;
    private String status;

}
