package com.kep.portal.service.platform;

import com.kep.core.model.dto.platform.PlatformSubscribeDto;
import com.kep.portal.model.entity.customer.Customer;
import com.kep.portal.model.entity.platform.PlatformSubscribe;
import com.kep.portal.model.entity.platform.PlatformSubscribeMapper;
import com.kep.portal.repository.platform.PlatformSubscribeRepository;
import com.kep.portal.service.customer.CustomerServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Example;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import javax.validation.constraints.NotNull;
import java.util.Collections;
import java.util.List;
import java.util.Set;

/**
 * 플랫폼 (카카오) 구독
 */
@Service
@Transactional
@Slf4j
public class PlatformSubscribeService {

    @Resource
    private PlatformSubscribeRepository platformSubscribeRepository;

    @Resource
    private PlatformSubscribeMapper platformSubscribeMapper;

    @Nullable
    public PlatformSubscribe findOne(@NotNull Example<PlatformSubscribe> example){
        return platformSubscribeRepository.findOne(example).orElse(null);
    }

    /**
     * 저장
     * @param dto
     */
    public void store(PlatformSubscribeDto dto){
        PlatformSubscribe subscribe = this.findOne(Example.of(PlatformSubscribe.builder()
                        .serviceId(dto.getServiceId())
                        .platformUserId(dto.getPlatformUserId())
                .build()));

        if(subscribe == null){
            subscribe = platformSubscribeMapper.map(dto);
        }
        subscribe.setModified(dto.getModified());
        subscribe.setEnabled(dto.getEnabled());
        platformSubscribeRepository.save(subscribe);
    }

    /**
     * 플랫폼 구독
     * @param platformUserIds
     * @return
     */
    public List<PlatformSubscribe> getAll(Set<String> platformUserIds){
        if(!platformUserIds.isEmpty()){
            return platformSubscribeRepository.findAllByPlatformUserIdIn(platformUserIds);
        }
        return Collections.emptyList();
    }
}
