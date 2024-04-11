package com.kep.portal.model.dto.issue;

import com.fasterxml.jackson.annotation.JsonIncludeProperties;
import com.kep.core.model.dto.issue.IssueStatus;
import com.kep.core.model.dto.issue.IssueSupportChangeType;
import com.kep.core.model.dto.issue.IssueSupportStatus;
import com.kep.core.model.dto.issue.IssueSupportType;
import com.kep.core.model.dto.member.MemberDto;
import com.kep.portal.model.dto.subject.IssueCategoryChildrenDto;
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
    private Long id;

    /**
     * 상담 직원전환/검토 요청 구분
     */
    private IssueSupportType type;

    /**
     * 상담 직원전환/검토 요청 상태
     */
    @NotNull
    private IssueSupportStatus status;

    /**
     * 지원 요청자 정보
     */
    @JsonIncludeProperties({"id","username","nickname","teams"})
    private MemberDto questionerInfo;

    /**
     * 지원 요청 시간
     */
    private ZonedDateTime questionModified;

    /**
     * 상담 검토/직원전환 요청 내용
     */
    private String question;

    /**
     * 답변 그룹장 정보
     */
    @JsonIncludeProperties({"id","username","nickname","teams"})
    private MemberDto answererInfo;

    /**
     * 답변 시간
     */
    private ZonedDateTime answerModified;

    /**
     * 매니저 답변 내용
     */
    private String answer;

    /**
     * 상담직원전환 구분 값
     */
    private IssueSupportChangeType changeType;

    /**
     * 시스템전환시 branchId 저장
     */
    private Long branchId;

    /**
     * 시스템전환 - 소분류 카테고리 ID
     */
    private Long categoryId;

    /**
     * 상담직원선택 - 상담원ID
     */
    private Long selectMemberId;

    private String changeName;

    private List<IssueCategoryChildrenDto> categoryInfo;

    private IssueStatus issueStatus;
}
