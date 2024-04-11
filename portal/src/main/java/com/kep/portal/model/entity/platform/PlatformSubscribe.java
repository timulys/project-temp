package com.kep.portal.model.entity.platform;

import com.kep.portal.model.converter.BooleanConverter;
import lombok.*;
import org.hibernate.annotations.*;

import javax.persistence.*;
import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import java.time.ZonedDateTime;

/**
 * 유저
 */
@Entity
@DynamicInsert
@DynamicUpdate
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Table(
        uniqueConstraints = {
                @UniqueConstraint(name = "UK_PLATFORM_SUBSCRIBE__APP", columnNames = {"serviceId", "platformUserId"})
        },
        indexes = {
                @Index(name = "IDX_PLATFORM_SUBSCRIBE__USER", columnList = "platformUserId")
        }
)
public class PlatformSubscribe
{

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Comment("PK")
    @Positive
    private Long id;

    @Comment("채널 PK")
    private String serviceId;

    @Comment("플랫폼 고객 식별 PK")
    private String platformUserId;

    @Comment("구독 , 취소")
    @Convert(converter = BooleanConverter.class)
    private Boolean enabled;

    @Comment("수정 일시")
    @NotNull
    private ZonedDateTime modified;
}
