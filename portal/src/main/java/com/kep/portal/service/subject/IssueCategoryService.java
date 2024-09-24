package com.kep.portal.service.subject;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kep.core.model.dto.ApiResultCode;
import com.kep.core.model.dto.legacy.LegacyBnkCategoryDto;
import com.kep.core.model.dto.subject.IssueCategoryBasicDto;
import com.kep.portal.model.dto.subject.*;
import com.kep.core.model.exception.BizException;
import com.kep.portal.client.LegacyClient;
import com.kep.portal.model.entity.branch.BranchChannel;
import com.kep.portal.model.entity.channel.Channel;
import com.kep.portal.model.entity.channel.ChannelEnv;
import com.kep.portal.model.entity.subject.IssueCategory;
import com.kep.portal.model.entity.subject.IssueCategoryMapper;
import com.kep.portal.repository.channel.ChannelEnvRepository;
import com.kep.portal.repository.channel.ChannelRepository;
import com.kep.portal.repository.subject.IssueCategoryRepository;
import com.kep.portal.service.branch.BranchChannelService;
import com.kep.portal.service.branch.BranchService;
import com.kep.portal.service.channel.ChannelEnvService;
import com.kep.portal.util.CommonUtils;
import com.kep.portal.util.SecurityUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.util.ObjectUtils;

import javax.annotation.Nullable;
import javax.annotation.Resource;
import javax.transaction.Transactional;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import java.time.ZonedDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Transactional
@Slf4j
public class IssueCategoryService {

    @Resource
    private IssueCategoryRepository issueCategoryRepository;

    @Resource
    private IssueCategoryMapper issueCategoryMapper;
    @Resource
    private IssueCategoryMemberService issueCategoryMemberService;
    @Resource
    private BranchChannelService branchChannelService;
    @Resource
    private ChannelEnvService channelEnvService;

    @Resource
    private SecurityUtils securityUtils;
    @Resource
    private ObjectMapper objectMapper;
    
    @Resource
    private LegacyClient legacyClient;

    // TODO: 채널 설정 완료시, 설정에서 가져옴
    private static final Integer MAX_DEPTH = 3;
    @Autowired
    private BranchService branchService;
    @Autowired
    private ChannelEnvRepository channelEnvRepository;
    @Autowired
    private ChannelRepository channelRepository;

    private Integer getCategoryMaxDepth(Long channelId){
        return channelEnvService.getByChannelId(channelId).getMaxIssueCategoryDepth();
    }

    @Nullable
    public List<IssueCategoryChildrenDto> search(@NotNull @Positive Long channelId, String name) throws Exception {

        List<IssueCategory> searchedCategories = null;

        if (!ObjectUtils.isEmpty(name)) {
            searchedCategories = issueCategoryRepository.search(channelId,
//					securityUtils.getBranchId(),
                    true,
                    true,
//					MAX_DEPTH,
                    name);

            List<IssueCategory> lastDepthCategories = new ArrayList<>();
            settingIssueCategorySearch(searchedCategories, lastDepthCategories, getCategoryMaxDepth(channelId));

            log.debug(objectMapper.writeValueAsString(lastDepthCategories));
            return issueCategoryMapper.mapChildren(flatAncestor(lastDepthCategories));
        } else {
            searchedCategories = issueCategoryRepository.search(channelId,
//					securityUtils.getBranchId(),
                    true, true, getCategoryMaxDepth(channelId));

            return issueCategoryMapper.mapChildren(flatAncestor(searchedCategories));
        }
    }

    private void settingIssueCategorySearch(List<IssueCategory> searchedCategories, List<IssueCategory> lastDepthCategories, int maxDepth) {
        for (IssueCategory category : searchedCategories) {
            if (category.getDepth().equals(maxDepth)) {
                lastDepthCategories.add(category);
            }
            List<IssueCategory> list = issueCategoryRepository.findAllByParentAndEnabledIsTrue(category);
            settingIssueCategorySearch(list, lastDepthCategories, maxDepth);
        }
    }

    @Nullable
    public List<IssueCategoryChildrenDto> searchById(@NotNull Long id, @NotNull Long channelId) throws Exception {
        List<IssueCategory> searchedCategories = issueCategoryRepository.searchById(getCategoryMaxDepth(channelId), id);
        log.debug(objectMapper.writeValueAsString(searchedCategories));
        return issueCategoryMapper.mapChildren(flatAncestor(searchedCategories));
    }

