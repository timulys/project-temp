package com.kep.portal.service.guide;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.kep.core.model.dto.guide.GuideBlockDto;
import com.kep.core.model.dto.guide.GuideDto;
import com.kep.core.model.dto.guide.GuidePayload;
import com.kep.core.model.dto.guide.GuideType;
import com.kep.core.model.dto.issue.payload.IssuePayload;
import com.kep.core.model.exception.BizException;
import com.kep.portal.model.dto.guide.GuideSearchDto;
import com.kep.portal.model.dto.guide.GuideSearchResponseDto;
import com.kep.portal.model.entity.branch.Branch;
import com.kep.portal.model.entity.guide.*;
import com.kep.portal.model.entity.member.Member;
import com.kep.portal.model.entity.member.MemberMapper;
import com.kep.portal.model.entity.member.MemberRole;
import com.kep.portal.model.entity.privilege.Level;
import com.kep.portal.model.entity.team.Team;
import com.kep.portal.model.entity.team.TeamMapper;
import com.kep.portal.repository.branch.BranchRepository;
import com.kep.portal.repository.guide.GuideBlockRepository;
import com.kep.portal.repository.guide.GuideRepository;
import com.kep.portal.repository.member.MemberRepository;
import com.kep.portal.repository.member.MemberRoleRepository;
import com.kep.portal.repository.privilege.RoleRepository;
import com.kep.portal.repository.team.TeamRepository;
import com.kep.portal.util.CommonUtils;
import com.kep.portal.util.SecurityUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FilenameUtils;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.openxml4j.opc.OPCPackage;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import javax.transaction.Transactional;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Transactional
@Slf4j
public class GuideService {
    @Resource
    private ObjectMapper objectMapper;

    @Resource
    private GuideMapper guideMapper;

    @Resource
    private GuideRepository guideRepository;

    @Resource
    private BranchRepository branchRepository;

    @Resource
    private GuideBlockRepository guideBlockRepository;

    @Resource
    private MemberRepository memberRepository;
    @Resource
    private MemberMapper memberMapper;

    @Resource
    private GuideCategoryService guideCategoryService;

    @Resource
    private TeamRepository teamRepository;
    @Resource
    private TeamMapper teamMapper;

    @Resource
    private GuideBlockMapper guideBlockMapper;
    @Resource
    private SecurityUtils securityUtils;
    @Resource
    private MemberRoleRepository memberRoleRepository;
    @Resource
    private RoleRepository roleRepository;

    //가이드 검색(SB-CP-T03)
    public Page<GuideDto> getAllSubCategory(Pageable pageable, Long categoryId) throws JsonProcessingException {
        List<Long> childrenIds = null;

        Long branchId = securityUtils.getBranchId();
        Assert.notNull(branchId, "Not Found Branch");

        Long teamId = securityUtils.getTeamId();
        Assert.notNull(teamId, "Not Found Team");

        // categoryId가 없으면 3depth 카테고리를 전부 가져옴
        // categoryId가 있으면 하위 카테고리 까지 전부 가져옴
        if (categoryId == null) {
            childrenIds = guideCategoryService.getAllDepthCategory(branchId);
        } else {
            childrenIds = guideCategoryService.getAllSubCategory(categoryId, branchId);
        }

        // childrenIds에 속한 가이드를 모두 가져옴
        Page<Guide> guides = guideRepository.findByGuideCategoryIdIn(childrenIds, teamId, branchId, pageable);

        List<GuideDto> guideDtos = new ArrayList<>();

        // 가이드 블록, 생성자, 수정자 세팅
        settingGuideBlock(guides.getContent(), guideDtos);

        orderGuideBlock(guideDtos);

        Page<GuideDto> page = new PageImpl<>(guideDtos, guides.getPageable(), guides.getTotalElements());
        return page;
    }


    private GuideDto convertGuideToDto(Guide guide) throws Exception {

        GuideDto guideDto = guideMapper.map(guide);
        List<Long> blockIds = objectMapper.readValue(guide.getBlockIds().toString(), new TypeReference<List<Long>>() {
        });

        List<GuideBlock> guideBlockList = guideBlockRepository.findAllByIdIn(blockIds);
        List<GuideBlockDto> guideBlockDtos = guideBlockMapper.map(guideBlockList);

        guideDto.setBlocks(guideBlockDtos);

        if (guide.getTeamId() != null) {
            Team team = teamRepository.findById(guide.getTeamId()).orElse(null);
            guideDto.setTeam(teamMapper.map(team));
        }
        if (guide.getCreatorId() != null) {
            Member creator = memberRepository.findById(guide.getCreatorId()).orElse(null);
            guideDto.setCreator(memberMapper.map(creator));
        }
        if (guide.getModifierId() != null) {
            Member modifier = memberRepository.findById(guide.getModifierId()).orElse(null);
            guideDto.setModifier(memberMapper.map(modifier));
        }

        return guideDto;

    }

    private void settingGuideBlock(List<Guide> guides, List<GuideDto> guideDtos) {
        try {
            for (Guide guide : guides) {
//                GuideDto guideDto = guideMapper.map(guide);
//                List<Long> blockIds = objectMapper.readValue(guide.getBlockIds().toString(), new TypeReference<List<Long>>() {
//                });
//
//                List<GuideBlock> guideBlockList = guideBlockRepository.findAllByIdIn(blockIds);
//                List<GuideBlockDto> guideBlockDtos = guideBlockMapper.map(guideBlockList);
//
//                guideDto.setBlocks(guideBlockDtos);
//
//                if (guide.getTeamId() != null) {
//                    Team team = teamRepository.findById(guide.getTeamId()).orElse(null);
//                    guideDto.setTeam(teamMapper.map(team));
//                }
//                if (guide.getCreatorId() != null) {
//                    Member creator = memberRepository.findById(guide.getCreatorId()).orElse(null);
//                    guideDto.setCreator(memberMapper.map(creator));
//                }
//                if (guide.getModifierId() != null) {
//                    Member modifier = memberRepository.findById(guide.getModifierId()).orElse(null);
//                    guideDto.setModifier(memberMapper.map(modifier));
//                }

                guideDtos.add(convertGuideToDto(guide));
            }
        } catch (Exception e) {
            log.error(e.getLocalizedMessage());
        }
    }

