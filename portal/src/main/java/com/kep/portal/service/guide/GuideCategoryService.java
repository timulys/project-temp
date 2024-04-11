package com.kep.portal.service.guide;

import com.kep.core.model.dto.guide.GuideCategoryDto;
import com.kep.portal.model.dto.guide.GuideCategorySetting;
import com.kep.portal.model.entity.branch.Branch;
import com.kep.portal.model.entity.guide.GuideCategory;
import com.kep.portal.model.entity.guide.GuideCategoryMapper;
import com.kep.portal.model.entity.privilege.Level;
import com.kep.portal.repository.guide.GuideCategoryRepository;
import com.kep.portal.repository.guide.GuideRepository;
import com.kep.portal.service.branch.BranchService;
import com.kep.portal.util.CommonUtils;
import com.kep.portal.util.SecurityUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.util.ObjectUtils;

import javax.annotation.Resource;
import javax.transaction.Transactional;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Transactional
@Slf4j
public class GuideCategoryService {
    @Resource
    private GuideCategoryMapper categoryMapper;

    @Resource
    private GuideCategoryRepository categoryRepository;

    @Resource
    private SecurityUtils securityUtils;

    @Resource
    private BranchService branchService;

    public Integer setCategoryMaxDepth(Integer maxDepth) {
        Branch branch = branchService.findById(securityUtils.getBranchId());

        Assert.isTrue(branch.getHeadQuarters(), "Branch is not headquarters");
        Assert.isTrue(branch.getMaxGuideCategoryDepth().equals(0), "Already set up");

        Branch headQuarters = branchService.findHeadQuarters();
        headQuarters.setMaxGuideCategoryDepth(maxDepth);
        return headQuarters.getMaxGuideCategoryDepth();
    }

    public int getCategoryMaxDepth() {
        return branchService.findHeadQuarters().getMaxGuideCategoryDepth();
    }

    public List<GuideCategoryDto> getAll(Long branchId) {
        if (branchId == null)
            branchId = securityUtils.getBranchId();

        List<GuideCategory> guideCategories = categoryRepository.findByBranchAndDepthCategory(branchId, 1);

        List<GuideCategoryDto> dtos = categoryMapper.map(guideCategories);



        for (GuideCategoryDto dto : dtos) {
            recursiveGetAllAndIsOpenCategory(dto.getChildren(), branchId);
        }

        return dtos;
    }

    public List<GuideCategoryDto> getMyBranchAll() {
        if(securityUtils.isHeadQuarters() && securityUtils.hasRole(Level.ROLE_ADMIN)){
            List<GuideCategory> guideCategories = categoryRepository.findAllByDepthCategory(1);
            return categoryMapper.map(guideCategories);
        }

        Long branchId = securityUtils.getBranchId();

        List<GuideCategory> guideCategories = categoryRepository.findByMyBranchDepthCategory(branchId, 1);

        List<GuideCategoryDto> dtos = categoryMapper.map(guideCategories);
        for (GuideCategoryDto dto : dtos) {
            recursiveGetAllCategory(dto.getChildren(), branchId);
        }

        return dtos;
    }



    private void recursiveGetAllAndIsOpenCategory(List<GuideCategoryDto> list, Long branchId) {
        if (list.isEmpty())
            return;

        GuideCategoryDto delete = null;
        for (GuideCategoryDto dto : list) {
            if (!dto.getIsOpen() && !dto.getBranchId().equals(branchId)) {
                delete = dto;
            } else {
                recursiveGetAllAndIsOpenCategory(dto.getChildren(), branchId);
            }
        }
        list.remove(delete);
    }

    private void recursiveGetAllCategory(List<GuideCategoryDto> list, Long branchId) {
        if (list.isEmpty())
            return;

        GuideCategoryDto delete = null;
        for (GuideCategoryDto dto : list) {
            if (!dto.getBranch().getId().equals(branchId)) {
                delete = dto;
            } else {
                recursiveGetAllCategory(dto.getChildren(), branchId);
            }
        }
        list.remove(delete);
    }

