package com.kep.core.model.dto.issue;

public enum IssueStatus {

//	relay, // 오픈빌더 등 타 솔루션
	open, // 상담 요청
	assign,// 배정 완료
	close, // 상담 종료
	ask, // 고객 질의
	reply, // 상담원 답변
	urgent // 고객 질의 중 미답변 시간 초과
}
