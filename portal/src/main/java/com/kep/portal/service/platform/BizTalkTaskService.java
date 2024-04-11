package com.kep.portal.service.platform;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.kep.core.model.dto.customer.CustomerDto;
import com.kep.core.model.dto.member.MemberDto;
import com.kep.core.model.dto.platform.BizTalkTaskDto;
import com.kep.core.model.dto.platform.BizTalkTaskStatus;
import com.kep.core.model.dto.platform.PlatformTemplateDto;
import com.kep.core.model.dto.platform.kakao.KakaoBizMessageTemplatePayload;
import com.kep.portal.model.dto.platform.BizTalkTaskCondition;
import com.kep.portal.model.entity.customer.*;
import com.kep.portal.model.entity.member.Member;
import com.kep.portal.model.entity.member.MemberMapper;
import com.kep.portal.model.entity.platform.*;
import com.kep.portal.model.entity.privilege.Level;
import com.kep.portal.repository.customer.CustomerRepository;
import com.kep.portal.repository.member.MemberRepository;
import com.kep.portal.repository.platform.BizTalkRequestRepository;
import com.kep.portal.repository.platform.BizTalkTaskRepository;
import com.kep.portal.repository.platform.PlatformTemplateRepository;
import com.kep.portal.util.SecurityUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import org.springframework.util.ObjectUtils;

import javax.annotation.Resource;
import java.util.*;

@Slf4j
@Transactional
@Service
public class BizTalkTaskService {

    @Resource
    private BizTalkTaskRepository bizTalkTaskRepository;

    @Resource
    private PlatformTemplateRepository platformTemplateRepository;
    @Resource
    private PlatformTemplateMapper platformTemplateMapper;
    @Resource
    private MemberRepository memberRepository;
    @Resource
    private MemberMapper memberMapper;
    @Resource
    private CustomerRepository customerRepository;
    @Resource
    private CustomerMapper customerMapper;
    @Resource
    private BizTalkRequestRepository bizTalkRequestRepository;
    @Resource
    private ObjectMapper objectMapper;
    @Resource
    private BizTalkTaskMapper bizTalkTaskMapper;

    @Resource
    private SecurityUtils securityUtils;

    public Page<BizTalkTaskDto> search(BizTalkTaskCondition condition, Pageable pageable) {
        if (securityUtils.hasRole(Level.ROLE_OPERATOR)) {
            condition.setMemberId(securityUtils.getMemberId());
        }

        if ("phone".equals(condition.getKeywordType())) {
            condition.setKeyword(phoneFormat(condition.getKeyword()).replace("010-", "+82 10-"));
        }

        if(condition.getStatus() == null){
            return new PageImpl<>(Collections.emptyList());
        }

        Page<BizTalkTask> search = bizTalkTaskRepository.search(condition, pageable);
        List<BizTalkTaskDto> list = new ArrayList<>();

        for (BizTalkTask task : search) {
            BizTalkTaskDto dto = bizTalkTaskMapper.map(task);

            dto.setTemplate(getTemplate(task.getTemplateId(), task.getRequestId()));
            dto.setCustomer(getCustomer(task.getCustomerId()));
            dto.setCreator(getMember(task.getCreator()));

            BizTalkRequest byId = bizTalkRequestRepository.findById(task.getRequestId()).orElse(null);
            Assert.notNull(byId, "Request is null");

            if (!ObjectUtils.isEmpty(byId.getFriendPayload())) {
                dto.getTemplate().setPayload(byId.getFriendPayload());
            }

            list.add(dto);
        }

        return new PageImpl<>(list, search.getPageable(), search.getTotalElements());
    }

    private String phoneFormat(String number) {
        String regEx = "(\\d{3})(\\d{3,4})(\\d{4})";
        return number.replaceAll(regEx, "$1-$2-$3");
    }

    private MemberDto getMember(Long creator) {
        Member member = memberRepository.findById(creator).orElse(null);
        return memberMapper.map(member);
    }

    private CustomerDto getCustomer(Long customerId) {
        Customer customer = customerRepository.findById(customerId).orElse(null);
        return customerMapper.map(customer);
    }

    private PlatformTemplateDto getTemplate(Long templateId, Long requestId) {
        PlatformTemplateDto result;
        BizTalkRequest bizTalkRequest = bizTalkRequestRepository.findById(requestId).orElse(null);
        Assert.notNull(bizTalkRequest, "Request is null");

        if (ObjectUtils.isEmpty(bizTalkRequest.getFriendPayload())) {
            PlatformTemplate entity = platformTemplateRepository.findById(templateId).orElse(null);
            result = platformTemplateMapper.map(entity);
            result.setPayload(substitution(result.getPayload()));
        } else {
            KakaoBizMessageTemplatePayload templatePayload;
            try {
                templatePayload = objectMapper.readValue(bizTalkRequest.getFriendPayload(), KakaoBizMessageTemplatePayload.class);
            } catch (JsonProcessingException e) {
                throw new RuntimeException(e);
            }
            result = new PlatformTemplateDto();
            result.setName(templatePayload.getTemplateName());
            result.setPayload(substitution(bizTalkRequest.getFriendPayload()));
        }
        return result;
    }

    private String substitution(String body) {
        //TODO : 프로젝트에 따라 템플릿 내용을 치환해줘야 함
        return body;
    }

    public Map<String, Integer> setTaskCancel(List<Long> ids) {
        List<BizTalkTask> allById = bizTalkTaskRepository.findAllById(ids);

        int succeed = 0;
        int failed = 0;
        for (BizTalkTask task : allById) {
            if (task.getStatus().equals(BizTalkTaskStatus.cancel)) {
                failed++;
            } else {
                task.setStatus(BizTalkTaskStatus.cancel);
                bizTalkTaskRepository.save(task);
                succeed++;
            }
        }

        Map<String, Integer> result = new HashMap<>();
        result.put("succeed", succeed);
        result.put("failed", failed);
        return result;
    }

    public void saveReserveTalk(BizTalkRequest save) {
        for (Long customerId : save.getCustomers()) {

            Customer customer = customerRepository.findById(customerId).orElse(null);
            Assert.notNull(customer, "Not found customer");

            bizTalkTaskRepository.save(BizTalkTask.builder()
                    .platform(save.getPlatform())
                    .status(BizTalkTaskStatus.open)
                    .requestStatus(save.getStatus())
                    .channelId(save.getChannelId())
                    .branchId(save.getBranchId())
                    .teamId(save.getTeamId())
                    .reserved(save.getReserved())
                    .templateId(save.getTemplateId())
                    .customerId(customerId)
                    .requestId(save.getId())
                    .creator(save.getCreator())
                    .build());
        }
    }

    public Map<String, Integer> memberTaskCancel(List<Long> ids) {
        List<BizTalkTask> taskAll = bizTalkTaskRepository.findAllById(ids);

        int succeed = 0;
        int failed = 0;
        for (BizTalkTask task : taskAll) {

            if (!task.getCreator().equals(securityUtils.getMemberId())) {
                failed++;
                continue;
            }

            if (task.getStatus().equals(BizTalkTaskStatus.cancel)) {
                failed++;
            } else {
                task.setStatus(BizTalkTaskStatus.cancel);
                bizTalkTaskRepository.save(task);
                succeed++;
            }
        }

        Map<String, Integer> result = new HashMap<>();
        result.put("succeed", succeed);
        result.put("failed", failed);
        return result;
    }
}