    public void setCUD(GuideCategorySetting guideCategorySettings) {
        List<GuideCategoryDto> update = guideCategorySettings.getUpdate();
        List<Long> delete = guideCategorySettings.getDelete();

        Branch branch = branchService.findById(securityUtils.getBranchId());

        Assert.notNull(branch, "Not Found Branch");

        if (guideCategorySettings.getCreate() != null) {
            List<GuideCategoryDto> create = guideCategorySettings.getCreate();
            for (GuideCategoryDto dto : create) {
                GuideCategory entity = categoryMapper.map(dto);
                entity.setId(null);
                entity.setCreator(securityUtils.getMemberId());
                entity.setModifier(securityUtils.getMemberId());

                if (dto.getBranchId() != null) {
                    dto.setBranchId(dto.getBranchId());
                } else {
                    entity.setBranch(branch);
                }

                if (dto.getParentId() != null) {
                    GuideCategory parent = categoryRepository.findById(dto.getParentId()).orElse(null);
                    if (parent == null) {
                        log.error("Not Found Parent Category");
                        continue;
                    }
                    if (parent.getDepth() >= getCategoryMaxDepth()) {
                        log.error("Depth Outbound");
                        continue;
                    } else {
                        entity.setDepth(parent.getDepth() + 1);
                        entity.setParent(parent);
                        entity.setChildren(new ArrayList<>());
                        parent.getChildren().add(entity);
                    }
                } else {
                    entity.setDepth(1);
                    entity.setChildren(new ArrayList<>());
                }
                categoryRepository.save(entity);
                if (dto.getChildren() != null) {
                    recursiveSave(dto.getChildren(), entity, entity.getDepth() + 1);
                }
            }
            categoryRepository.flush();
        }

        if (guideCategorySettings.getUpdate() != null) {
            for (GuideCategoryDto dto : update) {
                GuideCategory category = null;
                if(securityUtils.isHeadQuarters()){
                    category = categoryRepository.findById(dto.getId()).orElse(null);
                }else{
                    category = categoryRepository.findByIdAndBranchId(dto.getId(), branch.getId()).orElse(null);
                }

                if (category == null) {
                    log.error("Not Found Category");
                    continue;
                }
                category.setModifier(securityUtils.getMemberId());
                CommonUtils.copyNotEmptyProperties(dto, category);
                if (dto.getBranchId() != null) {
                    Branch updateBranch = branchService.findById(dto.getBranchId());
                    if (updateBranch == null) {
                        log.error("Branch Not Found");
                        continue;
                    }
                    if (!category.getBranch().getId().equals(dto.getBranchId())) {
                        category.setBranch(updateBranch);
                    }
                }
            }
            categoryRepository.flush();
        }

        if (guideCategorySettings.getDelete() != null) {
            List<Long> deleteIds = new ArrayList<>();
            for (Long categoryId : delete) {
                GuideCategory guideCategory;
                if(securityUtils.isHeadQuarters()){
                    guideCategory = categoryRepository.findById(categoryId).orElse(null);
                }else{
                    guideCategory = categoryRepository.findByIdAndBranchId(categoryId, branch.getId()).orElse(null);
                }

                if (guideCategory != null) {
                    guideCategory.setBranch(null);
                    guideCategory.setParent(null);
                    deleteIds.add(guideCategory.getId());

                    if (!ObjectUtils.isEmpty(guideCategory.getChildren())) {
                        recursiveDelete(guideCategory.getChildren(), branch.getId(), deleteIds);
                    }
                }
            }
            deleteIds = deleteIds.stream().sorted(Comparator.reverseOrder()).distinct().collect(Collectors.toList());
            categoryRepository.deleteAllByIdInBatch(deleteIds);
        }
    }

    public List<Long> getAllSubCategory(Long categoryId, Long branchId) {
        List<GuideCategory> guideCategoryList = categoryRepository.findByIdAndBranchIdOrIsOpenTrue(categoryId, branchId);
        Assert.notEmpty(guideCategoryList, "Not Found GuideCategory");
        return getChildrenIds(branchId, guideCategoryList);
    }

    public GuideCategory findById(Long categoryId) {
        return categoryRepository.findById(categoryId).orElse(null);
    }


    /**
     * 조회된 3depth 카테고리에서 사용가능한 카테고리 필터링
     *
     * @param branchId
     * @return
     */
    public List<Long> getAllDepthCategory(Long branchId) {
        int maxDepth = getCategoryMaxDepth();
        List<GuideCategory> guideCategoryList = categoryRepository.findByBranchAndDepthCategory(branchId, maxDepth);
        List<Long> childrenIds = guideCategoryList.stream().filter(c -> {
            if (c.getBranch().getId().equals(branchId)) {
                return true;
            }

            if (maxDepth == 1) {
                return c.getIsOpen();
            } else if (maxDepth == 2) {
                return c.getParent().getIsOpen();
            } else if (maxDepth == 3) {
                return c.getParent().getParent().getIsOpen();
            }

            return false;
        }).map(GuideCategory::getId).collect(Collectors.toList());
        return childrenIds;
    }

