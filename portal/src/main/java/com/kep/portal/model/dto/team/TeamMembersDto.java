package com.kep.portal.model.dto.team;

import com.kep.core.model.dto.member.MemberDto;

import io.swagger.v3.oas.annotations.media.Schema;
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

    @Schema(description = "팀 아이디")
    @Positive
    private Long id;

    @Schema(description = "팀명")
    private String name;

    @Schema(description = "팀 멤버 목록")
    private List<MemberDto> members;

}
