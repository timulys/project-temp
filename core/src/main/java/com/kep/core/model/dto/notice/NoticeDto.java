package com.kep.core.model.dto.notice;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.time.ZonedDateTime;
import java.util.List;

/**
 * 공지사항 정보
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class NoticeDto {
	/**
	 * 공지사항 PK
	 */
	@Schema(description = "공지사항 아이디")
	private Long id;

	/**
	 * 제목
	 */
	@Schema(description = "공지사항 제목")
	@NotEmpty // tim.c : 사용자가 공백으로 입력을 할 수도 있어서 서버레벨에서 Validation check
	private String title;

	/**
	 * 본문
	 */
	@Schema(description = "공지사항 본문")
	@NotEmpty // tim.c : 사용자가 공백으로 입력을 할 수도 있어서 서버레벨에서 Validation check
	private String content;

	/**
	 * 오픈 범위(전체공개, 브랜치공개)
	 */
	@Schema(description = "오픈 범위(전체공개, 브랜치공개)")
	private NoticeOpenType openType;

	/**
	 * 상단 고정 여부
	 */
	@Schema(description = "상단 고정 여부")
	private Boolean fixation;

	/**
	 * 사용 여부
	 */
	@Schema(description = "사용 여부")
	private Boolean enabled;

	/**
	 * 생성자
	 */
	@Schema(description = "생성자")
	private Long creator;

	/**
	 * 생성 일시
	 */
	@Schema(description = "생성 일시")
	private ZonedDateTime created;

	/**
	 * 수정자
	 */
	@Schema(description = "수정자")
	private Long modifier;

	/**
	 * 변경 일시
	 */
	@Schema(description = "변경 일시")
	private ZonedDateTime modified;

	/**
	 * 일괄 처리를 위한 리스트
	 */
	@Schema(description = "일괄 처리를 위한 리스트")
	private List<Long> ids;

	@Schema(description = "팀 아이디")
	private Long teamId;

	@Schema(description = "브랜치 아이디")
	private Long branchId;
}
