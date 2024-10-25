package com.kep.portal.service.issue.event;

import java.io.File;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.annotation.Nullable;
import javax.annotation.Resource;
import javax.transaction.Transactional;
import javax.validation.constraints.NotNull;

import com.kep.core.model.dto.env.CounselEnvDto;
import com.kep.core.model.dto.system.SystemEnvEnum;
import com.kep.core.model.dto.upload.UploadDto;
import com.kep.core.model.dto.upload.UploadHistoryDto;
import com.kep.portal.model.entity.channel.ChannelEnv;
import com.kep.portal.service.env.CounselEnvService;
import com.kep.portal.service.upload.UploadHistoryService;
import com.kep.portal.service.upload.UploadService;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.util.ObjectUtils;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kep.core.model.dto.channel.ChannelEnvDto;
import com.kep.core.model.dto.issue.IssueCloseType;
import com.kep.core.model.dto.issue.IssueDto;
import com.kep.core.model.dto.issue.IssueLogDto;
import com.kep.core.model.dto.issue.IssueLogStatus;
import com.kep.core.model.dto.issue.IssueStatus;
import com.kep.core.model.dto.issue.payload.IssuePayload;
import com.kep.core.model.dto.platform.PlatformType;
import com.kep.core.model.dto.upload.UploadPlatformRequestDto;
import com.kep.portal.client.PlatformClient;
import com.kep.portal.config.property.ModeProperty;
import com.kep.portal.config.property.SocketProperty;
import com.kep.portal.model.entity.issue.Issue;
import com.kep.portal.model.entity.issue.IssueLog;
import com.kep.portal.model.entity.issue.IssueLogMapper;
import com.kep.portal.model.entity.issue.IssueMapper;
import com.kep.portal.service.channel.ChannelEnvService;
import com.kep.portal.service.issue.IssueLogService;
import com.kep.portal.service.issue.IssueService;
import com.kep.portal.service.statistics.StatisticsService;
import com.kep.portal.util.SecurityUtils;
import com.kep.portal.util.UploadUtils;

import lombok.extern.slf4j.Slf4j;

/**
 * 상담원 이벤트
 */
@Service
@Transactional
@Slf4j
public class EventByOperatorService {

    @Resource
    private SocketProperty socketProperty;
    @Resource
    private SimpMessagingTemplate simpMessagingTemplate;
    @Resource
    private PlatformClient platformClient;
    @Resource
    private SecurityUtils securityUtils;
    @Resource
    private IssueService issueService;
    @Resource
    private IssueMapper issueMapper;
    @Resource
    private IssueLogService issueLogService;
    @Resource
    private IssueLogMapper issueLogMapper;
    @Resource
    private ChannelEnvService channelEnvService;
    @Resource
    private CounselEnvService counselEnvService;
    @Resource
    private EventBySystemService eventBySystemService;
    @Resource
    private StatisticsService statisticsService;

    @Resource
    private ModeProperty modeProperty;
    @Resource
    private UploadUtils uploadUtils;
    @Resource
    private ObjectMapper objectMapper;

    @Resource
    private UploadService uploadService;

    @Resource
    private UploadHistoryService uploadHistoryService;

