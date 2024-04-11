package com.kep.core.model.dto.issue;

import com.fasterxml.jackson.annotation.JsonIncludeProperties;
import com.kep.core.model.dto.member.MemberDto;
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

    @JsonIncludeProperties({"id","username","nickname","teams"})
    private MemberDto questionerInfo;

    @JsonIncludeProperties({"id","username","nickname","teams"})
    private MemberDto answererInfo;

    private String changeName;

    private String content;

    private ZonedDateTime created;
}
