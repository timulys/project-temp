package com.kep.portal.util;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;
import org.springframework.util.ObjectUtils;

import java.beans.PropertyDescriptor;
import java.util.HashSet;
import java.util.Set;

public class CommonUtils {

	/**
	 * Not Empty (!ObjectUtils.isEmpty) 필드만 복사
	 */
	public static void copyNotEmptyProperties(Object source, Object target) {

		BeanUtils.copyProperties(source, target, getEmptyPropertyNames(source));
	}

	public static String[] getEmptyPropertyNames(Object source) {

		final BeanWrapper beanWrapper = new BeanWrapperImpl(source);
		PropertyDescriptor[] propertyDescriptors = beanWrapper.getPropertyDescriptors();

		Set<String> emptyPropertyNames = new HashSet<>();
		for (PropertyDescriptor propertyDescriptor : propertyDescriptors) {
			Object propertyValue = beanWrapper.getPropertyValue(propertyDescriptor.getName());
			if (ObjectUtils.isEmpty(propertyValue)) {
				emptyPropertyNames.add(propertyDescriptor.getName());
			}
		}

		return emptyPropertyNames.toArray(new String[0]);
	}
}
