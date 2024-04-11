package com.kep.portal.model.entity.channel;

import com.kep.core.model.dto.platform.PlatformType;
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
 * 채널, Platform 서비스 단위 (ex, 카카오 채널)
 */
@Entity
@DynamicInsert
@DynamicUpdate
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Table(
		uniqueConstraints = {
			@UniqueConstraint(name = "UK_CHANNEL__PLATFORM", columnNames = {"platform", "serviceKey"})
		},
		indexes = {
				@Index(name = "IDX_CHANNEL__BRANCH", columnList = "branchId")
		}
)
public class Channel {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Comment("PK")
	@Positive
	private Long id;

	@Comment("대표 브랜치")
	@Positive
	private Long branchId;

	@Comment("이름")
	@NotEmpty
	private String name;

	@Comment("플랫폼")
	@Enumerated(EnumType.STRING)
	@NotNull
	private PlatformType platform;

	@Comment("서비스 키")
	@NotEmpty
	private String serviceKey;

	@Comment("서비스 아이디")
	@NotEmpty
	private String serviceId;

	@Comment("수정자")
	@NotNull
	@Positive
	private Long modifier;
	@Comment("수정 일시")
	private ZonedDateTime modified;

	@PrePersist
	@PreUpdate
	public void prePersist() {
		if (modified == null) {
			modified = ZonedDateTime.now();
		}
	}
}
