package com.kep.portal.model.entity.customer;

import com.kep.core.model.dto.customer.AnniversaryType;
import lombok.*;
import org.hibernate.annotations.Comment;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import java.time.LocalDate;
import java.time.ZonedDateTime;

/**
 * 식별 고객 기념일
 */
@Entity
@Table(uniqueConstraints = {
        @UniqueConstraint(name = "UK_CUSTOMER_ANNIVERSARY", columnNames = {"customerId", "type"})
    }, indexes = {
        @Index(name = "IDX_CUSTOMER_ANNIVERSARY", columnList = "customerId,anniversary")
    }
)
@DynamicInsert
@DynamicUpdate
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class CustomerAnniversary {

    @Comment("PK")
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Positive
    private Long id;

    @Comment("식별 고객 PK")
    @NotNull
    private Long customerId;

    @Comment("기념일 타입")
    @Enumerated(EnumType.STRING)
    @NotNull
    private AnniversaryType type;

    @Comment("기념 일자")
    @NotNull
    private LocalDate anniversary;

    @Comment("생성 일시")
    @NotNull
    private ZonedDateTime created;
}
