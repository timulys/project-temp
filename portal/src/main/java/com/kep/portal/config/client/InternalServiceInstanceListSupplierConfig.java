package com.kep.portal.config.client;

import com.kep.portal.config.property.CoreProperty;
import org.springframework.cloud.loadbalancer.core.ServiceInstanceListSupplier;
import org.springframework.context.annotation.Bean;

import javax.annotation.Resource;

/**
 * Platform 모듈, 로드 밸런서 정의 (서비스 이름, 서버 목록 등) 인스턴스 생성
 *
 * spring.cloud 에서 설정
 */
@Deprecated
//@Configuration
public class InternalServiceInstanceListSupplierConfig {

	@Resource
	private CoreProperty coreProperty;

	@Bean
	ServiceInstanceListSupplier internalServiceInstanceListSupplier() {
		return new CustomServiceInstanceListSupplier(coreProperty.getPlatformServiceUri(), coreProperty.getPlatformServers());
	}
}
