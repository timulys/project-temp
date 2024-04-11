package com.kep.legacy.config.datasource;

import org.springframework.stereotype.Component;

import java.lang.annotation.*;

/**
 * 솔루션용 Mapper
 */
@Target({ ElementType.TYPE })
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Component
public @interface ConnectBranchMapper {
	String value() default "";
}
