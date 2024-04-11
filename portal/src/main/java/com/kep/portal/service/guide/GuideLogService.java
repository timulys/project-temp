package com.kep.portal.service.guide;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.kep.core.model.dto.guide.GuideLogDto;
import com.kep.core.model.dto.guide.GuideType;
import com.kep.portal.model.entity.guide.Guide;
import com.kep.portal.model.entity.guide.GuideBlock;
import com.kep.portal.model.entity.guide.GuideLog;
import com.kep.portal.model.entity.guide.GuideLogMapper;
import com.kep.portal.repository.guide.GuideBlockRepository;
import com.kep.portal.repository.guide.GuideLogRepository;
import com.kep.portal.repository.guide.GuideRepository;
import com.kep.portal.repository.member.MemberRepository;
import com.kep.portal.util.SecurityUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import javax.annotation.Resource;
import javax.transaction.Transactional;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Transactional
@Slf4j
public class GuideLogService {

    @Resource
    private GuideLogRepository guideLogRepository;

    @Resource
    private GuideRepository guideRepository;

    @Resource
    private MemberRepository memberRepository;

    @Resource
    private GuideLogMapper mapper;

    @Resource
    private SecurityUtils securityUtils;
    @Resource
    private GuideBlockRepository guideBlockRepository;

    @Resource
    private ObjectMapper objectMapper;

    public GuideLogDto store(GuideLogDto dto) {
        Guide guide = guideRepository.findById(dto.getGuideId()).orElse(null);
        Assert.notNull(guide,"Not found guide");

        dto.setCreator(securityUtils.getMemberId());
        return mapper.map(guideLogRepository.save(mapper.map(dto)));
    }

    public Map<Integer, Boolean> getRequireLog(GuideLogDto dto) throws JsonProcessingException {
        Guide guide = guideRepository.findById(dto.getGuideId()).orElse(null);
        Assert.notNull(guide, "Not Found Guide");


        Map<Integer, Boolean> requireBlocKCheck = new HashMap<>();

        List<Long> blockIds = objectMapper.readValue(guide.getBlockIds().toString(), new TypeReference<List<Long>>() {
        });

        List<GuideBlock> sortedGuideBlock = guideBlockRepository.findAllByIdIn(blockIds);
//        List<GuideBlock> sortedGuideBlock = guide.getBlocks().stream().sorted(Comparator.comparing(GuideBlock::getId)).collect(Collectors.toList());


        for (int i = 0; i < sortedGuideBlock.size(); i++) {
            GuideBlock block = sortedGuideBlock.get(i);

            if (block.getRequireId() != null) {
                Set<Long> collect = guideLogRepository.findByGuideIdAndIssueIdAndBlockId(dto.getGuideId(), dto.getIssueId(), block.getRequireId()).stream().map(GuideLog::getContentId).collect(Collectors.toSet());
                GuideBlock guideBlock = sortedGuideBlock.stream().filter(item -> item.getId().equals(block.getRequireId())).findFirst().orElse(null);
                Assert.notNull(guideBlock,"Not Found Guideblock");
                requireBlocKCheck.put(i, guideBlock.getContentCount().equals(collect.size()));
            }
        }

        return requireBlocKCheck;
    }
}
