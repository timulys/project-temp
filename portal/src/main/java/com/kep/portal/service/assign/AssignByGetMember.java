package com.kep.portal.service.assign;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.kep.core.model.dto.channel.ChannelEnvDto;
import com.kep.portal.model.entity.issue.Issue;
import com.kep.portal.model.entity.issue.IssueAssign;
import com.kep.portal.model.entity.issue.IssueExtra;
import com.kep.portal.model.entity.member.Member;
import com.kep.portal.repository.member.MemberRepository;
import com.kep.portal.service.channel.ChannelEnvService;
import com.kep.portal.service.member.MemberService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * 상담원 지정 배정
 */
@Service
@Slf4j
public class AssignByGetMember implements Assignable {

    private static final String signature = "assignByGetMember";

    @Resource
    private ObjectMapper objectMapper;

    @Resource
    private MemberRepository memberRepository;

    @Resource
    private ChannelEnvService channelEnvService;

    @Resource
    private MemberService memberService;


    public AssignByGetMember(AssignProvider assignProvider) {
        assignProvider.addMethod(signature, this);
    }

    @Override
    public List<Member> apply(IssueAssign issueAssign, Issue issue, List<Member> members) {

        log.info("ASSIGN BY GET MEMBER, ISSUE: {}, MEMBERS: {}", issue.getId(),
                members.stream().map(Member::getId).collect(Collectors.toList()));


     // 이전 프로세스에서 이미 상담원 대상이 선택된 경우, 추가적인 처리 없이 반환
        if (!members.isEmpty()) {
            log.debug("ASSIGN BY GET MEMBER : {}", members);
            return members;
        }

        // 상담원 아이디가 있는지 체크
        try {
            IssueExtra issueExtra = issue.getIssueExtra();
            if (issueExtra != null) {
                log.info("GET MEMBER ISSUE EXTRA PARAMETER :{} ", issueExtra.getParameter());
                Map<String, Object> parameter = objectMapper.readValue(issueExtra.getParameter(), new TypeReference<Map<String, Object>>() {});

                //  FIXME :: eddie.j BNK 로직 삭제 예정
                // vndrCustNo를 사용하여 상담원을 찾는 로직
                if (!ObjectUtils.isEmpty(parameter.get("vndrCustNo"))) {
                    String vndrCustNo = (String) parameter.get("vndrCustNo");
                    Member member = memberRepository.findByUsername(vndrCustNo);
                    if (member != null) {
                        members.add(member);
                        log.info("Member found by vndrCustNo (username): {}", member);
                    }
                } else if (!ObjectUtils.isEmpty(parameter.get("mid"))) {
                    // 상담원 다이렉트 체크 해지시 다이렉트 배정을 사용하지 않는다.
                    ChannelEnvDto channelEnvDto = channelEnvService.getByChannel(issue.getChannel());
                    boolean isMemberDirect = false;
                    if (!ObjectUtils.isEmpty(channelEnvDto)) {
                        isMemberDirect = channelEnvDto.getMemberDirectEnabled();
                    }
                    if (isMemberDirect) {
                        Long memberId = Long.valueOf((String) parameter.get("mid"));
                        Member member = memberRepository.findById(memberId).orElse(null);
                        if (member != null) {
                            members.add(member);
                            log.info("Member found by mid: {}", member);
                        }
                    }
                }
            }

        } catch (Exception e){
            log.error("ASSIGN BY GET MEMBER, FAILED, {}, ISSUE: {} ", e.getMessage(), issue.getId(), e);
        }

        return members;
    }

}
