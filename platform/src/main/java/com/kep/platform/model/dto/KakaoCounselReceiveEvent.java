package com.kep.platform.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Positive;
import java.util.List;

/**
 * 카카오 상담톡 수신 이벤트
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class KakaoCounselReceiveEvent {

	/**
	 * 메시지를 발송한 사용자 키
	 */
	@NotEmpty
	private String userKey;

	/**
	 * 메시지를 수신한 발신프로필 키
	 */
	@NotEmpty
	private String senderKey;

	// ////////////////////////////////////////////////////////////////////////
	// Message Event Only
	// ////////////////////////////////////////////////////////////////////////
	/**
	 * 메시지 전달 상담톡 서버 시각 (실제 유저가 입력한 시각은 아님)
	 */
	@Positive
	private Long time;

	/**
	 * 메시지 고유 id
	 */
	@Positive
	private Long serialNumber;

	/**
	 * 사용자가 전송한 메시지 종류
	 */
	private MessageType type;

	public enum MessageType {
		text, photo, video, audio, file
	}

	/**
	 * 사용자가 전송한 메시지 내용
	 *
	 * API 포맷 변경 (content -> contents)
	 * https://bzm-center.kakao.com/#/notice/296
	 */
	@Deprecated
	private Object content;
	private List<Object> contents;

	/**
	 * 사용자가 전송한 메시지가 1000자 초과일 경우
	 * - 300자까지 content로 전달
	 * - 300자 이상은 attachment url(txt파일)로 전달
	 */
	private Object attachment;

	/**
	 * 사용자가 전송한 메시지의 extra 정보
	 * - BK/MD 버튼을 클릭하여 전송한 메시지
	 */
	private String extra;

	// ////////////////////////////////////////////////////////////////////////
	// Reference Event Only
	// ////////////////////////////////////////////////////////////////////////
	/**
	 * 고객사에서 설정한 메타정보
	 */
	private Reference reference;

	/**
	 * 고객사에서 설정한 메타 정보가 없을 경우 가장 마지막 정보
	 */
	private Reference lastReference;

	/**
	 * 사용자 메타 정보 수신
	 */
	@Data
	@NoArgsConstructor
	@AllArgsConstructor
	@Builder
	public static class Reference {

		/**
		 * 버튼을 통해 전달된 메타 정보
		 */
		private String extra;
		/**
		 * 상담을 어떻게 시작했는지 값
		 */
		private String bot;
		/**
		 * 봇으로 상담 시작 시, 봇 블럭 이벤트 값
		 */
		private String botEvent;
		/**
		 * 마지막 메타 정보 생성일
		 */
		private String createdAt;
	}

	/**
	 * 메시지를 발신한 사용자의 앱 유저 아이디 (없을 경우 null로 표시)
	 */
	private String appUserId;

	/**
	 * 레버리지 지원/미지원 여부, true 경우 지원
	 */
	private String supportButton;

	// ////////////////////////////////////////////////////////////////////////
	// Expired Session Event Only
	// ////////////////////////////////////////////////////////////////////////
	@NotEmpty
	private String sessionId;

	// ////////////////////////////////////////////////////////////////////////
	// Message Bot Only
	// ////////////////////////////////////////////////////////////////////////
}
