package com.kep.portal.model.entity.subject;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.kep.portal.model.converter.BooleanConverter;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.Comment;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/**
 * 이슈 분류
 */
@Entity
// TODO: unique(channel,parent,name)?
@Table(indexes = {
//		@Index(name = "IDX_ISSUE_CATEGORY_SEARCH", columnList = "branchId, channelId, exposed, enabled, depth, name")
		@Index(name = "IDX_ISSUE_CATEGORY__SEARCH", columnList = "channelId, enabled, depth, name"),
		@Index(name = "IDX_ISSUE_CATEGORY__PARENT", columnList = "channelId, parentId, enabled")
})
@DynamicInsert
@DynamicUpdate
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class IssueCategory {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Comment("PK")
	@Positive
	private Long id;

	@Comment("이름")
	@NotEmpty
	private String name;

//	@NotNull
//	@Positive
//	private Long branchId;

	@Comment("채널 PK")
	@NotNull
	@Positive
	private Long channelId;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "parentId", foreignKey = @ForeignKey(name="FK_ISSUE_CATEGORY__PARENT"))
	@Comment("상위 분류 PK")
	@JsonIgnoreProperties({"parent", "path"})
	private IssueCategory parent;

	@Comment("계층")
	@NotNull
	@Positive
	private Integer depth;

	@Comment("정렬")
	@NotNull
	@Positive
	private Integer sort;

	@Column(length = 1)
	@ColumnDefault("'Y'")
	@Comment("전체 오픈 여부")
	@NotNull
	@Convert(converter = BooleanConverter.class)
	private Boolean exposed;

	@Column(length = 1)
	@ColumnDefault("'Y'")
	@Comment("사용 여부")
	@NotNull
	@Convert(converter = BooleanConverter.class)
	private Boolean enabled;

	@Comment("수정자")
	@NotNull
	@Positive
	private Long modifier;
	@Comment("수정 일시")
	@NotNull
	private ZonedDateTime modified;

	/**
	 * FIXME :: BNK 코드. 엔티티에서 제거 필요 20240715 volka
	 */
	//BNK 분류 코드값 추가
	@Comment("상담유형 BNK 코드")
	private String bnkCode;

	@PrePersist
	@PreUpdate
	public void prePersist() {
		if (modified == null) {
			modified = ZonedDateTime.now();
		}
	}

	@Transient
	@JsonIgnore
	public static List<IssueCategory> getPath(@NotNull IssueCategory issueCategory) {

		List<IssueCategory> path = new ArrayList<>();

		IssueCategory current = issueCategory;
		path.add(current);

		while (current.getParent() != null) {
			current = current.getParent();
			path.add(current);
		}

		path.sort(Comparator.comparingInt(IssueCategory::getDepth));
		return path;
	}

	@Transient
	@JsonIgnore
	public List<Long> channelIds;
}
