package com.kep.portal.repository.platform;

import com.kep.portal.model.entity.platform.BizTalkRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface BizTalkRequestRepository extends JpaRepository<BizTalkRequest, Long>, BizTalkRequestSearchRepository {

}
