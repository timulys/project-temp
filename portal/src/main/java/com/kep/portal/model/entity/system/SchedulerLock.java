package com.kep.portal.model.entity.system;

import lombok.*;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;
import javax.validation.constraints.Size;
import java.time.ZonedDateTime;

/**
 * 스케줄러 락 (ShedLock)
 */
@Entity
@DynamicInsert
@DynamicUpdate
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class SchedulerLock {

	@Id
	@Size(max = 64)
	private String name;

//	@Column(columnDefinition = "TIMESTAMP(3)")
	private ZonedDateTime lockUntil;

//	@Column(columnDefinition = "TIMESTAMP(3)")
	private ZonedDateTime lockedAt;

	private String lockedBy;
}
