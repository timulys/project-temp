package com.kep.portal.model.converter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kep.core.model.dto.issue.payload.IssuePayload;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.ObjectUtils;

import javax.annotation.Resource;
import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

/**
 * IssuePayload <-> String
 */
@Converter
@Slf4j
public class IssuePayloadConverter implements AttributeConverter<IssuePayload, String> {

    @Resource
    private ObjectMapper objectMapper;

    @Override
    public String convertToDatabaseColumn(IssuePayload attribute) {
        try {
            if(!ObjectUtils.isEmpty(attribute)){
                return objectMapper.writeValueAsString(attribute);
            }
            return null;
        } catch (Exception e) {
            log.error("ISSUE PAYLOAD MESSAGE : {}",e.getMessage() , e);
            return "";
        }
    }

    @Override
    public IssuePayload convertToEntityAttribute(String dbData) {
        try {
            if(dbData != null){
                return objectMapper.readValue(dbData,IssuePayload.class);
            }
            return null;
        } catch (Exception e) {
            log.error("ISSUE PAYLOAD MESSAGE : {}",e.getMessage() , e);
            return null;
        }
    }
}
