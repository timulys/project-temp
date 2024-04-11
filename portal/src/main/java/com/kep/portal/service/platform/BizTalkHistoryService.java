package com.kep.portal.service.platform;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.kep.core.model.dto.customer.CustomerDto;
import com.kep.core.model.dto.issue.IssueCloseType;
import com.kep.core.model.dto.issue.IssueLogStatus;
import com.kep.core.model.dto.issue.IssueStatus;
import com.kep.core.model.dto.issue.IssueType;
import com.kep.core.model.dto.issue.payload.IssuePayload;
import com.kep.core.model.dto.member.MemberDto;
import com.kep.core.model.dto.platform.*;
import com.kep.core.model.dto.platform.kakao.KakaoBizMessageTemplatePayload;
import com.kep.core.model.dto.platform.kakao.KakaoBizTalkSendResponse;
import com.kep.portal.config.property.PortalProperty;
import com.kep.portal.model.dto.platform.*;
import com.kep.portal.model.entity.channel.Channel;
import com.kep.portal.model.entity.customer.Customer;
import com.kep.portal.model.entity.customer.Guest;
import com.kep.portal.model.entity.issue.Issue;
import com.kep.portal.model.entity.issue.IssueLog;
import com.kep.portal.model.entity.member.Member;
import com.kep.portal.model.entity.platform.*;
import com.kep.portal.model.entity.privilege.Level;
import com.kep.portal.repository.customer.GuestRepository;
import com.kep.portal.repository.platform.BizTalkHistoryRepository;
import com.kep.portal.repository.platform.BizTalkRequestRepository;
import com.kep.portal.repository.platform.PlatformTemplateRepository;
import com.kep.portal.service.assign.Assignable;
import com.kep.portal.service.channel.ChannelService;
import com.kep.portal.service.customer.CustomerServiceImpl;
import com.kep.portal.service.customer.GuestService;
import com.kep.portal.service.issue.IssueLogService;
import com.kep.portal.service.issue.IssueService;
import com.kep.portal.service.member.MemberService;
import com.kep.portal.util.SecurityUtils;
import com.kep.portal.util.ZonedDateTimeUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.util.ObjectUtils;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import javax.transaction.Transactional;
import javax.validation.constraints.NotNull;
import java.io.OutputStream;
import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
@Transactional
public class BizTalkHistoryService {

    @Resource
    private BizTalkHistoryRepository bizTalkHistoryRepository;

    @Resource
    private BizTalkHistoryMapper bizTalkHistoryMapper;
    @Resource
    private ObjectMapper objectMapper;

    @Resource
    private MemberService memberService;
    @Resource
    private BizTalkRequestRepository bizTalkRequestRepository;
    @Resource
    private PlatformTemplateRepository platformTemplateRepository;
    @Resource
    private PlatformTemplateMapper platformTemplateMapper;
    @Resource
    private SecurityUtils securityUtils;
    @Resource
    private CustomerServiceImpl customerService;
    @Resource
    private GuestService guestService;
    @Resource
    private ChannelService channelService;

    @Resource
    private IssueService issueService;
    @Resource
    private IssueLogService issueLogService;
    @Resource
    private PortalProperty portalProperty;

    /**
     * 톡 발송 이력 성공 / 실패 (합계)
     * @param from
     * @param to
     * @param branchId
     * @param teamId
     * @param memberId
     * @return
     */
    public BizTalkHistoryStatisticsDto sum(@NotNull LocalDate from , @NotNull LocalDate to , Long branchId , Long teamId , Long memberId , PlatformType type){
        ZonedDateTime start = ZonedDateTimeUtil.start(from.toString());
        ZonedDateTime end = ZonedDateTimeUtil.end(to.toString());
        List<BizTalkHistory> entities = bizTalkHistoryRepository.statisticsSuccessFail(start , end , branchId , teamId , memberId , type);

        List<BizTalkHistoryStatisticsSuccessFailDto> successFails = this.successFails(entities);
        BizTalkHistoryStatisticsSuccessFailDto successFail = BizTalkHistoryStatisticsSuccessFailDto.builder()
                .succeed(0L)
                .failed(0L)
                .build();

        if(!ObjectUtils.isEmpty(successFails)){
            for (BizTalkHistoryStatisticsSuccessFailDto dto : successFails){
                successFail.setSucceed(successFail.getSucceed() + dto.getSucceed());
                successFail.setFailed(successFail.getFailed() + dto.getFailed());
            }
        }


        List<BizTalkHistoryStatisticsTemplateDto> templates = this.templates(entities);

        if(!ObjectUtils.isEmpty(templates)){
            templates.forEach(item->item.setTemplateSums(null));
        }

        return BizTalkHistoryStatisticsDto.builder()
                .from(from)
                .to(to)
                .type(type)
                .successFail(successFail)
                .templates(templates)
                .build();
    }

