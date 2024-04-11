package com.kep.portal.model.entity.customer;

import com.kep.portal.model.converter.FixedCryptoConverter;
import com.kep.portal.model.entity.issue.IssueExtra;
import com.kep.portal.model.entity.platform.PlatformSubscribe;
import lombok.*;
import org.hibernate.annotations.Comment;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.validator.constraints.URL;

import javax.persistence.*;
import javax.validation.constraints.Positive;
import java.util.List;

/**
 * 식별 고객 (본인인증, 고객사 로그인 등)
 */
@Entity
@Table(indexes = {
		@Index(name  = "IDX_CUSTOMER__SEARCH"		, columnList = "name")
		,@Index(name = "IDX_CUSTOMER__IDENTIFIER"	, columnList = "identifier")
})
@DynamicInsert
@DynamicUpdate
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Customer {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Comment("PK")
	@Positive
	private Long id;

	@Comment("고객 고유키")
	private String identifier;

	@Convert(converter = FixedCryptoConverter.class)
	@Comment("이름")
	private String name;

	@Comment("프로필")
	@URL
	private String profile;

	@Convert(converter = FixedCryptoConverter.class)
	@Comment("연령대")
	private String age;
	
	@OneToMany(fetch = FetchType.EAGER)
	@JoinColumn(name = "customerId", updatable = false, foreignKey = @ForeignKey(name="FK_CUSTOMER__CUSTOMER_ANNIVERSARY"))
	@Comment("생년월일")
	private List<CustomerAnniversary> anniversaries;

	@Transient
	private List<CustomerContact> contacts;

	@Transient
	private List<CustomerAuthorized> authorizeds;

	@Transient
	private List<PlatformSubscribe> platformSubscribes;

	@Transient
	private List<IssueExtra> inflows;


}
