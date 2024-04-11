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

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class NoticeUploadDto {

	
	private Long id;
	
	private Long noticeId;
	
	@JsonIncludeProperties({"id","original_name","name","path","url","size"})
	private UploadDto upload;
}