    /**
     * 톡 발송 이력 성공 / 실패 (대시보드)
     * @param from
     * @param to
     * @param branchId
     * @param teamId
     * @param memberId
     * @return
     */
    public BizTalkHistoryStatisticsDto statistics(@NotNull LocalDate from , @NotNull LocalDate to
            , Long branchId , Long teamId , Long memberId
            , PlatformType type)
    {
        ZonedDateTime start = ZonedDateTimeUtil.start(from.toString());
        ZonedDateTime end = ZonedDateTimeUtil.end(to.toString());
        List<BizTalkHistory> entities = bizTalkHistoryRepository.statisticsSuccessFail(start , end , branchId , teamId , memberId , type);

        List<BizTalkHistoryStatisticsSuccessFailDto> successFails = this.successFails(entities);
        List<BizTalkHistoryStatisticsTemplateDto>  templates = this.templates(entities);

        return BizTalkHistoryStatisticsDto.builder()
                .from(from)
                .to(to)
                .type(type)
                .successFails(successFails)
                .templates(templates)
                .build();

    }

    /**
     * 톡 발송 상세 (대시보드)
     * @param entities
     * @return
     */
    private List<BizTalkHistoryStatisticsTemplateDto> templates(List<BizTalkHistory> entities){
        List<BizTalkHistoryStatisticsTemplateDto> templates = new ArrayList<>();

        //승인된 template
        List<PlatformTemplate> platformTemplates = platformTemplateRepository.findAllByStatus(PlatformTemplateStatus.approve);

        //친구톡은 템플릿 사용안해도 가능함
        platformTemplates.add(PlatformTemplate.builder()
                .id(0L)
                .name("기타")
                .build());

        //templateId null -> long
        entities.forEach(item->item.setTemplateId(item.getTemplateId() != null ? item.getTemplateId() : 0L));
        for (PlatformTemplate platformTemplate : platformTemplates){

            BizTalkHistoryStatisticsTemplateDto dto = BizTalkHistoryStatisticsTemplateDto.builder()
                    .templateId(platformTemplate.getId())
                    .title(platformTemplate.getName())
                    .total(0L)
                    .build();


            Map<LocalDate , Long> templateSums = entities.stream()
                    .filter(item -> item.getTemplateId().equals(platformTemplate.getId()))
                    .collect(Collectors.groupingBy(item -> item.getSendDate().toLocalDate(), Collectors.counting()));

            List<BizTalkHistoryStatisticsTemplateSumDto> templateSumDtos = new ArrayList<>();
            if(!ObjectUtils.isEmpty(templateSums)){
                for (Map.Entry<LocalDate , Long> entry : templateSums.entrySet()){
                    templateSumDtos.add(BizTalkHistoryStatisticsTemplateSumDto.builder()
                            .created(entry.getKey())
                            .template(entry.getValue())
                            .build());
                    dto.setTotal(dto.getTotal() + entry.getValue());
                }
            }

            dto.setTemplateSums(templateSumDtos);
            templates.add(dto);

        }
        return templates;
    }

