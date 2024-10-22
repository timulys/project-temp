package com.kep.portal.repository.forbidden;

import org.springframework.data.jpa.repository.JpaRepository;

import com.kep.portal.model.entity.forbidden.Forbidden;

import java.util.Optional;

public interface ForbiddenRepository extends JpaRepository<Forbidden, Long>{

    Optional<Forbidden> findByWord(String word);
}
