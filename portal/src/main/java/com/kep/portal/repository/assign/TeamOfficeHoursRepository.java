package com.kep.portal.repository.assign;

import com.kep.portal.model.entity.work.TeamOfficeHours;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
@Deprecated
public interface TeamOfficeHoursRepository extends JpaRepository<TeamOfficeHours, Long> {

    TeamOfficeHours findByTeamId(Long teamId);
}
