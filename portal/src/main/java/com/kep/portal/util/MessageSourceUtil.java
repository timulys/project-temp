package com.kep.portal.util;

import com.kep.core.model.enums.MessageCode;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class MessageSourceUtil {
    private final MessageSource messageSource;

    public String getMessage(MessageCode messageCode) {
        return messageSource.getMessage(messageCode.getCode(), null, LocaleContextHolder.getLocale());
    }

    public String success() {
        return messageSource.getMessage("success", null, LocaleContextHolder.getLocale());
    }
}