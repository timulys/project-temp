package com.kep.portal.service.platform;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.kep.core.model.dto.channel.ChannelDto;
import com.kep.core.model.dto.customer.CustomerContactType;
import com.kep.core.model.dto.customer.CustomerDto;
import com.kep.core.model.dto.env.CounselEnvDto;
import com.kep.core.model.dto.member.MemberDto;
import com.kep.core.model.dto.notification.*;
import com.kep.core.model.dto.platform.*;
import com.kep.core.model.dto.platform.kakao.KakaoAlertSendEvent;
import com.kep.core.model.dto.platform.kakao.KakaoBizMessageTemplatePayload;
import com.kep.core.model.dto.platform.kakao.KakaoBizTalkSendResponse;
import com.kep.core.model.dto.platform.kakao.KakaoFriendSendEvent;
import com.kep.portal.client.PlatformClient;
import com.kep.portal.config.property.PortalProperty;
import com.kep.portal.model.dto.notification.NotificationInfoDto;
import com.kep.portal.model.dto.platform.BizTalkRequestCondition;
import com.kep.portal.model.entity.branch.Branch;
import com.kep.portal.model.entity.branch.BranchTeam;
import com.kep.portal.model.entity.customer.Customer;
import com.kep.portal.model.entity.customer.CustomerContact;
import com.kep.portal.model.entity.customer.CustomerMapper;
import com.kep.portal.model.entity.member.Member;
import com.kep.portal.model.entity.member.MemberMapper;
import com.kep.portal.model.entity.platform.*;
import com.kep.portal.model.entity.team.Team;
import com.kep.portal.repository.branch.BranchChannelRepository;
import com.kep.portal.repository.branch.BranchTeamRepository;
import com.kep.portal.repository.customer.CustomerContactRepository;
import com.kep.portal.repository.customer.CustomerRepository;
import com.kep.portal.repository.member.MemberRepository;
import com.kep.portal.repository.platform.BizTalkRequestRepository;
import com.kep.portal.repository.platform.BizTalkTaskRepository;
import com.kep.portal.repository.platform.PlatformTemplateRepository;
import com.kep.portal.service.branch.BranchChannelService;
import com.kep.portal.service.channel.ChannelService;
import com.kep.portal.service.env.CounselEnvService;
import com.kep.portal.service.notification.NotificationService;
import com.kep.portal.util.SecurityUtils;
import com.kep.portal.util.WorkDateTimeUtils;
import com.mchange.rmi.NotAuthorizedException;
import lombok.extern.slf4j.Slf4j;
import org.checkerframework.checker.units.qual.A;
import org.jasypt.encryption.StringEncryptor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import org.springframework.util.ObjectUtils;

import javax.annotation.Resource;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Transactional
@Service
public class BizTalkRequestService {
    @Resource
    private BizTalkRequestRepository bizTalkRequestRepository;

    @Resource
    private BIzTalkRequestMapper bizTalkRequestMapper;

    @Resource
    private NotificationService notificationService;
    @Resource
    private PlatformTemplateRepository platformTemplateRepository;
    @Resource
    private PlatformTemplateMapper platformTemplateMapper;

    @Resource
    private MemberRepository memberRepository;
    @Resource
    private MemberMapper memberMapper;

    @Resource
    private PlatformClient platformClient;

    @Resource
    private CustomerRepository customerRepository;
    @Resource
    private CustomerMapper customerMapper;
    @Resource
    private SecurityUtils securityUtils;
    @Resource
    private ObjectMapper objectMapper;
    @Resource
    private BizTalkTaskRepository bizTalkTaskRepository;
    @Resource
    private BizTalkTaskService bizTalkTaskService;
    @Resource
    private CustomerContactRepository customerContactRepository;
    @Resource
    private BranchTeamRepository branchTeamRepository;
    @Resource(name = "fixedEncryptor")
    private StringEncryptor fixedEncryptor;
    @Resource
    private CounselEnvService counselEnvService;
    @Resource
    private ChannelService channelService;
    @Resource
    private BizTalkHistoryService bizTalkHistoryService;

    @Resource
    private PortalProperty portalProperty;

