package com.kep.portal.model.dto.issue;

import com.fasterxml.jackson.annotation.JsonIncludeProperties;
import com.kep.core.model.dto.issue.IssueStatus;
import com.kep.core.model.dto.issue.IssueSupportChangeType;
import com.kep.core.model.dto.issue.IssueSupportStatus;
import com.kep.core.model.dto.issue.IssueSupportType;
import com.kep.core.model.dto.member.MemberDto;
import com.kep.portal.model.dto.subject.IssueCategoryChildrenDto;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.time.ZonedDateTime;
import java.util.List;


/**
 * 상담 지원 요청 상세
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class IssueSupportDetailDto {

    @Schema(description = "상담 지원 요청 아이디(PK)")
    private Long id;

    @Schema(description = "상담 요청 구분 ( change : 상담 직원 전환 요청 , question : 상담 검토 요청 )")
    private IssueSupportType type;

    @Schema(description = "상담 직원전환/검토 요청 상태 ( request : 요청 , reject : 반려 , finish : 완료 , change : 상담직원변경 receive : 상담이어받기 , auto : 전환자동승인 , end : 상담종료 )")
    @NotNull
    private IssueSupportStatus status;

    @Schema(description = "상담 검토/직원전환 요청자 정보 ( 이전 담당 상담원 정보 )")
    @JsonIncludeProperties({"id","username","nickname","teams"})
    private MemberDto questionerInfo;

    @Schema(description = "상담 검토/직원전환 요청 시간")
    private ZonedDateTime questionModified;

    @Schema(description = "상담 검토/직원전환 요청 내용")
    private String question;

    @Schema(description = "상담 검토/직원전환 답변자 정보")
    @JsonIncludeProperties({"id","username","nickname","teams"})
    private MemberDto answererInfo;

    @Schema(description = "상담 검토/직원전환 답변 시간")
    private ZonedDateTime answerModified;

    @Schema(description = "상담 검토/직원전환 답변 내용")
    private String answer;

    @Schema(description = "상담직원 전환방식 ( select : 상담직원 선택 , auto : 시스템 전환 )")
    private IssueSupportChangeType changeType;

    @Schema(description = "브랜치 아이디")
    private Long branchId;

    @Schema(description = "선택한 이슈 카테고리 아이디")
    private Long categoryId;

    @Schema(description = "선택한 상담 직원 아이디")
    private Long selectMemberId;

    @Schema(description = "선택한 상담 직원명")
    private String changeName;

    private List<IssueCategoryChildrenDto> categoryInfo;

    @Schema(description = "이슈 상태 ( open :  상담 요청 , assign : 배정 완료  , close : 상담 종료 , ask : 고객 질의 , replay : 상담원 답변 , urgent : 고객 질의 중 미답변 시간 초과 ( 지연상태 ) )")
    private IssueStatus issueStatus;
}
