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

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class IssueMemoDto {
	
	@Schema(description = "이슈 메모 아이디")
	private Long id;
	
	@Schema(description = "이슈 아이디")
	private Long issueId;
	
	@Schema(description = "내용")
	private String content;
	
	@Schema(description = "사용자 아이디")
	private Long memberId;
	
	@Schema(description = "게스트 아이디")
	private Long guestId;
	
	@Schema(description = "생성일시")
	private ZonedDateTime created;
	
	@Schema(description = "수정일시")
	private ZonedDateTime modified;
	
	@Schema(description = "삭제여부")
	private boolean deleted ;
	
	@JsonIncludeProperties({"id","username","nickname"})
	@Schema(description = "사용자 정보")
	private MemberDto member;

	@Schema(description = "메모 아이디 목록")
	private List<Long> ids;


}
