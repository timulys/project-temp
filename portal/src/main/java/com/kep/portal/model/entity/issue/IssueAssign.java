package com.kep.portal.model.entity.issue;

import lombok.*;
import javax.validation.constraints.Positive;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class IssueAssign {

    /**
     * {@link Issue} PK
     */
    @Positive
    private Long id;

    /**
     * 상담원 변경 요청시, 유저 지정
     */
    private Long memberId;
    /**
     * 싱크 후 자연유입 상담원 랜덤 배정
     */
    private String memberIds; 
    /**
     * BNK 유저 지정 CUSTOM 
     */
    private String vndrCustNo;
    
    /**
     * 상담원 변경 요청시, 브랜치 지정
     */
    @Positive
    private Long branchId;

    /**
     * 상담원 변경 요청시, 분류 지정
     */
    @Positive
    private Long issueCategoryId;

    /**
     * 상담지원요청 여부
     */
    private Boolean issueSupportYn;

    /**
     * 상담지원요청이력 정보
     */
    private IssueSupportHistory issueSupportHistory;

    /**
     * 상담지원요청 전체 정보
     */
    private IssueSupport issueSupport;

    /**
     * 상담원 변경 요청시, 요청한 유저
     */
    private Long supportRequester;

    /**
     * 재배정 여부
     */
    @Builder.Default
    private boolean reassigned = false;
}