    public Page<BizTalkRequestDto> search(BizTalkRequestCondition condition, Pageable pageable) {
        if(condition.getStatus() == null){
            return new PageImpl<>(Collections.emptyList());
        }
        Page<BizTalkRequest> requestPage = bizTalkRequestRepository.search(condition, pageable);
        List<BizTalkRequestDto> dtoList = new ArrayList<>();
        for (BizTalkRequest bizTalkRequest : requestPage) {
            BizTalkRequestDto dto = bizTalkRequestMapper.map(bizTalkRequest);

            dto.setCustomers(getCustomerList(bizTalkRequest.getCustomers()));
            dto.setCreator(getCreator(bizTalkRequest.getCreator()));
            dto.setTemplate(getTemplate(bizTalkRequest.getTemplateId(), bizTalkRequest));

            try {
                if (!ObjectUtils.isEmpty(bizTalkRequest.getFriendPayload())) {
                    KakaoBizMessageTemplatePayload templatePayload = objectMapper.readValue(bizTalkRequest.getFriendPayload(), KakaoBizMessageTemplatePayload.class);

                    dto.getTemplate().setPayload(bizTalkRequest.getFriendPayload());
                    dto.getTemplate().setName(templatePayload.getTemplateName());
                }
            } catch (JsonProcessingException e) {
                throw new RuntimeException(e);
            }

            dtoList.add(dto);
        }
        return new PageImpl<>(dtoList, requestPage.getPageable(), requestPage.getTotalElements());
    }

    /**
     * 친구톡 발송가능 시간 검증
     * @param reservedDate
     */
    private void validFriendTalkTime(String reservedDate) {
        LocalTime startTime = portalProperty.getFriendTalkEnableStartTime(); //08:00
        LocalTime endTime = portalProperty.getFriendTalkEnableEndTime(); //20:50

        ZonedDateTime now = ZonedDateTime.now();
        ZonedDateTime reservedDateTime = ObjectUtils.isEmpty(reservedDate) ? now : WorkDateTimeUtils.stringToDateTime(reservedDate) ;

        LocalDate date = reservedDateTime.toLocalDate();
        LocalTime time = reservedDateTime.toLocalTime();

        if (now.toLocalDate().isBefore(date) || time.isBefore(startTime) || time.isAfter(endTime)) {
            throw new IllegalArgumentException("Transfer exception time");
        }
    }

    public String store(BizTalkRequestDto dto) throws JsonProcessingException {

        String resultMessage = null;

        Assert.isNull(dto.getId(), "Already Request");

        //친구톡일 때 발송 가능 시간 검증 volka
        if (dto.getPlatform() == PlatformType.kakao_friend_talk) validFriendTalkTime(dto.getReserveDate());

        BizTalkRequest.BizTalkRequestBuilder bizTalkRequestBuilder = BizTalkRequest.builder()
                .platform(dto.getPlatform())
                .status(BizTalkRequestStatus.ready) // 처음 등록시 대기 상태
                .templateId(dto.getTemplateId())
                .branchId(securityUtils.getBranchId())
                .channelId(dto.getChannelId())
                .teamId(securityUtils.getTeamId())
                .customers(dto.getToCustomers())
                .creator(securityUtils.getMemberId())
                .modifier(securityUtils.getMemberId())
                ;

        if (!ObjectUtils.isEmpty(dto.getReserveDate())) {
            ZonedDateTime reserveDate = WorkDateTimeUtils.stringToDateTime(dto.getReserveDate());
            bizTalkRequestBuilder.reserved(reserveDate);

            if (reserveDate.isBefore(ZonedDateTime.now())) {
                throw new IllegalArgumentException("Reservations cannot be made on previous dates.");
            }
        }

        if (!ObjectUtils.isEmpty(dto.getFriendPayload())) {
            bizTalkRequestBuilder.friendPayload(objectMapper.writeValueAsString(dto.getFriendPayload()));
        }

        BizTalkRequest bizTalkRequest = bizTalkRequestBuilder.build();

        BizTalkRequest save = bizTalkRequestRepository.save(bizTalkRequest);

        CounselEnvDto counselEnv = counselEnvService.get(save.getBranchId());

        BranchTeam branchTeam = branchTeamRepository.findByBranchAndTeam(Branch.builder().id(securityUtils.getBranchId()).build(), Team.builder().id(securityUtils.getTeamId()).build());
        switch (save.getPlatform()) {
            case kakao_alert_talk:
                if (counselEnv.getAlertTalkAutoSendEnable()) {
                    modify(BizTalkRequestDto.builder().id(save.getId()).status(BizTalkRequestStatus.auto).build());
                    resultMessage = "알림톡이 발송되었습니다.";
                    break;
                } else {
                    sendRequestTalkAlert(NotificationType.talk_request_approve, "톡 발송 승인 요청", branchTeam.getMember().getId());
                    resultMessage = "알림톡 발송이 접수되었습니다. 매니저 승인 후 메시지가 발송됩니다.";
                    break;
                }
            case kakao_friend_talk:
                if (counselEnv.getFriendTalkAutoSendEnable()) {
                    modify(BizTalkRequestDto.builder().id(save.getId()).status(BizTalkRequestStatus.auto).build());
                    resultMessage = "친구톡이 발송되었습니다.";
                    break;
                } else {
                    sendRequestTalkAlert(NotificationType.talk_request_approve, "톡 발송 승인 요청", branchTeam.getMember().getId());
                    resultMessage = "친구톡 발송이 접수되었습니다. 매니저 승인 후 메시지가 발송됩니다.";
                    break;
                }
        }

        // 예약된 요청
        if (save.getReserved() != null) {
            bizTalkTaskService.saveReserveTalk(save);
        }


        return resultMessage;
    }