    /**
     * {@code issueCategories} 자신을 포함한 모든 조상을 하나의 리스트로 만듦
     */
    private List<IssueCategory> flatAncestor(@NotNull List<IssueCategory> issueCategories) {

        Set<Long> issueCategoryIds = new HashSet<>(); // 중복 체크용
        List<IssueCategory> ancestor = new ArrayList<>();

        for (IssueCategory issueCategory : issueCategories) {
            List<IssueCategory> path = IssueCategory.getPath(issueCategory);
            for (IssueCategory category : path) {
                if (issueCategoryIds.add(category.getId())) {
                    ancestor.add(category);
                }
            }
        }

        return ancestor;
    }

    /**
     * 검색된 분류 목록 -> 해당 분류가 속한 대분류 시퀀스 목록
     */
    @Deprecated
    private List<Long> getTopCategoryIds(List<IssueCategory> searchedCategories) throws Exception {

        Set<Long> topCategoryIds = new HashSet<>();

        for (IssueCategory issueCategory : searchedCategories) {
            List<IssueCategory> issueCategories = IssueCategory.getPath(issueCategory);
            if (!issueCategories.isEmpty()) {
                topCategoryIds.add(issueCategories.get(0).getId());
            }
        }

        return new ArrayList<>(topCategoryIds);
    }

    @Deprecated
    private List<IssueCategoryChildrenDto> getAllTree(List<Long> topCategoryIds) throws Exception {

        // 전체 분류 순회
        List<IssueCategory> all = issueCategoryRepository.findAll(Example.of(IssueCategory.builder().enabled(true).build()));
        List<IssueCategoryChildrenDto> allTree = issueCategoryMapper.mapChildren(all);

        return allTree.stream().filter(o -> topCategoryIds.contains(o.getId())).collect(Collectors.toList());
    }

    public List<IssueCategoryBasicDto> getAll(@Positive Long channelId, @Positive Long parentId, Boolean enabled) throws Exception {

        IssueCategory search = new IssueCategory();

        if (channelId == null) {
            BranchChannel branchChannel = branchChannelService.findOneByBranchId(securityUtils.getBranchId());
            Assert.notNull(branchChannel, "branchChannel is null");
            channelId = branchChannel.getChannel().getId();
        }
        search.setChannelId(channelId);

        if (parentId != null) {
            // parentId 하위 분류 목록
            IssueCategory parent = issueCategoryRepository.findById(parentId).orElse(null);
            Assert.notNull(parent, "parent is null");
            Assert.isTrue(channelId.equals(parent.getChannelId()), "parent's channel is wrong, expect: " + channelId
                    + ", actual: " + parent.getChannelId());
            search.setParent(parent);
        } else {
            search.setDepth(1);
        }

        if (enabled != null) {
            search.setEnabled(enabled);
        }

        log.info("ISSUE CATEGORY SEARCH: {}", objectMapper.writeValueAsString(search));
        Sort sort = Sort.by(Sort.Direction.ASC, "sort");
        List<IssueCategory> entities = issueCategoryRepository.findAll(Example.of(search), sort);
        return issueCategoryMapper.mapBasic(entities);
    }

    /**
     * 브랜치에 포함된 채널에 포함된 분류 목록
     */
    public List<IssueCategoryWithChannelDto> getAllByBranch(@Positive Long branchId, @Positive Long parentId) throws Exception {

        IssueCategory search = new IssueCategory();

        List<BranchChannel> branchChannel = branchChannelService.findAllByBranchId(
                branchId != null ? branchId : securityUtils.getBranchId());
        if (branchChannel.isEmpty()) {
            return Collections.emptyList();
        }
        List<Channel> channels = branchChannel.stream().map(BranchChannel::getChannel).collect(Collectors.toList());
        search.setChannelIds(channels.stream().map(Channel::getId).collect(Collectors.toList()));

        if (parentId != null) {
            // parentId 하위 분류 목록
            IssueCategory parent = issueCategoryRepository.findById(parentId).orElse(null);
            Assert.notNull(parent, "PARENT IS NULL");
            search.setParent(parent);
        } else {
            search.setDepth(1);
        }

        log.info("ISSUE CATEGORY SEARCH: {}", objectMapper.writeValueAsString(search));
        List<IssueCategory> entities = issueCategoryRepository.search(search.getChannelIds(), search.getParent(), search.getDepth());
        return issueCategoryMapper.mapWithChannel(entities, channels);
    }

    public IssueCategory findById(@NotNull @Positive Long id) {

        return issueCategoryRepository.findById(id).orElse(null);
    }

    public List<IssueCategory> findAllByParent(@NotNull IssueCategory parent) {

        return issueCategoryRepository.findAll(Example.of(IssueCategory.builder()
                .parent(parent)
                .build()));
    }

