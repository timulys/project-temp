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
	 * 객체 복사 (Empty 필드 포함, id 필드는 제외됨), 엔터티 수정시 사용
	 */
	public static void copyProperties(Object source, Object target) {
		// id 필드는 항상 제외
		Set<String> exceptPropertyNames = new HashSet<>();
		exceptPropertyNames.add("id");

		if (source != null) {
			BeanUtils.copyProperties(source, target, exceptPropertyNames.toArray(new String[0]));
		}
	}


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
