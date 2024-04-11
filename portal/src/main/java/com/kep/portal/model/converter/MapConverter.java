package com.kep.portal.model.converter;

import javax.persistence.Converter;
import java.util.Map;

/**
 * Map -> JSON Converter
 */
@Converter
public class MapConverter extends EntityConverter<Map<String, Object>> {
}
