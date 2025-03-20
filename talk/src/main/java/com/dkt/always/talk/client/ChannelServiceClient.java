package com.dkt.always.talk.client;

import com.kep.core.model.dto.ApiResult;
import com.kep.core.model.dto.channel.ChannelDto;
import jakarta.validation.constraints.NotNull;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

/**
 * Portal channel service openfeign
 */
@FeignClient(name = "channel-service", url = "${spring.cloud.discovery.client.simple.instances.portal-service[0].uri}")
public interface ChannelServiceClient {
    /** Client Methods */
    @GetMapping("/api/v1/channel/{id}")
    ApiResult<ChannelDto> getChannel(@PathVariable @NotNull Long id);
}
