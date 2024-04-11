package com.kep.portal.model.entity.assign;

import com.kep.portal.model.converter.BooleanConverter;
import lombok.*;
import org.hibernate.annotations.Comment;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

/**
 * 배정 룰
 */
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Table(indexes = {
		@Index(name = "IDX_ASSIGN__SORT", columnList = "sort")
})
public class AssignMethod {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Positive
	@Comment("PK")
	private Long id;

	@Column(updatable = false)
	@NotEmpty
	@Comment("SERVICE ASSIGN 파일 명")
	private String signature;

	@NotEmpty
	@Comment("SERVICE ASSIGN 명")
	private String name;

	@NotNull
	@Positive
	@Comment("정렬")
	private Integer sort;

	@Column(length = 1)
	@Convert(converter = BooleanConverter.class)
	@NotNull
	@Comment("사용유무")
	private Boolean enabled;
}
