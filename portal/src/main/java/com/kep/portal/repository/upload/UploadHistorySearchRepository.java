package com.kep.portal.repository.upload;

import com.kep.portal.model.dto.upload.UploadHistorySearchCondition;
import com.kep.portal.model.entity.upload.UploadHistory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface UploadHistorySearchRepository {
    Page<UploadHistory> search(UploadHistorySearchCondition condition, Pageable pageable);

    Page<UploadHistory> findByIssueCategoryId(List<Long> id, Pageable pageable);
}
