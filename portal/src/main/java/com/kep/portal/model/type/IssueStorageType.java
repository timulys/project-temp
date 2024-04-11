package com.kep.portal.model.type;

public enum IssueStorageType {

	send_delay_first_reply // 상담대기 중 상담직원응답 지연 안내 발송 여부
	, send_delay_guest // 고객응답 지연 자동종료 발송 여부
	, send_warning_delay_guest // 고객응답 지연 자동종료 예고 발송 여부
	, send_close // 상담종료 안내 발송 여부
	, send_warning_close // 상담종료 예고 사용시, 상담종료 안내 발송 여부
}
