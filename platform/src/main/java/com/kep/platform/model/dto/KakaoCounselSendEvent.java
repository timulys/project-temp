package com.kep.platform.model.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.URL;
import org.springframework.util.ObjectUtils;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.util.Arrays;
import java.util.List;

/**
 * 카카오 상담톡 발신 이벤트
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class KakaoCounselSendEvent {

	public static final int MAX_IMAGE_FILE_SIZE = 5 * 1000; // 5MB
	public static final int MAX_LINK_IMAGE_FILE_SIZE = 500; // 500KB
	public static final List<String> ALLOW_IMAGE_TYPE = Arrays.asList("jpg, jpeg, gif, bmp, png, tiff".split(", "));
	public static final int MAX_GENERAL_FILE_SIZE = 3 * 1000 * 100; // 300MB
	public static final int MAX_VIDEO_FILE_SIZE = 3 * 1000 * 100; // 300MB
	public static final int MAX_AUDIO_FILE_SIZE = 10 * 1000; // 10MB
	public static final List<String> ALLOW_GENERAL_FILE_TYPE = Arrays.asList("pdf, odp, ppt, pptx, key, show, doc, docx, hwp, txt, rtf, xml, wks, wps, xps, md, odf, odt, pages, ods, csv, tsv, xls, xlsx, numbers, cell, psd, ai, sketch, tif, tiff, tga, webp, zip, gz, bz2, rar, 7z, lzh, alz".split(", "));
	public static final List<String> ALLOW_VIDEO_FILE_TYPE = Arrays.asList("mp4, m4v, avi, asf, wmv, mkv, ts, mpg, mpeg, mov, flv, ogv".split(", "));
	public static final List<String> ALLOW_AUDIO_FILE_TYPE = Arrays.asList("mp3, wav, flac, tta, tak, aac, wma, ogg, m4a".split(", "));
	public static final int MAX_TEXT_SIZE_TEXT_TYPE = 1000;
	public static final int MAX_TEXT_SIZE_LINK_TYPE = 60;
	public static final int MAX_BUTTON_SIZE = 5;
	public static final int SERIAL_NUMBER_SIZE = 30;
	public static final int SERIAL_NUMBER_PADDING_SIZE = 5;
	public static final String SERIAL_NUMBER_PREFIX = "a";
	public static final String SERIAL_NUMBER_PREFIX_ON_EMPTY = "b";

	// 메세지 타입
	public static final String MESSAGE_TYPE_TEXT = "TX";
	public static final String MESSAGE_TYPE_IMAGE = "IM";
	public static final String MESSAGE_TYPE_LINK = "LI";
	public static final String MESSAGE_TYPE_FILE = "FI";
	public static final String MESSAGE_TYPE_AUDIO = "AU";

	// 버튼 타입
	public static final String BUTTON_TYPE_WEB = "WL";
	public static final String BUTTON_TYPE_APP = "AL";
	public static final String BUTTON_TYPE_TEXT = "BK";
	public static final String BUTTON_TYPE_BOT = "BT";
	public static final String BUTTON_TYPE_BIZ_FORM = "BF";

	// 이미지 타입
	public static final String IMAGE_TYPE_LINK = "link";

	/**
	 * 발신 프로필 키
	 */
	@NotEmpty
	@Size(max = 40)
	private String senderKey;

	/**
	 * 사용자 키
	 */
	@NotEmpty
	@Size(max = 20)
	private String userKey;

	/**
	 * 메시지일련번호 (발신 메시지에 대한 고유값)
	 */
	@NotEmpty
	@Size(max = 30)
	private String serialNumber;

	/**
	 * 메시지 타입 (TX: 텍스트, IM: 이미지, FI: 일반파일, AU: 오디오파일, LI: 링크타입버튼)
	 */
	@NotEmpty
	@Size(max = 2)
	@Pattern(regexp = "TX|IM|LI|FI|AU")
	private String messageType;

	/**
	 * 메시지
	 */
	@Size(max = 1000)
	private String message;

	/**
	 * 이미지 URL
	 */
	@URL
	@Size(max = 1000)
	private String imageUrl;

	/**
	 * 파일 URL
	 */
	@URL
	@Size(max = 1000)
	private String fileUrl;

	/**
	 * 파일 이름
	 */
	@Size(max = 1000)
	private String fileName;

	/**
	 * 파일 사이즈
	 */
	private Long fileSize;

	/**
	 * 시스템 자동 응답 메시지 (S1: 상담불가, S2: 상담부재, S3: 무응답 상담종료, S4: 상담대기)
	 * 비즈 메세지 센터에서 각각 메세지 세팅
	 * <pre>
	 * "message_type": "LI",
	 * "auto_answer": "S1"
	 * </pre>
	 */
	@Size(max = 2)
	@Pattern(regexp = "S1|S2|S3|S4")
	private String autoAnswer;

	/**
	 * 버튼
	 */
	@Size(max = 5)
	private List<Link> links;

	/**
	 * 바로 연결
	 */
	private Supplement supplement;

	@Data
	@NoArgsConstructor
	@AllArgsConstructor
	@Builder
	public static class Link {

		/**
		 * 버튼명
		 */
		@NotEmpty
		@Size(max = 28)
		private String name;

		/**
		 * 버튼 타입
		 */
		@NotEmpty
		@Size(max = 2)
		@Pattern(regexp = "WL|AL|BK|BT|BF")
		private String type;

		/**
		 * Android Custom Scheme
		 */
		@Size(max = 1000)
		private String schemeAndroid;

		/**
		 * iOS Custom Scheme
		 */
		@Size(max = 1000)
		private String schemeIos;

		/**
		 * 모바일 클릭 URL (http|https)
		 */
		@URL
		private String urlMobile;

		/**
		 * PC 클릭 URL (http|https)
		 */
		@URL
		private String urlPc;

		/**
		 * BK, MD 버튼 발송 시 전달할 extra 정보
		 * <pre>
		 * 4.2.1.1.1 버튼 타입별 속성
		 * extra, event는 최대 50자이며 [A-Za-z0-9_]{1,50}에 해당해야 합니다
		 * </pre>
		 */
		@Size(max = 50)
		@Pattern(regexp = "[A-Za-z0-9_]{1,50}")
		private String extra;

		/**
		 * 상담톡에서 봇 상담으로 전환 (호출할 봇 이벤트명)
		 * <pre>
		 * 4.2.1.1.1 버튼 타입별 속성
		 * extra, event는 최대 50자이며 [A-Za-z0-9_]{1,50}에 해당해야 합니다
		 * </pre>
		 */
		@Size(max = 50)
		@Pattern(regexp = "[A-Za-z0-9_]{1,50}")
		private String event;

		/**
		 * 비즈니스폼 버튼 사용을 위해 필요한 비즈니스폼키 <별첨. 비즈니스폼 업로드> 를 통해 발급
		 */
		private String bizFormKey;
	}

	public static class Supplement {

		@Size(max = 10)
		private List<QuickReply> quickReply;
	}

	public static class QuickReply {

		/**
		 * 버튼명
		 */
		@NotEmpty
		@Size(max = 14)
		private String name;

		/**
		 * 버튼 타입
		 */
		@NotEmpty
		@Size(max = 2)
		@Pattern(regexp = "WL|AL|BK|BT|BF")
		private String type;

		/**
		 * Android Custom Scheme
		 */
		@Size(max = 1000)
		private String schemeAndroid;

		/**
		 * iOS Custom Scheme
		 */
		@Size(max = 1000)
		private String schemeIos;

		/**
		 * 모바일 클릭 URL (http|https)
		 */
		@URL
		private String urlMobile;

		/**
		 * PC 클릭 URL (http|https)
		 */
		@URL
		private String urlPc;

		/**
		 * 상담톡/봇 전환 시 전달할 메타정보
		 */
		@Size(max = 50)
		private String chatExtra;

		/**
		 * 봇 전환 시 연결할 봇 이벤트명
		 */
		@Size(max = 50)
		private String chatEvent;

		/**
		 * 비즈니스폼 버튼 사용을 위해 필요한 비즈니스폼키 <별첨. 비즈니스폼 업로드> 를 통해 발급
		 */
		private String bizFormKey;
	}

	// ////////////////////////////////////////////////////////////////////////
	// End With Bot Event Only
	// ////////////////////////////////////////////////////////////////////////
	/**
	 * 봇 이벤트 명
	 */
	@Size(max = 100)
	private String botEvent;

	// ////////////////////////////////////////////////////////////////////////
	// Utilities
	// ////////////////////////////////////////////////////////////////////////
	/**
	 * 텍스트 메세지 추가 (줄바꿈 이후 append)
	 */
	@JsonIgnore
	public void appendTextMessage(@NotEmpty String message) {
		if (ObjectUtils.isEmpty(this.message)) {
			this.message = message;
//		} else if (this.message.endsWith("\n")) {
//			this.message += message;
		} else {
			this.message += "\n" + message;
		}
	}
}