    private void sendRequestTalkAlert(NotificationType talk_request_approve, String payload, Long receiver) {
        NotificationDto notificationDto = NotificationDto.builder()
                .target(NotificationTarget.member)
                .icon(NotificationIcon.member)
                .displayType(NotificationDisplayType.toast)
                .type(talk_request_approve)
                .payload(payload)
                .build();
        NotificationInfoDto info = NotificationInfoDto.builder()
                .receiverId(receiver)
                .senderId(securityUtils.getMemberId())
                .build();
        notificationService.store(info, notificationDto, securityUtils.getMemberId());
    }

    //    @PreAuthorize("hasAnyAuthority('WRITE_TALK')")
    public KakaoBizTalkSendResponse modify(BizTalkRequestDto dto) throws JsonProcessingException {
        BizTalkRequest bizTalkRequest = bizTalkRequestRepository.findById(dto.getId()).orElse(null);
        Assert.notNull(bizTalkRequest, "BizTalkRequest not found");

        if (!bizTalkRequest.getStatus().equals(BizTalkRequestStatus.ready)) {
            throw new IllegalArgumentException("It has already been processed.");
        }


        PlatformTemplateDto template = getTemplateDto(bizTalkRequest);

        Assert.notNull(template, "PlatformTemplate not found");

        if (template.getPlatform().equals(PlatformType.kakao_alert_talk) && !template.getStatus().equals(PlatformTemplateStatus.approve)) {
            throw new IllegalArgumentException("Unapproved template.");
        } else if (template.getPlatform().equals(PlatformType.kakao_friend_talk) && !ObjectUtils.isEmpty(template.getId()) && !template.getStatus().equals(PlatformTemplateStatus.approve)) {
            throw new IllegalArgumentException("Unapproved template.");
        }


        bizTalkRequest.setModifier(securityUtils.getMemberId());
        bizTalkRequest.setReasonReject(dto.getReasonReject());
        bizTalkRequest.setStatus(dto.getStatus());
        BizTalkRequest entity = bizTalkRequestRepository.save(bizTalkRequest);

        // 예약된 요청
        if (bizTalkRequest.getReserved() != null) {
            List<BizTalkTask> tasks = bizTalkTaskRepository.findAllByRequestId(entity.getId());
            for (BizTalkTask task : tasks) {
                task.setRequestStatus(entity.getStatus());
                if (entity.getStatus().equals(BizTalkRequestStatus.reject)) {
                    task.setStatus(BizTalkTaskStatus.cancel);
                }
                bizTalkTaskRepository.save(task);
            }
            return null;
        }

        if (entity.getStatus().equals(BizTalkRequestStatus.approve) || entity.getStatus().equals(BizTalkRequestStatus.auto)) {

            List<Long> customerIds = objectMapper.readValue(bizTalkRequest.getCustomers().toString(), new TypeReference<List<Long>>() {
            });

            // 승인했으면 고객 PK로 전화번호 추출
            List<CustomerContact> customerContactList = customerContactRepository.findAllByCustomerIdIn(customerIds);

            List<String> phoneNumbers = customerContactList.stream().filter(item -> item.getType().equals(CustomerContactType.call)).map(item -> item.getPayload()).collect(Collectors.toList());

            // TODO: 2023-08-09 009 수동 승인일 경우 고객에게 소켓알림 전송
            if (BizTalkRequestStatus.approve.equals(entity.getStatus())) {
                sendRequestTalkAlert(NotificationType.talk_approve, "톡 발송 승인", bizTalkRequest.getCreator());
            }

            return sendKakaoBizTalk(bizTalkRequest, template, phoneNumbers);
        } else if (entity.getStatus().equals(BizTalkRequestStatus.reject)) {
            // TODO: 2023-08-09 009 고객에게 반려 소켓알림 전송
            sendRequestTalkAlert(NotificationType.talk_reject, bizTalkRequest.getReasonReject(), bizTalkRequest.getCreator());
            bizTalkHistoryService.rejectHistorySave(bizTalkRequest);
        }
        return null;
    }

