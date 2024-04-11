package com.kep.platform.repository;

import com.kep.platform.model.entity.PlatformSession;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PlatformSessionRepository extends CrudRepository<PlatformSession, String> {
}
