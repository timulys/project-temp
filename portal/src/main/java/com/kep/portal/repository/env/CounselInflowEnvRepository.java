package com.kep.portal.repository.env;

import com.kep.portal.model.entity.env.CounselInflowEnv;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CounselInflowEnvRepository extends JpaRepository<CounselInflowEnv, Long> {

    CounselInflowEnv findByBranchIdAndParams(Long branchId , String params);

    List<CounselInflowEnv> findAllByBranchIdAndEnabled(Long branchId, boolean enabled);
}
