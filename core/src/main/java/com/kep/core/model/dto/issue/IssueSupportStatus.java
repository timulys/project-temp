package com.kep.core.model.dto.issue;

public enum IssueSupportStatus {
	request, // 상담검토/상담직원전환 요청
	reject, // 반려
	finish, // 완료
	change, // 상담직원변경
	receive, // 상담이어받기
	auto, // 전환자동승인
	end // 상담종료
}
