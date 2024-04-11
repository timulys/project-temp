 /**
  * IssueMemo Repository 
  * 
  *  @생성일자      / 만든사람		/ 수정내용
  *  2023.04.04 / philip.lee7   / 신규
  */

package com.kep.portal.repository.issue;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.kep.portal.model.entity.issue.IssueMemo;

public interface IssueMemoRepository extends JpaRepository<IssueMemo, Long> {

	List<IssueMemo> findAllByIssueIdAndDeletedOrderByCreatedAsc(Long issueId,boolean deleted);
}
