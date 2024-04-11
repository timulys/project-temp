package com.kep.portal.model.entity.customer;

import com.kep.core.model.dto.customer.CustomerContactType;
import com.kep.portal.model.converter.FixedCryptoConverter;
import lombok.*;
import org.hibernate.annotations.Comment;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;
import javax.transaction.Transactional;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * 고객 연락처
 */
@Entity
@DynamicInsert
@DynamicUpdate
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class CustomerContact {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Comment("PK")
	private Long id;

	@Comment("고객 PK")
	@NotNull
	private Long customerId;

	@Comment("연락처 타입")
	@Enumerated(EnumType.STRING)
	@NotNull
	private CustomerContactType type;

	@Convert(converter = FixedCryptoConverter.class)
	@Comment("연락처 정보")
//	@NotEmpty
	@Size(max = 1000)
	private String payload;
}
