package com.kep.portal.service.branch.impl;

import com.kep.core.model.dto.branch.BranchDto;
import com.kep.core.model.dto.work.WorkType;
import com.kep.portal.model.dto.branch.request.PatchBranchRequestDto;
import com.kep.portal.model.dto.branch.request.PostBranchRequestDto;
import com.kep.portal.model.dto.branch.response.PatchBranchResponseDto;
import com.kep.portal.model.entity.branch.Branch;
import com.kep.portal.model.entity.branch.BranchMapper;
import com.kep.portal.repository.branch.BranchRepository;
import com.kep.portal.service.branch.BranchManageService;
import com.kep.portal.util.MessageSourceUtil;
import com.kep.portal.util.SecurityUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.ZonedDateTime;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class BranchManageServiceImpl implements BranchManageService {
    // Autowired Components
    private final BranchRepository branchRepository;

    private final BranchMapper branchMapper;
    private final SecurityUtils securityUtils;
    private final MessageSourceUtil messageUtil;

    @Override
    public ResponseEntity<? super PatchBranchResponseDto> patchBranch(PatchBranchRequestDto dto) {
        boolean existedByBranchId = branchRepository.existsById(dto.getId());
        if (!existedByBranchId) return null;

        Branch branch = branchRepository.findById(dto.getId()).get();
        branch.setName(dto.getName());
        branch.setAssign(WorkType.Cases.valueOf(dto.getAssign()));
        branch.setEnabled(dto.getEnabled());
        branch.setOffDutyHours(dto.getOffDutyHours());
        if (dto.getMaxCounselType() != null)
            branch.setMaxCounselType(WorkType.MaxCounselType.valueOf(dto.getMaxCounselType()));
        branch.setMaxCounsel(dto.getMaxCounsel());
        branch.setModifier(securityUtils.getMemberId());
        branch.setModified(ZonedDateTime.now());

        BranchDto branchDto = branchMapper.map(branch);

        return PatchBranchResponseDto.success(branchDto, messageUtil.success());
    }

    /**
     * 브랜치 등록
     */
    @Override
    public BranchDto saveBranch(BranchDto dto, Branch headQuarters) {
        Branch branch = branchMapper.map(dto);
        branch.setCreator(securityUtils.getMemberId());
        branch.setCreated(ZonedDateTime.now());
        branch.setMaxCounsel(0); // 최대 상담 건수 0건으로 수정 관련 jira : KICA-511
        branch.setMaxCounselType(headQuarters.getMaxCounselType());
        branch.setMaxMemberCounsel(headQuarters.getMaxMemberCounsel());
        branch.setAssign(headQuarters.getAssign());

        Branch saveBranch = branchRepository.save(branch);
        return branchMapper.map(saveBranch);
    }
}