    /**
     * TODO 유효성 검사 (최대 자리수 요건 정의 필요, 임시 저장 시 검증 적용 필요) 20240717 volka
     *
     * @param guidePayload
     * @param enabled
     * @return
     * @throws Exception
     */
    public GuideDto store(GuidePayload guidePayload, boolean enabled) throws Exception {


        Assert.notNull(guidePayload.getCategoryId(), "category_id must not be null");
        Assert.notNull(guidePayload.getName(), "name must not be null");
        Assert.notNull(guidePayload.getType(), "type must not be null");
        Assert.isTrue(guidePayload.getContents().size() <= 10, "guide blocks are under 10 size");

        if (enabled) {
            Assert.notEmpty(guidePayload.getContents(), "content must not be empty");
        }

        Long branchId = guidePayload.getBranchId() == null ? securityUtils.getBranchId() : guidePayload.getBranchId();
        Team team = null;

        Branch branch = branchRepository.findById(branchId)
                .orElse(null);
        Assert.notNull(branch, "Not Found Branch");

        if (guidePayload.getTeamId() != null || securityUtils.getTeamId() != null) {
            team = teamRepository.findById(guidePayload.getTeamId() == null ? securityUtils.getTeamId() : guidePayload.getTeamId())
                    .orElse(null);
            Assert.notNull(team, "Not Found Team");
        }

        Member member = memberRepository.findById(securityUtils.getMemberId()).orElse(null);
        Assert.notNull(member, "Not Found Member");

        GuideCategory category = guideCategoryService.findById(guidePayload.getCategoryId());
        Assert.notNull(category, "Not Found GuideCategory");

        Guide guide = null;

        // id가 없으면 생성
        if (guidePayload.getId() == null) {
            List<Guide> guideList = guideRepository.findAllByName(guidePayload.getName());
            if (guideList.size() > 0) {
                throw new IllegalArgumentException("Already guide name");
            }

            guide = Guide.builder()
                    .name(guidePayload.getName())
                    .teamId(team == null ? null : team.getId())
                    .branch(branch)
                    .isBranchOpen(guidePayload.getIsBranchOpen())
                    .isTeamOpen(guidePayload.getIsTeamOpen())
                    .creatorId(member.getId())
                    .modifierId(member.getId())
                    .guideCategory(category)
                    .blockIds(new ArrayList<>())
                    .type(guidePayload.getType())
                    .build();

            if (securityUtils.hasRole(Level.ROLE_ADMIN)) {
                guide.setIsTeamOpen(true);
            }

            if (category.getIsOpen()) {
                guide.setIsBranchOpen(true);
                guide.setIsTeamOpen(true);
            }

            guide = guideRepository.save(guide);
            if (!guidePayload.getContents().isEmpty()) createBlock(guidePayload, guide);
        }
        // id가 있으면 수정
        else {
            List<Guide> guideList = guideRepository.findAllByName(guidePayload.getName());
            if (guideList.size() > 1) {
                throw new IllegalArgumentException("Already guide name");
            }

            guide = guideRepository.findById(guidePayload.getId()).orElse(null);
            Assert.notNull(guide, "Not Found Guide");


            // 수정시 자신이나, 같은 브랜치의 관리자만이 수정 가능

            Member guideCreator = memberRepository.findById(guide.getCreatorId()).orElse(null);

            Assert.isTrue(guide.getCreatorId().equals(securityUtils.getMemberId()) ||
                    (Objects.requireNonNull(guideCreator).getBranchId().equals(securityUtils.getBranchId()) && securityUtils.hasRole(Level.ROLE_ADMIN)), "No Authority");
            CommonUtils.copyNotEmptyProperties(guidePayload, guide);


            guide.setModifierId(member.getId());

            List<Long> originBlockIds = objectMapper.readValue(guide.getBlockIds().toString(), new TypeReference<List<Long>>() {
            });
            guideBlockRepository.deleteAllById(originBlockIds);
            if (!guidePayload.getContents().isEmpty()) createBlock(guidePayload, guide);


            if (!guide.getBranch().getId().equals(branch.getId())) {
                guide.setBranch(branch);
            }

            if (team != null) {
                if (!guide.getTeamId().equals(team.getId())) {
                    guide.setTeamId(team.getId());
                }
            }

            if (securityUtils.hasRole(Level.ROLE_ADMIN)) {
                guide.setIsTeamOpen(true);
            }

            if (!guide.getGuideCategory().getId().equals(category.getId())) {
                guide.setGuideCategory(category);
                if (category.getIsOpen()) {
                    guide.setIsBranchOpen(true);
                    guide.setIsTeamOpen(true);
                }
            }
        }
        guide.setEnabled(enabled);
        return guideMapper.map(guide);
    }


    private void validRequiredMap(Map<Integer, Integer> requiredMap) {
        Set<Integer> keySet = requiredMap.keySet();
        Collection<Integer> required = requiredMap.values();

        //중복 체크
        if (!required.stream().filter(v -> Collections.frequency(required, v) > 1).collect(Collectors.toSet()).isEmpty()) {
            throw new IllegalArgumentException("requiredIds are not duplicated");
        }

        Integer val = null;

        for (Integer idx : keySet) {
            val = requiredMap.get(idx);
            if (val.equals(idx)) throw new IllegalArgumentException("requiredIds must be unique");
            //서로 참조
            if (keySet.contains(val) && requiredMap.get(val).equals(idx)) throw new IllegalArgumentException("requiredIds not reference each other");
        }
    }

