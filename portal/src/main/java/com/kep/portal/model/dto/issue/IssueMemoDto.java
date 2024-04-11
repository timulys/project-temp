/**
 * IssueMemoDto 신규
 * 
 *  @생성일자      / 만든사람		 	/ 수정내용
 * 	 2023.04.04 / philip.lee7   /  신규
 */

package com.kep.portal.model.dto.issue;

import java.time.ZonedDateTime;

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
	
}
