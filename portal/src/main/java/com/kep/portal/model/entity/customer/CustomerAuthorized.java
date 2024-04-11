package com.kep.portal.model.entity.customer;

import com.kep.core.model.dto.platform.AuthorizeType;
import com.kep.portal.model.entity.platform.PlatformSubscribe;
import lombok.*;
import org.hibernate.annotations.Comment;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.validator.constraints.URL;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import java.time.ZonedDateTime;
import java.util.List;

/**
 * 본인인증 플랫폼
 */
@Entity
@Table(indexes = {
        @Index(name = "IDX_CUSTOMER__ID", columnList = "customerId")
})
@DynamicInsert
@DynamicUpdate
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class CustomerAuthorized {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Comment("PK")
    @Positive
    private Long id;

    @Comment("customer PK")
    @NotNull
    @Positive
    private Long customerId;

    @Comment("인증 타입")
    @NotNull
    @Enumerated(EnumType.STRING)
    private AuthorizeType type;

//    @Comment("플랫폼 고객 PK")
    @Comment("BNK 카카오CI PK")
    @NotNull
    private String platformUserId;

    @Comment("생성 일시")
    @NotNull
    private ZonedDateTime created;
    
    //BNk 고객정보 조회를 위한 참조관계 추가
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "customerId", referencedColumnName = "id", insertable = false, updatable = false)
    private Customer customer;
    
    @Transient
    private List<PlatformSubscribe> platformSubscribe;

}
