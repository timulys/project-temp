package com.dkt.always.talk.repository.template;

import com.dkt.always.talk.entity.template.PlatformTemplate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TemplateSearchRepository extends JpaRepository<PlatformTemplate, Long>, PlatformTemplateSearchCustomRepository {
}
