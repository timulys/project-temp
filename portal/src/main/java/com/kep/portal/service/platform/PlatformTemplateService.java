package com.kep.portal.service.platform;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kep.core.model.dto.member.MemberDto;
import com.kep.core.model.dto.platform.BizTalkMessageType;
import com.kep.core.model.dto.platform.PlatformTemplateDto;
import com.kep.core.model.dto.platform.PlatformTemplateStatus;
import com.kep.core.model.dto.platform.PlatformType;
import com.kep.core.model.dto.platform.kakao.KakaoBizMessageTemplatePayload;
import com.kep.core.model.dto.platform.kakao.KakaoBizMessageTemplateType;
import com.kep.core.model.dto.platform.kakao.KakaoBizTemplateResponse;
import com.kep.core.model.dto.platform.kakao.profile.KakaoSendProfileResponse;
import com.kep.core.model.dto.upload.UploadPlatformRequestDto;
import com.kep.portal.client.PlatformClient;
import com.kep.portal.model.dto.platform.PlatformTemplateCondition;
import com.kep.portal.model.dto.platform.PlatformTemplateResponseDto;
import com.kep.portal.model.entity.member.Member;
import com.kep.portal.model.entity.member.MemberMapper;
import com.kep.portal.model.entity.platform.PlatformTemplate;
import com.kep.portal.model.entity.platform.PlatformTemplateMapper;
import com.kep.portal.model.entity.platform.PlatformTemplateRejectHistory;
import com.kep.portal.model.entity.platform.PlatformTemplateRejectHistoryMapper;
import com.kep.portal.model.entity.team.Team;
import com.kep.portal.repository.member.MemberRepository;
import com.kep.portal.repository.platform.PlatformSubscribeRepository;
import com.kep.portal.repository.platform.PlatformTemplateRejectHistoryRepository;
import com.kep.portal.repository.platform.PlatformTemplateRepository;
import com.kep.portal.repository.team.TeamMemberRepository;
import com.kep.portal.repository.team.TeamRepository;
import com.kep.portal.service.channel.ChannelService;
import com.kep.portal.util.SecurityUtils;
import com.kep.portal.util.UploadUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.util.ObjectUtils;

