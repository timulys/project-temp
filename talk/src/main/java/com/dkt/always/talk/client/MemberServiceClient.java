package com.dkt.always.talk.client;

import com.kep.core.model.dto.ApiResult;
import com.kep.core.model.dto.member.MemberDto;
import jakarta.validation.constraints.NotNull;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

/**
 * Portal member service openfeign
 */
@FeignClient(name = "member-service", url = "${spring.cloud.discovery.client.simple.instances.portal-service[0].uri}") // FIXME : url은 application.yml로 따로 관리 예정
public interface MemberServiceClient {
    /** Client Methods*/
    @GetMapping("/api/v1/member/{id}")
    ApiResult<MemberDto> getMember(@PathVariable @NotNull Long id);

}