    public IssueCategoryBasicDto store(@NotNull @Positive Long channelId, @NotNull IssueCategoryStoreDto issueCategoryStoreDto) {

        IssueCategory parent = null;
        int depth = 1;
        if (issueCategoryStoreDto.getParentId() != null) {
            parent = this.findById(issueCategoryStoreDto.getParentId());
            Assert.notNull(parent, "parent is null");
            depth = parent.getDepth() + 1;
        }

        IssueCategory issueCategory = issueCategoryMapper.mapStore(issueCategoryStoreDto);
        issueCategory.setChannelId(channelId);
        issueCategory.setParent(parent);
        issueCategory.setDepth(depth);
        issueCategory.setEnabled(true);
        issueCategory.setExposed(true);
//		issueCategory.setBranchId(securityUtils.getBranchId());
        issueCategory.setModifier(securityUtils.getMemberId());
        issueCategory.setModified(ZonedDateTime.now());

        issueCategory = issueCategoryRepository.save(issueCategory);
        return issueCategoryMapper.mapBasic(issueCategory);
    }

    public IssueCategoryBasicDto store(@NotNull IssueCategoryStoreDto issueCategoryStoreDto, @NotNull @Positive Long id) {

        IssueCategory issueCategory = this.findById(id);
        Assert.notNull(issueCategory, "issue category is null");

        issueCategoryStoreDto.setParentId(null); // 상위 분류 수정 금지, 상위 분류를 바꾸려면 삭제 후 생성
        CommonUtils.copyNotEmptyProperties(issueCategoryStoreDto, issueCategory);
        issueCategory.setModifier(securityUtils.getMemberId());
        issueCategory.setModified(ZonedDateTime.now());

        issueCategory = issueCategoryRepository.save(issueCategory);
        return issueCategoryMapper.mapBasic(issueCategory);
    }

    public void delete(@NotNull @Positive Long id) {

        IssueCategory issueCategory = this.findById(id);
        Assert.notNull(issueCategory, "issue category is null");

        List<IssueCategory> issueCategories = findAllByParent(issueCategory);
        // 하위 분류가 있는 경우 삭제 금지
        if (!issueCategories.isEmpty()) {
            // TODO: 에러 코드 필요
            throw new UnsupportedOperationException("<<SB-SA-006-001>> cannot delete non-empty category");
        }

        // 상담원 배분 설정이 있는 경우 삭제 금지
        if (!issueCategoryMemberService.findAllByIssueCategory(issueCategory.getId()).isEmpty()) {
            // TODO: 에러 코드 필요
            throw new UnsupportedOperationException("<<SB-SA-006-002>> cannot delete category matched with members");
        }

        issueCategory.setEnabled(false);
        issueCategory.setName(issueCategory.getName() + "_" + System.currentTimeMillis());
        issueCategoryRepository.save(issueCategory);
    }

    public List<IssueCategory> findByParentIdIn(List<Long> id){
        return issueCategoryRepository.findByParentIdIn(id);
    }
    
    /**
     * BNK 카테고리 API 연동
     *
     * 조회 구분, 현업 이관 업무, 현업 이관 부서에 따라 BNK의 카테고리 정보를 조회합니다.
     * - 조회구분 (L): 사용자의 조회구분 선택값을 기반으로 IssueCategoryController에서 요청합니다.
     * - 조회구분 (M): 조회구분에서 선택된 값(value)을 fld_cd로 설정하여 API 호출합니다.
     * - 현업이관부서 (S): 조회구분에서 선택된 값(value)을 fld_cd로, 현업이관업무에서 선택된 값(value)을 wrk_seq로 설정하여 API 호출합니다.
     *
     * @param gubun 조회 구분값 (L, M, S 중 하나)
     * @param fld_cd 현업 이관 업무 코드값 (조회구분에서 반환된 value 값)
     * @param wrk_seq 현업 이관 부서 코드값 (조회구분, 현업이관업무에서 반환된 value 값)
     * @return LegacyBnkCategoryDto BNK 카테고리 정보
     */
    public LegacyBnkCategoryDto getBnkCategoryInfo(LegacyBnkCategoryDto dto) {
        log.info("Fetching BNK Category data for GUBUN: {}", dto.getGubun());
        
        if (dto.getGubun() == null || (!dto.getGubun().equals("L") 
        		&& !dto.getGubun().equals("M") 
        		&& !dto.getGubun().equals("S"))) {
            throw new IllegalArgumentException("Invalid gubun value: " + dto.getGubun());
        }
        
        return legacyClient.getBnkCategoryInfo(dto);
    }



//    private void delete(List<Long> deleteIds, List<IssueCategory> entities, Map<Long, IssueCategory> entityMap) {
//
//
//        IssueCategory entity = null;
//        Long parentId = null;
//        Map<Long, List<IssueCategory>> parentCategoryTree = new HashMap<>();
//
//        for (IssueCategory issueCategory : entities) {
//            if (issueCategory.getParent() != null) {
//                parentId = issueCategory.getParent().getId();
//
//                if (parentCategoryTree.containsKey(parentId)) {
//                    parentCategoryTree.get(parentId).add(issueCategory);
//                } else {
//                    List<IssueCategory> list = new ArrayList<>();
//                    list.add(issueCategory);
//                    parentCategoryTree.put(parentId, list);
//                }
//            }
//        }
//
//
//        for (Long categoryId : deleteIds) {
//            entity = issueCategoryMap.get(categoryId);
//            if (entity == null) throw new BizException("not exists category");
//
////                parentId = entity.getParent();
////                if (parentId != null) havingParentCategory.add(parentId);
//        }
//
//        havingParentCategory.forEach(id -> {
//            //하위 카테고리 있으면 삭제 못함
//            if (issueCategoryMap.containsKey(id)) throw new BizException("can not delete category");
//            issueCategoryMap.remove(id);
//        });
//
//        issueCategoryRepository.deleteAllByIdInBatch(deleteIds);
//    }