    /**
     * routing: /issue.{id}.message
     */
    public IssueDto message(Long issueId, IssuePayload issuePayload) throws Exception {

        // 이슈 검색
        Issue issue = issueService.findById(issueId);
        Assert.notNull(issue, "ISSUE NOT FOUND");

        // 배정 확인
        Assert.notNull(issue.getMember(), "NOT ASSIGNED");
        Assert.isTrue(issue.getMember().getId().equals(securityUtils.getMemberId()), "WRONG ASSIGNED");

        // 배정전이나 종료된 이슈에는 메세지를 보낼 수 없음
        // TODO: Issue.canReply()
        if (IssueStatus.open.equals(issue.getStatus())
                || IssueStatus.close.equals(issue.getStatus())) {
            // TODO: error code
            throw new IllegalStateException("ISSUE STATUS IS " + issue.getStatus().name() + ", NEED TO ASSIGN");
        }

        List<IssueLogDto> issueLogs = new ArrayList<>();

        // 자동메세지 (상담시작 공통 인사말)
        if (IssueStatus.assign.equals(issue.getStatus())) {
            IssueLog welcomeMessage = eventBySystemService.saveWelcome(issue);
            if (welcomeMessage != null) {
                issueLogs.add(issueLogMapper.map(welcomeMessage));
            }
            // 통계 데이터 수집 응답
            issue.setMemberFirstAsked(ZonedDateTime.now());
        }

        // TODO: 2023-08-18 018 issuePayload에 있는 변수 치환?
        for (IssuePayload.Chapter chapter : issuePayload.getChapters()) {
            for (IssuePayload.Section section : chapter.getSections()) {
                switch (section.getType()) {
                    case action:
                        for (IssuePayload.Action action : section.getActions()) {
                            Long memberId = securityUtils.getMemberId();
                            action.setData(action.getData().replaceAll("\\$\\{멤버번호\\}", memberId.toString()));
                            action.setData(action.getData().replaceAll("\\$\\{상담번호\\}", issueId.toString()));
                        }
                }
            }
        }

        // 이벤트 (메세지) 저장
        IssueLog issueLog = IssueLog.builder()
                .issueId(issue.getId())
                .status(IssueLogStatus.send)
                .payload(objectMapper.writeValueAsString(issuePayload))
                .creator(securityUtils.getMemberId())
                .created(ZonedDateTime.now())
                .build();

        issueLog = issueLogService.save(issueLog, issue.getMember());
        issueLogs.add(issueLogMapper.map(issueLog));

        // 이슈 상태 변경
        if (IssueStatus.assign.equals(issue.getStatus())
                || IssueStatus.ask.equals(issue.getStatus())
                || IssueStatus.urgent.equals(issue.getStatus())) {
            issue.setStatus(IssueStatus.reply);
            issue.setStatusModified(ZonedDateTime.now());
        }

        // 미답변 카운트, 시간 초기화
        issue.setAskCount(0L);
        issue.setFirstAsked(null);
        // 상담원 메세지도 마지막 메세지로 저장
        issue.setLastIssueLog(issueLog);
        issue.setModified(ZonedDateTime.now());
        issue = issueService.save(issue);
        issueService.joinIssueSupport(issue);
        IssueDto issueDto = issueMapper.map(issue);
        issueDto.setLastIssueLog(issueLogs.get(issueLogs.size() - 1));

        // 플랫폼으로 이벤트 전송
        platformClient.message(issueDto, issueLogs);
        // 소켓으로 이슈 전송
        simpMessagingTemplate.convertAndSend(socketProperty.getIssuePath(), issueDto);
        // 소켓으로 이벤트 전송
        for (IssueLogDto issueLogDto : issueLogs) {
            simpMessagingTemplate.convertAndSend(socketProperty.getIssuePath() +
                    "." + issue.getId() + "." + "message", issueLogDto);
        }

        return issueDto;
    }

    /**
     * 상담 종료 안내 (by 상담원)
     */
    public IssueDto warningClose(@NotNull Long issueId, Map<String, Object> options)
            throws Exception {

        // 이슈 검색
        Issue issue = issueService.findById(issueId);
        Assert.notNull(issue, "ISSUE NOT FOUND");

        // 이미 종료된 이슈
        if (IssueStatus.close.equals(issue.getStatus())) {
            log.warn("ISSUE IS ALREADY CLOSED, ID: {}", issueId);
            return issueMapper.map(issue);
        }

        // 배정 확인
        Assert.notNull(issue.getMember(), "NOT ASSIGNED");
        Assert.isTrue(issue.getMember().getId().equals(securityUtils.getMemberId()), "WRONG ASSIGNED");

        // 종료 안내 메세지 발송
        ChannelEnvDto channelEnv = channelEnvService.getByChannel(issue.getChannel());
        // 이슈 상태 변경
        if (IssueStatus.assign.equals(issue.getStatus())
                || IssueStatus.ask.equals(issue.getStatus())
                || IssueStatus.urgent.equals(issue.getStatus())) {
            issue.setStatus(IssueStatus.reply);
            issue.setStatusModified(ZonedDateTime.now());
            issue = issueService.save(issue);
        }

        eventBySystemService.sendClose(issue, channelEnv);
        return issueMapper.map(issue);
    }

