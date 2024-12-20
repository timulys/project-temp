package com.kep.core.model.dto.issue;

import com.fasterxml.jackson.annotation.JsonIncludeProperties;
import com.kep.core.model.dto.member.MemberDto;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.time.ZonedDateTime;


/**
 * 지원요청이력(작업이력상세)
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class IssueSupportHistoryDto {
    @Schema(description = "상담 지원 이력 아이디(PK)")
    private Long id;

    @Schema(description = "상담 직원전환/검토 요청 구분 ( change : 상담 직원 전환 요청 , question : 상담 검토 요청 )")
    private IssueSupportType type;

    @Schema(description = "상담 직원전환/검토 요청 상태 ( request : 상담검토/상담직원전환 요청 , reject : 반려 , finish : 완료 , change : 상담직원 변경 , receive : 상담 이어받기 , auto : 전환 자동승인 , end : 상담종료 )")
    @NotNull
    private IssueSupportStatus status;

    @Schema(description = "상담 직원전환/검토 요청자 정보")
    @JsonIncludeProperties({"id","username","nickname","teams"})
    private MemberDto questionerInfo;

    @Schema(description = "상담 직원전환/검토 승인/반려자 정보")
    @JsonIncludeProperties({"id","username","nickname","teams"})
    private MemberDto answererInfo;

    @Schema(description = "변경 상담 직원")
    private String changeName;

    @Schema(description = "전환 변경 사유")
    private String content;

    @Schema(description = "요청 일자")
    private ZonedDateTime created;
}
