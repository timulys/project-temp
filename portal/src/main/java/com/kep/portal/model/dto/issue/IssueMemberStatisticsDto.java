package com.kep.portal.model.dto.issue;

import com.kep.portal.model.entity.member.Member;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.annotation.Nullable;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class IssueMemberStatisticsDto {
    private Long memberId;
    private Long waiting;
    private Long ing;
    private Long delay;
    private Long complete;

    @Nullable
    private Member member;
}
