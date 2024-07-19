/**
 * 
 * 공지사항 첨부파일 연결 테이블
 * @수정일자	  / 수정자			 / 수정내용 
 * 2023.03.28 / philip.lee7	 / 신규
 * 2023.05.15 / asher.shin   / 사이즈값 추가
 */
package com.kep.portal.model.dto.notice;

import java.time.ZonedDateTime;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIncludeProperties;
import com.kep.core.model.dto.upload.UploadDto;
import com.kep.portal.model.entity.upload.Upload;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class NoticeUploadDto {

	
	@Schema(description = "공지사항 업로드 아이디")
	private Long id;
	
	@Schema(description = "공지사항 아이디")
	private Long noticeId;
	
	@JsonIncludeProperties({"id","original_name","name","path","url","size"})
	@Schema(description = "업로드 정보")
	private UploadDto upload;
}
