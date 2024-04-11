package com.kep.platform.model.dto;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

/**
 * 카카오 상담톡 송신 이벤트 유효성 검사
 */
@Documented
@Constraint(validatedBy = KakaoCounselSendEventValidator.class)
@Target({ElementType.FIELD,ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
public @interface KakaoCounselSendEventConstraint {

    String message() default "Invalid Event";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