    // guidePayload속 content를 guideBlock으로 데이터 저장
    private void createBlock(GuidePayload guidePayload, Guide guide) throws JsonProcessingException {

        guide.getBlockIds().clear();
        Map<Integer, Integer> requireMap = new HashMap<>();
        List<GuideBlock> requireGuideBlockList = new ArrayList<>();

        Integer idx = 0;
        for (GuidePayload.Content content : guidePayload.getContents()) {
            if (content.getRequireId() != null) {
                requireMap.put(idx, content.getRequireId().intValue());
            }

            idx++;
        }

        validRequiredMap(requireMap);

//        int currentIndex = 0;

        for (GuidePayload.Content content : guidePayload.getContents()) {
            if (content.getPayload().getChapters().isEmpty()) {
                List<IssuePayload.Chapter> chapterList = new ArrayList<>();
                IssuePayload.Chapter chapter = new IssuePayload.Chapter();

                List<IssuePayload.Section> sectionList = new ArrayList<>();
                chapter.setSections(sectionList);

                chapterList.add(chapter);
                content.getPayload().setChapters(chapterList);
            }
            String payload = objectMapper.writeValueAsString(content.getPayload()).replace("[ { } ]", "[ ]");


            GuideBlock guideBlock = GuideBlock.builder()
                    .blockName(content.getBlockName())
                    .payload(payload)
                    .build();

//            if (content.getRequireId() != null) {
//                requireMap.put(currentIndex, content.getRequireId().intValue());
//            }

            StringBuilder messageCondition = new StringBuilder();
            StringBuilder fileCondition = new StringBuilder();

            final String separator = "|";
            for (IssuePayload.Chapter chapter : content.getPayload().getChapters()) {

                if (chapter.getSections().size() == 1) {
                    IssuePayload.Section section = chapter.getSections().get(0);
                    // 메시지 검색조건 추가
                    if (section.getType() == IssuePayload.SectionType.text) {
                        messageCondition.append(section.getData()).append(separator);
                    }
                } else if (chapter.getSections().size() > 1) {
                    IssuePayload.Section nextSection = chapter.getSections().get(1);
                    if (nextSection.getType().equals(IssuePayload.SectionType.action)) {
                        // 파일 검색 조건 추가
                        if ("file".equals(nextSection.getExtra())) {
                            fileCondition.append(chapter.getSections().get(0).getData()).append(separator);

                        }
                        // 메시지 검색조건 추가
                        else {
                            messageCondition.append(chapter.getSections().get(0).getData()).append(separator);
                        }
                    }
                }
            }

            guideBlock.setContentCount(content.getPayload().getChapters().size());
            guideBlock.setMessageCondition(messageCondition.toString());
            guideBlock.setFileCondition(fileCondition.toString());
            guideBlock.setGuideId(guide.getId());
            GuideBlock save = guideBlockRepository.save(guideBlock);
            log.info("save = {}", objectMapper.writeValueAsString(save));

            guide.getBlockIds().add(guideBlock.getId());
            requireGuideBlockList.add(guideBlock);

//            currentIndex++;
        }

        requireMap.forEach((k, v) -> {
            log.info("entry = {},{}", k, v);
            requireGuideBlockList.get(k).setRequireId(requireGuideBlockList.get(v).getId());
        });
    }

    //가이드 검색(SB-CP-T07)
    public GuideSearchResponseDto getSearchDto(GuideSearchDto searchDto) throws Exception {

        Assert.notNull(searchDto.getKeyword(), "Keywords are required.");

        Long teamId = securityUtils.getTeamId();
        Long branchId = securityUtils.getBranchId();
        searchDto.setTeamId(teamId);
        searchDto.setBranchId(branchId);

        List<Long> childrenIds = null;

        // categoryId가 없으면 3depth 카테고리를 전부 가져옴
        // categoryId가 있으면 하위 카테고리 까지 전부 가져옴
        if (searchDto.getCategoryId() == null) {
            childrenIds = guideCategoryService.getAllDepthCategory(searchDto.getBranchId());
        } else {
            childrenIds = guideCategoryService.getAllSubCategory(searchDto.getCategoryId(), searchDto.getBranchId());
        }

        // 카테고리에 속한 가이드중 이름에 keyword가 포함된 가이드
        List<GuideDto> nameSearchDtos = new ArrayList<>();
        settingGuideBlock(guideRepository.findByNameSearch(searchDto, childrenIds), nameSearchDtos);
        Long nameSearchCount = guideRepository.countByNameSearch(searchDto, childrenIds);

        // 카테고리에 속한 가이드중 message에 keyword가 포함된 가이드
        List<GuideDto> messageSearchDtos = new ArrayList<>();
        settingGuideBlock(guideRepository.findByMessageSearch(searchDto, childrenIds), messageSearchDtos);
        Long messageSearchCount = guideRepository.countByMessageSearch(searchDto, childrenIds);


        // 카테고리에 속한 가이드중 file에 keyword가 포함된 가이드
        List<GuideDto> fileSearchDtos = new ArrayList<>();
        settingGuideBlock(guideRepository.findByFileSearch(searchDto, childrenIds), fileSearchDtos);
        Long fileSearchCount = guideRepository.countByFileSearch(searchDto, childrenIds);


        return GuideSearchResponseDto.builder()
                .nameSearch(orderGuideBlock(nameSearchDtos))
                .nameCount(nameSearchCount)
                .messageSearch(orderGuideBlock(messageSearchDtos))
                .messageCount(messageSearchCount)
                .fileSearch(orderGuideBlock(fileSearchDtos))
                .fileCount(fileSearchCount)
                .build();
    }


