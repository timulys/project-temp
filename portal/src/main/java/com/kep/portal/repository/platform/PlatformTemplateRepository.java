package com.kep.portal.repository.platform;

import com.kep.core.model.dto.platform.PlatformTemplateStatus;
import com.kep.portal.model.entity.platform.PlatformTemplate;
import feign.Param;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Set;


@Repository
public interface PlatformTemplateRepository extends JpaRepository<PlatformTemplate, Long>, PlatformTemplateSearchRepository {
    Page<PlatformTemplate> findAll(Example example, Pageable pageable);

    PlatformTemplate findByCode(String templateCode);

    List<PlatformTemplate> findAllByIdIn(Set<Long> ids);

    List<PlatformTemplate> findAllByStatus(PlatformTemplateStatus status);

    @Query(value = "SELECT TEMPLATE_SEQUENCE.NEXTVAL FROM DUAL", nativeQuery = true)
    Long findNextOfTemplateSequence();

    @Query(value = "CALL NEXT VALUE FOR TEMPLATE_SEQUENCE", nativeQuery = true)
    Long findNextOfTemplateSequenceOnH2();

    @Query(value = "SELECT NEXT_VAL FROM TEMPLATE_SEQUENCE FOR UPDATE", nativeQuery = true)
    Long findNextOfTemplateSequenceTable();

    @Query(value = "UPDATE TEMPLATE_SEQUENCE SET NEXT_VAL = NEXT_VAL + 1 WHERE NEXT_VAL = :currentVal", nativeQuery = true)
    @Modifying
    void updateTemplateSequenceTable(@Param("currentVal") Long currentVal);
}
