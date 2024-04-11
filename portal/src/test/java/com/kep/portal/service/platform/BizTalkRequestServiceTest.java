package com.kep.portal.service.platform;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.kep.core.model.dto.customer.CustomerContactType;
import com.kep.core.model.dto.platform.BizTalkRequestDto;
import com.kep.core.model.dto.platform.BizTalkRequestStatus;
import com.kep.core.model.dto.platform.PlatformType;
import com.kep.core.model.dto.work.WorkType;
import com.kep.portal.model.dto.platform.BizTalkRequestCondition;
import com.kep.portal.model.entity.channel.Channel;
import com.kep.portal.model.entity.customer.Customer;
import com.kep.portal.model.entity.customer.CustomerContact;
import com.kep.portal.model.entity.customer.Guest;
import com.kep.portal.model.entity.member.Member;
import com.kep.portal.model.entity.platform.BIzTalkRequestMapper;
import com.kep.portal.model.entity.platform.BizTalkRequest;
import com.kep.portal.model.entity.platform.PlatformTemplate;
import com.kep.portal.repository.channel.ChannelRepository;
import com.kep.portal.repository.customer.CustomerRepository;
import com.kep.portal.repository.customer.GuestRepository;
import com.kep.portal.repository.member.MemberRepository;
import com.kep.portal.repository.platform.BizTalkRequestRepository;
import com.kep.portal.repository.platform.PlatformTemplateRepository;
import com.kep.portal.service.customer.GuestService;
import com.kep.portal.service.member.MemberService;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import javax.swing.text.html.BlockView;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
//@Transactional
@Slf4j
class BizTalkRequestServiceTest {

    @Resource
    private BizTalkRequestRepository bizTalkRequestRepository;

    @Resource
    private BizTalkRequestService bizTalkRequestService;

    @Resource
    private CustomerRepository customerRepository;
    @Resource
    private PlatformTemplateRepository platformTemplateRepository;
    @Resource
    private MemberRepository memberRepository;
    @Resource
    private BIzTalkRequestMapper bIzTalkRequestMapper;

    @Resource
    private ObjectMapper objectMapper;

    private Long templateId, guest1Id, guest2Id, guest3Id, memberId, channelId;

    @BeforeEach
    void init() {
        Member memberEntity = memberRepository.save(Member.builder()
                .username("useruseruseruser")
                .nickname("닉네임")
                .branchId(1L)
                .enabled(true)
                .managed(false)
                .status(WorkType.OfficeHoursStatusType.rest)
                .created(ZonedDateTime.now())
                .creator(1L)
                .modified(ZonedDateTime.now())
                .modifier(1L)
                .build());
        memberId = memberEntity.getId();

        channelId = 1L;
        Customer customer = customerRepository.save(Customer.builder()
                .name("test")
                .build());
        CustomerContact customerContact = CustomerContact.builder()
                .customerId(customer.getId())
                .type(CustomerContactType.call)
                .payload("+82 10-0000-0000")
                .build();
        customer.setContacts(Arrays.asList(customerContact));
        customerRepository.save(customer);

        PlatformTemplate templateEntity = platformTemplateRepository.save(PlatformTemplate.builder()
                .name("플랫폼 테스트")
                .code("TEST_TEMPLATE")
                .branchId(1L)
                .platform(PlatformType.kakao_friend_talk)
                .payload("{\n" +
                        "    \"sender_key\": \"96b95232834a463b11711dd0592380c0e427630f\",\n" +
                        "    \"template_code\": \"R000000071_00025\",\n" +
                        "    \"template_name\": \"친구톡 텍스트형\",\n" +
                        "    \"template_content\": \"친구톡 텍스트형 템플릿 내용\",\n" +
                        "    \"buttons\": [\n" +
                        "        {\n" +
                        "            \"ordering\": 1,\n" +
                        "            \"name\": \"웹으로이동\",\n" +
                        "            \"link_type\": \"WL\",\n" +
                        "            \"link_mo\": \"https://www.naver.com\"\n" +
                        "        },\n" +
                        "        {\n" +
                        "            \"ordering\": 2,\n" +
                        "            \"name\": \"에코 버튼\",\n" +
                        "            \"link_type\": \"BK\"\n" +
                        "        }\n" +
                        "    ]\n" +
                        "}\n")
                .creator(memberId)
                .created(ZonedDateTime.now())
                .modifier(memberId)
                .modified(ZonedDateTime.now())
                .build());

        templateId = templateEntity.getId();

        BizTalkRequest request = BizTalkRequest.builder()
                .platform(PlatformType.kakao_friend_talk)
                .status(BizTalkRequestStatus.ready)
                .templateId(templateId)
                .channelId(1L)
                .branchId(1L)
                .teamId(1L)
                .customers(Arrays.asList(customer.getId()))
                .created(ZonedDateTime.now())
                .creator(memberId)
                .modified(ZonedDateTime.now())
                .modifier(memberId)
                .build();


        BizTalkRequest save = bizTalkRequestRepository.save(request);
        log.info("bizTalkRequestDto = {}", bIzTalkRequestMapper.map(save));
    }

    @Disabled
    @Test
    void search() {
        Pageable pageable = PageRequest.of(0, 10, Sort.Direction.DESC, "created");
        BizTalkRequestCondition condition = BizTalkRequestCondition.builder()
                .type(PlatformType.kakao_friend_talk)
                .build();

        Page<BizTalkRequestDto> search = bizTalkRequestService.search(condition, pageable);

        log.info("search = {}", search.getContent());
    }
}