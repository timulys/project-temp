package com.kep.portal.repository.upload;

import com.kep.portal.model.dto.upload.UploadHistorySearchCondition;
import com.kep.portal.model.entity.upload.UploadHistory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UploadHistoryRepository extends JpaRepository<UploadHistory, Long>, UploadHistorySearchRepository {
}
