package com.kep.core.model.dto.issue;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.kep.core.model.dto.legacy.LegacyBnkCategoryDto;
import com.kep.core.model.dto.member.MemberDto;
import com.kep.core.model.dto.subject.IssueCategoryDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Size;
import java.time.ZonedDateTime;

/**
 * 이슈, 상세 정보
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class IssueExtraDto {
	/**
	 * 후처리 요약
	 */
	private String summary;
	
	/**
	 * bnk 현업이관메모 추가
	 */
	private String bnkSummary;
	/**
	 * 후처리 분류
	 */
	private Long issueCategoryId;
	private IssueCategoryDto issueCategory;
	private ZonedDateTime summaryModified;
	private IssueExtraDto issueExtraDto;
    private LegacyBnkCategoryDto legacyDto;

	/**
	 * 메모
	 */
	private String memo;
	private ZonedDateTime memoModified;


	/**
	 * [2023.05.09/asher.shin/요약 완료 여부]
	 */
	private boolean summaryCompleted;

//	/**
//	 * 이슈 생성시, 파라미터
//	 */
//	@Size(max = 1000)
//	private String parameter;

	/**
	 * 유입 경로
	 */
	private String inflow;
	private ZonedDateTime inflowModified;
	
	/**
	 * BNK 현업이관 분류 사용여부 - showTransfer
	 */
	private boolean showTransfer;
	
	
	/**
     * BNK 현업이관 분류 - fldCd
     */
    private String fldCd;
    
    /**
     * BNK 현업이관 분류 - fldDeptCd
     */
    private String fldDeptCd;

    /**
     * dealGubun 선택값 - selectedRadioData
     */
    private String selectedRadioData;

	/**
	 * 현업이관분류 소분류 값
	 */
	private String wrkSeq;
	/**
	 * BNK 현업이관 분류 카테고리
	 */
//	private LegacyBnkCategoryDto legacyBnkCategory;
	
	/**
	 * BNK 고객 번호
	 */
	private String custNo;

	/**
	 * BNK 계약 실행 번호
	 */
	private String cntrtNum;

	/**
	 * 상담 연결 시간
	 */
	private String dealStratTime;

	/**
	 * 상담 종료 시간
	 */
	private String dealEndTime;

	/**
	 * 상담 일자
	 */
	private String dealYmd;

	/**
	 * BNK 담당 직원 번호 (vndrCustNo)
	 */
	private String vndrCustNo;
//	private MemberDto memberDto;  // member DTO와 연결

	/**
	 * 상담 유형 (대) 선택값
	 */
	private String selectedMainCategory;

	/**
	 * 상담 유형 (중) 선택값
	 */
	private String selectedMiddleCategory;
	
	private String selectBnkCategory;
}
