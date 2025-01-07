package com.kep.portal.config.property;


import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotEmpty;

/**
 * Talk Biz Service 프로퍼티
 */
@ConfigurationProperties(prefix = "application.talk")
@Validated
@Data
public class TalkProperty {
    @NotEmpty
    private String apiVersion;
    @NotEmpty
    private String apiBasePath;
}