    /**
     * 상담 종료 (by 상담원)
     *
     * @생성일자 / 만든사람		 / 수정내용
     * 2023.05.09 / philip.lee   / 상담원 강제종료
     */
    public IssueDto close(@NotNull Long issueId, Map<String, Object> options)
            throws Exception {

        // 이슈 검색
        Issue issue = issueService.findById(issueId);
        Assert.notNull(issue, "ISSUE NOT FOUND");

        // 이미 종료된 이슈
        if (IssueStatus.close.equals(issue.getStatus())) {
            log.warn("ISSUE IS ALREADY CLOSED, ID: {}", issueId);
        }

        // 배정 확인
        Assert.notNull(issue.getMember(), "NOT ASSIGNED");
        Assert.isTrue(issue.getMember().getId().equals(securityUtils.getMemberId()), "WRONG ASSIGNED");

        // 종료시, 함께 보내야할 메세지

        // 종료 안내 메세지 발송
        ChannelEnvDto channelEnv = channelEnvService.getByChannel(issue.getChannel());

        IssuePayload issuePayload;
        try {
            issuePayload = channelEnv.getEnd().getGuide().getNoticeMessage();
        } catch (Exception e) {
            log.warn(e.getLocalizedMessage());
            issuePayload = new IssuePayload("고객님 다음 상담을 위해 상담을 종료합니다." +
                    " 궁금하신 내용이 생기시면 언제든 다시 문의주시기 바라며," +
                    " 오늘도 행복한 하루되세요^^");
        }

        eventBySystemService.close(issue, issuePayload, true);

        return issueMapper.map(issue);
    }

    /**
     * 파일 전송
     */
    @Nullable
    public String upload(@NotNull Long issueId, @NotNull MultipartFile multipartFile) throws Exception {
        Issue issue = issueService.findById(issueId);
        IssueDto issueDto = issueMapper.map(issue);
        Assert.notNull(issueDto, "issue not found");

        CounselEnvDto counselEnvDto = counselEnvService.get(issue.getBranchId());
        Assert.isTrue(counselEnvDto.getIssueFileMimeType().getEnabled(), "Disable File Transfer");
        String mimeType = uploadUtils.getMimeType(multipartFile);

        String serviceKey = issue.getChannel().getServiceKey();
        PlatformType platformType = issue.getChannel().getPlatform();

        log.info("serviceKey: {}, platformType: {}", serviceKey, platformType);

        File file = uploadUtils.upload(multipartFile);
        Assert.notNull(file, "file is null");
        // TODO: if existStorage,
        UploadPlatformRequestDto uploadPlatformRequestDto = UploadPlatformRequestDto.builder()
                .sourcePath(file.getAbsolutePath())
                .build();
        // else if canConnectPortalFromPlatform,
        // else, request form data

        log.info("uploadPlatformRequestDto: {}",uploadPlatformRequestDto);

        // [KICA-290] - 모든 파일 업로드 가능하도록 임시 협의
        String platformUploadUrl = platformClient.uploadFile(issueMapper.map(issue), uploadPlatformRequestDto);
        String sourceUrl = convertURL(platformUploadUrl);
        /*
        if (uploadUtils.isImage(multipartFile)) {
            platformUploadUrl = platformClient.uploadImage(issueMapper.map(issue), uploadPlatformRequestDto);
            sourceUrl = platformUploadUrl;
            // sourceUrl = convertURL(sourceUrl);

        } else {
            platformUploadUrl = platformClient.uploadFile(issueMapper.map(issue), uploadPlatformRequestDto);
            sourceUrl = platformUploadUrl;
            // sourceUrl = convertURL(sourceUrl);
        }
         */
        log.info("platformUploadUrl: {}", sourceUrl);

        //upload save
        UploadDto uploadDto = UploadDto.builder()
                .type("portal")
                .originalName(multipartFile.getOriginalFilename())
                .name(file.getName())
                .mimeType(mimeType)
                .build();

        UploadDto uploadStore = uploadService.store(uploadDto, file);

        //upload-history save
        UploadHistoryDto uploadHistoryDto = UploadHistoryDto.builder()
                .fileName(uploadStore.getOriginalName())
                .mimeType(uploadStore.getMimeType())
                .size(uploadStore.getSize())
                .url(uploadStore.getUrl())
                .build();

        uploadHistoryService.store(uploadHistoryDto, issue);

        return sourceUrl;
    }

    public static String convertURL(String originalURL) {
        // 정규표현식을 사용하여 "https://dn.api1.kage.kakao.com/dn/DNdiq" 부분을 추출
        String pattern = "dn.api1.kage.kakao.com/dn/([A-Za-z0-9]+)";
        originalURL = originalURL.replaceAll(pattern, "dn-m.talk.kakao.com/talkm");

        return originalURL;
    }

}
