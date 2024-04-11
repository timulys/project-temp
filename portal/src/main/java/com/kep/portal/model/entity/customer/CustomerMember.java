package com.kep.portal.model.entity.customer;

import com.kep.portal.model.converter.BooleanConverter;
import lombok.*;
import org.hibernate.annotations.Comment;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

@Entity
@DynamicInsert
@DynamicUpdate
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Table(
        indexes = {
                @Index(name = "IDX_CUSTOMER_MEMBER_FAVORITE", columnList = "memberId, favorite"),
        },
        uniqueConstraints = {
                @UniqueConstraint(name = "UK_CUSTOMER_MEMBER_CUSTOMER", columnNames = {"memberId", "customerId"}
        )}
)
public class CustomerMember {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Comment("PK")
    @Positive
    private Long id;

    @Positive
    @Comment("member PK")
    private Long memberId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "customerId", foreignKey = @ForeignKey(name="FK_CUSTOMER_MEMBER__CUSTOMER"))
    @Comment("고객 정보")
    private Customer customer;

    @Column(length = 1)
    @Convert(converter = BooleanConverter.class)
    @NotNull
    @Comment("즐겨찾기")
    private Boolean favorite;
}