    /**
     * 가이드 단건 상세 조회
     * @param guideId
     * @return
     * @throws Exception
     */
    public GuideDto getGuide(Long guideId) throws Exception {
        Guide guide = null;

        if (securityUtils.hasRole(Level.ROLE_ADMIN)) {
            guide = guideRepository.findById(guideId).orElseThrow(() -> new BizException("Guide Not Found"));
        } else {
            guide = guideRepository.findByIdForManager(guideId, securityUtils.getBranchId(), securityUtils.getTeamId())
                    .orElseThrow(() -> new BizException("Guide Not Found"));
        }

        return convertGuideToDto(guide);
    }

    // 가이드 검색(SB-CA-006)
    public Page<GuideDto> getSearchGuide(GuideSearchDto searchDto, Pageable pageable) throws JsonProcessingException {
        if (searchDto.getKeyword() != null)
            Assert.isTrue(!searchDto.getKeyword().isEmpty(), "Empty Keyword");

        Branch branch = branchRepository.findById(securityUtils.getBranchId()).orElse(null);
        Assert.notNull(branch, "Branch is null");

        List<Long> childrenIds = null;
        Page<Guide> entities = null;
        List<GuideDto> guideDtoList = new ArrayList<>();


        // 본사일경우 모든 카테고리와 모든 브랜치
        if(branch.getHeadQuarters() && securityUtils.hasRole(Level.ROLE_ADMIN)){
            // categoryId가 없으면 3depth 카테고리를 전부 가져옴
            // categoryId가 있으면 하위 카테고리 까지 전부 가져옴
            if (searchDto.getCategoryId() == null) {
                childrenIds = guideCategoryService.getHeadAllDepthCategory();
            } else {
                childrenIds = guideCategoryService.getHeadAllSubCategory(searchDto.getCategoryId());
            }
            entities = guideRepository.findByHeadGuideSearchForAdmin(childrenIds, searchDto, pageable);
        }else{
            Long branchId = securityUtils.getBranchId();
            Long teamId = securityUtils.getTeamId();
            searchDto.setBranchId(branchId);
            if (securityUtils.getTeamId() != null)
                searchDto.setTeamId(teamId);

            // categoryId가 없으면 3depth 카테고리를 전부 가져옴
            // categoryId가 있으면 하위 카테고리 까지 전부 가져옴
            if (searchDto.getCategoryId() == null) {
                childrenIds = guideCategoryService.getAllDepthCategory(searchDto.getBranchId());
            } else {
                childrenIds = guideCategoryService.getAllSubCategory(searchDto.getCategoryId(), searchDto.getBranchId());
            }

            // 역할이 ADMIN이면 브랜치 전체 조회
            // 역할이 ADMIN이 아니면 브랜치 + 팀 조회
            if (securityUtils.hasRole(Level.ROLE_ADMIN)) {
                entities = guideRepository.findByGuideSearchForAdmin(childrenIds, searchDto, pageable);
            } else {
                entities = guideRepository.findByGuideSearchForManager(childrenIds, searchDto, pageable);
            }
        }










        settingGuideBlock(entities.getContent(), guideDtoList);
        orderGuideBlock(guideDtoList);

        return new PageImpl<>(guideDtoList, entities.getPageable(), entities.getTotalElements());

    }

