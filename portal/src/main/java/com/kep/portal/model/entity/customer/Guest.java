package com.kep.portal.model.entity.customer;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;
import org.hibernate.Hibernate;
import org.hibernate.annotations.Comment;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.springframework.util.ObjectUtils;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import java.time.ZonedDateTime;
import java.util.Objects;

/**
 * 비식별 고객
 */
@Entity
@Table(name = "CUSTOMER_GUEST",
	uniqueConstraints = {
		@UniqueConstraint(name = "UK_GUEST__SEARCH", columnNames = { "channelId", "userKey" })
},
	indexes = {
		@Index(name = "IDX_GUEST__CUSTOMER", columnList = "customerId"),
		@Index(name = "IDX_GUEST__NAME", columnList = "name"),
		@Index(name = "IDX_GUEST_CUSTNO", columnList = "cust_no")
})
@DynamicInsert
@DynamicUpdate
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Guest {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Comment("PK")
	@Positive
	private Long id;

	@Comment("채널 PK")
	@NotNull
	private Long channelId;

	@Comment("유저키")
	@NotNull
	private String userKey;

	@Comment("플랫폼 고객 PK")
	private String platformUserId;
  
//	@Deprecated
//	@Comment("식별 고객 PK")
//	@Positive
//	private Long customerId;
	
	@Comment("이름")
	private String name;
	
	//BNK 고객검색 키워드 
	@Comment("고객번호")
	@Column(name="cust_no")
	private String custNo;
	

	@OneToOne
	@JoinColumn(name = "customerId", foreignKey = @ForeignKey(name="FK_GUEST__CUSTOMER"))
	@Comment("식별 고객 PK")
	@JsonIgnoreProperties({"customerContacts"})
	private Customer customer;

	@Comment("생성 일시")
	@NotNull
	private ZonedDateTime created;

	@PrePersist
	public void prePersist() {
		if (this.created == null) {
			this.created = ZonedDateTime.now();
		}
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
		Guest guest = (Guest) o;

		return Objects.equals(this.id, guest.id);
	}

	@Override
	public int hashCode() {
		return Objects.hash(id);
	}
}
