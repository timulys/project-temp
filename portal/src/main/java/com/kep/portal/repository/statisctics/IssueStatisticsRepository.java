package com.kep.portal.repository.statisctics;

import com.kep.portal.model.dto.statistics.IssueStatisticsStatus;
import com.kep.portal.model.entity.statistics.IssueStatistics;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface IssueStatisticsRepository extends JpaRepository<IssueStatistics, Long> , IssueStatisticsSearchRepository{
    List<IssueStatistics> findByIssueId(Long id);


    IssueStatistics findByIssueIdAndStatusAndCreated(Long id , IssueStatisticsStatus status , LocalDate localDate);
}
