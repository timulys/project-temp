package com.kep.portal.repository.platform;


import com.kep.portal.model.entity.platform.PlatformSubscribe;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Set;

@Repository
public interface PlatformSubscribeRepository extends JpaRepository<PlatformSubscribe, Long> {
    List<PlatformSubscribe> findAllByPlatformUserIdIn(Set<String> platformUserId);

}
