package com.kep.portal.repository.team;

import com.kep.portal.model.entity.branch.Branch;
import com.kep.portal.model.entity.issue.IssueExtra;
import com.kep.portal.model.entity.team.Team;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import javax.validation.constraints.NotNull;
import java.util.Collection;
import java.util.List;

@Repository
public interface TeamRepository extends JpaRepository<Team, Long> {

    Page<Team> findAllByIdIn(Collection<Long> ids, Pageable pageable);
}
