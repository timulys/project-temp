package com.kep.portal.repository.env;

import com.kep.core.model.dto.system.SystemEnvEnum;
import com.kep.portal.model.entity.env.CounselInflowEnv;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CounselInflowEnvRepository extends JpaRepository<CounselInflowEnv, Long> {

    CounselInflowEnv findByBranchIdAndParams(Long branchId , String params);

    List<CounselInflowEnv> findAllByBranchIdAndEnabled(Long branchId, boolean enabled);

    // jhchoi: 상담 유입경로 설정 > 사용대상이 '제한없음'인 경로 값만 조회
    List<CounselInflowEnv> findAllByBranchIdAndEnabledAndInflowPathType(Long branchId, boolean enabled, SystemEnvEnum.InflowPathType inflowPathType);
}
