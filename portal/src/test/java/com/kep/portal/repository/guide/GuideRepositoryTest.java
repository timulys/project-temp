package com.kep.portal.repository.guide;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.kep.core.model.dto.guide.*;
import com.kep.core.model.dto.work.WorkType;
import com.kep.portal.model.dto.guide.GuideSearchDto;
import com.kep.portal.model.dto.guide.GuideSearchResponseDto;
import com.kep.portal.model.entity.branch.Branch;
import com.kep.portal.model.entity.guide.*;
import com.kep.portal.model.entity.member.Member;
import com.kep.portal.model.entity.team.Team;
import com.kep.portal.repository.branch.BranchRepository;
import com.kep.portal.repository.member.MemberRepository;
import com.kep.portal.repository.team.TeamRepository;
import com.kep.portal.service.guide.GuideCategoryService;
import lombok.extern.slf4j.Slf4j;
import org.jeasy.random.EasyRandom;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.util.Assert;

import javax.annotation.Resource;
import javax.transaction.Transactional;

import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;


@SpringBootTest
@Transactional
@Slf4j
class GuideRepositoryTest {

    @Resource
    private ObjectMapper objectMapper;

    @Resource
    private GuideMapper guideMapper;

    @Resource
    private GuideCategoryMapper categoryMapper;

    @Resource
    private GuideRepository guideRepository;

    @Resource
    private GuideCategoryRepository categoryRepository;

    @Resource
    private BranchRepository branchRepository;

    @Resource
    private TeamRepository teamRepository;

    @Resource
    private MemberRepository memberRepository;

    @Resource
    private GuideBlockRepository guideBlockRepository;

    @Resource
    private GuideCategoryService guideCategoryService;

    private Long grandParentId, branchId, teamId;

    @BeforeEach
    void beforeEach() throws Exception {

        Branch branch = branchRepository.save(Branch.builder()
                .name("TEST BRANCH")
                .assign(WorkType.Cases.branch)
                .enabled(true)
                .headQuarters(false)
                .offDutyHours(true)
                .creator(1L)
                .created(ZonedDateTime.now())
                .modifier(1L)
                .modified(ZonedDateTime.now())
                .offDutyHours(true)
                .build());
        branchId = branch.getId();

        Member member = memberRepository.save(Member.builder()
                .username("TEST_MEMBER_2")
                .nickname("TEST_MEMBER_2")
                .branchId(branchId)
                .status(WorkType.OfficeHoursStatusType.on)
                .creator(1L)
                .created(ZonedDateTime.now())
                .modifier(1L)
                .modified(ZonedDateTime.now())
                .enabled(true)
                .build());
        member = memberRepository.save(member);

        Team team = teamRepository.save(Team.builder()
                .name("TEST TEAM")
//                .branch(branch)
//                .manager(member)
                .created(ZonedDateTime.now())
                .creator(1L)
                .modified(ZonedDateTime.now())
                .modifier(1L)
                .build()
        );
        teamId = team.getId();

        Long childId = null, parentId = null;
        EasyRandom generator = new EasyRandom();
        for (int i = 0; i < 10; i++) {
            log.info("{}", i % 3 + 1);
            GuideCategory guideCategory = GuideCategory.builder()
                    .name(generator.nextObject(String.class))
                    .branch(branch)
                    .modifier(2L)
                    .creator(2L)
                    .depth(i % 3 + 1)
                    .build();
            log.info("guideCategory = {}", i % 3 + 1);
            guideCategory = categoryRepository.save(guideCategory);
            if (i == 8) childId = guideCategory.getId();
            if (i == 5) parentId = guideCategory.getId();
            if (i == 2) grandParentId = guideCategory.getId();
        }
        log.info("childId = {}, parentId = {} ,grandParentId = {}", childId, parentId, grandParentId);

        GuideCategory child = categoryRepository.findById(childId).orElse(null);
        child.setDepth(3);
        assertNotNull(child);
        GuideCategory parent = categoryRepository.findById(parentId).orElse(null);
        parent.setDepth(2);
        assertNotNull(parent);
        GuideCategory grandParent = categoryRepository.findById(grandParentId).orElse(null);
        grandParent.setDepth(1);
        assertNotNull(grandParent);

        List<GuideCategory> list1 = new ArrayList<>();
        parent.setChildren(list1);
        list1.add(child);

        List<GuideCategory> list2 = new ArrayList<>();
        grandParent.setChildren(list2);
        list2.add(parent);

        child.setParent(parent);
        parent.setParent(grandParent);
        categoryRepository.save(child);
        categoryRepository.save(parent);
        categoryRepository.save(grandParent);

        // 가이드(단일) 생성
        Guide guide = Guide.builder()
                .name("보험신규청약")
                .teamId(teamId)
                .teamId(team.getId())
                .enabled(true)
                .branch(branch)
                .creatorId(member.getId())
                .modifierId(member.getId())
                .guideCategory(child)
                .type(GuideType.single)
                .build();

        GuideBlock guideBlock = GuideBlock.builder()
                .contentCount(0)
                .payload("{\"version\":\"0.1\",\"chapters\":[{\"sections\":[{\"type\":\"text\",\"data\":\"고객님 [실속있는 종신보험] 설명드릴께요.\"}]},{\"sections\":[{\"type\":\"text\",\"data\":\"[실속있는 종신보험]은 병력이 있어도 나이가 많아도 가능한 보험입니다.\\n\\n저해약환급금형은 일반형 계약보다 낮은 보험료로 동일한 보장을 받을 수 있으나, 납입기간 중 계약 해지 시 일반형 계약보다 적은 50% 수준으로 해약환급금을 받는 상품입니다.\"}]},{\"sections\":[{\"type\":\"text\",\"data\":\"상세한 정보 제공을 위해 휴대폰 본인인증이 필요합니다.\"},{\"type\":\"action\",\"actions\":[{\"type\":\"link\",\"name\":\"휴대폰 본인인증\",\"data\":\"https://kakaoenterprise.com\",\"device_type\":\"all\"}]}]},{\"sections\":[{\"type\":\"text\",\"data\":\"상품설명서를 다운받아 확인해주세요.\"},{\"type\":\"action\",\"actions\":[{\"type\":\"link\",\"name\":\"보험신규청약 상품설명서.pdf\",\"data\":\"https://drive.google.com/uc?export=download&id=1qhKcLFJZqM8ncKYy8IlV8QkXeIVo4E37\",\"device_type\":\"all\"}]}]}]}")
                .build();
//        guide.addBlocks(guideBlock);


        guideRepository.save(guide);
        guideBlockRepository.save(guideBlock);


    }

