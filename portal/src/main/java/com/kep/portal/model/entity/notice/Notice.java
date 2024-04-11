package com.kep.portal.model.entity.notice;

import com.kep.core.model.dto.notice.NoticeOpenType;
import com.kep.portal.model.converter.BooleanConverter;
import lombok.*;
import org.hibernate.annotations.Comment;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.springframework.lang.Nullable;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;
import java.time.ZonedDateTime;
import java.util.List;

/**
 * 이슈, 상세 정보
 */
@Entity
@DynamicInsert
@DynamicUpdate
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Table(indexes = {
		@Index(name = "IDX_NOTICE__SEARCH_PORTAL", columnList = "branchId, openType, fixation desc, created desc"),
		@Index(name = "IDX_NOTICE__SEARCH_MANAGER", columnList = "branchId, fixation desc, created desc"),
		//@Index(name = "IDX_NOTICE__SEARCH_KEYWORD_PORTAL", columnList = "branchId, openType, title, content, fixation desc, created desc"),
		@Index(name = "IDX_NOTICE__SEARCH_KEYWORD_PORTAL", columnList = "branchId, openType, title, fixation desc, created desc"),
		//@Index(name = "IDX_NOTICE__SEARCH_KEYWORD_MANAGER", columnList = "branchId, title, content, fixation desc, created desc")
		@Index(name = "IDX_NOTICE__SEARCH_KEYWORD_MANAGER", columnList = "branchId, title, fixation desc, created desc")
})
public class Notice {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Comment("PK")
	@Positive
	private Long id;

	@Comment("공지사항 제목")
	@Size(max = 500)
	private String title;

	@Comment("공지사항 본문")
	@Column(length=5000)
	private String content;

	@Comment("오픈 범위(전체공개, 브랜치공개)")
	@Enumerated(EnumType.STRING)
	private NoticeOpenType openType;

	@Comment("상단 고정 여부")
	@Column(length = 1)
	@Convert(converter = BooleanConverter.class)
	@Builder.Default
	private Boolean fixation = false;

	@Comment("사용 여부")
	@Column(length = 1)
	@Convert(converter = BooleanConverter.class)
	@Builder.Default
	private Boolean enabled = true;

	@Comment("브랜치 PK")
	@NotNull
	@Positive
	private Long branchId;

	@Comment("branch team PK")
	private Long teamId;

	@Comment("등록 유저 PK")
	@NotNull
	@Positive
	private Long creator;

	@Comment("등록 시간")
	@NotNull
	private ZonedDateTime created;

	@Comment("수정 유저 PK")
	@Positive
	private Long modifier;

	@Comment("수정 시간")
	private ZonedDateTime modified;


    @Comment("공지사항 업로드 리스트")
    @OneToMany(mappedBy = "notice")
	private List<NoticeUpload> noticeUpload;

	@Transient
	private Long readFlag;

}
