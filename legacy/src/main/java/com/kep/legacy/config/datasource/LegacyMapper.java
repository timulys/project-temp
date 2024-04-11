package com.kep.legacy.config.datasource;

import org.springframework.stereotype.Component;

import java.lang.annotation.*;

/**
 * 기간계용 Mapper
 */
@Target({ ElementType.TYPE })
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Component
public @interface LegacyMapper {
	String value() default "";
}
