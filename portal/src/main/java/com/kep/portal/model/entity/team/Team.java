package com.kep.portal.model.entity.team;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.kep.portal.model.entity.member.Member;
import lombok.*;
import org.hibernate.annotations.*;

import javax.persistence.*;
import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.Table;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import java.time.ZonedDateTime;
import java.util.List;

/**
 * 팀
 */
@Entity
@DynamicInsert
@DynamicUpdate
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
@Table(uniqueConstraints = {
		@UniqueConstraint(name = "UK_TEAM__NAME", columnNames = { "name"})
})
public class Team {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Positive
	@Comment("TEAM PK")
	private Long id;

	@NotEmpty
	@Comment("상담 그룹 명")
	private String name;

	@ColumnDefault("0")
	@Comment("그룹 회원 수")
	private Integer memberCount;

	@NotNull
	@Positive
	@Comment("생성 MEMBER PK")
	private Long creator;

	@NotNull
	@Comment("최초 생성일")
	private ZonedDateTime created;


	@NotNull
	@Positive
	@Comment("수정 MEMBER PK")
	private Long modifier;

	@NotNull
	@Comment("수정일")
	private ZonedDateTime modified;

	@Transient
	@JsonBackReference
	private List<Member> members;

	@Transient
	private Long ownerId;

	@PrePersist
	@PreUpdate
	public void prePersist() {
		if (created == null) {
			created = ZonedDateTime.now();
		}
		if (modified == null) {
			modified = ZonedDateTime.now();
		}
	}

}