import javax.annotation.Resource;
import javax.validation.constraints.NotNull;
import java.time.ZonedDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class PlatformTemplateService {
    @Resource
    private SecurityUtils securityUtils;

    @Resource
    private UploadUtils uploadUtils;

    @Resource
    private PlatformTemplateRepository platformTemplateRepository;

    @Resource
    private PlatformTemplateMapper platformTemplateMapper;

    @Resource
    private MemberMapper memberMapper;

    @Resource
    private MemberRepository memberRepository;

    @Resource
    private TeamRepository teamRepository;

    @Resource
    private TeamMemberRepository teamMemberRepository;

    @Resource
    private PlatformTemplateRejectHistoryRepository platformTemplateRejectHistoryRepository;

    @Resource
    private PlatformTemplateRejectHistoryMapper platformTemplateRejectHistoryMapper;

    @Resource
    private PlatformClient platformClient;

    @Resource
    private ChannelService channelService;

    @Resource
    private ObjectMapper objectMapper;
    private final PlatformSubscribeRepository platformSubscribeRepository;

    public PlatformTemplateService(PlatformSubscribeRepository platformSubscribeRepository) {
        this.platformSubscribeRepository = platformSubscribeRepository;
    }

    /**
     * 상담관리 > 상담지원 > 템플릿 관리 목록 조회
     */
    public Page<PlatformTemplateResponseDto> search(PlatformTemplateCondition platformTemplateCondition, @NotNull Pageable pageable) {

        Page<PlatformTemplate> templatePage =  platformTemplateRepository.search(platformTemplateCondition, pageable);

        // 조회된 목록으로 frontend에 필요한 데이터 가공
        List<PlatformTemplateResponseDto> dtos = new ArrayList<>();
        for (PlatformTemplate platformTemplate : templatePage.getContent()) {
            PlatformTemplateResponseDto dto = platformTemplateMapper.mapResponse(platformTemplate);

            // 작성자 이름 및 팀 정보 조회
            dto.setCreatorInfo(getMemberInfo(platformTemplate.getCreator()));

            // 수정자 이름 및 팀 정보 조회
            if(!ObjectUtils.isEmpty(platformTemplate.getModifier())){
                dto.setModifierInfo(getMemberInfo(platformTemplate.getModifier()));
            }

            // 알림톡일 경우 반려여부를 체크해서 담아줌
            if(PlatformType.kakao_alert_talk.equals(platformTemplateCondition.getPlatform())){
                List<PlatformTemplateRejectHistory> platformTemplateRejectHistoryList = platformTemplateRejectHistoryRepository.findAllByPlatformTemplateIdOrderByIdAsc(platformTemplate.getId());

                // 반려에 대한 내용이 있을 경우 반려코멘트와 반려여부를 담아줌
                if(!ObjectUtils.isEmpty(platformTemplateRejectHistoryList) && platformTemplateRejectHistoryList.size() > 0){
                    dto.setRejectYn(true);
                    dto.setRejectComment(platformTemplateRejectHistoryList.get(platformTemplateRejectHistoryList.size()-1).getCommentContent());
                }
            }

            dtos.add(dto);
        }

        return new PageImpl<>(dtos, templatePage.getPageable(), templatePage.getTotalElements());
    }

    /**
     * 상담관리 > 템플릿 관리 > 목록 > 수정 시 상세데이터
     * 상담포털 > 알림톡/친구톡 > 템플릿 목록 팝업 > 템플릿 상세 조회
     * @param id
     * @return
     */
    public PlatformTemplateResponseDto detail(Long id) throws Exception{
        PlatformTemplate platformTemplate = platformTemplateRepository.findById(id).orElse(null);
        PlatformTemplateResponseDto platformTemplateResponseDto = platformTemplateMapper.mapResponse(platformTemplate);
        platformTemplateResponseDto.setDetail(objectMapper.readValue(platformTemplate.getPayload(), KakaoBizMessageTemplatePayload.class));

        platformTemplateResponseDto.setChannelInfo(channelService.getById(platformTemplate.getChannelId()));

        // 알림톡일 경우 반려여부를 체크해서 담아줌
        if(PlatformType.kakao_alert_talk.equals(platformTemplateResponseDto.getPlatform())){
            List<PlatformTemplateRejectHistory> platformTemplateRejectHistoryList = platformTemplateRejectHistoryRepository.findAllByPlatformTemplateIdOrderByIdAsc(platformTemplate.getId());

            platformTemplateResponseDto.getDetail().setTemplateComments(platformTemplateRejectHistoryMapper.map(platformTemplateRejectHistoryList));
        }

        return platformTemplateResponseDto;
    }
    
    /**
     * 상담관리 > 템플릿 관리 > 목록 > 선택 삭제
     * @param platformTemplateDto
     * @return
     */
    public Map delete(PlatformTemplateDto platformTemplateDto){
        List<PlatformTemplate> platformTemplateList = new ArrayList<>();

        int bizFailCnt = 0;
        int bizSuccessCnt = 0;
        int bizStatusFailCnt = 0;
        int deleteCnt = 0;

        for(Long id : platformTemplateDto.getIds()){
            PlatformTemplate platformTemplate = platformTemplateRepository.findById(id).orElse(null);

            Assert.notNull(platformTemplate, "platformTemplate can not be null");

            if(PlatformType.kakao_alert_talk.equals(platformTemplate.getPlatform())){
                if(PlatformTemplateStatus.temp.equals(platformTemplate.getStatus()) || PlatformTemplateStatus.reject.equals(platformTemplate.getStatus()) || PlatformTemplateStatus.approve.equals(platformTemplate.getStatus())){
                    KakaoBizTemplateResponse<KakaoBizMessageTemplatePayload> templateInfo =  platformClient.getKakaoBizTemplateInfo(platformTemplate.getSenderProfileKey(), platformTemplate.getCode());

                    if(!ObjectUtils.isEmpty(templateInfo.getData())){
                        // 카카오비즈메세지의 템플릿 상태가 R(대기)상태이면서, 검수상태가 등록(N)/반려(R)/승인(O)인 경우에만 삭제가 가능함
                        if("R".equals(templateInfo.getData().getTemplateStatus()) && ("N".equals(templateInfo.getData().getKepStatus()) || "R".equals(templateInfo.getData().getKepStatus()) || "O".equals(templateInfo.getData().getKepStatus()))){
                            KakaoBizTemplateResponse<KakaoBizMessageTemplatePayload> res = platformClient.deleteKakaoBizTemplate(platformTemplate.getSenderProfileKey(), platformTemplate.getCode());

                            // 카카오비즈메세지 센터에서의 삭제가 실패시 아래 삭제데이터에 포함하지 않음.
                            if(!"API_200".equals(res.getCode())){
                                bizFailCnt++;
                                continue;
                            } else {
                                bizSuccessCnt++;
                            }
                        } else {
                            // 그 외에는 삭제 불가능
                            bizStatusFailCnt++;
                            continue;
                        }
                    }

                    platformTemplate.setModifier(securityUtils.getMemberId());
                    platformTemplate.setModified(ZonedDateTime.now());
                    platformTemplate.setStatus(PlatformTemplateStatus.delete);

                    deleteCnt++;
                    platformTemplateList.add(platformTemplate);
                }
            }else if(PlatformType.kakao_friend_talk.equals(platformTemplate.getPlatform())){
                platformTemplate.setModifier(securityUtils.getMemberId());
                platformTemplate.setModified(ZonedDateTime.now());
                platformTemplate.setStatus(PlatformTemplateStatus.delete);

                deleteCnt++;
                platformTemplateList.add(platformTemplate);
            }
        }

        if(!ObjectUtils.isEmpty(platformTemplateList) && platformTemplateList.size() > 0){
            platformTemplateRepository.saveAll(platformTemplateList);
        }

        Map<String, Object> returnMap = new HashMap<>();
        returnMap.put("deleteCnt", deleteCnt); // 내부 DB 삭제처리 완료 Count
        returnMap.put("bizSuccessCnt", bizSuccessCnt); // 카카오 비즈메세지 센터의 삭제 완료 Count
        returnMap.put("bizFailCnt", bizFailCnt); // 카카오 비즈메세지 센터의 삭제처리 실패 Count
        returnMap.put("bizStatusFailCnt", bizStatusFailCnt); // 카카오 비즈메세지 센터의 상태가 맞지 않은 Count(템플릿 상태가 R(대기)상태이면서, 검수상태가 등록(N)/반려(R)인 경우에만 삭제가 가능함. 실시간으로 비즈메세지 센터의 템플릿 상태를 조회하여 체크)

        return returnMap;
    }

    /**
     * 상담관리 > 템플릿 관리 > 등록/수정 시 발신프로필 목록
     */
    public List<KakaoSendProfileResponse> getProfileKeyList() {
        KakaoBizTemplateResponse<List<KakaoSendProfileResponse>> res = platformClient.getKakaoBizProfileList();

        return res.getData();
    }

    /**
     * 상담관리 > 템플릿 관리 > 등록/수정 시 발신프로필 목록
     */
    public KakaoSendProfileResponse checkProfileKey(String senderProfileKey) {
        KakaoBizTemplateResponse<KakaoSendProfileResponse> res = platformClient.getKakaoBizProfileInfo(senderProfileKey);

        return res.getData();
    }

    /**
     * 상담관리 > 템플릿 관리 > 등록/수정 시 템플릿 코드 자동생성
     */
    public PlatformTemplateDto getNewTemplateCode() {
        Long nextKey = platformTemplateRepository.selectKey();

        String clientId = platformClient.selectKakaoTemplateClientId();

        String key = String.format("%05d", nextKey);

        PlatformTemplateDto platformTemplateDto = new PlatformTemplateDto();
        platformTemplateDto.setCode(clientId + "_" + key);

        return platformTemplateDto;
    }

    /**
     * 상담관리 > 템플릿 관리 > 알림톡 템플릿 등록/수정 시 카테고리 목록
     */
    public List<KakaoBizTemplateResponse.TemplateCategory> getCategoryList() {
        KakaoBizTemplateResponse<List<KakaoBizTemplateResponse.TemplateCategory>> res = platformClient.getKakaoBizTemplateCategoryList();

        return res.getData();
    }

    /**
     * 알림톡 템플릿 등록 시 이미지 업로드 호출
     * target => main - 강조유형이 이미지형, 아이템리스트형에서 이미지 업로드 시
     *           highlight - 강조유형이 아이템리스트형일때 아이템하이라이트의 썸네일 이미지 업로드 시
     * @param uploadDto
     * @param target
     * @return
     * @throws Exception
     */
    public KakaoBizTemplateResponse uploadAlertTemplateImage(UploadPlatformRequestDto uploadDto, String target) throws Exception {
        return platformClient.uploadAlertTemplateImage(uploadDto, target);
    }

    /**
     * 상담관리 > 템플릿 관리 > 알림톡 템플릿 등록/수정 저장 후 검수 요청
     */
    public KakaoBizTemplateResponse<KakaoBizMessageTemplatePayload> saveAlertTemplate(String senderProfileKey, Long id, KakaoBizMessageTemplatePayload templatePayload) throws Exception {
        Assert.notNull(templatePayload.getTemplateCode(), "template_code is not null");
        Assert.notNull(templatePayload.getTemplateName(), "template_name is not null");

        KakaoBizTemplateResponse<KakaoBizMessageTemplatePayload> res = new KakaoBizTemplateResponse<>();

        PlatformTemplateStatus status = PlatformTemplateStatus.request;

        if(!ObjectUtils.isEmpty(templatePayload.getTemplateStatus()) && "temp".equals(templatePayload.getTemplateStatus())){
            status = PlatformTemplateStatus.temp;
        } else {
            Assert.notNull(templatePayload.getTemplateName(), "template_name is not null");
            Assert.notNull(templatePayload.getTemplateContent(), "template_content is not null");
            Assert.notNull(templatePayload.getTemplateMessageType(), "template_message_type is not null");
            Assert.notNull(templatePayload.getTemplateEmphasizeType(), "template_emphasize_type is not null");
            Assert.notNull(templatePayload.getCategoryCode(), "category_code is not null");

            if(KakaoBizMessageTemplateType.NONE.equals(templatePayload.getTemplateEmphasizeType())){
                templatePayload.setBizTalkMessageType(BizTalkMessageType.AT);
            }

            if(KakaoBizMessageTemplateType.TEXT.equals(templatePayload.getTemplateEmphasizeType())){
                Assert.notNull(templatePayload.getTemplateTitle(), "template_title is not null");
                Assert.notNull(templatePayload.getTemplateSubtitle(), "template_subtitle is not null");

                templatePayload.setBizTalkMessageType(BizTalkMessageType.AT);
            }

            if(KakaoBizMessageTemplateType.IMAGE.equals(templatePayload.getTemplateEmphasizeType())){
                Assert.notNull(templatePayload.getTemplateImageUrl(), "template_image_url is not null");
                Assert.notNull(templatePayload.getTemplateImageName(), "template_image_name is not null");

                templatePayload.setBizTalkMessageType(BizTalkMessageType.AI);
            }

            if(KakaoBizMessageTemplateType.ITEM_LIST.equals(templatePayload.getTemplateEmphasizeType())){
                Assert.isTrue(templatePayload.getSecurityFlag().equals("false"), "item_list security_flag is not true");
                Assert.notNull(templatePayload.getTemplateItem(), "template_item is not null");
                Assert.isTrue(templatePayload.getTemplateItem().getList().size() > 1, "template_item list size is 2 or more");
                Assert.isTrue(!(ObjectUtils.isEmpty(templatePayload.getTemplateImageUrl()) && ObjectUtils.isEmpty(templatePayload.getTemplateHeader())
                        && ObjectUtils.isEmpty(templatePayload.getTemplateItemHighlight())), "one of the template_image,template_header,template_item_highlight must be not null");

                templatePayload.setBizTalkMessageType(BizTalkMessageType.AT);

                if(!ObjectUtils.isEmpty(templatePayload.getTemplateImageUrl())){
                    templatePayload.setBizTalkMessageType(BizTalkMessageType.AI);
                }
            }

            if(KakaoBizMessageTemplatePayload.TemplateMessageType.EX.equals(templatePayload.getTemplateMessageType())
                    || KakaoBizMessageTemplatePayload.TemplateMessageType.MI.equals(templatePayload.getTemplateMessageType())){
                Assert.notNull(templatePayload.getTemplateExtra(), "template_extra is not null");
            }

            if(KakaoBizMessageTemplatePayload.TemplateMessageType.AD.equals(templatePayload.getTemplateMessageType())
                    || KakaoBizMessageTemplatePayload.TemplateMessageType.MI.equals(templatePayload.getTemplateMessageType())){
                Assert.notNull(templatePayload.getButtons(), "buttons is not null");

                Assert.isTrue(KakaoBizMessageTemplatePayload.LinkType.AC.equals(templatePayload.getButtons().get(0).getLinkType()), "channel_button is first ordering");

                int channelCnt = 0;
                for(KakaoBizMessageTemplatePayload.Button button : templatePayload.getButtons()){
                    if(KakaoBizMessageTemplatePayload.LinkType.AC.equals(button.getLinkType())){
                        channelCnt++;
                    }
                }

                Assert.isTrue(channelCnt == 1, "channel_button is only one");
            }

            Assert.notNull(templatePayload.getSecurityFlag(), "security_flag is not null");
        }

        Assert.notNull(senderProfileKey, "senderProfileKey is not null");

        KakaoBizTemplateResponse<KakaoBizMessageTemplatePayload> templateInfo =  platformClient.getKakaoBizTemplateInfo(senderProfileKey, templatePayload.getTemplateCode());

        KakaoBizMessageTemplatePayload sendPayload = templatePayload;

        PlatformTemplate platformTemplate;
        if(ObjectUtils.isEmpty(id)){
            if(!ObjectUtils.isEmpty(templateInfo.getData())){
                Assert.notNull(templateInfo.getData(), "Kakao Biz Message Center Template Data is exists");
            }

            Assert.notNull(templatePayload.getSelectChannelId(), "select channel id is not null");

            platformTemplate = PlatformTemplate.builder()
                    .channelId(templatePayload.getSelectChannelId())
                    .name(templatePayload.getTemplateName())
                    .code(templatePayload.getTemplateCode())
                    .status(status)
                    .messageType(templatePayload.getBizTalkMessageType())
                    .senderProfileKey(senderProfileKey)
                    .payload(objectMapper.writeValueAsString(templatePayload))
                    .platform(PlatformType.kakao_alert_talk)
                    .branchId(securityUtils.getBranchId())
                    .creator(securityUtils.getMemberId())
                    .created(ZonedDateTime.now())
                    .build();
        } else {
            platformTemplate = platformTemplateRepository.findById(id).orElse(null);

            if(!(PlatformTemplateStatus.temp.equals(status) || PlatformTemplateStatus.temp.equals(platformTemplate.getStatus())) && ObjectUtils.isEmpty(templateInfo.getData())){
                Assert.notNull(templateInfo.getData(), "Kakao Biz Message Center Template Data is Null");

                Assert.isTrue(!"I".equals(templateInfo.getData().getKepStatus()), "Kakao Biz Message Center Template Data Status is Request");
                Assert.isTrue(!"O".equals(templateInfo.getData().getKepStatus()), "Kakao Biz Message Center Template Data Status is Approval");
            }

            Assert.notNull(platformTemplate, "Not Found by id to PlatformTemplate");

            if(!PlatformTemplateStatus.temp.equals(status) && !PlatformTemplateStatus.temp.equals(platformTemplate.getStatus())){
                sendPayload = this.settingSendTemplatePayload(templatePayload, sendPayload, platformTemplate.getCode());
            }

            platformTemplate.setCode(templatePayload.getTemplateCode());
            platformTemplate.setName(templatePayload.getTemplateName());
            platformTemplate.setStatus(status);
            platformTemplate.setMessageType(templatePayload.getBizTalkMessageType());
            platformTemplate.setPayload(objectMapper.writeValueAsString(templatePayload));
            platformTemplate.setModifier(securityUtils.getMemberId());
            platformTemplate.setModified(ZonedDateTime.now());
        }

        if(!ObjectUtils.isEmpty(templatePayload.getTemplateStatus()) && "temp".equals(templatePayload.getTemplateStatus())){
            platformTemplateRepository.save(platformTemplate);
        } else {
            res = platformClient.saveKakaoBizTemplate(senderProfileKey, sendPayload);

            if(!"API_200".equals(res.getCode())){
                return res;
            } else {
                platformTemplateRepository.save(platformTemplate);

                // TODO: 매 저장/수정 요청 시 검수요청이 바로 되므로 검수요청취소를 임시적으로 실행함 추후 삭제해야함.
                platformClient.cancelKakaoBizTemplateRequest(senderProfileKey, templatePayload.getTemplateCode());
            }
        }

        return res;
    }

    /**
     * 알림톡 템플릿 등록 시 이미지 업로드 호출
     * target => main - 강조유형이 이미지형, 아이템리스트형에서 이미지 업로드 시
     *           highlight - 강조유형이 아이템리스트형일때 아이템하이라이트의 썸네일 이미지 업로드 시
     * @param uploadDto
     * @return
     * @throws Exception
     */
    public KakaoBizTemplateResponse uploadFriendTemplateImage(UploadPlatformRequestDto uploadDto) throws Exception {
        return platformClient.uploadFriendTemplateImage(uploadDto);
    }

    /**
     * 상담관리 > 템플릿 관리 > 친구톡 템플릿 등록/수정
     */
    public PlatformTemplateDto saveFriendTemplate(String senderProfileKey, Long id, KakaoBizMessageTemplatePayload templatePayload) throws Exception {
        Assert.notNull(senderProfileKey, "senderProfileKey is not null");
        Assert.notNull(templatePayload.getTemplateName(), "template_name is not null");
        Assert.notNull(templatePayload.getTemplateContent(), "template_content is not null");
        Assert.notNull(templatePayload.getBizTalkMessageType(), "biz_talk_message_type is not null");

        if(BizTalkMessageType.FI.equals(templatePayload.getBizTalkMessageType()) || BizTalkMessageType.FW.equals(templatePayload.getBizTalkMessageType())){
            Assert.notNull(templatePayload.getTemplateImageUrl(), "template_image_url is not null");
        }

        KakaoBizMessageTemplatePayload sendPayload = templatePayload;

        PlatformTemplate platformTemplate;
        if(ObjectUtils.isEmpty(id)){
            Assert.notNull(templatePayload.getSelectChannelId(), "select channel id is not null");

            platformTemplate = PlatformTemplate.builder()
                    .channelId(templatePayload.getSelectChannelId())
                    .name(templatePayload.getTemplateName())
                    .code(templatePayload.getTemplateCode())
                    .status(PlatformTemplateStatus.approve)
                    .messageType(templatePayload.getBizTalkMessageType())
                    .senderProfileKey(senderProfileKey)
                    .payload(objectMapper.writeValueAsString(templatePayload))
                    .platform(PlatformType.kakao_friend_talk)
                    .branchId(securityUtils.getBranchId())
                    .creator(securityUtils.getMemberId())
                    .created(ZonedDateTime.now())
                    .build();
        } else {
            platformTemplate = platformTemplateRepository.findById(id).orElse(null);

            Assert.notNull(platformTemplate, "Not Found by id to PlatformTemplate");

//            sendPayload = this.settingSendTemplatePayload(templatePayload, sendPayload, platformTemplate.getCode());

            platformTemplate.setName(templatePayload.getTemplateName());
            platformTemplate.setMessageType(templatePayload.getBizTalkMessageType());
            platformTemplate.setPayload(objectMapper.writeValueAsString(templatePayload));
            platformTemplate.setModifier(securityUtils.getMemberId());
            platformTemplate.setModified(ZonedDateTime.now());
        }

        platformTemplate = platformTemplateRepository.save(platformTemplate);

        return platformTemplateMapper.map(platformTemplate);
    }

    /**
     * 이름 및 팀 정보 호출
     */
    public MemberDto getMemberInfo(Long memberId) {
        Member member = memberRepository.findById(memberId).orElse(null);

        // 소속 정보 가져오기
        Set<Long> teamIds = teamMemberRepository.findAllByMemberId(memberId)
                .stream().map(item->item.getTeam().getId()).collect(Collectors.toSet());

        // 팀 정보
        List<Team> teams = teamRepository.findAllById(teamIds);
        if(member != null && !ObjectUtils.isEmpty(teams)){
            member.setTeams(teams);
        }

        return memberMapper.map(member);
    }

    public KakaoBizMessageTemplatePayload settingSendTemplatePayload(KakaoBizMessageTemplatePayload templatePayload, KakaoBizMessageTemplatePayload sendPayload, String templateCode){
        sendPayload = new KakaoBizMessageTemplatePayload();

        sendPayload.setTemplateCode(templateCode);
        sendPayload.setNewTemplateCode(templatePayload.getTemplateCode());
        sendPayload.setNewTemplateName(templatePayload.getTemplateName());
        sendPayload.setNewTemplateContent(templatePayload.getTemplateContent());
        sendPayload.setSecurityFlag(templatePayload.getSecurityFlag());
        sendPayload.setNewTemplateMessageType(templatePayload.getTemplateMessageType());
        sendPayload.setNewTemplateExtra(templatePayload.getTemplateExtra());
        sendPayload.setNewTemplateAd(templatePayload.getTemplateAd());
        sendPayload.setNewTemplateEmphasizeType(templatePayload.getTemplateEmphasizeType());
        sendPayload.setNewTemplateTitle(templatePayload.getTemplateTitle());
        sendPayload.setNewTemplateSubtitle(templatePayload.getTemplateSubtitle());
        sendPayload.setNewCategoryCode(templatePayload.getCategoryCode());
        sendPayload.setButtons(templatePayload.getButtons());
        sendPayload.setQuickReplies(templatePayload.getQuickReplies());
        sendPayload.setNewTemplateHeader(templatePayload.getTemplateHeader());
        sendPayload.setNewTemplateItemHighlight(templatePayload.getTemplateItemHighlight());
        sendPayload.setNewTemplateItem(templatePayload.getTemplateItem());
        sendPayload.setNewTemplateImageUrl(templatePayload.getTemplateImageUrl());
        sendPayload.setNewTemplateImageName(templatePayload.getTemplateImageName());

        return sendPayload;
    }

    public KakaoBizTemplateResponse<KakaoBizMessageTemplatePayload> cancel(String profileKey, String templateCode){
        KakaoBizTemplateResponse<KakaoBizMessageTemplatePayload> templateInfo =  platformClient.getKakaoBizTemplateInfo(profileKey, templateCode);

        KakaoBizTemplateResponse<KakaoBizMessageTemplatePayload> res = new KakaoBizTemplateResponse<KakaoBizMessageTemplatePayload>();

        if(!ObjectUtils.isEmpty(templateInfo)){
            if("I".equals(templateInfo.getData().getKepStatus())){
                res = platformClient.cancelKakaoBizTemplateRequest(profileKey, templateCode);
            }
        }

        PlatformTemplate platformTemplate = platformTemplateRepository.findByCode(templateCode);

        platformTemplate.setStatus(PlatformTemplateStatus.temp);

        platformTemplateRepository.save(platformTemplate);

        return res;
    }
}