    @Test
    @DisplayName("카테고리 하위 모든 가이드")
    void getGuideByCategory() throws JsonProcessingException {
        GuideCategory guideCategory = categoryRepository.findById(grandParentId).orElse(null);
        GuideCategoryDto guideCategoryDto = categoryMapper.map(guideCategory);
        assertNotNull(guideCategory);
        List<Long> childrenIds = new ArrayList<>();
        childrenIds.add(guideCategoryDto.getId());
        if (guideCategoryDto.getChildren() != null) {
            recursiveLogChildrenId(guideCategoryDto.getChildren(), childrenIds);
            childrenIds.forEach(item -> log.info("item = {}", item));
            Page<Guide> guides = guideRepository.findByGuideCategoryIdIn(childrenIds, teamId, branchId, PageRequest.of(0, 20));
            Assert.notEmpty(guides.getContent(), "Not Found Guide");
            List<GuideDto> guideDtos = guideMapper.map(new ArrayList<>(guides.getContent()));

            log.info("guides = {}", objectMapper.writeValueAsString(guideDtos));
        }
    }

    @Test
    @DisplayName("가이드 검색")
    void search() throws Exception {

        GuideSearchDto searchDto = GuideSearchDto.builder()
                .branchId(branchId)
//                .categoryId(grandParentId) // 필수아님
                .teamId(teamId)
                .keyword("보험") // 필수
                .build();

        Assert.notNull(searchDto.getKeyword(), "Keywords are required.");

        List<Long> childrenIds = null;

        // categoryId가 없으면 3depth 카테고리를 전부 가져옴
        // categoryId가 있으면 하위 카테고리 까지 전부 가져옴
        if (searchDto.getCategoryId() == null) {
            childrenIds = guideCategoryService.getAllDepthCategory(searchDto.getBranchId());
        } else {
            childrenIds = guideCategoryService.getAllSubCategory(searchDto.getCategoryId(), searchDto.getBranchId());
        }

        // 카테고리에 속한 가이드중 이름에 keyword가 포함된 가이드
        List<Guide> nameSearchEntities = guideRepository.findByNameSearch(searchDto, childrenIds);
        Long nameSearchCount = guideRepository.countByNameSearch(searchDto,childrenIds);

        // 카테고리에 속한 가이드중 message에 keyword가 포함된 가이드
        List<Guide> messageSearchEntities = guideRepository.findByMessageSearch(searchDto, childrenIds);
        Long messageSearchCount = guideRepository.countByMessageSearch(searchDto,childrenIds);


        // 카테고리에 속한 가이드중 file에 keyword가 포함된 가이드
        List<Guide> fileSearchEntities = guideRepository.findByFileSearch(searchDto, childrenIds);
        Long fileSearchCount = guideRepository.countByFileSearch(searchDto,childrenIds);

        GuideSearchResponseDto guideSearchResponseDto = GuideSearchResponseDto.builder()
                .nameSearch(orderGuideBlock(guideMapper.map(nameSearchEntities)))
                .nameCount(nameSearchCount)
                .messageSearch(orderGuideBlock(guideMapper.map(messageSearchEntities)))
                .messageCount(messageSearchCount)
                .fileSearch(orderGuideBlock(guideMapper.map(fileSearchEntities)))
                .fileCount(fileSearchCount)
                .build();

        log.info("guideSearchResponseDto = {}", objectMapper.writeValueAsString(guideSearchResponseDto));
    }

    private List<GuideDto> orderGuideBlock(List<GuideDto> list) {
        for (GuideDto guideDto : list) {
            guideDto.setBlocks(
                    guideDto.getBlocks().stream().sorted(Comparator.comparing(GuideBlockDto::getId)).collect(Collectors.toList())
            );
        }
        return list;
    }

    void recursiveLogChildrenId(List<GuideCategoryDto> list, List<Long> childrenIds) {
        if (list == null || list.isEmpty())
            return;

        for (GuideCategoryDto dto : list) {
            childrenIds.add(dto.getId());
            recursiveLogChildrenId(dto.getChildren(), childrenIds);
        }
    }

}