    public PlatformTemplateDto getTemplateDto(BizTalkRequest bizTalkRequest) throws JsonProcessingException {
        PlatformTemplateDto template;
        if (bizTalkRequest.getPlatform().equals(PlatformType.kakao_friend_talk)) {

            if (!ObjectUtils.isEmpty(bizTalkRequest.getTemplateId())) {
                template = getTemplate(bizTalkRequest.getTemplateId(), bizTalkRequest);
                if (!ObjectUtils.isEmpty(bizTalkRequest.getFriendPayload())) {
                    KakaoBizMessageTemplatePayload templatePayload = objectMapper.readValue(bizTalkRequest.getFriendPayload(), KakaoBizMessageTemplatePayload.class);
                    template.setPayload(bizTalkRequest.getFriendPayload());
                    template.setMessageType(templatePayload.getBizTalkMessageType());
                    template.setName(templatePayload.getTemplateName());
                    template.setStatus(PlatformTemplateStatus.approve);
                }
            } else {
                template = new PlatformTemplateDto();
                KakaoBizMessageTemplatePayload templatePayload = objectMapper.readValue(bizTalkRequest.getFriendPayload(), KakaoBizMessageTemplatePayload.class);
                template.setPayload(bizTalkRequest.getFriendPayload());
                template.setPlatform(PlatformType.kakao_friend_talk);
                template.setMessageType(templatePayload.getBizTalkMessageType());
                template.setName(templatePayload.getTemplateName());
                template.setStatus(PlatformTemplateStatus.approve);

                ChannelDto channel = channelService.getById(bizTalkRequest.getChannelId());
                template.setSenderProfileKey(channel.getServiceKey());
            }
        } else {
            template = getTemplate(bizTalkRequest.getTemplateId(), bizTalkRequest);
            KakaoBizMessageTemplatePayload templatePayload = objectMapper.readValue(template.getPayload(), KakaoBizMessageTemplatePayload.class);
            template.setMessageType(templatePayload.getBizTalkMessageType());
        }
        return template;
    }

