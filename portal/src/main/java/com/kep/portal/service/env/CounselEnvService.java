package com.kep.portal.service.env;

import com.kep.core.model.dto.env.CounselEnvDto;
import com.kep.core.model.dto.env.CounselInflowEnvDto;
import com.kep.core.model.dto.system.SystemEnvEnum;
import com.kep.core.model.dto.system.SystemEnvEnum.FileMimeType;
import com.kep.core.model.exception.BizException;
import com.kep.portal.config.property.CoreProperty;
import com.kep.portal.config.property.PortalProperty;
import com.kep.portal.model.entity.env.CounselEnv;
import com.kep.portal.model.entity.env.CounselEnvMapper;
import com.kep.portal.model.entity.env.CounselInflowEnv;
import com.kep.portal.model.entity.env.CounselInflowEnvMapper;
import com.kep.portal.model.entity.system.SystemEnv;
import com.kep.portal.repository.env.CounselEnvRepository;
import com.kep.portal.repository.env.CounselInflowEnvRepository;
import com.kep.portal.util.SecurityUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.transaction.Transactional;
import javax.validation.constraints.NotNull;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
@Slf4j
public class CounselEnvService {

    @Resource
    private CounselEnvRepository counselEnvRepository;
    @Resource
    private CounselInflowEnvRepository counselInflowEnvRepository;
    @Resource
    private CounselEnvMapper counselEnvMapper;
    @Resource
    private CounselInflowEnvMapper counselInflowEnvMapper;
	@Resource
	private CoreProperty coreProperty;

    @Resource
    private SecurityUtils securityUtils;
    @Resource
    private PortalProperty portalProperty;

    @Value("${application.portal.bnk-kakao-talk-url}")
    private String bnkKaKaoTalkUrl;

    public CounselEnvDto get(@NotNull Long branchId) {
        CounselEnv counselEnv = counselEnvRepository.findByBranchId(branchId);
        return counselEnvMapper.map(counselEnv);
    }

    /**
     * 상담 환경 설정 생성 / 수정
     * @param dto
     * @return
     * @throws Exception
     */
    public CounselEnvDto store(@NotNull CounselEnvDto dto) {

        log.info("COUNSEL ENV DTO :{}" , dto);
        CounselEnv counselEnv = counselEnvRepository.findByBranchId(dto.getBranchId());
        if(counselEnv == null){
            counselEnv = counselEnvMapper.map(dto);
        } else {
            counselEnv.setRequestBlockEnabled(dto.getRequestBlockEnabled());
            counselEnv.setMemberAutoTransformEnabled(dto.getMemberAutoTransformEnabled());
            counselEnv.setIssueAutoCloseEnabled(dto.getIssueAutoCloseEnabled());
            counselEnv.setAlertTalkAutoSendEnable(dto.getAlertTalkAutoSendEnable());
            counselEnv.setFriendTalkAutoSendEnable(dto.getFriendTalkAutoSendEnable());
            counselEnv.setIssueDelay(SystemEnv.EnabledMinute.builder()
                            .enabled(dto.getIssueDelay().getEnabled())
                            .minute(dto.getIssueDelay().getMinute())
                    .build());
            counselEnv.setIssueFileMimeType(SystemEnv.EnableFileMimeType.builder()
                    .enabled(true)
                    .fileMimeType(FileMimeType.image)
            .build());
        }

        counselEnv.setModified(ZonedDateTime.now());
        counselEnv.setModifier(securityUtils.getMemberId());
        counselEnv = counselEnvRepository.save(counselEnv);
        return counselEnvMapper.map(counselEnv);
    }

    /**
     * 상담 유입경로 설정
     * @param dto
     * @return
     * @throws Exception
     */
    public CounselInflowEnvDto store(@NotNull CounselInflowEnvDto dto) {
        CounselInflowEnv counselInflowEnv;
        if(dto.getId() == null){
            // 중복 체크
            Optional<CounselInflowEnv> existingInflowEnv = Optional.empty();
            if(existingInflowEnv.isPresent()) {
                throw new BizException("SB-SA-005-001", "이미 존재하는 유입경로입니다.");
            }

            counselInflowEnv = counselInflowEnvMapper.map(dto);

            // 새로운 카카오 싱크 URL을 사용하여 유입경로 설정
            String newPath = bnkKaKaoTalkUrl + "path_" + dto.getParams();
            counselInflowEnv.setValue(newPath);
            log.info("유입경로 복사 URL 카카오싱크 :{}", counselInflowEnv.getValue());
            counselInflowEnv.setEnabled(true);
        } else {
            counselInflowEnv = counselInflowEnvRepository.findById(dto.getId()).orElse(null);
            if(counselInflowEnv == null){
                return null;
            }
            counselInflowEnv.setInflowPathType(dto.getInflowPathType());
        }

        counselInflowEnv.setModified(ZonedDateTime.now());
        counselInflowEnv.setModifier(securityUtils.getMemberId());
        counselInflowEnv.setName(dto.getName());
        counselInflowEnv = counselInflowEnvRepository.save(counselInflowEnv);
        return counselInflowEnvMapper.map(counselInflowEnv);
    }


    /**
     * 상담 유입경로 삭제
     * @param branchId
     * @param id
     * @return
     */
    public boolean remove(@NotNull Long branchId , @NotNull Long id){

        CounselInflowEnv counselInflowEnv = counselInflowEnvRepository.findById(id).orElse(null);
        if(counselInflowEnv != null && counselInflowEnv.getBranchId().equals(branchId)){
            counselInflowEnv.setEnabled(false);
            counselInflowEnvRepository.save(counselInflowEnv);
            return true;
        }
        return false;
    }

    /**
     * 상담 유입경로 목록
     * @param branchId
     * @return
     */
    public List<CounselInflowEnvDto> findAllAndEnabled(Long branchId) {
        List<CounselInflowEnv> counselInflowEnvs = counselInflowEnvRepository.findAllByBranchIdAndEnabled(branchId, true);
        return counselInflowEnvMapper.map(counselInflowEnvs);
    }
}
