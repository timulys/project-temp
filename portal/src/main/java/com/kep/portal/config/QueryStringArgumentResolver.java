package com.kep.portal.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kep.core.model.type.QueryParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.MethodParameter;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import javax.annotation.Resource;
import java.beans.PropertyDescriptor;
import java.lang.reflect.ParameterizedType;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 쿼리 파라미터를 객체로 매핑시 설정
 */
@Component
@Slf4j
public class QueryStringArgumentResolver implements HandlerMethodArgumentResolver {

    @Override
    public boolean supportsParameter(final MethodParameter parameter) {
        return parameter.hasParameterAnnotation(QueryParam.class);
//        return parameter.getParameterAnnotation(QueryParam.class) != null;
    }

    @Override
    public Object resolveArgument(@NonNull MethodParameter parameter, ModelAndViewContainer mavContainer,
                                  NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {
        Class<?> paramType = parameter.getParameterType();
        Object dto = paramType.getDeclaredConstructor().newInstance(); // Parsing Parameter Object

        Map<String, String[]> parameterMap = webRequest.getParameterMap();
        Map<String, Object> camelCaseMap = parameterMap.entrySet().stream()
                .collect(Collectors.toMap(
                        entry -> toCamelCase(entry.getKey()),
                        // List 또는 단일값 처리
                        entry -> entry.getValue().length > 1 ? Arrays.asList(entry.getValue()) : entry.getValue()[0]
                ));

        for (PropertyDescriptor propertyDescriptor : java.beans.Introspector.getBeanInfo(paramType).getPropertyDescriptors()) {
            String propertyName = propertyDescriptor.getName();
            if (camelCaseMap.containsKey(propertyName)) {
                Object value = camelCaseMap.get(propertyName);
                if (propertyDescriptor.getPropertyType().isAssignableFrom(List.class)) {
                    // List 타입일 경우 제네릭 타입에 맞게 변환
                    ParameterizedType listType = (ParameterizedType) propertyDescriptor.getReadMethod().getGenericReturnType();
                    Class<?> listGenericType = (Class<?>) listType.getActualTypeArguments()[0];

                    if (!(value instanceof List)) {
                        // 만약 넘어온 value가 List가 아닌 단건의 경우 List로 치환
                        value = Collections.singletonList(value);
                    }

                    List<?> convertedList = ((List<?>) value).stream()
                            .map(item -> convertToType(item, listGenericType))
                            .collect(Collectors.toList());

                    propertyDescriptor.getWriteMethod().invoke(dto, convertedList);
                } else if (!propertyDescriptor.getPropertyType().isAssignableFrom(List.class)) {
                    // List 타입이 아닌 단일값의 경우 직접 설정
                    propertyDescriptor.getWriteMethod().invoke(dto, convertToType(value, propertyDescriptor.getPropertyType()));
                }
            }
        }
        // 안정성을 위하여 Reflection 대신 WebDataBinder 사용
        WebDataBinder binder = binderFactory.createBinder(webRequest, dto, paramType.getSimpleName());
        binder.validate(); // 파라미터의 Validation 시행
        return dto;
    }

    // TODO : 현재는 Wrapper Class만 지원..추후 더욱 다양하게..
    private Object convertToType(Object item, Class<?> targetType) {
        if (targetType == String.class)
            return item.toString();
        else if (targetType == Integer.class)
            return Integer.parseInt(item.toString());
        else if (targetType == Long.class)
            return Long.parseLong(item.toString());
        else if (targetType == Double.class)
            return Double.parseDouble(item.toString());

        return item;
    }

    private String toCamelCase(String key) {
        String[] parts = key.split("_");
        return parts[0] + Arrays.stream(parts, 1, parts.length)
                .map(part -> Character.toUpperCase(part.charAt(0)) + part.substring(1).toLowerCase())
                .collect(Collectors.joining());
    }
}