    /**
     * 톡 발송 성공 / 실패
     * @param entities
     * @return
     */
    private List<BizTalkHistoryStatisticsSuccessFailDto> successFails(List<BizTalkHistory> entities){
        List<BizTalkHistoryStatisticsSuccessFailDto> successFails = new ArrayList<>();
        if(!ObjectUtils.isEmpty(entities)){
            for (BizTalkHistory entity : entities) {
                LocalDate created = entity.getSendDate().toLocalDate();
                BizTalkHistoryStatisticsSuccessFailDto dto = successFails.stream()
                        .filter(item -> item.getCreated().equals(created))
                        .findFirst().orElse(null);

                if (ObjectUtils.isEmpty(dto)) {
                    successFails.add(BizTalkHistoryStatisticsSuccessFailDto.builder()
                            .created(created)
                            .succeed(entity.getStatus().equals(BizTalkSendStatus.succeed) ? 1L : 0L)
                            .failed(entity.getStatus().equals(BizTalkSendStatus.failed) ? 1L : 0L)
                            .build());
                } else {
                    dto.setSucceed(entity.getStatus().equals(BizTalkSendStatus.succeed) ? dto.getSucceed() + 1L : dto.getSucceed());
                    dto.setFailed(entity.getStatus().equals(BizTalkSendStatus.failed) ? dto.getFailed() + 1L : dto.getFailed());
                }
            }
        }
        return successFails;
    }



    public void modify(KakaoBizTalkSendResponse dto) {
        List<String> cidList = dto.getResults().stream().map(item -> item.getCid()).collect(Collectors.toList());


        log.info("PLATFORM TO PORTAL: {}", dto);

        List<BizTalkHistory> histories = bizTalkHistoryRepository.findAllByCidIn(cidList);
        for (BizTalkHistory history : histories) {

            KakaoBizTalkSendResponse.Result result = dto.getResults().stream().filter(item -> item.getCid().equals(history.getCid())).findFirst().orElse(null);
            Assert.notNull(result, "Not Found Cid");
            history.setMessageId(result.getUid());

            switch (result.getCode()) {
                case "API_100":
                    history.setStatus(BizTalkSendStatus.send);
                    break;
                case "API_200":
                    history.setStatus(BizTalkSendStatus.succeed);
                    break;
                default:
                    history.setStatus(BizTalkSendStatus.failed);
                    history.setDetail(getFailMessage(result.getKko_status_code(), result.getKko_message()));
                    break;
            }

        }
        bizTalkHistoryRepository.saveAll(histories);

    }

    // TODO: 2023-06-29 029 yml 
    private String getFailMessage(String kko_status_code, String kko_message) {
        switch (kko_status_code) {
            case "1030":
                return "파라미터가 잘못됐습니다.";
            case "3008":
                return "전화번호 오류";
            case "3013":
                return "메시지가 비어있습니다";
            case "3014":
                return "메시지 길이 제한 오류(텍스트 타입 1000자 초과, 이미지 타입 400자 초과)";
            case "3015":
                return "템플릿을 찾을 수 없습니다.";
            case "3016":
                return "메시지 내용이 템플릿과 일치하지 않습니다.";
            case "3018":
                return "메시지를 전송할 수 없습니다";
            case "3022":
                return "메시지 발송 가능한 시간이 아님(친구톡/마케팅 메시지는 08시부터 20시까지 발송 가능)";
            case "602":
                return "발송할 수 없는 이미지 사이즈";
        }

        return kko_message;
    }

