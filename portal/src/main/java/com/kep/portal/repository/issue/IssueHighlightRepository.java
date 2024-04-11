package com.kep.portal.repository.issue;

import com.kep.portal.model.entity.issue.IssueHighlight;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IssueHighlightRepository extends JpaRepository<IssueHighlight, Long> {
}
