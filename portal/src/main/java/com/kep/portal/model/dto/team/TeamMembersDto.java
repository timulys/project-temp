package com.kep.portal.model.dto.team;

import com.kep.core.model.dto.member.MemberDto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Positive;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TeamMembersDto {

    @Positive
    private Long id;

    private String name;

    private List<MemberDto> members;

}
