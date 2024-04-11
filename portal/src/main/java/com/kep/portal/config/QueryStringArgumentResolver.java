package com.kep.portal.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kep.core.model.type.QueryParam;
import com.kep.portal.model.dto.issue.IssueSearchCondition;
import com.kep.portal.model.dto.member.MemberSearchCondition;
import com.kep.portal.model.dto.platform.BizTalkHistoryCondition;
import com.kep.portal.model.dto.platform.BizTalkRequestCondition;
import com.kep.portal.model.dto.platform.BizTalkTaskCondition;
import com.kep.portal.model.entity.platform.BizTalkTask;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.MethodParameter;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

/**
 * 쿼리 파라미터를 객체로 매핑시 설정
 */
@Component
@Slf4j
public class QueryStringArgumentResolver implements HandlerMethodArgumentResolver {

    @Resource
    private ObjectMapper objectMapper;

    @Override
    public boolean supportsParameter(final MethodParameter parameter) {

        return parameter.getParameterAnnotation(QueryParam.class) != null;
    }

    @Override
    public Object resolveArgument(@NonNull MethodParameter parameter, ModelAndViewContainer mavContainer,
                                  NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {

        HttpServletRequest request = (HttpServletRequest) webRequest.getNativeRequest();

        Map<String, Object> queryParam = new HashMap<>();
        for (String param : request.getParameterMap().keySet()) {
            if (request.getParameterValues(param).length == 1) {
                queryParam.put(param, request.getParameterValues(param)[0]);
                // 타입별 예외
                if (parameter.getParameterType().equals(IssueSearchCondition.class)) {
                    if ("status".equals(param)
                            || "member_id".equals(param)) {
                        // 항상 리스트 타입으로
                        queryParam.put(param, request.getParameterValues(param));
                    }
                } else if (parameter.getParameterType().equals(MemberSearchCondition.class)) {
                    if ("level_type".equals(param)) {
                        // 항상 리스트 타입으로
                        queryParam.put(param, request.getParameterValues(param));
                    }
                } else if (parameter.getParameterType().equals(BizTalkRequestCondition.class)) {
                    if ("status".equals(param)) {
                        queryParam.put(param, request.getParameterValues(param));
                    }
                } else if (parameter.getParameterType().equals(BizTalkTaskCondition.class)) {
                    if ("status".equals(param)) {
                        queryParam.put(param, request.getParameterValues(param));
                    }
                } else if (parameter.getParameterType().equals(BizTalkHistoryCondition.class)) {
                    if ("status".equals(param)) {
                        queryParam.put(param, request.getParameterValues(param));
                    }
                }
            } else {
                queryParam.put(param, request.getParameterValues(param));
            }
        }

        String json = objectMapper.writeValueAsString(queryParam);
        log.debug("QUERY PARAMS: {}", json);

        return objectMapper.readValue(json, parameter.getParameterType());
    }
}
