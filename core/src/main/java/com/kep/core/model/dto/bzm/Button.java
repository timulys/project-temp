
package com.kep.core.model.dto.bzm;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class Button {

    private String linkAndroid;
    private String linkIos;
    private String linkMobile;
    private String linkName;
    private String linkPc;
    private String linkType;
    private Long ordering;

}
