package com.kep.portal.model.entity.platform;

import com.kep.core.model.dto.platform.PlatformTemplateStatus;
import com.kep.core.model.dto.platform.PlatformType;
import lombok.*;
import org.hibernate.annotations.Comment;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;
import java.time.ZonedDateTime;

/**
 * 플랫폼 템플릿 반려 이력
 */
@Entity
@Table
@DynamicInsert
@DynamicUpdate
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class PlatformTemplateRejectHistory {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Comment("PK")
	@Positive
	private Long id;

	@Comment("플랫폼 템플릿 PK")
	@Positive
	@NotNull
	private Long platformTemplateId;

	@Comment("카카오 플랫폼 코멘트 PK")
	@Positive
	private Integer commentSeqno;

	@Comment("카카오 플랫폼 코멘트 결과 및 요청사항")
	@Size(max = 4000)
	private String commentContent;

	@Comment("카카오 플랫폼 코멘트 작성자")
	private String commentUserName;

	@Comment("카카오 플랫폼 코멘트 작성자")
	private String commentCreateAt;

	@Comment("카카오 플랫폼 코멘트 상태")
	private String commentStatus;

	@Comment("카카오 플랫폼 코멘트 생성자")
	private String regBy;

	@Comment("카카오 플랫폼 코멘트 생성 일시")
	private String regDate;

	@Comment("카카오 플랫폼 코멘트 수정자")
	private String updateBy;

	@Comment("카카오 플랫폼 코멘트 수정 일시")
	private String updateDate;

	@Comment("생성자")
	@NotNull
	@Positive
	private Long creator;

	@Comment("생성 일시")
	@NotNull
	private ZonedDateTime created;
}
