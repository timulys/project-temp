package com.kep.portal.model.converter;

import javax.persistence.Converter;
import java.util.List;

/**
 * Map -> JSON Converter
 */
@Converter
public class ListOfLongConverter extends EntityConverter<List<Long>> {
}
