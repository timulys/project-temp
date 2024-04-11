package com.kep.portal.config.client;

import com.kep.portal.config.property.CoreProperty;
import org.springframework.cloud.client.DefaultServiceInstance;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.loadbalancer.core.ServiceInstanceListSupplier;
import reactor.core.publisher.Flux;

import javax.validation.constraints.NotEmpty;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 로드 밸런서에서 사용할 서비스 정의 (서비스 이름, 서버 목록 등)
 *
 * spring.cloud 에서 설정
 */
@Deprecated
public class CustomServiceInstanceListSupplier implements ServiceInstanceListSupplier {

	private static final String SERVICE_ID_DELIM = "-";

	private final String serviceId;
	private final List<CoreProperty.Server> servers;

	public CustomServiceInstanceListSupplier(@NotEmpty String serviceId, @NotEmpty List<CoreProperty.Server> servers) {

		this.serviceId = serviceId;
		this.servers = servers;
	}

	@Override
	public String getServiceId() {
		return serviceId;
	}

	@Override
	public Flux<List<ServiceInstance>> get() {

		List<ServiceInstance> instances = servers.stream().map(
				server -> new DefaultServiceInstance(
				serviceId + SERVICE_ID_DELIM + server.getId(), serviceId
				, server.getHost(), server.getPort(), false)).collect(Collectors.toList());
		return Flux.just(instances);
	}
}
