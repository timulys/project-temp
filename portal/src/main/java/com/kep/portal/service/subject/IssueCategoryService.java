package com.kep.portal.service.subject;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kep.core.model.dto.legacy.LegacyBnkCategoryDto;
import com.kep.core.model.dto.subject.IssueCategoryBasicDto;
import com.kep.core.model.dto.subject.IssueCategoryDto;
import com.kep.core.model.exception.BizException;
import com.kep.portal.client.LegacyClient;
import com.kep.portal.model.dto.subject.*;
import com.kep.portal.model.entity.branch.BranchChannel;
import com.kep.portal.model.entity.channel.Channel;
import com.kep.portal.model.entity.channel.ChannelEnv;
import com.kep.portal.model.entity.subject.IssueCategory;
import com.kep.portal.model.entity.subject.IssueCategoryMapper;
import com.kep.portal.repository.channel.ChannelEnvRepository;
import com.kep.portal.repository.subject.IssueCategoryRepository;
import com.kep.portal.service.branch.BranchChannelService;
import com.kep.portal.service.channel.ChannelEnvService;
import com.kep.portal.util.CommonUtils;
import com.kep.portal.util.SecurityUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Sort;
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
    private ChannelEnvRepository channelEnvRepository;

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

    @Deprecated
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
     * 상담 카테고리 저장 (신규) 20240926 volka
     *
     * @return
     */
    public void saveIssueCategories(Long channelId, List<IssueCategoryTreeDto> issueCategories) {

        Long branchId = securityUtils.getBranchId();

        ChannelEnv channelEnv = channelEnvRepository.findByChannelId(channelId).orElseThrow(() -> new BizException("not exist channel"));

        boolean channelMainBranchAdmin = channelEnv.getChannel().getBranchId().equals(branchId) && securityUtils.isAdmin();
        boolean headQuartersAdmin = securityUtils.isHeadQuarters() && securityUtils.isAdmin();
        Integer maxDepth = channelEnv.getMaxIssueCategoryDepth();
        //TODO :: IllegalArgumentException으로 수정
        if (isInitDepth(maxDepth)) throw new BizException("Issue Category is not initialized");

        Long memberId = securityUtils.getMemberId();

        Map<Long, IssueCategory> entityMap = issueCategoryRepository.findAllByChannelIdWithParent(channelId).stream()
                .collect(Collectors.toMap(IssueCategory::getId, entity -> entity));

        //입력 검증
        validIssueCategory(issueCategories, maxDepth, entityMap, channelMainBranchAdmin, headQuartersAdmin, channelId);

        recursiveSaveIssueCategory(memberId, channelId, null, issueCategories, entityMap, channelMainBranchAdmin, headQuartersAdmin);

    }

    //id가 있는 경우 parent가 바뀌진 않음
    private void modifyIssueCategory(Long memberId, Long channelId, IssueCategory record, IssueCategoryTreeDto dto) {
        record.setName(dto.getName());
        record.setEnabled(dto.getEnabled());
        record.setDepth(dto.getDepth());
        record.setChannelId(channelId);
        record.setSort(dto.getSort());
        record.setExposed(dto.getExposed());
        record.setModifier(memberId);
    }

    private void modifyIssueCategoryOnlyExposed(Long memberId, IssueCategory record, IssueCategoryTreeDto dto) {
        record.setExposed(dto.getExposed());
        record.setModifier(memberId);
    }

    //본사 관리자가 메인브랜치가 아닌 채널의 카테고리 전체오픈 여부 수정 시 검증
    private void compareIssueCategoryExcludeExposed(IssueCategory record, IssueCategoryTreeDto dto, Long channelId) {
        if (
                !record.getName().equals(dto.getName())
                || !record.getEnabled().equals(dto.getEnabled())
                || !record.getDepth().equals(dto.getDepth())
                || !record.getChannelId().equals(channelId)
                || !record.getSort().equals(dto.getSort())
        ) throw new BizException("only change exposed when request user is not main branch admin and headquarters admin");
    }

    /**
     * IssueCategory 입력 검증
     *
     * TODO :: BizException -> IllegalArgumentException으로 수정 volka -> 현재 예외처리 핸들러에서 IllegalArgumentException의 경우 예외 메시지 응답처리 해주는 부분이 없음 -> 프론트 연계 테스트를 위해 BizException으로 임시 변경
     * @param issueCategories
     * @param maxDepth
     * @param recordMap
     */
    private void validIssueCategory(List<IssueCategoryTreeDto> issueCategories, int maxDepth, Map<Long, IssueCategory> recordMap, boolean channelMainBranchAdmin, boolean headQuartersAdmin, Long channelId) {

        List<IssueCategoryTreeDto> flattenTargets = new ArrayList<>(issueCategories);
        List<IssueCategoryTreeDto> nextFlattenTargets = new ArrayList<>();
        int totalCount = 0;

        if (!headQuartersAdmin && !channelMainBranchAdmin) throw new BizException("saving category only main branch admin"); //본사 관리자가 아닐 경우 본인 소속 브랜치가 채널의 메인 브랜치일 경우에만 카테고리 저장 가능
        if (isDuplicatedSort(flattenTargets)) throw new BizException("not duplicated sort"); //정렬 중복

        for (int i = 1; i <= maxDepth; i++) {

            totalCount += flattenTargets.size();
            IssueCategory record = null;

            for (IssueCategoryTreeDto dto : flattenTargets) {
                if (dto.getDepth() != i) throw new BizException("not correct depth variables"); //valid depth
                if (dto.getIssueCategoryId() != null) {
                    record = recordMap.get(dto.getIssueCategoryId());
                    if (record == null) throw new BizException("issue category is not found");
                    if (!channelMainBranchAdmin && headQuartersAdmin) compareIssueCategoryExcludeExposed(record, dto, channelId); //본사 브랜치 관리자가 타 브랜치의 상담 카테고리 전체오픈여부만 수정가능
                }

                if (!headQuartersAdmin && !dto.getExposed()) throw new BizException("exposed true is only headQuarters Admin"); //본사 관리자가 아닐 때 카테고리 전체오픈 불가

                //최하위 노드, 최대 뎁스 1일 때 제외
                if (i < maxDepth && maxDepth > 1) {
                    validIssueCategoryChildren(dto);
                    nextFlattenTargets.addAll(dto.getChildren());
                }
            }

            if (!nextFlattenTargets.isEmpty()) {
                flattenTargets.clear();
                flattenTargets.addAll(nextFlattenTargets);
                nextFlattenTargets.clear();
            }
        }

        //카테고리 삭제 요건은 없기 때문에 항상 데이터 개수는 입력 >= 레코드
        if (totalCount < recordMap.keySet().size()) throw new BizException("not enough categories size");
    }

    /**
     * 이슈 카테고리 하위노드 검증
     * @param parent
     */
    private void validIssueCategoryChildren(IssueCategoryTreeDto parent) {
        //설정 뎁스까지 카테고리 못채운경우
        if (parent.getChildren() == null || parent.getChildren().isEmpty()) throw new BizException("must having children");

        Boolean parentEnabled = parent.getEnabled();
        Boolean parentExposed = parent.getExposed();
        boolean validEnabledAndExposed = parent.getChildren().stream()
                .anyMatch(
                        child ->
                                (parentEnabled.equals(false) && child.getEnabled().equals(true))
                                || (parentExposed.equals(false) && child.getExposed().equals(true))
                );
        if (validEnabledAndExposed) throw new BizException("can not enabled or exposed true when parent false");
        if (isDuplicatedSort(parent.getChildren())) throw new BizException("not duplicated sort");
    }

    /**
     * 하위 사용여부
     *
     * 정렬 중복
     * @param issueCategories
     * @return
     */
    private boolean isDuplicatedSort(List<IssueCategoryTreeDto> issueCategories) {
        int size = issueCategories.stream()
                .map(IssueCategoryTreeDto::getSort)
                .collect(Collectors.toSet()).size();

        return size != issueCategories.size();
    }


    /**
     * IssueCategory 트리 저장/수정 재귀 volka
     * @param memberId
     * @param channelId
     * @param parent
     * @param dtoChildren
     * @param recordMap
     */
    private void recursiveSaveIssueCategory(Long memberId, Long channelId, IssueCategory parent, List<IssueCategoryTreeDto> dtoChildren, Map<Long, IssueCategory> recordMap, boolean channelMainBranchAdmin, boolean headQuartersAdmin) {
        IssueCategory record = null;

        for (IssueCategoryTreeDto child : dtoChildren) {
            if (child.getIssueCategoryId() == null) {
                record = issueCategoryRepository.save(child.toEntity(memberId, channelId, parent));
            } else {
                record = recordMap.get(child.getIssueCategoryId());
                if (!channelMainBranchAdmin && headQuartersAdmin) {
                    modifyIssueCategoryOnlyExposed(memberId, record, child); //메인브랜치 관리자가 아닌 본사 관리자가 전체오픈여부 수정 시
                } else {
                    modifyIssueCategory(memberId, channelId, record, child);
                }
            }

            if (!ObjectUtils.isEmpty(child.getChildren())) {
                recursiveSaveIssueCategory(memberId, channelId, record, child.getChildren(), recordMap, channelMainBranchAdmin, headQuartersAdmin);
            }
        }
    }

    /**
     * 상담 카테고리 트리 생성 volka
     * @param issueCategories
     * @param maxDepth
     * @return
     */
    private List<IssueCategoryTreeDto> createCategoryTree(List<IssueCategory> issueCategories, Integer maxDepth) {

        List<IssueCategoryTreeDto> dtoList = issueCategories.stream()
                .map(IssueCategoryTreeDto::of)
                .collect(Collectors.toList());

        List<IssueCategoryTreeDto> result = null;

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
                        categoryTreeDto.getChildren().add(issueCategoryTreeDto);
                    }
                }
            }

            dtoMap.values().stream()
                    .filter(item -> !item.getChildren().isEmpty())
                    .forEach(dto -> {
                        dto.getChildren().sort(Comparator.comparingInt(IssueCategoryTreeDto::getSort));
                    });

            result = depthMap.get(1); //대분류

        } else {
            result = dtoList;
        }

        result.sort(Comparator.comparingInt(IssueCategoryTreeDto::getSort)); //대분류 sort

        return result;
    }

    /**
     * 채널별 상담 카테고리 전체 조회 (Tree구조) volka
     *
     * maxDepth == 0 일 경우(미설정시) 카테고리가 없는게 정상(추가불가) (현재 20240924 기획 기준)
     *
     * @param channelId
     * @return
     */
    public IssueCategoryResponse getAllIssueCategories(Long channelId, boolean forManagement) {
        ChannelEnv channelEnv = channelEnvRepository.findByChannelId(channelId).orElseThrow(() -> new BizException("not exist channel"));
        if (isInitDepth(channelEnv.getMaxIssueCategoryDepth())) return null;

        List<IssueCategory> issueCategories = null;

        if (forManagement) {
            issueCategories = issueCategoryRepository.findAllByChannelIdWithParent(channelId);
        } else {
            //FIXME :: 임시로 컨트롤러에서 forManagement 변수로 분기해서 사용. 권한 정리되면 분리 필요
            issueCategories = issueCategoryRepository.findAllByChannelIdAndEnabledIsTrueAndExposedIsTrueWithParent(channelId);
        }
        if (issueCategories.isEmpty()) return null;

        return new IssueCategoryResponse(channelEnv.getMaxIssueCategoryDepth(), createCategoryTree(issueCategories, channelEnv.getMaxIssueCategoryDepth()));
    }



    public IssueCategoryTreeDto getIssueCategoryTreeByLowestOne(Long channelId, Long issueCategoryId) {
        ChannelEnv channelEnv = channelEnvRepository.findByChannelId(channelId).orElseThrow(() -> new BizException("not exist channel"));
        if (isInitDepth(channelEnv.getMaxIssueCategoryDepth())) return null;

        List<IssueCategory> issueCategories = issueCategoryRepository.findAllByChannelIdWithParent(channelId);
        if (issueCategories.isEmpty()) return null;

        Map<Long, IssueCategory> entityMap = issueCategories.stream().collect(Collectors.toMap(IssueCategory::getId, item -> item));


        return createCategoryTreeByLowest(IssueCategoryTreeDto.of(entityMap.get(issueCategoryId)), entityMap);
    }


    private IssueCategoryTreeDto createCategoryTreeByLowest(IssueCategoryTreeDto target, Map<Long, IssueCategory> entityMap) {

        if (target.getParentId() != null) {
            IssueCategoryTreeDto parent = IssueCategoryTreeDto.of(entityMap.get(target.getParentId()));
            parent.getChildren().add(target);

            target = createCategoryTreeByLowest(parent, entityMap);
        }

        return target;
    }


    /**
     * 최하위 뎁스 카테고리 아이디 목록 조회 (입력 카테고리가 최하위 뎁스일 경우 입력 반환 / 아닐경우 하위 뎁스 카테고리 목록 반환 [본인 포함 X -> 이 경우 당연히 입력은 최하위가 아니니까])
     * @param channelId
     * @param issueCategoryId
     * @return
     */
    public List<Long> getLowestCategoriesById(Long channelId, Long issueCategoryId) {
        IssueCategory target = issueCategoryRepository.findById(issueCategoryId).orElseThrow(() -> new BizException("not exist issue category"));
        channelId = channelId == null ? target.getChannelId() : channelId;
        Integer categoryMaxDepth = getCategoryMaxDepth(channelId);
        if (isInitDepth(categoryMaxDepth)) throw new BizException("not init issue category depth");

        List<IssueCategory> issueCategories = issueCategoryRepository.findAllByChannelIdWithParent(channelId);
        List<IssueCategory> lowestDepthCategories = null;

        if (categoryMaxDepth > 1 && target.getDepth() < categoryMaxDepth) {
            Map<Integer, List<IssueCategory>> depthMap = issueCategories.stream().collect(Collectors.groupingBy(IssueCategory::getDepth));
            lowestDepthCategories = recursiveLowerCategories(target.getDepth() + 1, Arrays.asList(target), depthMap);
        } else {
            lowestDepthCategories = Arrays.asList(target);
        }

        return lowestDepthCategories.stream().map(IssueCategory::getId).collect(Collectors.toList());
    }

    public List<IssueCategory> recursiveLowerCategories(Integer depth, List<IssueCategory> parentList, Map<Integer, List<IssueCategory>> depthMap) {
        if (!depthMap.containsKey(depth)) return parentList;
        List<IssueCategory> lowerDepthCategories = depthMap.get(depth).stream()
                .filter(category -> parentList.contains(category.getParent()))
                .collect(Collectors.toList());

        if (!depthMap.containsKey(depth + 1)) {
            return lowerDepthCategories;
        } else {
            return recursiveLowerCategories(depth + 1, lowerDepthCategories, depthMap);
        }
    }


    private boolean isInitDepth(@NotNull Integer maxDepth) {
        return maxDepth.equals(0);
    }

    public IssueCategoryDto getIssueCategorDtoUseChannelId(Long channelId){
        IssueCategory issueCategory = this.getIssueCategorUseChannelId(channelId);
        return this.issueCategoryEntityToDto(issueCategory);
    }

    public IssueCategory getIssueCategorUseChannelId(Long channelId){
        return issueCategoryRepository.findTopByChannelIdOrderByDepthDescParentIdAscIdAsc(channelId);
    }

    private IssueCategoryDto issueCategoryEntityToDto(IssueCategory issueCategory){
        IssueCategoryDto issueCategoryDto = new IssueCategoryDto();
        if(Objects.isNull(issueCategory)){
            return issueCategoryDto;
        }
        issueCategoryDto.setId(issueCategory.getId());
        issueCategoryDto.setName(issueCategory.getName());
        issueCategoryDto.setBranchId(issueCategoryDto.getBranchId());
        issueCategoryDto.setDepth(issueCategory.getDepth());
        issueCategoryDto.setParent(issueCategory.getParent() == null ? null : this.issueCategoryEntityIssueCategoryBasicDto(issueCategory.getParent()));
        issueCategoryDto.setExposed(issueCategory.getExposed());
        issueCategoryDto.setBnkCode(issueCategory.getBnkCode());
        return issueCategoryDto;
    }

    private IssueCategoryBasicDto issueCategoryEntityIssueCategoryBasicDto(IssueCategory issueCategory){
        IssueCategoryBasicDto issueCategoryBasicDto = new IssueCategoryBasicDto();
        issueCategoryBasicDto.setId(issueCategory.getId());
        issueCategoryBasicDto.setName(issueCategory.getName());
        issueCategoryBasicDto.setParentId(Objects.nonNull(issueCategory.getParent()) ?  issueCategory.getParent().getId() : null );
        issueCategoryBasicDto.setDepth(issueCategory.getDepth());
        issueCategoryBasicDto.setEnabled(issueCategory.getEnabled());
        issueCategoryBasicDto.setExposed(issueCategory.getExposed());
        issueCategoryBasicDto.setBnkCode(issueCategory.getBnkCode());
        return issueCategoryBasicDto;
    }
}
