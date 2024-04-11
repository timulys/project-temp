 /**
  * issue_memo Entity 신규
  * 
  *  @생성일자      / 만든사람		 	/ 수정내용
  *  2023.04.04 / philip.lee7   	/ 신규
  */

package com.kep.portal.model.entity.issue;

import java.time.ZonedDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;

import org.hibernate.annotations.Comment;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(indexes = {
		@Index(name = "IDX_ISSUE_MEMO__SEARCH", columnList = "id,issueId")
})
@DynamicInsert
@DynamicUpdate
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class IssueMemo {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Comment("PK")
	@Positive
	private Long id;
	
	@NotNull
	@Comment("상담창 PK")
	private Long issueId;
	
	@Column(length = 1000)
	@Size(max = 1000)
	@Comment("메모내용")
	private String content;
	
	@NotNull
	@Comment("상담원 PK")
	private Long memberId;

	@Comment("게스트 PK")
	private Long guestId;

	@Comment("생성시간")
	private ZonedDateTime created;

	@Comment("수정시간")
	private ZonedDateTime modified;

	@Comment("삭제여부")
	private boolean deleted ;
}
