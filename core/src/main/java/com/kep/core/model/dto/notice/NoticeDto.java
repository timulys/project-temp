package com.kep.core.model.dto.notice;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

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
	private Long id;

	/**
	 * 제목
	 */
	private String title;

	/**
	 * 본문
	 */
	private String content;

	/**
	 * 오픈 범위(전체공개, 브랜치공개)
	 */
	private NoticeOpenType openType;

	/**
	 * 상단 고정 여부
	 */
	private Boolean fixation;

	/**
	 * 사용 여부
	 */
	private Boolean enabled;

	/**
	 * 생성자
	 */
	private Long creator;

	/**
	 * 생성 일시
	 */
	private ZonedDateTime created;

	/**
	 * 수정자
	 */
	private Long modifier;

	/**
	 * 변경 일시
	 */
	private ZonedDateTime modified;

	/**
	 * 일괄 처리를 위한 리스트
	 */
	private List<Long> ids;

	private Long teamId;
}
