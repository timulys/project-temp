package com.kep.core.model.dto.issue;

public enum IssueCloseType {

	guest,//고객종료
	operator, //상담사 종료
	manager, // 강제 종료
	system, //자동 종료
	opened //배정전 종료
}