    public void store(BizTalkRequest bizTalkRequest, Queue<String> cidQueue, BizTalkSendStatus status) throws JsonProcessingException {

        List<BizTalkHistory> entities = new ArrayList<>();
        List<Long> customerIds = objectMapper.readValue(bizTalkRequest.getCustomers().toString(), new TypeReference<List<Long>>() {
        });
        for (Long customerId : customerIds) {
            entities.add(BizTalkHistory.builder()
                    .type(bizTalkRequest.getPlatform())
                    .status(status)
                    .requestId(bizTalkRequest.getId())
                    .branchId(bizTalkRequest.getBranchId())
                    .teamId(bizTalkRequest.getTeamId())
                    .creator(bizTalkRequest.getCreator())
                    .customerId(customerId)
                    .templateId(bizTalkRequest.getTemplateId())
                    .sendDate(ZonedDateTime.now())
                    .cid(cidQueue.poll())
                    .build());

            List<Guest> gusetList = guestService.findAllByCustomerId(customerId);

            Issue issue = Issue.builder()
                    .branchId(bizTalkRequest.getBranchId())
                    .created(ZonedDateTime.now())
                    .modified(ZonedDateTime.now())
                    .status(IssueStatus.close)
                    .type(IssueType.info)
                    .closeType(IssueCloseType.system)
                    .channel(getChannel(bizTalkRequest.getChannelId()))
                    // 싱크인증 한 게스트가 없으면 임시로 게스트 생성
                    .guest(gusetList.isEmpty() ? getGuest(customerId, bizTalkRequest.getChannelId()) : gusetList.get(gusetList.size() - 1))
                    .member(getMember(bizTalkRequest.getCreator()))
                    .customerId(customerId)
                    .teamId(bizTalkRequest.getTeamId())
                    .build();

            Issue issueEntity = issueService.save(issue);
            String templateContent;
            if (ObjectUtils.isEmpty(bizTalkRequest.getFriendPayload())) {
                PlatformTemplateDto template = getTemplate(bizTalkRequest.getTemplateId(), bizTalkRequest.getId());
                KakaoBizMessageTemplatePayload templatePayload = objectMapper.readValue(template.getPayload(), KakaoBizMessageTemplatePayload.class);
                templateContent = templatePayload.getTemplateContent();
            } else {
                KakaoBizMessageTemplatePayload templatePayload = objectMapper.readValue(bizTalkRequest.getFriendPayload(), KakaoBizMessageTemplatePayload.class);
                templateContent = templatePayload.getTemplateContent();
            }

            List<IssuePayload.Chapter> chapters = new ArrayList<>();
            List<IssuePayload.Section> sections = new ArrayList<>();
            sections.add(IssuePayload.Section.builder()
                    .type(IssuePayload.SectionType.text)
                    .data(templateContent)
                    .build());
            chapters.add(IssuePayload.Chapter.builder()
                    .sections(sections)
                    .build());

            IssuePayload issuePayload = IssuePayload.builder()
                    .chapters(chapters)
                    .build();

            IssueLog issueLog = IssueLog.builder()
                    .created(ZonedDateTime.now())
                    .creator(portalProperty.getSystemMemberId())
                    .issueId(issueEntity.getId())
                    .payload(objectMapper.writeValueAsString(issuePayload))
                    .status(IssueLogStatus.send)
                    .build();

            IssueLog issueLogEntity = issueLogService.save(issueLog);

            issue.setLastIssueLog(issueLogEntity);
            issueService.save(issue);
        }

        bizTalkHistoryRepository.saveAll(entities);
    }

    private Member getMember(Long creator) {
        return memberService.findById(creator);
    }

    private Guest getGuest(Long customerId, Long channelId) {
        Guest guest = guestService.findOne(Example.of(Guest.builder().customer(Customer.builder().id(customerId).build()).build()));
        if (ObjectUtils.isEmpty(guest)) {
            guest = guestService.save(Guest.builder()
                    .channelId(channelId)
                    .created(ZonedDateTime.now())
                    .userKey(customerId.toString()) // TODO: 2023-06-09 009 issue 생성할 때 guest가 없으면 생성해야 되는데 userKey는 어떻게 하지?
                    .customer(getCustomer(customerId))
                    .build());
        }
        return guest;
    }

    private Channel getChannel(Long channelId) {
        return channelService.findById(channelId);
    }

