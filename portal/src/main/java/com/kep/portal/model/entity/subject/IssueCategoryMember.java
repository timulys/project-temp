package com.kep.portal.model.entity.subject;

import com.kep.portal.model.entity.member.Member;
import lombok.*;
import org.hibernate.annotations.Comment;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import java.time.ZonedDateTime;

/**
 * {@link IssueCategory}, {@link Member} 매칭
 */
@Entity
@Table(indexes = {
		@Index(name = "IDX_ISSUE_CATEGORY_MEMBER__SEARCH", columnList = "branchId, channelId, issueCategoryId, memberId", unique = true)
})
@DynamicInsert
@DynamicUpdate
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class IssueCategoryMember {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Comment("PK")
	@Positive
	private Long id;

	@Comment("브랜치 PK")
	@NotNull
	private Long branchId;

	@Comment("채널 PK")
	@NotNull
	private Long channelId;

	@Comment("분류 PK")
	@NotNull
	private Long issueCategoryId;

	@Comment("유저 PK")
	@NotNull
	private Long memberId;

	@Comment("수정자")
	@NotNull
	@Positive
	private Long modifier;
	@Comment("수정 일시")
	@NotNull
	private ZonedDateTime modified;

	@PrePersist
	@PreUpdate
	public void prePersist() {
		if (modified == null) {
			modified = ZonedDateTime.now();
		}
	}
}
