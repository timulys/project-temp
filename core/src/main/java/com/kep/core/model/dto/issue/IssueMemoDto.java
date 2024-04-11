/**
 * IssueMemoDto 신규
 * 
 *  @생성일자      / 만든사람		 	/ 수정내용
 * 	 2023.04.04 / philip.lee7   /  신규
 * 	 2023.04.07 / philip.lee7	/  ids 파라미터추가(다중삭제 필요)
 */

package com.kep.core.model.dto.issue;

import java.time.ZonedDateTime;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIncludeProperties;
import com.kep.core.model.dto.member.MemberDto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class IssueMemoDto {
	
	private Long id;
	
	private Long issueId;
	
	private String content;
	
	private Long memberId;
	
	private Long guestId;
	
	private ZonedDateTime created;
	
	private ZonedDateTime modified;
	
	private boolean deleted ;
	
	@JsonIncludeProperties({"id","username","nickname"})
	private MemberDto member;

	private List<Long> ids;


}
