package com.kep.core.model.dto.event;

/**
 * 플랫폼 이벤트 타입
 */
public enum PlatformEventType {

	OPEN,
	MESSAGE, // 상담톡 메세지, 비즈톡 발송
	CLOSE, // 상담톡 세션 종료, 비즈톡 결과 처리 완료
	VERIFY, // 비즈톡 결과 요청
	RELAY,
	RESOURCE,
	UNKNOWN
}