    /**
     * 추후 엑셀 업로드
     *
     * @param file
     * @throws IOException
     * @throws InvalidFormatException
     */
    public void saveExcelGuide(MultipartFile file) throws IOException, InvalidFormatException {


        Assert.notNull(file, "File must not be null");

        String extension = FilenameUtils.getExtension(file.getOriginalFilename());
        if (!extension.equals("xlsx")) {
            throw new IllegalArgumentException("Only xlsx file");
        }

        OPCPackage opcPackage = OPCPackage.open(file.getInputStream());
        Workbook workbook = new XSSFWorkbook(opcPackage);

        Sheet guideSheet = workbook.getSheetAt(0);

        GuidePayload guidePayload = new GuidePayload();

        for (int i = 2; i < guideSheet.getPhysicalNumberOfRows(); i++) {
            Row guideRow = guideSheet.getRow(i);

            long guideId = (long) guideRow.getCell(0).getNumericCellValue();

            String guideName = guideRow.getCell(1).getStringCellValue();
            boolean guideEnabled = guideRow.getCell(2).getBooleanCellValue();
            boolean guideBranchOpen = guideRow.getCell(3).getBooleanCellValue();
            boolean guideTeamOpen = guideRow.getCell(4).getBooleanCellValue();
            Long guideTeamId = guideRow.getCell(5) != null ? (long) guideRow.getCell(5).getNumericCellValue() : null;
            Long guideBranchId = guideRow.getCell(6) != null ? (long) guideRow.getCell(6).getNumericCellValue() : null;
            GuideType guideType = Enum.valueOf(GuideType.class, guideRow.getCell(7).getStringCellValue());
            long guideCategoryId = (long) guideRow.getCell(8).getNumericCellValue();

            guidePayload.setName(guideName);
            guidePayload.setCategoryId(guideCategoryId);
            guidePayload.setType(guideType);
            guidePayload.setIsBranchOpen(guideBranchOpen);
            guidePayload.setIsTeamOpen(guideTeamOpen);
            guidePayload.setBranchId(guideBranchId);
            guidePayload.setTeamId(guideTeamId);

            List<GuidePayload.Content> contents = new ArrayList<>();
            guidePayload.setContents(contents);
            Sheet blockSheet = workbook.getSheetAt(1);

            for (int j = 2; j < blockSheet.getPhysicalNumberOfRows(); j++) {
                Row blockRow = blockSheet.getRow(j);
                GuidePayload.Content content = new GuidePayload.Content();
                if (blockRow == null || blockRow.getCell(0) == null) {
                    break;
                }
                long guidePk = (long) blockRow.getCell(0).getNumericCellValue();

                if (guidePk < guideId)
                    continue;
                else if (guidePk > guideId)
                    break;

                String blockName = blockRow.getCell(2) != null ? blockRow.getCell(2).getStringCellValue() : null;
                Long requireId = blockRow.getCell(3) != null ? (long) blockRow.getCell(3).getNumericCellValue() : null;

                content.setBlockName(blockName);
                content.setRequireId(requireId);

                IssuePayload issuePayload = new IssuePayload();
                List<IssuePayload.Chapter> chapters = new ArrayList<>();


                int blockIndex = 4;
                int blockStep = 14;
                int buttonStep = 2;

                while (blockRow.getCell(blockIndex) != null) {
                    List<IssuePayload.Section> sections = new ArrayList<>();
                    IssuePayload.Chapter chapter = new IssuePayload.Chapter();

                    String blockType = blockRow.getCell(blockIndex).getStringCellValue();


                    String blockContent = blockRow.getCell(blockIndex + 1) != null ? blockRow.getCell(blockIndex + 1).getStringCellValue() : null;
                    switch (blockType) {
                        case "text":
                            sections.add(IssuePayload.Section.builder()
                                    .type(IssuePayload.SectionType.text)
                                    .data(blockContent)
                                    .build());
                            break;
                        case "text_button":
                            sections.add(IssuePayload.Section.builder()
                                    .type(IssuePayload.SectionType.text)
                                    .data(blockContent)
                                    .build());

                            List<IssuePayload.Action> actions = new ArrayList<>();
                            int innerButtonIndex = blockIndex + buttonStep;
                            while (blockRow.getCell(innerButtonIndex) != null) {
                                String buttonName = blockRow.getCell(innerButtonIndex).getStringCellValue();
                                String buttonUrl = blockRow.getCell(innerButtonIndex + 1).getStringCellValue();
                                actions.add(IssuePayload.Action.builder()
                                        .type(IssuePayload.ActionType.link)
                                        .name(buttonName)
                                        .data(buttonUrl)
                                        .deviceType(IssuePayload.DeviceType.all)
                                        .build());

                                innerButtonIndex += buttonStep;
                            }
                            sections.add(IssuePayload.Section.builder()
                                    .type(IssuePayload.SectionType.action)
                                    .actions(actions)
                                    .build());
                            break;
                        case "text_file":
                            String fileName = blockRow.getCell(blockIndex + 12).getStringCellValue();
                            String fileUrl = blockRow.getCell(blockIndex + 13).getStringCellValue();

                            sections.add(IssuePayload.Section.builder()
                                    .type(IssuePayload.SectionType.text)
                                    .data(fileName)
                                    .build());

                            List<IssuePayload.Action> fileActions = new ArrayList<>();
                            fileActions.add(IssuePayload.Action.builder()
                                    .type(IssuePayload.ActionType.link)
                                    .name("파일 다운로드")
                                    .data(fileUrl)
                                    .deviceType(IssuePayload.DeviceType.all)
                                    .build());
                            sections.add(IssuePayload.Section.builder()
                                    .extra("file")
                                    .type(IssuePayload.SectionType.action)
                                    .actions(fileActions)
                                    .build());
                            break;
                    }
                    chapter.setSections(sections);
                    chapters.add(chapter);
                    blockIndex += blockStep;
                }
                issuePayload.setChapters(chapters);
                content.setPayload(issuePayload);
                contents.add(content);
            }

            List<Guide> guideList = guideRepository.findAllByName(guidePayload.getName());
            if (guideList.size() > 0) {
                log.error("ALREADY GUIDE NAME: {}", guideName);
                continue;
            }

            Branch branch = null;
            if (guidePayload.getBranchId() != null) {
                branch = branchRepository.findById(guidePayload.getBranchId()).orElse(null);
            }

            GuideCategory guideCategory = guideCategoryService.findById(guideCategoryId);

            Guide guide = Guide.builder()
                    .name(guidePayload.getName())
                    .teamId(guidePayload.getTeamId())
                    .branch(branch)
                    .isBranchOpen(guidePayload.getIsBranchOpen())
                    .isTeamOpen(guidePayload.getIsTeamOpen())
                    .creatorId(securityUtils.getMemberId())
                    .modifierId(securityUtils.getMemberId())
                    .guideCategory(guideCategory)
                    .blockIds(new ArrayList<>())
                    .type(guidePayload.getType())
                    .enabled(guideEnabled)
                    .build();

            guide = guideRepository.save(guide);

            if (guidePayload.getContents() != null && !guidePayload.getContents().isEmpty()) createBlock(guidePayload, guide);

        }
    }

    /**
     * 모바일에서 사용하는 가이드 조회
     *
     * @param categoryId
     * @return
     */
    public List<GuideDto> getAllGuide(Long categoryId) {

        Long branchId = securityUtils.getBranchId();
        Long teamId = securityUtils.getTeamId();

        List<Long> childrenIds = guideCategoryService.getAllSubCategory(categoryId, branchId);


        List<Guide> guides = guideRepository.findByGuideCategoryIdIn(childrenIds, teamId, branchId);

        List<GuideDto> guideDtos = guideMapper.map(guides);
        orderGuideBlock(guideDtos);

        return guideDtos;
    }

