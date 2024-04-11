package com.kep.portal.repository.issue;

import com.kep.portal.model.entity.assign.AssignMethod;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AssignMethodRepository extends JpaRepository<AssignMethod, Long> {
}
