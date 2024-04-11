package com.kep.portal.model.dto.statistics;

import java.time.ZonedDateTime;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
public class ReplyStatusDetailDto {

	private Long issueId;
	private String timeGroup;

	private Long memberId;
	private String memberName; // 상담원명
	private Long guestId; // 고객ID
	private String guestName; // 고객명
	
	private String status;//relay: 외부연동, open: 상담요청, assign: 배정완료, close: 종료, ask: 고객질의, reply: 상담원응대, send_delay_first_reply : 지연
	private String statusToShow;//relay: 외부연동, open: 상담요청, assign: 배정완료, close: 종료, ask: 고객질의, reply: 상담원응대
	
	private Long watingTime;//대기시간	
	private Long counselTime;//상담시간, status가 close인 경우에만 계산됨
	
	private Long issueCategoryId; // issue_extra
	private String issueCategoryName; // issue_category
	
	private String summary;//issue_extra
	private ZonedDateTime created;
	private ZonedDateTime closed;
}
