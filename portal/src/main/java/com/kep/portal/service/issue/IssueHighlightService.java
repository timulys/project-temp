package com.kep.portal.service.issue;

import com.kep.core.model.dto.issue.IssueHighlightDto;
import com.kep.portal.model.entity.issue.IssueHighlight;
import com.kep.portal.model.entity.issue.IssueHighlightMapper;
import com.kep.portal.repository.issue.IssueHighlightRepository;
import com.kep.portal.util.SecurityUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.transaction.Transactional;
import javax.validation.constraints.NotNull;
import java.time.ZonedDateTime;
import java.util.List;

@Service
@Transactional
@Slf4j
public class IssueHighlightService {

    @Resource
    private IssueHighlightRepository issueHighlightRepository;

    @Resource
    private SecurityUtils securityUtils;

    @Resource
    private IssueHighlightMapper issueHighlightMapper;

    /**
     * Highlight save
     * @param dto
     * @return
     */
    public IssueHighlightDto store(IssueHighlightDto dto){

        IssueHighlight entity = IssueHighlight.builder()
                .keyword(dto.getKeyword())
                .creator(securityUtils.getMemberId())
                .created(ZonedDateTime.now())
                .modifier(securityUtils.getMemberId())
                .modified(ZonedDateTime.now())
                .build();

        issueHighlightRepository.save(entity);
        return issueHighlightMapper.map(entity);
    }

    /**
     *  Highlight list
     * @return
     */
    public List<IssueHighlightDto> index(){
        List<IssueHighlight> entities = issueHighlightRepository.findAll(Sort.by(Sort.Direction.DESC, "id"));
        return issueHighlightMapper.map(entities);
    }

    public IssueHighlightDto show(@NotNull Long id){
        IssueHighlight entity = issueHighlightRepository.findById(id).orElse(null);
        return issueHighlightMapper.map(entity);
    }

    public boolean delete(@NotNull Long id){
        IssueHighlight entity = issueHighlightRepository.findById(id).orElse(null);
        if(entity != null){
            issueHighlightRepository.delete(entity);
            return true;
        }
        return false;
    }

    public IssueHighlightDto update(@NotNull Long id , IssueHighlightDto dto){
        IssueHighlight entity = issueHighlightRepository.findById(id).orElse(null);
        if(entity != null){
            entity.setKeyword(dto.getKeyword());
            entity.setModifier(securityUtils.getMemberId());
            entity.setModified(ZonedDateTime.now());
            issueHighlightRepository.save(entity);
            return issueHighlightMapper.map(entity);
        }
        return null;
    }
}
