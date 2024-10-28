package com.kep.portal.service.guide;

import com.kep.core.model.dto.guide.GuideCategoryDto;
import com.kep.core.model.exception.BizException;
import com.kep.portal.model.dto.guide.GuideCategorySetting;
import com.kep.portal.model.entity.branch.Branch;
import com.kep.portal.model.entity.guide.GuideCategory;
import com.kep.portal.model.entity.guide.GuideCategoryMapper;
import com.kep.portal.model.entity.privilege.Level;
import com.kep.portal.repository.guide.GuideCategoryRepository;
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
        //각 브랜치별로 가이드 카테고리 설정 가능.
//        Assert.isTrue(branch.getHeadQuarters(), "Branch is not headquarters");
        Assert.notNull(branch, "not found branch");
        Assert.isTrue(branch.getMaxGuideCategoryDepth().equals(0), "Already set up");
        Assert.isTrue(maxDepth > 0, "Max depth must be greater than 0");

        branch.setMaxGuideCategoryDepth(maxDepth);

//        Branch headQuarters = branchService.findHeadQuarters();
//        headQuarters.setMaxGuideCategoryDepth(maxDepth);
        return branch.getMaxGuideCategoryDepth();
    }

    public int getCategoryMaxDepth() {

        Branch branch = branchService.findById(securityUtils.getBranchId());
        if (branch == null) throw new BizException("Not found Branch");

        return branch.getMaxGuideCategoryDepth();
    }

    public List<GuideCategoryDto> getAll(Long branchId) {
        if (branchId == null)
            branchId = securityUtils.getBranchId();
//        branchId = 1L; FIXME :: 테스트 후 삭제

        List<GuideCategory> guideCategories = categoryRepository.findMyBranchEnabledCategory(branchId, 1);
        if (guideCategories.isEmpty()) return Collections.emptyList();

        List<GuideCategoryDto> dtos = categoryMapper.map(guideCategories);

        recursiveGetAllAndIsOpenCategory(dtos, branchId);

        return dtos;
    }

    private boolean isHeadQuartersAdmin() {
        return securityUtils.isHeadQuarters() && securityUtils.hasRole(Level.ROLE_ADMIN);
    }

    /**
     * 카테고리 관리 카테고리 조회
     * @return
     */
    public List<GuideCategoryDto> getMyBranchAll() {
        if(isHeadQuartersAdmin()){
            List<GuideCategory> guideCategories = categoryRepository.findAllByDepthCategory(1);
            return categoryMapper.map(guideCategories);
        }

        Long branchId = securityUtils.getBranchId();

        List<GuideCategory> guideCategories = categoryRepository.findAllByBranchIdAndDepth(branchId, 1);

        List<GuideCategoryDto> dtos = categoryMapper.map(guideCategories);
        recursiveGetAllAndIsOpenCategory(dtos, branchId);

        return dtos;
    }


    private void recursiveGetAllAndIsOpenCategory(List<GuideCategoryDto> list, Long branchId) {
        if (list.isEmpty())
            return;

        GuideCategoryDto delete = null;
        for (GuideCategoryDto dto : list) {
            if (!dto.getIsOpen() && !dto.getBranch().getId().equals(branchId)) {
                delete = dto;
            } else {
                recursiveGetAllAndIsOpenCategory(dto.getChildren(), branchId);
            }
        }
        list.remove(delete);
    }

    private void recursiveGetAllOnlyBranch(List<GuideCategoryDto> list, Long branchId) {
        if (list.isEmpty())
            return;

        GuideCategoryDto delete = null;
        for (GuideCategoryDto dto : list) {
            if (!dto.getBranch().getId().equals(branchId)) {
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


    public void saveCreateCategory(GuideCategorySetting guideCategorySettings, Branch branch, Long memberId, boolean headQuartersAdmin) {
        List<GuideCategoryDto> create = guideCategorySettings.getCreate();

        for (GuideCategoryDto dto : create) {
            if (dto.getEnabled() == null) dto.setEnabled(true);
            if (!headQuartersAdmin && dto.getIsOpen()) throw new BizException("open is only can headQuarters admin"); //전체오픈은 본사 관리자만 가능
            dto.setIsOpen(headQuartersAdmin ? dto.getIsOpen() : false); //본사 > 관리자일 경우 전체 오픈 설정 가능. 아닐경우 브랜치오픈 고정

            GuideCategory entity = categoryMapper.map(dto);
            entity.setId(null);
            entity.setCreator(memberId);
            entity.setModifier(memberId);
            entity.setBranch(branch);

            if (dto.getParentId() != null) {
                GuideCategory parent = categoryRepository.findById(dto.getParentId()).orElse(null);
                if (parent == null) {
                    log.error("Create Not Found Parent Category");
                    throw new BizException("Create Not Found Parent Category");
                }
                if (parent.getDepth() >= getCategoryMaxDepth()) {
                    log.error("Create Depth Outbound");
                    throw new BizException("Create Depth Outbound");
                } else {
                    Assert.isTrue(parent.getBranch().equals(branch), "guide category is created only in my branch");
                    if (!parent.getEnabled()) Assert.isTrue(!entity.getEnabled(), "children can not be enable true when parent enabled is false");
                    if (!parent.getIsOpen()) Assert.isTrue(!entity.getIsOpen(), "children can not be isOpen true when parent isOpen is false");
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
                recursiveSave(dto.getChildren(), entity, entity.getDepth() + 1, branch);
            }
        }
        categoryRepository.flush();
    }

    private void validGuideCategoryIsOpen(Boolean parentCategoryIsOpen, Boolean childCategoryIsOpen) {
        if (!parentCategoryIsOpen && childCategoryIsOpen) throw new BizException("can not be child isOpen true when parent isOpen is false");
    }

    private void validGuideCategoryEnabled(Boolean parentCategoryEnabled, Boolean childCategoryEnabled) {
        if (!parentCategoryEnabled && childCategoryEnabled) throw new BizException("can not be child enabled true when parent enabled is false");
    }

    private void validGuideCategoryParent(GuideCategory parent, GuideCategoryDto input, Map<Long, GuideCategoryDto> dtoMap, Boolean categoryEnabled) {

        GuideCategoryDto parentDto = dtoMap.get(parent.getId());
        if (parentDto == null) {
            validGuideCategoryIsOpen(parent.getIsOpen(), input.getIsOpen());
            validGuideCategoryEnabled(parent.getEnabled(), categoryEnabled);
        } else {
            validGuideCategoryIsOpen(parentDto.getIsOpen(), input.getIsOpen());
            validGuideCategoryEnabled(parentDto.getEnabled(), categoryEnabled);
        }
    }

    private void saveUpdateCategory(GuideCategorySetting guideCategorySettings, Branch branch, Long memberId, boolean headQuartersAdmin) {
        List<GuideCategoryDto> update = guideCategorySettings.getUpdate();

        GuideCategory parent = null;
        Boolean categoryEnabled = null;

        for (GuideCategoryDto dto : update) {
            GuideCategory category;

            //본사 > 관리자일 경우엔 타브랜치 가이드 카테고리 전체오픈 여부만 수정 가능 -> 조회시 전체
            if (headQuartersAdmin){
                category = categoryRepository.findById(dto.getId())
                        .orElseThrow(() -> new BizException("Update Not Found Category"));
                category.setIsOpen(dto.getIsOpen()); //카테고리 전체 오픈 여부는 본사 > 관리자만 가능
            } else {
                if (dto.getIsOpen()) throw new BizException("open is only can headQuarters admin"); //전체오픈은 본사 관리자만 가능
                category = categoryRepository.findByIdAndBranchId(dto.getId(), branch.getId())
                        .orElseThrow(() -> new BizException("Update Not Found Category or this category is not in this branch"));
            }

            Map<Long, GuideCategoryDto> dtoMap = update.stream().collect(Collectors.toMap(GuideCategoryDto::getId, item -> item));

            List<GuideCategory> children = category.getChildren();

            parent = category.getParent();
            GuideCategoryDto childDto = null;
            categoryEnabled = dto.getEnabled() == null ? category.getEnabled() : dto.getEnabled();

            if (parent != null) {
                validGuideCategoryParent(parent, dto, dtoMap, categoryEnabled);
            }

            //카테고리 전체 오픈일 때 하위노드 전체오픈, 사용여부 검증
            if (!ObjectUtils.isEmpty(children)) {
                for (GuideCategory child : children) {
                    childDto = dtoMap.get(child.getId());

                    if (childDto == null) {
                        //자식 노드가 입력에 없을 땐 레코드 기준. (입력받지 않은 데이터이므로)
                        validGuideCategoryIsOpen(dto.getIsOpen(), child.getIsOpen());
                        validGuideCategoryEnabled(categoryEnabled, child.getEnabled());
                    } else {
                        //자식 노드가 입력에 있을 땐 입력 기준 (DB 반영 전이므로)
                        validGuideCategoryIsOpen(dto.getIsOpen(), childDto.getIsOpen());
                        validGuideCategoryEnabled(categoryEnabled, childDto.getEnabled());
                    }
                }
            }

            //소속 브랜치 카테고리일 경우 수정 가능
            if (category.getBranch().equals(branch)) {
                category.setName(dto.getName()); //명칭 수정
            }


            category.setModifier(memberId);
            CommonUtils.copyNotEmptyProperties(dto, category);

            //categoryRepository.save(category);
        }
        categoryRepository.flush();
    }

    private void saveDeleteCategory(GuideCategorySetting guideCategorySettings, Branch branch, Long memberId) {

        if (!securityUtils.isAdmin()) throw new BizException("delete only can admin");

        List<Long> delete = guideCategorySettings.getDelete();
        delete = delete.stream().sorted(Comparator.reverseOrder()).distinct().collect(Collectors.toList());
        for (Long categoryId : delete) {
            //수정 / 삭제는 본인 소속 브랜치 관리자만 가능
            GuideCategory guideCategory = categoryRepository.findByIdAndBranchId(categoryId, branch.getId())
                    .orElseThrow(() -> new BizException("Delete Not Found Category or this category is not in this branch"));

            guideCategory.setEnabled(false); //사용안함 처리
            guideCategory.setModifier(memberId);
            //사용 불가일 때 하위 노드 사용 가능 탐색
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




    public void setCUD(GuideCategorySetting guideCategorySettings) {
        Branch branch = branchService.findById(securityUtils.getBranchId());
        boolean headQuartersAdmin = isHeadQuartersAdmin(); //본사 관리자 여부
        Assert.notNull(branch, "Not Found Branch");

        if (branch.getMaxGuideCategoryDepth() == 0) throw new IllegalStateException("Guide category depth is not initialized");
        Long memberId = securityUtils.getMemberId();
//        Long memberId = 1L;//FIXME ::  테스트 후 제거

        if (!ObjectUtils.isEmpty(guideCategorySettings.getCreate())) {
            saveCreateCategory(guideCategorySettings, branch, memberId, headQuartersAdmin);
        }

        if (!ObjectUtils.isEmpty(guideCategorySettings.getUpdate())) {
            saveUpdateCategory(guideCategorySettings, branch, memberId, headQuartersAdmin);
        }

        if (!ObjectUtils.isEmpty(guideCategorySettings.getDelete())) {
            saveDeleteCategory(guideCategorySettings, branch, memberId);
        }
    }

    public List<Long> getAllSubCategory(Long categoryId, Long branchId) {
        List<GuideCategory> guideCategoryList = categoryRepository.findByIdAndBranchIdOrIsOpenTrue(categoryId, branchId);
        Assert.notEmpty(guideCategoryList, "Not Found GuideCategory");
        guideCategoryList = guideCategoryList.stream()
                .filter(GuideCategory::getEnabled)
                .collect(Collectors.toList());
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

            if (!c.getEnabled()) return false;

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
            if (branchId.equals(0L)) { //FIXME :: branchId 0 ??? 1이 본사인데
                if (dto.getDepth() == getCategoryMaxDepth())
                    childrenIds.add(dto.getId());
                recursiveGetAllSubCategory(dto.getChildren(), childrenIds, branchId);
            } else {
                if ((dto.getIsOpen() || branchId.equals(dto.getBranchId()) || branchId.equals(dto.getBranch().getId())) && dto.getEnabled()) {
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
                if (dto.getIsOpen() || branchId.equals(dto.getBranchId()) || branchId.equals(dto.getBranch().getId())) {
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
     * 카테고리 자식 추가
     *
     * @param list
     * @param parent
     * @param depth
     */
    private void recursiveSave(List<GuideCategoryDto> list, GuideCategory parent, int depth, Branch branch) {
        if (list == null || list.isEmpty())
            return;

        for (GuideCategoryDto dto : list) {
            if (dto.getEnabled() == null) dto.setEnabled(parent.getEnabled());
            if (!parent.getEnabled() && dto.getEnabled()) throw new BizException("children can not be enable true when parent enabled is false");

            GuideCategory category = categoryMapper.map(dto);
            if (category.getChildren() != null) category.getChildren().clear();
            if (dto.getBranchId() != null && !parent.getBranch().getId().equals(dto.getBranchId())) throw new BizException("guide category is created only in my branch");
            Assert.isTrue(parent.getBranch().equals(branch), "guide category is created only in my branch");

            category.setDepth(depth);
            category.setBranch(branch);
            category.setCreator(parent.getCreator());
            category.setModifier(parent.getModifier());
            category.setId(null);
            GuideCategory save = categoryRepository.save(category);
            save.setParent(parent);
            save.setChildren(new ArrayList<>());
            parent.getChildren().add(save);
            if (depth < getCategoryMaxDepth())
                recursiveSave(dto.getChildren(), save, depth + 1, branch);
        }
    }

    public List<Long> getHeadAllDepthCategory() {
        int maxDepth = getCategoryMaxDepth();
        List<GuideCategory> guideCategoryList = categoryRepository.findAllByDepthCategory(maxDepth);
        Assert.notEmpty(guideCategoryList, "Not Found GuideCategory");

        //사용여부 필터링 추가
        return guideCategoryList.stream()
                .filter(GuideCategory::getEnabled)
                .map(GuideCategory::getId)
                .collect(Collectors.toList())
                ;

//        return guideCategoryList.stream().map(GuideCategory::getId).collect(Collectors.toList());
    }

    public List<Long> getHeadAllSubCategory(Long categoryId) {
        List<GuideCategory> guideCategoryList = categoryRepository.findAllById(categoryId);
        Assert.notEmpty(guideCategoryList, "Not Found GuideCategory");

        //사용여부 필터링 추가
        guideCategoryList = guideCategoryList.stream().filter(GuideCategory::getEnabled).collect(Collectors.toList());

        return getChildrenIds(0L, guideCategoryList);
    }

}
