package com.kep.portal.service.system;

import com.kep.core.model.dto.issue.payload.IssuePayload;
import com.kep.portal.model.entity.channel.ChannelEnv;
import com.kep.portal.model.entity.channel.ChannelEnvMapper;
import com.kep.portal.model.entity.channel.ChannelStartAuto;
import com.kep.portal.model.entity.issue.Issue;
import com.kep.portal.model.entity.member.Member;
import com.kep.portal.service.branch.BranchService;
import com.kep.portal.service.channel.ChannelEnvService;
import com.kep.portal.service.member.MemberService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.util.ObjectUtils;

import javax.annotation.Resource;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
public class AutoMessageService {

    @Resource
    private BranchService branchService;

    @Resource
    private MemberService memberService;

    @Resource
    private ChannelEnvService channelEnvService;

    @Resource
    private ChannelEnvMapper channelEnvMapper;

    private final static String issuePayloadDisplay = "image";

    /**
     * welcome
     * @return
     */
    public IssuePayload welcome(@NotNull Issue issue) {

        ChannelEnv channelEnv = channelEnvMapper.map(channelEnvService.getByChannel(issue.getChannel()));
        Assert.notNull(channelEnv , "not null channel id : "+issue.getChannel().getId());

        IssuePayload issuePayload = null;
        Assert.notNull(channelEnv.getStart() , "not null channel start id : "+issue.getChannel().getId());

//        if(channelEnv.getStart() != null && channelEnv.getStart().getWelcom() != null
//                && SystemEnvEnum.SwitchStatusType.off.equals(channelEnv.getStart().getWelcom().getStatus())){
//            return issuePayload;
//        }

        Assert.notNull(issue.getMember() , "not null member : "+issue.getMember());

        Member member = memberService.findById(issue.getMember().getId());

        //UsedMessage 여부 조건 추가 20241122
        if(member.getUsedMessage() && !ObjectUtils.isEmpty(member.getFirstMessage())){
            issuePayload = member.getFirstMessage();
        } else {
            ChannelStartAuto start = channelEnv.getStart();

            String message = "";

            //message
            List<IssuePayload.Section> sections = new ArrayList<>();
            sections.add(IssuePayload.Section.builder()
                    .type(IssuePayload.SectionType.text)
                    .data(message)
                    .build());

            //image
            if(!ObjectUtils.isEmpty(start.getWelcom())){
                sections.add(IssuePayload.Section.builder()
                        .type(IssuePayload.SectionType.file)
                        .display(issuePayloadDisplay)
                        .build());
            }

            List<IssuePayload.Chapter> chapters = new ArrayList<>();
            chapters.add(IssuePayload.Chapter.builder()
                            .sections(sections)
                    .build());
            issuePayload = IssuePayload.builder()
                    .version(IssuePayload.CURRENT_VERSION)
                    .chapters(chapters)
                    .build();
        }

        return issuePayload;

    }
}
