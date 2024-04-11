package com.kep.portal.repository.member;

import java.util.Collection;
import java.util.List;

import javax.validation.constraints.NotNull;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.kep.portal.model.entity.member.MemberRole;

@Repository
public interface MemberRoleRepository extends JpaRepository<MemberRole, Long> {

    void deleteByMemberId(Long memberId);

//    List<MemberRole> findByMemberIdAndAuthorityIn(Long memberId , List<String> authority);
//
    List<MemberRole> findAllByMemberId(Long memberId);

    List<MemberRole> findAllByMemberIdIn(Collection<Long> ids);

    List<MemberRole> findAllByRoleIdIn(@NotNull Collection<Long> roleIds);

    List<MemberRole> findAllByRoleId(@NotNull Long roleId);
}
