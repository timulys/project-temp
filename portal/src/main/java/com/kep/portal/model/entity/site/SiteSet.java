package com.kep.portal.model.entity.site;

import lombok.*;
import org.hibernate.annotations.Comment;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import java.time.ZonedDateTime;

/**
 * 사이트 설정
 */
//@Entity
@DynamicInsert
@DynamicUpdate
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class SiteSet {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Positive
	private Long id;

	@NotEmpty
	private String name;

	@NotEmpty
	private String payload;

	@NotNull
	@Positive
	private Long modifier;
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
