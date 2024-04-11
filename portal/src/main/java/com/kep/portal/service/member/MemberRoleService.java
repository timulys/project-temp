package com.kep.portal.service.member;

import com.kep.portal.repository.member.MemberRoleRepository;
import com.kep.portal.util.SecurityUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.transaction.Transactional;

@Deprecated
@Service
@Transactional
@Slf4j
public class MemberRoleService {

    @Resource
    private MemberRoleRepository memberRoleRepository;
    @Resource
    private SecurityUtils securityUtils;

//    public List<MemberRole> save(List<MemberRoleDto> memberRoleDtos , Long memberId){
//
//        List<String> authority = new ArrayList<>();
//        for (MemberRoleDto dto : memberRoleDtos){
//            authority.add(dto.getAuthority());
//        }
//
//        log.info("authority1:{}",authority);
//
//        if (authority.size() > 0){
//            List<MemberRole> memberRoles = memberRoleRepository.findByMemberIdAndAuthorityIn(memberId , authority);
//            List<MemberRole> memberRoleList = new ArrayList<>();
//
//            for (MemberRole role : memberRoles){
//                if(authority.contains(role.getAuthority())){
//                    authority.remove(role.getAuthority());
//                }
//            }
//
//            log.info("authority2:{}" , authority);
//
//            for(String auth : authority){
//                memberRoleList.add(MemberRole.builder()
//                        .member(Member.builder().id(memberId).build())
//                        .authority(auth)
//                        .modifier(securityUtils.getMemberId())
//                        .modified(ZonedDateTime.now())
//                        .build());
//            }
//            if(memberRoleList.size() > 0) {
//                memberRoleRepository.saveAll(memberRoleList);
//            }
//        }
//
//        return memberRoleRepository.findByMemberId(memberId);
//    }


}
