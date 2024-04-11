package com.kep.portal.repository.guide;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.kep.core.model.dto.guide.GuideCategoryDto;
import com.kep.core.model.dto.work.WorkType;
import com.kep.portal.model.entity.branch.Branch;
import com.kep.portal.model.entity.guide.GuideCategory;
import com.kep.portal.model.entity.guide.GuideCategoryMapper;
import com.kep.portal.repository.branch.BranchRepository;
import com.mysema.commons.lang.Assert;
import lombok.extern.slf4j.Slf4j;
import org.jeasy.random.EasyRandom;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;
import javax.transaction.Transactional;

import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
@Slf4j
class GuideCategoryRepositoryTest {

    @Resource
    private ObjectMapper objectMapper;

    @Resource
    private GuideCategoryRepository categoryRepository;

    @Resource
    private GuideCategoryMapper categoryMapper;

    @Resource
    private BranchRepository branchRepository;

    private Long branchId;
    private Long childId;
    private Long parentId;
    private Long grandParentId;

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

        EasyRandom generator = new EasyRandom();
        for (int i = 0; i < 10; i++) {
            log.info("{}", i % 3 + 1);
            GuideCategory guideCategory = GuideCategory.builder()
                    .name(generator.nextObject(String.class))
                    .branch(branch)
                    .modifier(2L)
                    .creator(2L)
                    .isOpen(false)
                    .depth(i % 3 + 1)
                    .build();
            guideCategory = categoryRepository.save(guideCategory);
            if (i == 8) childId = guideCategory.getId();
            if (i == 7) parentId = guideCategory.getId();
            if (i == 3) grandParentId = guideCategory.getId();
        }
        log.info("childId = {}, parentId = {} ,grandParentId = {}", childId, parentId, grandParentId);

        GuideCategory child = categoryRepository.findById(childId).orElse(null);
        assertNotNull(child);
        GuideCategory parent = categoryRepository.findById(parentId).orElse(null);
        assertNotNull(parent);
        GuideCategory grandParent = categoryRepository.findById(grandParentId).orElse(null);
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

    }

    @Test
    @DisplayName("findById 테스트")
    void testFindById() {
        GuideCategory child = categoryRepository.findById(childId).orElse(null);
        assertNotNull(child);
        assertNotNull(child.getParent());
        assertNotNull(child.getParent().getParent());
        assertEquals(grandParentId, child.getParent().getParent().getId());
    }

    @Test
    @DisplayName("카테고리 생성 및 조회")
    void testSave() throws JsonProcessingException {

        Branch branch = branchRepository.findById(branchId).orElse(null);
        GuideCategory c1 = GuideCategory.builder()
                .name("대분류")
                .branch(branch)
                .modifier(2L)
                .creator(2L)
                .depth(1)
                .build();

        GuideCategory c2 = GuideCategory.builder()
                .name("중분류")
                .branch(branch)
                .modifier(2L)
                .creator(2L)
                .depth(2)
                .build();

        GuideCategory c3 = GuideCategory.builder()
                .name("소분류")
                .branch(branch)
                .modifier(2L)
                .creator(2L)
                .depth(3)
                .build();

        GuideCategory save = categoryRepository.save(c1);
        GuideCategory save1 = categoryRepository.save(c2);
        GuideCategory save2 = categoryRepository.save(c3);

        save2.setParent(save1);
        save1.setParent(save);

        List<GuideCategory> list1 = new ArrayList<>();
        list1.add(save1);
        save.setChildren(list1);

        List<GuideCategory> list2 = new ArrayList<>();
        list2.add(save2);
        save1.setChildren(list2);

        categoryRepository.save(save);
        categoryRepository.save(save1);
        categoryRepository.save(save2);


        List<GuideCategory> entities = categoryRepository.findByBranchAndDepthCategory(branchId,1);
        if(entities.size() > 0){
            log.info("entities =\n{}", objectMapper.writeValueAsString(entities));
        }



        List<GuideCategoryDto> dtos = categoryMapper.map(entities);
        if(dtos.size() > 0){
            log.info("dtos =\n{}", objectMapper.writeValueAsString(dtos));
        }

    }
}