    public KakaoBizTalkSendResponse sendKakaoBizTalk(BizTalkRequest bizTalkRequest, PlatformTemplateDto platformTemplate, List<String> phoneNumbers) throws JsonProcessingException {
        String body;
        Queue<String> cidQueue = new LinkedList<>();
        switch (bizTalkRequest.getPlatform()) {
            case kakao_alert_talk:
                KakaoAlertSendEvent alertSendEvent = KakaoAlertSendEvent.builder()
                        .messageType(platformTemplate.getMessageType())
                        .senderKey(platformTemplate.getSenderProfileKey())
                        .build();

                List<KakaoAlertSendEvent.Message> messageList = new ArrayList<>();
                KakaoBizMessageTemplatePayload alertPayload = objectMapper.readValue(platformTemplate.getPayload(), KakaoBizMessageTemplatePayload.class);
                for (String phoneNumber : phoneNumbers) {
                    String cid = UUID.randomUUID().toString();
                    cidQueue.add(cid);
                    KakaoAlertSendEvent.Message message = KakaoAlertSendEvent.Message.builder()
                            .cid(cid)
                            .templateCode(platformTemplate.getCode())
                            .senderNo("0200001111") // TODO: 2023-06-29 029 나중에 Properties에 있는 번호로 설정?
                            .message(alertPayload.getTemplateContent())
                            .smsMessage(alertPayload.getTemplateContent())
                            .smsType(KakaoAlertSendEvent.Message.SMSType.SM)
                            .build();

                    try {
                        message.setPhoneNumber(fixedEncryptor.decrypt(phoneNumber));
                    } catch (Exception e) {
                        message.setPhoneNumber(phoneNumber);
                    }

                    if (!ObjectUtils.isEmpty(alertPayload.getButtons())) {
                        List<KakaoAlertSendEvent.Message.Button> buttons = new ArrayList<>();
                        for (KakaoBizMessageTemplatePayload.Button button : alertPayload.getButtons()) {
                            buttons.add(KakaoAlertSendEvent.Message.Button.builder()
                                    .name(button.getName())
                                    .type(KakaoAlertSendEvent.Message.Button.ButtonType.valueOf(button.getLinkType().toString()))
                                    .schemeAndroid(button.getLinkAnd())
                                    .schemeIos(button.getLinkIos())
                                    .urlMobile(button.getLinkMo())
                                    .urlPc(button.getLinkPc())
                                    .chatExtra(button.getChatExtra())
                                    .chatEvent(button.getChatEvent())
                                    .pluginId(button.getPluginId())
                                    .relayId(button.getRelayId())
                                    .oneclickId(button.getRelayId())
                                    .productId(button.getProductId())
                                    .target(button.getTarget())
                                    .build());
                        }
                        message.setButton(buttons);
                    }

                    if (!ObjectUtils.isEmpty(alertPayload.getQuickReplies())) {
                        List<KakaoAlertSendEvent.Message.QuickReply> quickReplies = new ArrayList<>();
                        for (KakaoBizMessageTemplatePayload.QuickReply quickReply : alertPayload.getQuickReplies()) {
                            quickReplies.add(KakaoAlertSendEvent.Message.QuickReply.builder()
                                    .name(quickReply.getName())
                                    .type(KakaoAlertSendEvent.Message.QuickReply.QuickReplyType.valueOf(quickReply.getLinkType().toString()))
                                    .schemeAndroid(quickReply.getLinkAnd())
                                    .schemeIos(quickReply.getLinkIos())
                                    .urlMobile(quickReply.getLinkMo())
                                    .urlPc(quickReply.getLinkPc())
                                    .build());
                        }
                        message.setQuickReply(quickReplies);
                    }


                    message.setHeader(alertPayload.getTemplateHeader());

                    if (!ObjectUtils.isEmpty(alertPayload.getTemplateItemHighlight())) {
                        message.setItemHighlight(KakaoAlertSendEvent.Message.ItemHighlight.builder()
                                .title(alertPayload.getTemplateItemHighlight().getTitle())
                                .imageUrl(alertPayload.getTemplateItemHighlight().getImageUrl())
                                .description(alertPayload.getTemplateItemHighlight().getDescription())
                                .build());
                    }


                    if (!ObjectUtils.isEmpty(alertPayload.getTemplateItem()) && !ObjectUtils.isEmpty(alertPayload.getTemplateItem().getList())) {
                        List<KakaoAlertSendEvent.Message.Item.ItemList> itemLists = new ArrayList<>();
                        for (KakaoBizMessageTemplatePayload.TemplateItem.TemplateItemList templateItemList : alertPayload.getTemplateItem().getList()) {
                            itemLists.add(KakaoAlertSendEvent.Message.Item.ItemList.builder()
                                    .title(templateItemList.getTitle())
                                    .description(templateItemList.getDescription())
                                    .build());
                        }

                        message.setItem(KakaoAlertSendEvent.Message.Item.builder()
                                .list(itemLists)
                                .summary(KakaoAlertSendEvent.Message.Item.ItemSummary.builder()
                                        .title(alertPayload.getTemplateItem().getSummary().getTitle())
                                        .description(alertPayload.getTemplateItem().getSummary().getDescription())
                                        .build())
                                .build());
                    }

                    messageList.add(message);
                }
                alertSendEvent.setSendMessages(messageList);
                body = objectMapper.writeValueAsString(alertSendEvent);
                break;
            case kakao_friend_talk:
                KakaoFriendSendEvent sendEvent = KakaoFriendSendEvent.builder()
                        .messageType(platformTemplate.getMessageType())
                        .senderKey(platformTemplate.getSenderProfileKey())
                        .build();
                List<KakaoFriendSendEvent.Message> messages = new ArrayList<>();
                KakaoBizMessageTemplatePayload templatePayload = objectMapper.readValue(platformTemplate.getPayload(), KakaoBizMessageTemplatePayload.class);
                List<KakaoFriendSendEvent.Message.Button> buttons = null;
                if (!ObjectUtils.isEmpty(templatePayload.getButtons())) {
                    buttons = new ArrayList<>();
                    for (KakaoBizMessageTemplatePayload.Button button : templatePayload.getButtons()) {
                        buttons.add(KakaoFriendSendEvent.Message.Button.builder()
                                .name(button.getName())
                                .schemeAndroid(button.getLinkAnd())
                                .schemeIos(button.getLinkIos())
                                .urlMobile(button.getLinkMo())
                                .urlPc(button.getLinkPc())
                                .type(Enum.valueOf(KakaoFriendSendEvent.Message.Button.ButtonType.class, button.getLinkType().toString()))
                                .build());
                    }
                }
                for (String phoneNumber : phoneNumbers) {

                    KakaoFriendSendEvent.Message.Image image = null;

                    if (platformTemplate.getMessageType().equals(BizTalkMessageType.FI) || platformTemplate.getMessageType().equals(BizTalkMessageType.FW)) {
                        image = KakaoFriendSendEvent.Message.Image.builder()
                                .imgUrl(templatePayload.getTemplateImageUrl())
                                .imgLink(templatePayload.getTemplateImageLink())
                                .build();
                    }
                    String cid = UUID.randomUUID().toString();
                    cidQueue.add(cid);
                    KakaoFriendSendEvent.Message message = KakaoFriendSendEvent.Message.builder()
                            .cid(cid)
                            .senderNo("0200001111")
                            .message(templatePayload.getTemplateContent())
                            .button(buttons)
                            .image(image)
                            .smsMessage(templatePayload.getTemplateContent())
                            .smsType(KakaoFriendSendEvent.Message.SMSType.SM)
                            .build();
                    try {
                        message.setPhoneNumber(fixedEncryptor.decrypt(phoneNumber));
                    } catch (Exception e) {
                        message.setPhoneNumber(phoneNumber);
                    }
                    messages.add(message);
                }
                sendEvent.setSendMessages(messages);
                body = objectMapper.writeValueAsString(sendEvent);
                break;
            default:
                log.error("PLATFORM TYPE IS NOT SUPPORT");
                throw new IllegalArgumentException("PLATFORM TYPE IS NOT SUPPORT");
        }
        KakaoBizTalkSendResponse talkSendResponse = null;

        try {
            bizTalkHistoryService.store(bizTalkRequest, cidQueue, BizTalkSendStatus.send);
            talkSendResponse = platformClient.sendBizTalk(body, platformTemplate.getPlatform());
            return talkSendResponse;
        } catch (Exception e) {
            bizTalkHistoryService.fail(cidQueue, BizTalkSendStatus.failed);
            log.error("BIZ TALK REQUEST SERVICE, SEND BIZ TALK, MESSAGE: {}, ERROR: {}", e.getLocalizedMessage(), e);
            return null;
        }
    }