    /**
     * 브랜치 수정
     *
     * @param guideId
     * @param branchId
     */
    public void patchBranch(Long guideId, Long branchId) {
        Guide guide = guideRepository.findById(guideId).orElse(null);
        Assert.notNull(guide, "Not Found Guide");

        Member member = memberRepository.findById(securityUtils.getMemberId()).orElse(null);
        Assert.notNull(member, "Not Found Member");


        guide.setModifierId(member.getId());
        guide.setIsTeamOpen(true);
        if (branchId.equals(0L)) {
            guide.setIsBranchOpen(true);
            guide.setIsTeamOpen(true);
        } else {
            Branch branch = branchRepository.findById(branchId).orElse(null);
            Assert.notNull(branch, "Not Found Branch");
            guide.setIsBranchOpen(false);
            guide.setBranch(branch);
        }
    }

    /**
     * 팀 수정
     *
     * @param guideId
     * @param teamId
     */
    public void patchTeam(Long guideId, Long teamId) {
        Guide guide = guideRepository.findById(guideId).orElse(null);
        Assert.notNull(guide, "Not Found Guide");

        Member member = memberRepository.findById(securityUtils.getMemberId()).orElse(null);
        Assert.notNull(member, "Not Found Member");


        guide.setModifierId(member.getId());
        if (teamId.equals(0L)) {
            guide.setIsTeamOpen(true);
        } else {
            Team team = teamRepository.findById(teamId).orElse(null);
            Assert.notNull(team, "Not Found Team");
            guide.setIsTeamOpen(false);
            guide.setTeamId(team.getId());
        }
    }

    private List<GuideDto> orderGuideBlock(List<GuideDto> list) {
        for (GuideDto guideDto : list) {
            guideDto.setBlocks(
                    guideDto.getBlocks().stream().sorted(Comparator.comparing(GuideBlockDto::getId)).collect(Collectors.toList())
            );
        }

        return list;
    }

    /**
     * 가이드 삭제
     *
     * @param ids
     */
    public Map<String, String> deleteAllById(List<Long> ids) {
        List<Guide> deleteGuide = guideRepository.findAllById(ids);

        Map<String, String> resultMap = new HashMap<>();

        List<Long> saveDeleteIds = new ArrayList<>();
        for (Guide guide : deleteGuide) {
            List<MemberRole> creatorMemberRole = memberRoleRepository.findAllByMemberId(guide.getCreatorId());
            Assert.notNull(creatorMemberRole, "Not Found Creator memberRole ");
            Long guideCreatorLevel = Objects.requireNonNull(roleRepository.findById(creatorMemberRole.get(0).getRoleId()).orElse(null), "Role can not be null").getLevel().getId();


            List<MemberRole> currentMemberRole = memberRoleRepository.findAllByMemberId(securityUtils.getMemberId());
            Assert.notNull(currentMemberRole, "Not Found Current memberRole ");
            Long currentMemberLevel = Objects.requireNonNull(roleRepository.findById(currentMemberRole.get(0).getRoleId()).orElse(null), "Role can not be null").getLevel().getId();

            log.info("guide {}, current {}", guideCreatorLevel, currentMemberLevel);
            if (guideCreatorLevel < currentMemberLevel) {
                saveDeleteIds.add(guide.getId());
            } else if (guideCreatorLevel.equals(currentMemberLevel)) {
                if (guide.getCreatorId().equals(securityUtils.getMemberId())) {
                    saveDeleteIds.add(guide.getId());
                } else {
                    resultMap.put(guide.getName(), "자신이 생성한 가이드만 삭제할 수 있습니다.");
                }
            } else {
                resultMap.put(guide.getName(), "권한이 낮아 삭제하지 못했습니다.");
            }
        }


        guideRepository.deleteAllById(saveDeleteIds);
        guideBlockRepository.deleteAllByGuideIdIn(saveDeleteIds);

        return resultMap;
    }

    public Page<GuideDto> getNameSearch(GuideSearchDto searchDto, Pageable pageable) throws JsonProcessingException {
        Assert.notNull(searchDto.getKeyword(), "Keywords are required.");

        Long teamId = securityUtils.getTeamId();
        Long branchId = securityUtils.getBranchId();
        searchDto.setTeamId(teamId);
        searchDto.setBranchId(branchId);

        List<Long> childrenIds = null;

        // categoryId가 없으면 3depth 카테고리를 전부 가져옴
        // categoryId가 있으면 하위 카테고리 까지 전부 가져옴
        if (searchDto.getCategoryId() == null) {
            childrenIds = guideCategoryService.getAllDepthCategory(searchDto.getBranchId());
        } else {
            childrenIds = guideCategoryService.getAllSubCategory(searchDto.getCategoryId(), searchDto.getBranchId());
        }

        Page<Guide> nameSearchEntities = guideRepository.findByNameSearch(searchDto, childrenIds, pageable);

        List<GuideDto> guideDtoList = new ArrayList<>();
        settingGuideBlock(nameSearchEntities.getContent(), guideDtoList);
        orderGuideBlock(guideDtoList);
        Page<GuideDto> page = new PageImpl<>(guideDtoList, nameSearchEntities.getPageable(), nameSearchEntities.getTotalElements());

        return page;
    }

