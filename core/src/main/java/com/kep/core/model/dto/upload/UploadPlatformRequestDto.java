package com.kep.core.model.dto.upload;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.URL;
import org.springframework.web.multipart.MultipartFile;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UploadPlatformRequestDto {

	/**
	 * 파일 소스, URL
	 */
	@URL
	private String sourceUrl;

	/**
	 * 파일 소스, 로컬 스토리지에서 접근 가능시
	 */
	private String sourcePath;

	/**
	 * 파일 소스, 폼 데이터
	 */
	private MultipartFile sourceFile;

	/**
	 * 카카오 상담톡, 이미지 타입
	 */
	private String imageType;
}