    public PlatformTemplateDto getTemplate(Long templateId, BizTalkRequest request) {
        PlatformTemplateDto result;

        if (!ObjectUtils.isEmpty(templateId)) {
            PlatformTemplate entity = platformTemplateRepository.findById(templateId).orElse(null);
            result = platformTemplateMapper.map(entity);
            result.setPayload(substitution(result.getPayload()));
        } else {
            KakaoBizMessageTemplatePayload templatePayload;
            try {
                templatePayload = objectMapper.readValue(request.getFriendPayload(), KakaoBizMessageTemplatePayload.class);
            } catch (JsonProcessingException e) {
                throw new RuntimeException(e);
            }
            result = new PlatformTemplateDto();
            result.setName(templatePayload.getTemplateName());
            result.setPayload(substitution(request.getFriendPayload()));
        }
        return result;
    }

    private String substitution(String body) {
        //TODO : 프로젝트에 따라 템플릿 내용을 치환해줘야 함
        return body;
    }

    public MemberDto getCreator(Long creator) {
        Member member = memberRepository.findById(creator).orElse(null);
        return memberMapper.map(member);
    }

    private List<CustomerDto> getCustomerList(List<Long> customers) {
        List<Long> customerIds;
        try {
            customerIds = objectMapper.readValue(customers.toString(), new TypeReference<List<Long>>() {
            });
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        List<Customer> allById = customerRepository.findAllByIdIn(customerIds);
        return customerMapper.map(allById);
    }


    public BizTalkRequest findById(Long requestId) {
        return bizTalkRequestRepository.findById(requestId).orElse(null);
    }

    public BizTalkRequest save(BizTalkRequest bizTalkRequest) {
        return bizTalkRequestRepository.save(bizTalkRequest);
    }
}
