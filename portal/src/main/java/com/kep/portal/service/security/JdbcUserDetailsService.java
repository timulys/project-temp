package com.kep.portal.service.security;

import com.kep.portal.model.entity.member.Member;
import com.kep.portal.model.entity.member.*;
import com.kep.portal.model.entity.privilege.Level;
import com.kep.portal.model.entity.privilege.Role;
import com.kep.portal.model.entity.privilege.RolePrivilege;
import com.kep.portal.model.entity.team.TeamMember;
import com.kep.portal.model.security.AuthMember;
import com.kep.portal.repository.member.MemberRepository;
import com.kep.portal.repository.member.MemberRoleRepository;
import com.kep.portal.repository.privilege.RolePrivilegeRepository;
import com.kep.portal.repository.privilege.RoleRepository;
import com.kep.portal.repository.team.TeamMemberRepository;
import com.kep.portal.service.branch.BranchService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Slf4j
public class JdbcUserDetailsService implements UserDetailsService {

    @Resource
    private MemberRepository memberRepository;
    @Resource
    private RoleRepository roleRepository;
    @Resource
    private MemberRoleRepository memberRoleRepository;
    @Resource
    private RolePrivilegeRepository rolePrivilegeRepository;
    @Resource
    private BranchService branchService;

    @Resource
    private TeamMemberRepository teamMemberRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        Member member = memberRepository.findByUsername(username);
        if (member == null) {
            throw new UsernameNotFoundException(username);
        }

        if (member.getBranchId() == null) {
            member.setBranchId(branchService.findHeadQuarters().getId());
        }

        if (member.getTeams() == null) {
            member.setTeams(teamMemberRepository.findAllByMemberId(member.getId())
                    .stream()
                    .map(TeamMember::getTeam)
                    .collect(Collectors.toList()));
        }

        // Roles
        List<MemberRole> memberRoles = memberRoleRepository.findAllByMemberId(member.getId());
        Set<Long> roleIds = memberRoles.stream().map(MemberRole::getRoleId).collect(Collectors.toSet());
        List<Role> roleEntities = roleRepository.findAllById(roleIds);
        Set<String> privileges = roleEntities.stream().map(o -> Level.ROLE_PREFIX + o.getType()).collect(Collectors.toSet());
        privileges.addAll(roleEntities.stream().map(o -> Level.ROLE_PREFIX + o.getLevel().getType()).collect(Collectors.toSet()));

        // HEAD_QUARTERS (본사) 역할 추가
        if (member.getBranchId().equals(branchService.findHeadQuarters().getId())) {
            privileges.add(Level.ROLE_HEAD_QUARTERS);
        }

        // Privileges
        if (!roleIds.isEmpty()) {
            List<RolePrivilege> rolePrivileges = rolePrivilegeRepository.findAllByRoleIdIn(roleIds);
            privileges.addAll(rolePrivileges.stream().map(o -> o.getPrivilege().getType()).collect(Collectors.toSet()));
        }

        log.info("PRIVILEGES: {}", privileges);
        List<GrantedAuthority> authorities = getGrantedAuthorities(privileges);
        return new AuthMember(member, authorities, privileges);
    }

    private List<GrantedAuthority> getGrantedAuthorities(Set<String> privileges) {

        List<GrantedAuthority> authorities = new ArrayList<>();
        for (String privilege : privileges) {
            authorities.add(new SimpleGrantedAuthority(privilege));
        }

        return authorities;
    }
}
