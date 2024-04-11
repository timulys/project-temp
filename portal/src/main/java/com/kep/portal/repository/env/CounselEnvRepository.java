package com.kep.portal.repository.env;

import com.kep.portal.model.entity.env.CounselEnv;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CounselEnvRepository extends JpaRepository<CounselEnv, Long> {

    CounselEnv findByBranchId(Long branchId);
}
