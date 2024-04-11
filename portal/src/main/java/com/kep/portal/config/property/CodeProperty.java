package com.kep.portal.config.property;

import com.kep.portal.model.entity.site.Code;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import java.util.List;

@ConfigurationProperties(prefix = "application")
@Validated
@Data
public class CodeProperty {

	private List<Code> codes;

	public String get(String group, String code) {

		return null;
	}

	public List<String> get(String group) {

		return null;
	}
}
