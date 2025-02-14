package com.dkt.always.talk.config.property;

import jakarta.validation.constraints.NotEmpty;
import lombok.Data;
import org.hibernate.validator.constraints.URL;
import org.springframework.validation.annotation.Validated;

@Validated
@Data
public class Platform {
    @NotEmpty
    @URL
    private String apiBaseUrl;
    private String apiKey;
}
