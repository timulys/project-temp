package com.kep.portal.model.dto.issue;

import com.kep.portal.model.entity.member.Member;
import io.swagger.v3.oas.annotations.media.Schema;
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
    @Schema(description = "사용자 아이디")
    private Long memberId;
    @Schema(description = "대기")
    private Long waiting;
    @Schema(description = "상담중")
    private Long ing;
    @Schema(description = "지연")
    private Long delay;
    @Schema(description = "완료")
    private Long complete;

    /**
     * FIXME :: 엔티티 그대로 사용됨. 수정 필요 20240715 volka
     */
    @Schema(description = "사용자정보")
    @Nullable
    private Member member;
}
