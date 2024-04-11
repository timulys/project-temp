package com.kep.portal.model.entity.env;

import com.kep.core.model.dto.system.SystemEnvEnum;
import com.kep.portal.model.converter.BooleanConverter;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.Comment;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import java.time.ZonedDateTime;

@Entity
@DynamicInsert
@DynamicUpdate
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter

/**
 * 상담 환경 설정 상담 유입경로 설정
 */
@Table(
        indexes = {
                @Index(columnList = "branchId" , name = "IDX_COUNSEL_INFLOW_ENV__BRANCH"),
                @Index(columnList = "enabled" , name = "IDX_COUNSEL_INFLOW_ENV__ENABLED")
        }
        , uniqueConstraints = {
                @UniqueConstraint(
                        name = "UK_COUNSEL_INFLOW_ENV__BRANCH_PARAMS",
                        columnNames = {"branchId", "params"}
                )
        }
)
public class CounselInflowEnv {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Positive
    private Long id;

    @Positive
    @NotNull
    @Comment("branch PK")
    private Long branchId;

    @NotNull
    @Comment("상담 유입경로")
    private String params;

    @NotNull
    @Comment("상담 유입경로 명")
    private String name;

    @NotNull
    @Comment("상담 유입경로 대상 official : 공식 채널 , unlimited : 제한없음")
    @Enumerated(EnumType.STRING)
    private SystemEnvEnum.InflowPathType inflowPathType;

    @NotNull
    @Comment("상담 유입경로 값")
    @Column(name = "val")
    private String value;

    @NotNull
    @Comment("수정 회원 PK")
    private Long modifier;

    @NotNull
    @Comment("수정일")
    private ZonedDateTime modified;

    @Comment("사용 여부")
    @Column(length = 1)
    @ColumnDefault("'Y'")
    @Convert(converter = BooleanConverter.class)
    private Boolean enabled;

}
