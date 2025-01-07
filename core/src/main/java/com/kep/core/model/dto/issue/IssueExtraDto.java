package com.kep.core.model.dto.issue;

import com.kep.core.model.dto.channel.ChannelDto;
import com.kep.core.model.dto.legacy.LegacyBnkCategoryDto;
import com.kep.core.model.dto.subject.IssueCategoryDto;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

import java.time.ZonedDateTime;

/**
 * 이슈, 상세 정보
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class IssueExtraDto {

	@Schema(description = "상담 요약")
	private String summary;
	
	/**
	 * FIXME :: bnk 비즈니스. 제거 필요. legacy 및 현재 포탈에도 BNK 전송 로직에 사용됨. 20240715 volka
	 * bnk 현업이관메모 추가
	 */
	@Schema(description = "bnk 현업이관메모 추가")
	private String bnkSummary;
	/**
	 * 후처리 분류
	 */
	@Schema(description = "이슈 카테고리 아이디")
	private Long issueCategoryId;
	@Schema(description = "이슈 카테고리 정보")
	private IssueCategoryDto issueCategory;
	@Schema(description = "요약 수정일시")
	private ZonedDateTime summaryModified;
	@Schema(description = "이슈 상세")
	private IssueExtraDto issueExtraDto;
	/**
	 * FIXME :: BNK 로직. 사용 없음. 20240715 volka
	 */
    @Schema(description = "BNK 레거시 카테고리 (삭제 예정)")
	private LegacyBnkCategoryDto legacyDto;

	/**
	 * 메모
	 */
	@Schema(description = "메모")
	@Length(max = 200)
	private String memo;
	@Schema(description = "메모 수정일시")
	private ZonedDateTime memoModified;


	/**
	 * [2023.05.09/asher.shin/요약 완료 여부]
	 */
	@Schema(description = "요약 완료 여부 ( 요약 완료 : true , 요약 미완료 : false )")
	private boolean summaryCompleted;

//	/**
//	 * 이슈 생성시, 파라미터
//	 */
//	@Size(max = 1000)
//	private String parameter;

	/**
	 * 유입 경로
	 */
	@Schema(description = "유입 경로")
	private String inflow;
	@Schema(description = "유입 경로 수정 일시")
	private ZonedDateTime inflowModified;
	
	/**
	 * BNK 현업이관 분류 사용여부 - showTransfer
	 *
	 * FIXME :: BNK 로직 20240715 volka
	 */
	@Schema(description = "BNK 현업이관 분류 사용여부 (제거예정)")
	private boolean showTransfer;
	
	
	/**
     * BNK 현업이관 분류 - fldCd
	 * FIXME :: legacy BNK 로직 20240715 volka
     */
    @Schema(description = "BNK 현업이관 분류 :: 제거예정")
	private String fldCd;
    
    /**
     * BNK 현업이관 분류 - fldDeptCd
	 * FIXME :: legacy BNK 로직 20240715 volka
     */
    @Schema(description = "BNK 현업이관 분류 :: 제거예정")
	private String fldDeptCd;

    /**
     * dealGubun 선택값 - selectedRadioData
	 * FIXME :: legacy BNK 로직 20240715 volka
     */
    @Schema(description = "dealGubun 선택값 :: BNK 제거예정")
	private String selectedRadioData;

	/**
	 * 현업이관분류 소분류 값
	 * FIXME :: legacy BNK 로직 20240715 volka
	 */
	@Schema(description = "현업이관분류 소분류값 :: BNK로직. 제거예정")
	private String wrkSeq;
	/**
	 * BNK 현업이관 분류 카테고리
	 */
//	private LegacyBnkCategoryDto legacyBnkCategory;
	
	/**
	 * BNK 고객 번호
	 * FIXME :: legacy BNK 로직 20240715 volka
	 */
	@Schema(description = "BNK 고객번호 :: 제거예정")
	private String custNo;

	/**
	 * BNK 계약 실행 번호
	 * FIXME :: legacy BNK 로직 20240715 volka
	 */
	@Schema(description = "BNK 계약 실행 번호 :: 제거예정")
	private String cntrtNum;

	/**
	 * 상담 연결 시간
	 * FIXME :: 사용안됨
	 */
	@Schema(description = "상담 연결 시간")
	private String dealStratTime;

	/**
	 * 상담 종료 시간
	 * FIXME :: 사용안됨
	 */
	@Schema(description = "상담 종료 시간")
	private String dealEndTime;

	/**
	 * 상담 일자
	 * FIXME :: 사용안됨
	 */
	@Schema(description = "상담 일자")
	private String dealYmd;

	/**
	 * BNK 담당 직원 번호 (vndrCustNo)
	 * FIXME :: BNK 로직. 사용안됨. 20240715 volka
	 */
	@Schema(description = "BNK 담당 직원 번호 :: 제거예정")
	private String vndrCustNo;
//@Schema(description = "
//	private MemberDto memberDto;  // member DTO와 연결

	/**
	 * 상담 유형 (대) 선택값
	 * FIXME :: 사용안됨
	 */
	@Schema(description = "상담 유형 (대) 선택값")
	private String selectedMainCategory;

	/**
	 * 상담 유형 (중) 선택값
	 * FIXME :: 사용안됨
	 */
	@Schema(description = "상담 유형 (중) 선택값")
	private String selectedMiddleCategory;

	/**
	 * FIXME :: BNK 현업 분류 카테고리 20240715 volka
	 */
	@Schema(description = "BNK 현업 분류 카테고리 :: 제거예정")
	private String selectBnkCategory;

	@Schema(description = "채널 정보")
	private ChannelDto channelDto;
}
