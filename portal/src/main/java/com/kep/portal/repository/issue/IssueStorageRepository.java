package com.kep.portal.repository.issue;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.kep.portal.model.entity.issue.IssueStorage;
import com.kep.portal.model.type.IssueStorageType;

@Repository
public interface IssueStorageRepository extends JpaRepository<IssueStorage, Long> {}