    public Page<GuideDto> getFileSearch(GuideSearchDto searchDto, Pageable pageable) {
        Assert.notNull(searchDto.getKeyword(), "Keywords are required.");

        Long teamId = securityUtils.getTeamId();
        Long branchId = securityUtils.getBranchId();
        searchDto.setTeamId(teamId);
        searchDto.setBranchId(branchId);

        List<Long> childrenIds = null;

        // categoryId가 없으면 3depth 카테고리를 전부 가져옴
        // categoryId가 있으면 하위 카테고리 까지 전부 가져옴
        if (searchDto.getCategoryId() == null) {
            childrenIds = guideCategoryService.getAllDepthCategory(searchDto.getBranchId());
        } else {
            childrenIds = guideCategoryService.getAllSubCategory(searchDto.getCategoryId(), searchDto.getBranchId());
        }

        Page<Guide> fileSearchEntities = guideRepository.findByFileSearch(searchDto, childrenIds, pageable);

        List<GuideDto> guideDtoList = new ArrayList<>();
        settingGuideBlock(fileSearchEntities.getContent(), guideDtoList);
        orderGuideBlock(guideDtoList);
        Page<GuideDto> page = new PageImpl<>(guideDtoList, fileSearchEntities.getPageable(), fileSearchEntities.getTotalElements());

        return page;
    }

    public Page<GuideDto> getMessageSearch(GuideSearchDto searchDto, Pageable pageable) {
        Assert.notNull(searchDto.getKeyword(), "Keywords are required.");

        Long teamId = securityUtils.getTeamId();
        Long branchId = securityUtils.getBranchId();
        searchDto.setTeamId(teamId);
        searchDto.setBranchId(branchId);

        List<Long> childrenIds = null;

        // categoryId가 없으면 3depth 카테고리를 전부 가져옴
        // categoryId가 있으면 하위 카테고리 까지 전부 가져옴
        if (searchDto.getCategoryId() == null) {
            childrenIds = guideCategoryService.getAllDepthCategory(searchDto.getBranchId());
        } else {
            childrenIds = guideCategoryService.getAllSubCategory(searchDto.getCategoryId(), searchDto.getBranchId());
        }

        Page<Guide> messageSearchEntities = guideRepository.findByMessageSearch(searchDto, childrenIds, pageable);

        List<GuideDto> guideDtoList = new ArrayList<>();
        settingGuideBlock(messageSearchEntities.getContent(), guideDtoList);
        orderGuideBlock(guideDtoList);
        Page<GuideDto> page = new PageImpl<>(guideDtoList, messageSearchEntities.getPageable(), messageSearchEntities.getTotalElements());

        return page;
    }

    public void download(HttpServletResponse res) {
        try {
            Workbook workbook = new XSSFWorkbook();
            Sheet guideSheet = workbook.createSheet("가이드");
            Sheet guideBlockSheet = workbook.createSheet("가이드 내용");
            CellStyle cellStyle = workbook.createCellStyle();
            cellStyle.setWrapText(true);
            cellStyle.setVerticalAlignment(VerticalAlignment.CENTER);

            // 실제 데이터 입력
            guideSheetHeaderSetting(guideSheet, cellStyle);
            guideBlockSheetHeaderSetting(guideBlockSheet, cellStyle);
            addGuideToExcel(guideSheet, guideBlockSheet);
            // -------------


            String fileName = "guide_backup";

            res.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
            res.setHeader("Content-Disposition", "attachment;filename=" + fileName + ".xlsx");
            workbook.write(res.getOutputStream());
            workbook.close();
        } catch (Exception e) {
            log.error("GUIDE EXCEL DOWNLOAD ERROR: {}", e.getLocalizedMessage(), e);
        }
    }

    private void addGuideToExcel(Sheet guideSheet, Sheet guideBlockSheet) {
        int rowIndex = 2;

        Row row = null;
        Cell cell = null;

        int localIndex = 1;
        int blockRow = 2;

        int page = 0;
        final int pageSize = 15;
        boolean hasNext = true;
        try {
            while (hasNext) {
                PageRequest pageable = PageRequest.of(page++, pageSize, Sort.Direction.ASC, "created");
                Slice<Guide> guides = guideRepository.findAllByBranchIdAndTeamId(pageable, securityUtils.getBranchId(), securityUtils.getTeamId());
                hasNext = guides.hasNext();
                for (Guide guide : guides) {
                    row = guideSheet.createRow(rowIndex++);
                    cell = row.createCell(0);
                    cell.setCellValue(localIndex);
                    cell = row.createCell(1);
                    cell.setCellValue(guide.getName());
                    cell = row.createCell(2);
                    cell.setCellValue(guide.getEnabled());
                    cell = row.createCell(3);
                    cell.setCellValue(guide.getIsBranchOpen());
                    cell = row.createCell(4);
                    cell.setCellValue(guide.getIsTeamOpen());
                    cell = row.createCell(5);
                    if (guide.getTeamId() != null) {
                        cell.setCellValue(guide.getTeamId().intValue());
                    }
                    cell = row.createCell(6);
                    cell.setCellValue(guide.getBranch().getId().intValue());
                    cell = row.createCell(7);
                    cell.setCellValue(guide.getType().toString());
                    cell = row.createCell(8);
                    cell.setCellValue(guide.getGuideCategory().getId().intValue());

                    blockRow = addGuideBlockToExcel(guide.getBlockIds(), guideBlockSheet, localIndex, blockRow);
                    localIndex++;
                }
            }
        } catch (Exception e) {
            log.error("ADD GUIDE TO EXCEL ERROR: {}", e.getLocalizedMessage(), e);
        }

    }

