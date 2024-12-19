package com.kep.core.model.dto.issue;

public enum IssueLogStatus {
	send, // 상담원 메세지 발송
	receive, // 고객 메세지 발송
	fail,
	read
}
