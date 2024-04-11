package com.kep.core.model.dto.platform;

/**
 * 카카오 비즈톡 예약 상태
 */
public enum BizTalkTaskStatus {

	open, // 대기
	cancel, // 취소
	close, // 발송
	error // 오류
}
