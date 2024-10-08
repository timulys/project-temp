package com.kep.portal.service.guide;

import com.kep.core.model.dto.guide.GuideCategoryDto;
import com.kep.core.model.exception.BizException;
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
        Assert.isTrue(maxDepth > 0, "Max depth must be greater than 0");

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

        if (guideCategories.isEmpty()) return Collections.emptyList();

        List<GuideCategoryDto> dtos = categoryMapper.map(guideCategories);

        for (GuideCategoryDto dto : dtos) {
            if (!dto.getIsOpen() && !dto.getBranchId().equals(branchId)) {
                dtos.remove(dto);
            }

//            recursiveGetAllAndIsOpenCategory(dto.getChildren(), branchId);
        }

        return dtos;
    }

    public List<GuideCategoryDto> getMyBranchAll() {
        if(securityUtils.isHeadQuarters() && securityUtils.hasRole(Level.ROLE_ADMIN)){
            List<GuideCategory> guideCategories = categoryRepository.findAllByDepthCategory(1);
            return categoryMapper.map(guideCategories);
        }

        Long branchId = securityUtils.getBranchId();

        List<GuideCategory> guideCategories = categoryRepository.findByBranchIdAndDepth(branchId, 1);

        List<GuideCategoryDto> dtos = categoryMapper.map(guideCategories);
//        for (GuideCategoryDto dto : dtos) {
//            recursiveGetAllCategory(dto.getChildren(), branchId);
//        }

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

    /**
     * 소속 브랜치 id와 미일치 시 반환목록에서 제거 -> isOpen 여부 상관없이 수행이므로 조회 쿼리 자체에 브랜치와 동일할경우로 넣어주면 됨
     *
     * @param list
     * @param branchId
     */
    @Deprecated
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
        Branch branch = branchService.findById(securityUtils.getBranchId());
//        Branch branch = branchService.findById(1L);//FIXME ::  테스트 후 제거
        Assert.notNull(branch, "Not Found Branch");

        if (branch.getMaxGuideCategoryDepth() == 0) throw new IllegalStateException("Guide category depth is not initialized");
        Long memberId = securityUtils.getMemberId();
//        Long memberId = 1L;//FIXME ::  테스트 후 제거

        if (!ObjectUtils.isEmpty(guideCategorySettings.getCreate())) {
            List<GuideCategoryDto> create = guideCategorySettings.getCreate();

            for (GuideCategoryDto dto : create) {
                if (dto.getEnabled() == null) dto.setEnabled(true);
                GuideCategory entity = categoryMapper.map(dto);
                entity.setId(null);
                entity.setCreator(memberId);
                entity.setModifier(memberId);
                entity.setBranch(branch);
//                if (dto.getBranchId() != null) {
//                    dto.setBranchId(dto.getBranchId());
//                } else {
//                    entity.setBranch(branch);
//                }

                if (dto.getParentId() != null) {
                    GuideCategory parent = categoryRepository.findById(dto.getParentId()).orElse(null);
                    if (parent == null) {
                        log.error("Not Found Parent Category");
                        throw new BizException("Not Found Parent Category");
                    }
                    if (parent.getDepth() >= getCategoryMaxDepth()) {
                        log.error("Depth Outbound");
                        throw new BizException("Depth Outbound");
                    } else {
                        if (!parent.getEnabled()) Assert.isTrue(!entity.getEnabled(), "children can not be enable true when parent enabled is false");
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

                if (!ObjectUtils.isEmpty(dto.getChildren())) {
                    recursiveSave(dto.getChildren(), entity, entity.getDepth() + 1);
                }
            }
            categoryRepository.flush();
        }

        if (!ObjectUtils.isEmpty(guideCategorySettings.getUpdate())) {
            List<GuideCategoryDto> update = guideCategorySettings.getUpdate();
            for (GuideCategoryDto dto : update) {
                GuideCategory category;

                //본사 > 관리자일 경우엔 타브랜치 가이드 카테고리 전체오픈 여부만 수정 가능 -> 조회시 전체
                if (securityUtils.isHeadQuarters() && securityUtils.hasRole(Level.ROLE_ADMIN)){
                    category = categoryRepository.findById(dto.getId())
                            .orElseThrow(() -> new BizException("Not Found Category"));

                } else {
                    category = categoryRepository.findByIdAndBranchId(dto.getId(), branch.getId())
                            .orElseThrow(() -> new BizException("Not Found Category"));
                }

                //소속 브랜치 카테고리일 경우 수정 가능
                if (category.getBranch().equals(branch)) {
                    category.setName(dto.getName()); //명칭 수정
                }

//                category.setEnabled(dto.getEnabled()); //사용여부 추가 TODO :: 복구. 현재 프론트와 연동 때문에 delete list에 enabled 처리
                category.setIsOpen(dto.getIsOpen()); //브랜치 오픈 여부 수정 추가
                category.setModifier(memberId);
                CommonUtils.copyNotEmptyProperties(dto, category);
                //FIXME :: 주석처리 (소속 브랜치 카테고리만 수정 가능)
//                if (dto.getBranchId() != null) {
//                    Branch updateBranch = branchService.findById(dto.getBranchId());
//                    if (updateBranch == null) {
//                        log.error("Branch Not Found");
//                        continue;
//                    }
//                    if (!category.getBranch().getId().equals(dto.getBranchId())) {
//                        category.setBranch(updateBranch);
//                    }
//                }
            }
            categoryRepository.flush();
        }

        if (!ObjectUtils.isEmpty(guideCategorySettings.getDelete())) {
            List<Long> delete = guideCategorySettings.getDelete();
            delete = delete.stream().sorted(Comparator.reverseOrder()).distinct().collect(Collectors.toList());
//            List<Long> deleteIds = new ArrayList<>();
            for (Long categoryId : delete) {
                GuideCategory guideCategory = categoryRepository.findByIdAndBranchId(categoryId, branch.getId()).orElse(null);
//                if(securityUtils.isHeadQuarters()){
//                    guideCategory = categoryRepository.findById(categoryId).orElse(null);
//                }else{
//                    guideCategory = categoryRepository.findByIdAndBranchId(categoryId, branch.getId()).orElse(null);
//                }


                if (guideCategory != null) {
                    guideCategory.setEnabled(false); //사용안함 처리
//                    guideCategory.setBranch(null);
//                    guideCategory.setParent(null);
//                    deleteIds.add(guideCategory.getId());

                    if (!ObjectUtils.isEmpty(guideCategory.getChildren())) {
//                        recursiveDelete(guideCategory.getChildren(), branch.getId(), deleteIds);
                        boolean childIsEnabled;
                        if (!guideCategory.getEnabled()) {
                            childIsEnabled = guideCategory.getChildren().stream()
                                    .noneMatch(GuideCategory::getEnabled);
                            Assert.isTrue(childIsEnabled, "children can not be enable true when category enabled is false");
                        }
                    }
                }
            }
//            deleteIds = deleteIds.stream().sorted(Comparator.reverseOrder()).distinct().collect(Collectors.toList());
//            categoryRepository.deleteAllByIdInBatch(deleteIds); //FIXME :: 카테고리 삭제 불가 (enable false 처리) volka
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
            if (dto.getEnabled() == null) dto.setEnabled(parent.getEnabled());
            if (!parent.getEnabled() && dto.getEnabled()) throw new BizException("children can not be enable true when parent enabled is false");

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