    private int addGuideBlockToExcel(List<Long> guideBlockIds, Sheet guideBlockSheet, int localIndex, int blockRow) {
        Row row = null;
        Cell cell = null;

        try {
            List<Long> blockIds = objectMapper.readValue(guideBlockIds.toString(), new TypeReference<List<Long>>() {
            });

            List<GuideBlock> guideBlockList = guideBlockRepository.findAllByIdIn(blockIds);
            guideBlockList.sort((o1, o2) -> Math.toIntExact(o1.getId() - o2.getId()));


            for (int i = 0; i < guideBlockList.size(); i++) {
                int blockIndex = 4;
                int blockStep = 14;
                int buttonStep = 2;
                GuideBlock guideBlock = guideBlockList.get(i);
                row = guideBlockSheet.createRow(blockRow++);
                cell = row.createCell(0);
                cell.setCellValue(localIndex);
                if (guideBlockList.size() > 1) {
                    cell = row.createCell(1);
                    cell.setCellValue(i);

                    cell = row.createCell(2);
                    cell.setCellValue(guideBlock.getBlockName());

                    if (guideBlock.getRequireId() != null) {
                        int requireLocalIndex = getRequireIdAtLocal(guideBlockList, guideBlock.getRequireId());
                        cell = row.createCell(3);
                        cell.setCellValue(requireLocalIndex);
                    }
                }

                IssuePayload issuePayload = objectMapper.readValue(guideBlock.getPayload(), IssuePayload.class);

                for (int chapterIndex = 0; chapterIndex < issuePayload.getChapters().size(); chapterIndex++) {
                    IssuePayload.Chapter chapter = issuePayload.getChapters().get(chapterIndex);
                    cell = row.createCell(blockIndex);
                    // 파일 or 버튼
                    if (chapter.getSections().size() > 1) {
                        if ("file".equals(chapter.getSections().get(1).getExtra())) {
                            cell.setCellValue("text_file");
                            cell = row.createCell(blockIndex + 12);
                            cell.setCellValue(chapter.getSections().get(0).getData());

                            cell = row.createCell(blockIndex + 13);
                            cell.setCellValue(chapter.getSections().get(1).getActions().get(0).getData());
                        } else if (chapter.getSections().get(1).getExtra() == null) {
                            cell.setCellValue("text_button");
                            cell = row.createCell(blockIndex + 1);
                            cell.setCellValue(chapter.getSections().get(0).getData());

                            int innerButtonIndex = blockIndex + buttonStep;
                            for (IssuePayload.Action action : chapter.getSections().get(1).getActions()) {
                                cell = row.createCell(innerButtonIndex);
                                cell.setCellValue(action.getName());

                                cell = row.createCell(innerButtonIndex + 1);
                                cell.setCellValue(action.getData());
                                innerButtonIndex += buttonStep;
                            }
                        }
                    }
                    // 텍스트
                    else {
                        cell.setCellValue("text");
                        IssuePayload.Section section = chapter.getSections().get(0);
                        cell = row.createCell(blockIndex + 1);
                        cell.setCellValue(section.getData());
                    }
                    blockIndex += blockStep;
                }
            }
            return blockRow;
        } catch (Exception e) {
            log.error("GUIDE BLOCK TO EXCEL ERROR: {}", e.getLocalizedMessage(), e);
            return -1;
        }
    }

    private int getRequireIdAtLocal(List<GuideBlock> guideBlockList, Long requireId) {
        for (int i = 0; i < guideBlockList.size(); i++) {
            if (guideBlockList.get(i).getId().equals(requireId)) {
                return i;
            }
        }

        return 0;
    }

    private void guideSheetHeaderSetting(Sheet guideSheet, CellStyle cellStyle) {
        String[] descriptionName = new String[]{"가이드 ID", "이름", "임시저장", "브랜치 전체 오픈", "팀 전체 오픈", "팀 번호\n없으면 빈값", "브랜치 번호\n없으면 빈값", "single\nprocess", "카테고리 번호"};
        String[] headerName = new String[]{"ID", "가이드 이름", "임시저장 여부", "브랜치 오픈", "팀 오픈", "팀 PK", "브랜치 PK", "타입", "카테고리 ID"};
        guideSheet.setDefaultColumnWidth(18);

        Row descRow = guideSheet.createRow(0);
        Row headerRow = guideSheet.createRow(1);
        Cell cell = null;

        for (int i = 0; i < descriptionName.length; i++) {
            cell = descRow.createCell(i);
            cell.setCellValue(descriptionName[i]);
            cell.setCellStyle(cellStyle);

            cell = headerRow.createCell(i);
            cell.setCellValue(headerName[i]);
        }
        descRow.setHeightInPoints(120);
    }

    private void guideBlockSheetHeaderSetting(Sheet guideBlockSheet, CellStyle cellStyle) {
        String[] descriptionName = new String[]{"가이드 시트의 가이드 ID", "가이드별로 0부터 시작", "single: 빈값\nprocess: 필수", "single: 빈값", "텍스트\n(text)\n\n텍스트+버튼\n(text_button)\n\n텍스트+파일\n(text_file)", "텍스트: 내용\n\n텍스트+버튼: 내용, 버튼 이름, 버튼 URL\n(버튼 최대 5개)\n\n텍스트+파일: 파일 이름, 파일 URL\n\n아닌 값은 빈칸"};
        String[] headerName = new String[]{"가이드 ID", "블록 ID", "블록  이름", "필수 블록", "타입", "내용", "버튼 이름", "버튼 URL", "버튼 이름", "버튼 URL", "버튼 이름", "버튼 URL", "버튼 이름", "버튼 URL", "버튼 이름", "버튼 URL", "파일 이름", "파일 URL"};
        guideBlockSheet.setDefaultColumnWidth(18);

        Row row = null;
        Cell cell = null;

        row = guideBlockSheet.createRow(0);
        for (int i = 0; i < descriptionName.length; i++) {
            cell = row.createCell(i);
            cell.setCellValue(descriptionName[i]);
            cell.setCellStyle(cellStyle);
        }
        row = guideBlockSheet.createRow(1);
        for (int i = 0; i < headerName.length; i++) {
            cell = row.createCell(i);
            cell.setCellValue(headerName[i]);
        }
        guideBlockSheet.addMergedRegion(new CellRangeAddress(0, 0, 5, 17));
    }
}
