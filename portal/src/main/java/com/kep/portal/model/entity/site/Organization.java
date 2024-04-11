package com.kep.portal.model.entity.site;

import lombok.*;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Positive;

// TODO: Site
//@Entity
@DynamicInsert
@DynamicUpdate
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
public class Organization {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Positive
	private Long id;

	@NotEmpty
	private String name;
}
