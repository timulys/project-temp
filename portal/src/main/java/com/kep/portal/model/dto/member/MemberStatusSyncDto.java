package com.kep.portal.model.dto.member;

import com.kep.core.model.dto.branch.BranchDto;
import com.kep.core.model.dto.member.MemberDto;
import com.kep.portal.model.entity.member.Member;
import lombok.Getter;

@Getter
public class MemberStatusSyncDto {

    private Long id;
    private String dayOfWeek;
    private String startCounselTime;
    private String endCounselTime;
    private BranchDto branchDto;
    private MemberDto memberDto;

    public Member toMemberEntity() {
        Member member = new Member();
        member.setId(memberDto.getId());
        member.setUsername(memberDto.getUsername());
        member.setPassword(memberDto.getPassword());
        member.setNickname(memberDto.getNickname());
        member.setBranchId(memberDto.getBranchId());
        member.setEnabled(memberDto.getEnabled());
        member.setManaged(memberDto.getManaged());
        member.setProfile(memberDto.getProfile());
        member.setUsedMessage(memberDto.getUsedMessage());
        member.setFirstMessage(memberDto.getFirstMessage());
        member.setCreator(memberDto.getCreator());
        member.setCreated(memberDto.getCreated());
        member.setModifier(memberDto.getModifier());
        member.setModified(memberDto.getModified());
        member.setCounselCategory(memberDto.getCounselCategory());
        member.setVndrCustNo(memberDto.getVndrCustNo());
        member.setStatus(memberDto.getStatus());
        member.setMaxCounsel(memberDto.getMaxCounsel());
        member.setOutsourcing(memberDto.getOutsourcing());
        member.setSetting(memberDto.getSetting());
        return member;
    }
}