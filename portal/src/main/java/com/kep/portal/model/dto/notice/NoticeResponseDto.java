/**
 * 공지사항 정보
 * @수정일자	  / 수정자			/ 수정내용
 * 2023.03.28 / philip.lee7	/ noticeUpload, readFlag 변수 추가
 * 2023.04.06 / philip.lee7	/ branchId Long -> String 으로 변경, branchIds,roleId,roleIds 추가, branchId <--> branchIds 변경 메소드 추가, roleId <--> roleIds 변경 메소드 추가
 */
package com.kep.portal.model.dto.notice;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIncludeProperties;
import com.kep.core.model.dto.member.MemberDto;
import com.kep.core.model.dto.notice.NoticeOpenType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.ZonedDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 공지사항 정보
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class NoticeResponseDto {
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
	 * 생성자 정보
	 */
	@JsonIncludeProperties({"id","username","nickname"})
	private MemberDto creatorInfo;

	/**
	 * 생성 일시
	 */
	private ZonedDateTime created;

	/**
	 * 수정 일시
	 */
	private ZonedDateTime modified;

	/**
	 * 최근 일주일 여부
	 */
	private Boolean newNotice;

	/**
	 * 공지 사항 파일첨부 연결
	 */

	private List<NoticeUploadDto> noticeUpload;


	/**
	 * 읽기여부
	 */

	private Long readFlag;

	/**
	 * 브랜치 PK 다수
	 */
	@JsonIgnore
	private String branchId;

	private String branchName;

	private Long teamId;

	private String teamName;


	@JsonIgnore
	private String outsourcing;
	/**
	 * 역할 PK 다수
	 */
	@JsonIgnore
	private String roleId;


	private List<Long> Ids;


	private List<String> roleIds;


	private List<String> branchIds;

	private List<String> outsourcingIds;


}

