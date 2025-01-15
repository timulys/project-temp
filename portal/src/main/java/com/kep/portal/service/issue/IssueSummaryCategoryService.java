package com.kep.portal.service.issue;

import com.kep.core.model.exception.BizException;
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
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.HashMap;
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
        Map<Long, List<IssueSummaryCategoryDto>> groupByParentIdMap = issueSummaryCategoriesWithParent.stream()
                .map(IssueSummaryCategoryDto::from)
                .collect(Collectors.groupingBy(item -> item.getParentId() == null ? -1L : item.getParentId(), Collectors.toList())); // -1L -> top

        List<IssueSummaryCategoryDto> topCategoryDtos = groupByParentIdMap.get(-1L); // -1L -> top. 최상위에는 부모ID가 없음
        recurProcessTree(topCategoryDtos, groupByParentIdMap);

        return topCategoryDtos;
    }

    /**
     * 하위 노드 처리 재귀
     * @param dtos
     * @param groupByParentIdMap
     */
    private void recurProcessTree(List<IssueSummaryCategoryDto> dtos, Map<Long, List<IssueSummaryCategoryDto>> groupByParentIdMap) {
        for (IssueSummaryCategoryDto dto : dtos) {
            dto.setChildren(groupByParentIdMap.get(dto.getIssueSummaryCategoryId()));
            if (dto.getChildren() != null) recurProcessTree(dto.getChildren(), groupByParentIdMap);
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
        IssueSummaryCategory entity = issueSummaryCategoryRepository.findById(requestDto.getIssueSummaryCategoryId())
                .orElseThrow(() -> new IllegalArgumentException("not found issueSummaryCategory"));
        entity.modify(requestDto.getName(), requestDto.getSort(), requestDto.getEnabled(), securityUtils.getMemberId());
    }

    @Transactional
    public void delete(Long issueSummaryCategoryId) {
        IssueSummaryCategory entity = issueSummaryCategoryRepository.findById(issueSummaryCategoryId)
                .orElseThrow(() -> new IllegalArgumentException("not found issueSummaryCategory"));
        issueSummaryCategoryRepository.delete(entity);
    }

    public IssueSummaryCategoryResponse getOne(Long issueSummaryCategoryId) {
        List<IssueSummaryCategory> categories = issueSummaryCategoryRepository.findByIdWithParent(issueSummaryCategoryId);

        if (categories == null || categories.isEmpty()) throw new IllegalArgumentException("not found issueSummaryCategory");
        List<IssueSummaryCategoryDto> categoryTree = processTree(categories);

        return new IssueSummaryCategoryResponse(categoryTree);
    }

    @Transactional
    public void saveExcel(MultipartFile file) {
        // TODO :: file size check?
//        if (file.getSize() > 2020202020) throw new BizException("");
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(file.getInputStream()));) {

            List<IssueSummaryCategory> records = issueSummaryCategoryRepository.findAll();

            Integer initSortVal = 0;
            Boolean initEnabled = Boolean.TRUE;
            Long memberId = securityUtils.getMemberId();

            Map<String, IssueSummaryCategory> recordMap = records.stream().collect(Collectors.toMap(IssueSummaryCategory::getName, item -> item));
            Map<String, Integer> childrenSizeMap = new HashMap<>(); // 부모 카테고리명 : 자식 노드 사이즈

            for (IssueSummaryCategory record : records) {
                if (record.getDepth() < 3 && childrenSizeMap.putIfAbsent(record.getName(), initSortVal) != null) {
                    childrenSizeMap.compute(record.getName(), (k, v) -> v + 1);
                }
            }

            String line = null;

            while ((line = reader.readLine()) != null) {
                String[] sep = line.split(",");

                IssueSummaryCategory record = null;
                String name = null;
                String parentName = null;

                for (int i = 0; i < sep.length; i++) {
                    name = sep[i];
                    record = recordMap.get(name);
                    parentName = i == 0 ? null : sep[i - 1];

                    if (record == null) {

                        record = issueSummaryCategoryRepository.save(
                                IssueSummaryCategory.create(
                                        null,
                                        recordMap.getOrDefault(parentName, null), // 최상위 카테고리는 부모 없음
                                        name,
                                        childrenSizeMap.getOrDefault(parentName, initSortVal) + 1,
                                        i + 1,
                                        initEnabled, // 엑셀 업로드시 enable true 고정
                                        memberId
                                )
                        );

                        recordMap.put(name, record);
                        if (i == 0) { // depth 1
                            childrenSizeMap.put(name, initSortVal);
                        } else {
                            childrenSizeMap.compute(parentName, (k, v) -> v + 1);
                        }
                    }
                }
            }

        } catch (Exception e) {
            log.error(e.getLocalizedMessage());
            throw new BizException(e.getMessage());
        }
    }

}
