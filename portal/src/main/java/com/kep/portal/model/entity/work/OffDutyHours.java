package com.kep.portal.model.entity.work;

import com.kep.core.model.dto.work.WorkType;
import com.kep.portal.model.converter.BooleanConverter;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.ColumnDefault;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import java.time.ZonedDateTime;

/**
 * 근무 예외 시간
 */
@MappedSuperclass
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class OffDutyHours {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Positive
    private Long id;

    /**
     * 생성 회원
     */
    @NotNull
    @Positive
    private Long creator;
    @NotNull
    private ZonedDateTime created;

    /**
     * 수정 회원
     */
    @NotNull
    @Positive
    private Long modifier;
    @NotNull
    private ZonedDateTime modified;

    /**
     * 활성
     */
    @Column(length = 1)
    @Convert(converter = BooleanConverter.class)
    @NotNull
    @ColumnDefault("'N'")
    private Boolean enabled;

    /**
     * 근무예외 시작
     */
    @NotNull
    @Column(name = "start_created")
    private ZonedDateTime startCreated;

    /**
     * 근무예외 종료
     */
    @NotNull
    @Column(name = "end_created")
    private ZonedDateTime endCreated;

    /**
     * 내용
     */
    @NotNull
    private String contents;

    @Transient
    private WorkType.Cases cases;

    @Transient
    private Long casesId;
}
