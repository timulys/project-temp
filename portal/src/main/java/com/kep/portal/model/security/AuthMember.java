package com.kep.portal.model.security;

import com.kep.portal.model.entity.member.Member;
import com.kep.portal.model.entity.team.Team;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.util.ObjectUtils;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 인증(Spring Security) 유저
 */
@Getter
@Setter
@EqualsAndHashCode(of = {"id"}, callSuper = false)
public class AuthMember extends User {

	private static final long serialVersionUID = -2816937329199975786L;

	@NotNull
	@Positive
	private Long id;

	@NotEmpty
	private String nickname;

	@NotNull
	private Long branchId;

	private List<Long> teamIds;

	private Long teamId;

	private List<String> roles;
	
	private String vndrCustNo;

	private boolean forbiddenWordEnabled;

	private String clientIp;
	private String userAgent;

	public AuthMember(Member member, List<GrantedAuthority> authorities, Set<String> roles) {

		// TODO: 계정 잠김, 만료 등 요건 있을 경우 처리
		super(member.getUsername(), member.getPassword(), member.getEnabled(),
				true, true, true, authorities);
//				AuthorityUtils.createAuthorityList(authorities.toString()));

		this.id = member.getId();
		this.nickname = member.getNickname();
		this.branchId = member.getBranchId();
		if (!ObjectUtils.isEmpty(member.getTeams())) {
			this.teamIds = member.getTeams().stream().map(Team::getId).collect(Collectors.toList());
            Optional<Long> team = teamIds.stream().findFirst();
            team.ifPresent(aLong -> this.teamId = aLong);
        }
		this.roles = new ArrayList<>(roles);
		// BNK 커스텀
		this.vndrCustNo = member.getVndrCustNo();
		if(member.getSetting()!= null) {
			forbiddenWordEnabled = (boolean)(member.getSetting().get("forbidden_word_enabled") !=null?member.getSetting().get("forbidden_word_enabled"):true);
		}
	}
}
