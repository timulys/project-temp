package com.kep.portal.service.branch.aggregation;

import com.kep.core.model.dto.ResponseDto;
import com.kep.core.model.dto.branch.BranchDto;
import com.kep.core.model.dto.env.CounselEnvDto;
import com.kep.core.model.dto.privilege.RoleDto;
import com.kep.core.model.dto.system.SystemEnvDto;
import com.kep.core.model.dto.system.SystemEnvEnum;
import com.kep.core.model.dto.work.OfficeHoursDto;
import com.kep.core.model.dto.work.OfficeWorkDto;
import com.kep.core.model.dto.work.WorkType;
import com.kep.core.model.enums.MessageCode;
import com.kep.portal.model.dto.branch.request.PatchBranchRequestDto;
import com.kep.portal.model.dto.branch.request.PostBranchRequestDto;
import com.kep.portal.model.dto.branch.response.PatchBranchResponseDto;
import com.kep.portal.model.dto.branch.response.PostBranchResponseDto;
import com.kep.portal.model.entity.branch.Branch;
import com.kep.portal.model.entity.privilege.Role;
import com.kep.portal.service.branch.BranchManageService;
import com.kep.portal.service.branch.BranchService;
import com.kep.portal.service.env.CounselEnvService;
import com.kep.portal.service.privilege.RoleService;
import com.kep.portal.service.work.OfficeHoursService;
import com.kep.portal.util.MessageSourceUtil;
import com.kep.portal.util.SecurityUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class BranchServiceAggregation {
    // Autowired Components
    private final RoleService roleService;
    private final BranchService branchService;
    private final CounselEnvService counselEnvService;
    private final OfficeHoursService officeHoursService;
    private final BranchManageService branchManageService;

    private final SecurityUtils securityUtils;
    private final MessageSourceUtil messageUtil;

    /**
     * Branch 신규 등록
     */
    public ResponseEntity<? super PostBranchResponseDto> postBranch(PostBranchRequestDto dto) {
        for (BranchDto newBranchDto : dto.getBranchList()) {
            Branch headQuarters = branchService.findHeadQuarters();
            BranchDto branchDto = branchManageService.saveBranch(newBranchDto, headQuarters);
            if (branchDto == null) return ResponseDto.noSearchData(messageUtil.getMessage(MessageCode.NO_SEARCH_DATA));

            List<Role> basicRoleList = roleService.basic(branchDto.getId());
            if (basicRoleList.isEmpty()) return ResponseDto.noSearchData(messageUtil.getMessage(MessageCode.NO_SEARCH_DATA));

            List<RoleDto> roleList = basicRoleList.stream().map(role -> RoleDto.builder()
                    .id(role.getId())
                    .type(role.getType())
                    .name(role.getName())
                    .levelId(role.getLevel().getId())
                    .enabled(role.getEnabled())
                    .build()
            ).collect(Collectors.toList());
            branchDto.setRoles(roleList);

            // TODO : 추후 CounselService의 Store도 Save/Modify로 분리할 것
            counselEnvService.store(CounselEnvDto.builder()
                    .branchId(branchDto.getId())
                    .requestBlockEnabled(false)
                    .memberAutoTransformEnabled(true)
                    .issueAutoCloseEnabled(true)
                    .issueDelay(SystemEnvDto.EnabledMinute.builder()
                            .enabled(true)
                            .minute(60)
                            .build())
                    .issueFileMimeType(SystemEnvDto.EnableFileMimeType.builder()
                            .enabled(true)
                            .fileMimeType(SystemEnvEnum.FileMimeType.all)
                            .build())
                    .build());

            // 신규 브랜치 생성 시 HeadQuarter 설정 복사
            OfficeHoursDto officeHoursDto = officeHoursService.branch(headQuarters.getId());
            OfficeWorkDto officeWorkDto = OfficeWorkDto.builder()
                    .branchId(branchDto.getId())
                    .memberId(securityUtils.getMemberId())
                    .cases(WorkType.Cases.branch)
                    .officeHours(officeHoursDto)
                    .build();
            officeHoursService.branch(officeWorkDto, branchDto.getId());
        }

        return PostBranchResponseDto.success(messageUtil.success());
    }
}
