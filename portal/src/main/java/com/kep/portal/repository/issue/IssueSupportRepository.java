package com.kep.portal.repository.issue;

import com.kep.core.model.dto.issue.IssueSupportType;
import com.kep.portal.model.entity.issue.Issue;
import com.kep.portal.model.entity.issue.IssueSupport;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IssueSupportRepository extends JpaRepository<IssueSupport, Long>, IssueSupportSearchRepository {

    List<IssueSupport> findAllByIssueInOrderByIdDesc(List<Issue> issues);

    List<IssueSupport> findAllByIssueInAndType(List<Issue> issues , IssueSupportType issueSupportType);

}