    public Page<BizTalkHistoryDto> search(BizTalkHistoryCondition condition, Pageable pageable) {
        if (securityUtils.hasRole(Level.ROLE_OPERATOR)) {
            condition.setMemberId(securityUtils.getMemberId());
        }

        if ("phone".equals(condition.getKeywordType())) {
            condition.setKeyword(phoneFormat(condition.getKeyword()).replace("010-", "+82 10-"));
        }

        if(condition.getStatus() == null){
            return new PageImpl<>(Collections.emptyList());
        }

        Page<BizTalkHistory> search = bizTalkHistoryRepository.search(condition, pageable);
        List<BizTalkHistoryDto> list = new ArrayList<>();

        for (BizTalkHistory history : search) {
            BizTalkHistoryDto dto = bizTalkHistoryMapper.map(history);

            dto.setTemplate(getTemplate(history.getTemplateId(), dto.getRequestId()));
            dto.setCustomer(getCustomerDto(history.getCustomerId()));
            dto.setCreator(getCreator(history.getCreator()));

            BizTalkRequest byId = bizTalkRequestRepository.findById(history.getRequestId()).orElse(null);
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

    public void fail(Queue<String> cidQueue, BizTalkSendStatus failed) {
        List<String> cidList = new ArrayList<>(cidQueue);
        List<BizTalkHistory> list = bizTalkHistoryRepository.findAllByCidIn(cidList);

        for (BizTalkHistory history : list) {
            history.setStatus(failed);
        }
        bizTalkHistoryRepository.saveAll(list);
    }

    private MemberDto getCreator(Long creator) {
        Member member = getMember(creator);
        return MemberDto.builder().id(member.getId()).nickname(member.getNickname()).build();
    }

    private CustomerDto getCustomerDto(Long customerId) {
        Customer customer = customerService.findById(customerId);
        if (customer == null) {
            return null;
        }
        return CustomerDto.builder().id(customer.getId()).name(customer.getName()).build();
    }

    private Customer getCustomer(Long customerId) {
        return customerService.findById(customerId);
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

    public void save(BizTalkHistory entity) {
        bizTalkHistoryRepository.save(entity);
    }

    public void rejectHistorySave(BizTalkRequest bizTalkRequest) {
        try {
            List<Long> customerIds = objectMapper.readValue(bizTalkRequest.getCustomers().toString(), new TypeReference<List<Long>>() {
            });

            List<BizTalkHistory> entities = new ArrayList<>();
            for (Long customerId : customerIds) {
                entities.add(BizTalkHistory.builder()
                        .type(bizTalkRequest.getPlatform())
                        .status(BizTalkSendStatus.failed)
                        .requestId(bizTalkRequest.getId())
                        .branchId(bizTalkRequest.getBranchId())
                        .detail(bizTalkRequest.getReasonReject())
                        .teamId(bizTalkRequest.getTeamId())
                        .creator(bizTalkRequest.getCreator())
                        .customerId(customerId)
                        .templateId(bizTalkRequest.getTemplateId())
                        .sendDate(ZonedDateTime.now())
                        .cid(UUID.randomUUID().toString())
                        .build());
            }
            bizTalkHistoryRepository.saveAll(entities);
        } catch (JsonProcessingException e) {
            log.error(e.getLocalizedMessage(), e);
        }
    }

    public void portalHistoryDownload(HttpServletResponse res, BizTalkHistoryCondition condition) {
        try {
            Workbook workbook = new XSSFWorkbook();
            Sheet sheet = workbook.createSheet("상담포털 톡 발송이력");
            String[] headerName = new String[]{"ID", "메시지 ID", "구분", "템플릿명", "성공여부", "실패원인", "발송일시"};

            Row headerRow = sheet.createRow(0);
            sheet.setDefaultColumnWidth(18);
            for (int i = 0; i < headerName.length; i++) {
                headerRow.createCell(i).setCellValue(headerName[i]);
            }

            int rowIndex = 1;
            int page = 0;
            final int pageSize = 15;
            boolean hasNext = true;
            Row row = null;
            Cell cell = null;
            int localIndex = 1;

            try {
                while (hasNext) {
                    PageRequest pageable = PageRequest.of(page++, pageSize, Sort.Direction.DESC, "created");
                    Page<BizTalkHistoryDto> search = search(condition, pageable);
                    hasNext = search.hasNext();
                    for (BizTalkHistoryDto his : search) {
                        row = sheet.createRow(rowIndex++);
                        cell = row.createCell(0);
                        cell.setCellValue(localIndex);
                        cell = row.createCell(1);
                        cell.setCellValue(his.getMessageId());
                        cell = row.createCell(2);
                        if (his.getType().equals(PlatformType.kakao_alert_talk)) {
                            cell.setCellValue("알림톡");
                        } else if (his.getType().equals(PlatformType.kakao_friend_talk)) {
                            cell.setCellValue("친구톡");
                        } else if (his.getType().equals(PlatformType.kakao_counsel_talk)) {
                            cell.setCellValue("상담톡");
                        }
                        cell = row.createCell(3);
                        cell.setCellValue(his.getTemplate().getName());
                        cell = row.createCell(4);
                        if (his.getStatus().equals(BizTalkSendStatus.send)) {
                            cell.setCellValue("발송중");
                        } else if (his.getStatus().equals(BizTalkSendStatus.succeed)) {
                            cell.setCellValue("성공");
                        } else if (his.getStatus().equals(BizTalkSendStatus.failed)) {
                            cell.setCellValue("실패");
                        }
                        cell = row.createCell(5);
                        cell.setCellValue(his.getDetail());
                        cell = row.createCell(6);
                        ZonedDateTime zonedDateTime = ZonedDateTime.parse(his.getSendDate().toString());
                        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss z");
                        cell.setCellValue(zonedDateTime.format(formatter));
                        localIndex++;
                    }
                }
            } catch (Exception e) {
                log.error("PORTAL ADD TALK HISTORY TO EXCEL ERROR: {}", e.getLocalizedMessage(), e);
            }

            String fileName = "portal_talk_history_backup";

            res.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
            res.setHeader("Content-Disposition", "attachment; filename=" + fileName + ".xlsx");

            OutputStream outputStream = res.getOutputStream();
            workbook.write(outputStream);
            outputStream.close();

            workbook.close();
        } catch (Exception e) {
            log.error("PORTAL TALK HISTORY EXCEL DOWNLOAD ERROR: {}", e.getLocalizedMessage(), e);
        }

    }

    public void managementHistoryDownload(HttpServletResponse res, BizTalkHistoryCondition condition) {
        try {
            Workbook workbook = new XSSFWorkbook();
            Sheet sheet = workbook.createSheet("상담관리 톡 발송이력");
            String[] headerName = new String[]{"ID", "발송자", "메시지 ID", "구분", "템플릿명", "성공여부", "실패원인", "고객명", "발송일시"};

            Row headerRow = sheet.createRow(0);
            sheet.setDefaultColumnWidth(18);
            for (int i = 0; i < headerName.length; i++) {
                headerRow.createCell(i).setCellValue(headerName[i]);
            }

            int rowIndex = 1;
            int page = 0;
            final int pageSize = 15;
            boolean hasNext = true;
            Row row = null;
            Cell cell = null;
            int localIndex = 1;

            try {
                while (hasNext) {
                    PageRequest pageable = PageRequest.of(page++, pageSize, Sort.Direction.DESC, "created");
                    Page<BizTalkHistoryDto> search = search(condition, pageable);
                    hasNext = search.hasNext();
                    for (BizTalkHistoryDto his : search) {
                        row = sheet.createRow(rowIndex++);
                        cell = row.createCell(0);
                        cell.setCellValue(localIndex);
                        cell = row.createCell(1);
                        cell.setCellValue(his.getCreator().getNickname());
                        cell = row.createCell(2);
                        cell.setCellValue(his.getMessageId());
                        cell = row.createCell(3);
                        if (his.getType().equals(PlatformType.kakao_alert_talk)) {
                            cell.setCellValue("알림톡");
                        } else if (his.getType().equals(PlatformType.kakao_friend_talk)) {
                            cell.setCellValue("친구톡");
                        } else if (his.getType().equals(PlatformType.kakao_counsel_talk)) {
                            cell.setCellValue("상담톡");
                        }
                        cell = row.createCell(4);
                        cell.setCellValue(his.getTemplate().getName());
                        cell = row.createCell(5);
                        if (his.getStatus().equals(BizTalkSendStatus.send)) {
                            cell.setCellValue("발송중");
                        } else if (his.getStatus().equals(BizTalkSendStatus.succeed)) {
                            cell.setCellValue("성공");
                        } else if (his.getStatus().equals(BizTalkSendStatus.failed)) {
                            cell.setCellValue("실패");
                        }
                        cell = row.createCell(6);
                        cell.setCellValue(his.getDetail());
                        cell = row.createCell(7);
                        cell.setCellValue(his.getCustomer().getName());
                        cell = row.createCell(8);
                        ZonedDateTime zonedDateTime = ZonedDateTime.parse(his.getSendDate().toString());
                        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss z");
                        cell.setCellValue(zonedDateTime.format(formatter));
                        localIndex++;
                    }
                }
            } catch (Exception e) {
                log.error("MANAGEMENT ADD TALK HISTORY TO EXCEL ERROR: {}", e.getLocalizedMessage(), e);
            }

            String fileName = "management_talk_history_backup";

            res.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
            res.setHeader("Content-Disposition", "attachment; filename=" + fileName + ".xlsx");

            OutputStream outputStream = res.getOutputStream();
            workbook.write(outputStream);
            outputStream.close();

            workbook.close();
        } catch (Exception e) {
            log.error("MANAGEMENT TALK HISTORY EXCEL DOWNLOAD ERROR: {}", e.getLocalizedMessage(), e);
        }

    }
}
