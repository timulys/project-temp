package com.kep.portal.service.issue.event;

import com.kep.portal.model.dto.issue.response.PostCustomerSyncResponseDto;
import org.springframework.http.ResponseEntity;

public interface OperatorEventService {
    ResponseEntity<? super PostCustomerSyncResponseDto> customerSyncRequest(Long issueId);
}