    /**
     * 카테고리 하위 모든 자식들 조회
     *
     * @param list
     * @param childrenIds
     * @param branchId
     * 0이면 모든 브랜치
     */
    private void recursiveGetAllSubCategory(List<GuideCategoryDto> list, List<Long> childrenIds, Long branchId) {
        if (list == null || list.isEmpty())
            return;

        for (GuideCategoryDto dto : list) {
            if (branchId.equals(0L)) {
                if (dto.getDepth() == getCategoryMaxDepth())
                    childrenIds.add(dto.getId());
                recursiveGetAllSubCategory(dto.getChildren(), childrenIds, branchId);
            } else {
                if (dto.getIsOpen() || dto.getBranchId().equals(branchId)) {
                    if (dto.getDepth() == getCategoryMaxDepth())
                        childrenIds.add(dto.getId());
                    recursiveGetAllSubCategory(dto.getChildren(), childrenIds, branchId);
                }
            }

        }
    }

    /**
     * 카테고리 하위 자식들의 ID 가져오기
     *
     * @param branchId          0이면 모든 브랜치
     * @param guideCategoryList
     * @return
     */
    private List<Long> getChildrenIds(Long branchId, List<GuideCategory> guideCategoryList) {
        List<GuideCategoryDto> dtoList = categoryMapper.map(guideCategoryList);

        List<Long> childrenIds = new ArrayList<>();
        for (GuideCategoryDto dto : dtoList) {
            if (branchId.equals(0L)) {
                if (dto.getDepth() == getCategoryMaxDepth())
                    childrenIds.add(dto.getId());
                recursiveGetAllSubCategory(dto.getChildren(), childrenIds, branchId);

            } else {
                if (dto.getIsOpen() || dto.getBranchId().equals(branchId)) {
                    if (dto.getDepth() == getCategoryMaxDepth())
                        childrenIds.add(dto.getId());
                    recursiveGetAllSubCategory(dto.getChildren(), childrenIds, branchId);
                }
            }

        }
        return childrenIds;
    }

    /**
     * 브랜치가 맞지 않는 카테고리 삭제
     *
     * @param list
     * @param branchId
     * @param deleteIds
     */
    private void recursiveDelete(List<GuideCategory> list, Long branchId, List<Long> deleteIds) {
        if (list.isEmpty()) {
            return;
        }

        for (GuideCategory category : list) {
            if (category.getBranch().getId().equals(branchId) || securityUtils.isHeadQuarters()) {
                category.setBranch(null);
                category.setParent(null);
                deleteIds.add(category.getId());
            }
            recursiveDelete(category.getChildren(), branchId, deleteIds);
        }
    }

    /**
     * 카테고리 자식 저장
     *
     * @param list
     * @param parent
     * @param depth
     */
    private void recursiveSave(List<GuideCategoryDto> list, GuideCategory parent, int depth) {
        if (list == null || list.isEmpty())
            return;

        for (GuideCategoryDto dto : list) {
            GuideCategory category = categoryMapper.map(dto);
            if (category.getChildren() != null)
                category.getChildren().clear();
            category.setDepth(depth);
            if (dto.getBranchId() != null) {
                category.setBranch(branchService.findById(dto.getBranchId()));
            } else {
                category.setBranch(parent.getBranch());
            }
            category.setCreator(parent.getCreator());
            category.setModifier(parent.getModifier());
            category.setId(null);
            GuideCategory save = categoryRepository.save(category);
            save.setParent(parent);
            save.setChildren(new ArrayList<>());
            parent.getChildren().add(save);
            if (depth < getCategoryMaxDepth())
                recursiveSave(dto.getChildren(), save, depth + 1);
        }
    }

    public List<Long> getHeadAllDepthCategory() {
        int maxDepth = getCategoryMaxDepth();
        List<GuideCategory> guideCategoryList = categoryRepository.findAllByDepthCategory(maxDepth);
        return guideCategoryList.stream().map(GuideCategory::getId).collect(Collectors.toList());
    }

    public List<Long> getHeadAllSubCategory(Long categoryId) {
        List<GuideCategory> guideCategoryList = categoryRepository.findAllById(categoryId);
        Assert.notEmpty(guideCategoryList, "Not Found GuideCategory");
        return getChildrenIds(0L, guideCategoryList);
    }
}
