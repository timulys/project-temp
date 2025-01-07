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
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.ZonedDateTime;
import java.util.List;

/**
 * 공지사항 정보
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class NoticeResponseDto {
	@Schema(description = "공지사항 아이디(PK)" , requiredMode = Schema.RequiredMode.REQUIRED)
	private Long id;

	@Schema(description = "공지사항 제목")
	private String title;

	@Schema(description = "공지사항 본문")
	private String content;

	@Schema(description = "오픈 범위(전체공개, 브랜치공개) ( all : 전체공개 , branch : 브랜치 공개 )")
	private NoticeOpenType openType;

	@Schema(description = "상단 고정 여부")
	private Boolean fixation;

	@Schema(description = "사용 여부")
	private Boolean enabled;

	@Schema(description = "생성자 정보")
	@JsonIncludeProperties({"id","username","nickname"})
	private MemberDto creatorInfo;

	@Schema(description = "생성 일시")
	private ZonedDateTime created;

	@Schema(description = "수정 일시")
	private ZonedDateTime modified;

	@Schema(description = "최근 일주일 여부")
	private Boolean newNotice;

	/**
	 * 공지 사항 파일첨부 연결
	 */
	private List<NoticeUploadDto> noticeUpload;


	@Schema(description = "읽기 여부 ( 0 : 미확인 , 1 : 확인 )")
	private Long readFlag;

	@JsonIgnore
	@Schema(description = "브랜치 아이디")
	private String branchId;

	@Schema(description = "브랜치명")
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