    /**
     * 상담 카테고리 저장 (신규) 20240718 volka
     * TODO :: 공통팝업 협의 후 소스 정리
     *
     * @param issueCategorySetting
     * @return
     */
    public String saveIssueCategories(IssueCategorySetting issueCategorySetting) {

        Long channelId = issueCategorySetting.getChannelId();

        ChannelEnv channelEnv = channelEnvRepository.findByChannelId(channelId).orElseThrow(() -> new BizException("not exist channel"));
        if (channelEnv.getMaxIssueCategoryDepth().equals(0)) throw new IllegalStateException("Issue Category is not initialized");

        Integer maxDepth = channelEnv.getMaxIssueCategoryDepth();

        List<IssueCategoryTreeDto> stores = issueCategorySetting.getIssueCategories();
//        List<Long> unableIds = issueCategorySetting.getUnableIssueCategoryIds();


        List<IssueCategory> entities = issueCategoryRepository.findAllByChannelId(issueCategorySetting.getChannelId());

        if (maxDepth > 1) {
            Map<IssueCategory, List<IssueCategory>> depthMap =  entities.stream()
                    .filter(item -> item.getDepth() > 1)
                    .collect(Collectors.groupingBy(IssueCategory::getParent));
        }



        Map<Long, IssueCategory> issueCategoryMap = entities.stream()
                .collect(Collectors.toMap(IssueCategory::getId, item -> item));



        return ApiResultCode.succeed.name();
    }

    private List<IssueCategoryTreeDto> createCategoryTree(List<IssueCategory> issueCategories, Integer maxDepth) {

        List<IssueCategoryTreeDto> dtoList = issueCategories.stream()
                .map(IssueCategoryTreeDto::of)
                .collect(Collectors.toList());


        if (maxDepth > 1) {
            Map<Integer, List<IssueCategoryTreeDto>> depthMap = dtoList.stream().collect(Collectors.groupingBy(IssueCategoryTreeDto::getDepth));
            Map<Long, IssueCategoryTreeDto> dtoMap = dtoList.stream().collect(Collectors.toMap(IssueCategoryTreeDto::getIssueCategoryId, item -> item));

            Long parentId = null;
            List<IssueCategoryTreeDto> depthList = null;
            IssueCategoryTreeDto categoryTreeDto = null;

            for (Integer i = maxDepth; i > 1; i--) {
                depthList = depthMap.get(i);

                for (IssueCategoryTreeDto issueCategoryTreeDto : depthList) {
                    parentId = issueCategoryTreeDto.getParentId();
                    categoryTreeDto = dtoMap.get(parentId);

                    if (issueCategoryTreeDto.getParentId() != null && categoryTreeDto != null) {
                        dtoMap.get(parentId).getChildren().add(issueCategoryTreeDto);
                    }
                }
            }

            return depthMap.get(1);
        } else {
            return dtoList;
        }
    }

    /**
     * 채널별 상담 카테고리 전체 조회 (Tree구조)
     * @param channelId
     * @return
     */
    public List<IssueCategoryTreeDto> getAllCategoriesByChannelId(Long channelId) {
        ChannelEnv channelEnv = channelEnvRepository.findByChannelId(channelId).orElseThrow(() -> new BizException("not exist channel"));
        if (channelEnv.getMaxIssueCategoryDepth().equals(0)) return Collections.emptyList();

        List<IssueCategory> issueCategories = issueCategoryRepository.findAllByChannelId(channelId);
        if (issueCategories.isEmpty()) return Collections.emptyList();

        return createCategoryTree(issueCategories, channelEnv.getMaxIssueCategoryDepth());
    }
}
