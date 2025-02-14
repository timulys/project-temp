package com.dkt.always.talk.config.property;

import jakarta.validation.constraints.NotEmpty;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

import java.util.Map;

@ConfigurationProperties(prefix = "application.platform")
@Validated
@Data
public class PlatformProperty {
    @NotEmpty
    private Map<String, Platform> platforms;
}
