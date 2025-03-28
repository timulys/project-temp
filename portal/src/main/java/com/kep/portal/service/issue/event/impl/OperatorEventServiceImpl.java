package com.kep.portal.service.issue.event.impl;

import com.kep.core.model.dto.ResponseDto;
import com.kep.core.model.enums.MessageCode;
import com.kep.portal.model.dto.issue.response.PostCustomerSyncResponseDto;
import com.kep.portal.model.entity.env.CounselInflowEnv;
import com.kep.portal.model.entity.issue.Issue;
import com.kep.portal.repository.env.CounselInflowEnvRepository;
import com.kep.portal.repository.issue.IssueRepository;
import com.kep.portal.service.issue.event.EventBySystemService;
import com.kep.portal.service.issue.event.OperatorEventService;
import com.kep.portal.util.MessageSourceUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class OperatorEventServiceImpl implements OperatorEventService {
    /** Autowired Components **/
    private final MessageSourceUtil messageUtil;
    private final IssueRepository issueRepository;
    private final EventBySystemService eventBySystemService;
    private final CounselInflowEnvRepository counselInflowEnvRepository;

    /**
     * 고객 인증 요청(Kakao-Sync)
     * @param issueId
     * @return
     */
    @Override
    public ResponseEntity<? super PostCustomerSyncResponseDto> customerSyncRequest(Long issueId) {
        boolean existedByIssueId = issueRepository.existsById(issueId);
        if (!existedByIssueId) return ResponseDto.databaseErrorMessage(messageUtil.getMessage(MessageCode.NOT_EXISTED_DATA));

        Issue issue = issueRepository.findById(issueId).get();
        if (issue.getCustomerId() != null) return PostCustomerSyncResponseDto.existedSync(messageUtil.getMessage(MessageCode.NOT_EXISTED_CUSTOMER));

        // 가장 최초에 선언된 channel inflow를 선택(기본)
        List<CounselInflowEnv> counselInflowEnvList = counselInflowEnvRepository.findAll();
        if (counselInflowEnvList.size() < 1) {
            return ResponseDto.databaseErrorMessage(messageUtil.getMessage(MessageCode.NOT_EXISTED_DATA));
        }
        eventBySystemService.sendSync(issue, counselInflowEnvList.get(0));
        return PostCustomerSyncResponseDto.success(messageUtil.success());
    }
}
