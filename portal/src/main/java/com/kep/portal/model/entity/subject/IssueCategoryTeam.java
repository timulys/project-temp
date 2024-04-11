package com.kep.portal.model.entity.subject;

import com.kep.portal.model.entity.team.Team;
import lombok.*;
import org.hibernate.annotations.Comment;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import java.time.ZonedDateTime;

/**
 * 분류-팀 매칭
 *
 * TODO: unique(issue_category_id, team_id)
 */
//@Entity
@DynamicInsert
@DynamicUpdate
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class IssueCategoryTeam {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Comment("PK")
	@Positive
	private Long id;

	@ManyToOne
	@JoinColumn(name = "issue_category_id", foreignKey = @ForeignKey(name="FK_ISSUE_CATEGORY_TEAM__ISSUE_CATEGORY"))
	@Comment("분류 PK")
	@NotNull
	private IssueCategory issueCategory;

	@ManyToOne
	@JoinColumn(name = "team_id", foreignKey = @ForeignKey(name="FK_ISSUE_CATEGORY_TEAM__TEAM"))
	@Comment("상담그룹 PK")
	@NotNull
	private Team team;

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
