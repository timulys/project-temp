package com.kep.portal.service.issue;

import com.kep.portal.model.dto.issue.IssueSummaryCategoryDto;
import com.kep.portal.model.dto.issue.IssueSummaryCategoryResponse;
import com.kep.portal.model.dto.issue.SaveIssueSummaryCategoryRequest;
import com.kep.portal.model.entity.issue.IssueSummaryCategory;
import com.kep.portal.repository.issue.IssueSummaryCategoryRepository;
import com.kep.portal.util.SecurityUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


/**
 * 상담 요약(후처리) 서비스
 *
 * @author volka
 */
@Slf4j
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class IssueSummaryCategoryService {

    private final IssueSummaryCategoryRepository issueSummaryCategoryRepository;
    private final SecurityUtils securityUtils;


    public IssueSummaryCategoryResponse getAll() {
        List<IssueSummaryCategory> issueSummaryCategories = issueSummaryCategoryRepository.findAll();
        return new IssueSummaryCategoryResponse(processTree(issueSummaryCategories));
    }

    /**
     * 트리 생성
     * @param issueSummaryCategoriesWithParent :: parent fetch join. parent 없음 안 됨. volka
     * @return
     */
    private List<IssueSummaryCategoryDto> processTree(List<IssueSummaryCategory> issueSummaryCategoriesWithParent) {
        Map<Long, List<IssueSummaryCategoryDto>> childrenMap = issueSummaryCategoriesWithParent.stream()
                .map(IssueSummaryCategoryDto::from)
                .collect(Collectors.groupingBy(item -> {
                    return item.getParentId() == null ? -1L : item.getParentId();
                }, Collectors.toList()));

        List<IssueSummaryCategoryDto> topCategoryDtos = childrenMap.get(-1L);
        recurProcessTree(topCategoryDtos, childrenMap);

        return topCategoryDtos;
    }

    /**
     * 하위 노드 처리 재귀
     * @param dtos
     * @param childrenMap
     */
    private void recurProcessTree(List<IssueSummaryCategoryDto> dtos, Map<Long, List<IssueSummaryCategoryDto>> childrenMap) {
        for (IssueSummaryCategoryDto dto : dtos) {
            dto.setChildren(childrenMap.get(dto.getIssueSummaryCategoryId()));
            if (dto.getChildren() != null) recurProcessTree(dto.getChildren(), childrenMap);
        }
    }


    @Transactional
    public void save(SaveIssueSummaryCategoryRequest requestDto) {
        if (requestDto.getIssueSummaryCategoryId() == null) {
            add(requestDto);
        } else {
            modify(requestDto);
        }
    }

    private void add(SaveIssueSummaryCategoryRequest requestDto) {
        IssueSummaryCategory parent = requestDto.getParentId() == null ? null : issueSummaryCategoryRepository.findById(requestDto.getParentId())
                .orElseThrow(() -> new IllegalArgumentException("not found parent category"));

        IssueSummaryCategory entity = requestDto.toEntity(securityUtils.getMemberId(), parent);
        issueSummaryCategoryRepository.save(entity);
    }

    private void modify(SaveIssueSummaryCategoryRequest requestDto) {
        IssueSummaryCategory entity = issueSummaryCategoryRepository.findById(requestDto.getIssueSummaryCategoryId()).orElseThrow(() -> new IllegalArgumentException("not found issueSummaryCategory"));
        entity.modify(requestDto.getName(), requestDto.getSort(), requestDto.getEnabled(), securityUtils.getMemberId());
    }

    @Transactional
    public void delete(Long issueSummaryCategoryId) {
        IssueSummaryCategory entity = issueSummaryCategoryRepository.findById(issueSummaryCategoryId).orElseThrow(() -> new IllegalArgumentException("not found issueSummaryCategory"));
        issueSummaryCategoryRepository.delete(entity);
    }

    public IssueSummaryCategoryResponse getOne(Long issueSummaryCategoryId) {
        List<IssueSummaryCategory> categories = issueSummaryCategoryRepository.findByIdWithParent(issueSummaryCategoryId);

        if (categories == null || categories.isEmpty()) throw new IllegalArgumentException("not found issueSummaryCategory");
        List<IssueSummaryCategoryDto> categoryTree = processTree(categories);

        return new IssueSummaryCategoryResponse(categoryTree);
    }
}
