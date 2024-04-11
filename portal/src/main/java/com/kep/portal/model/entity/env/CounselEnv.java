package com.kep.portal.model.entity.env;


import java.time.ZonedDateTime;

import javax.persistence.AttributeOverride;
import javax.persistence.AttributeOverrides;
import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.Comment;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import com.kep.portal.event.CounselEnvEventListener;
import com.kep.portal.model.converter.BooleanConverter;
import com.kep.portal.model.entity.system.SystemEnv;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * 상담 환경 설정
 */

@Entity
@DynamicInsert
@DynamicUpdate
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@EntityListeners(CounselEnvEventListener.class)
public class CounselEnv {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Positive
    private Long id;

    @Positive
    @NotNull
    @Column(unique = true , updatable = false)
    private Long branchId;

    @Positive
    @NotNull
    private Long modifier;

    @NotNull
    private ZonedDateTime modified;

    /**
     *
     */
    @Column(length = 1)
    @Convert(converter = BooleanConverter.class)
    @ColumnDefault("'N'")
    @Comment("상담 인입제한")
    private Boolean requestBlockEnabled;

    /**
     * 상담직원 전환 자동 승인
     */
    @Column(length = 1)
    @Convert(converter = BooleanConverter.class)
    @ColumnDefault("'Y'")
    @Comment("상담직원 전환 자동 승인")
    private Boolean memberAutoTransformEnabled;


    /**
     * 근무 시간 종료후 진행중인방 종료
     */
    @Column(length = 1)
    @Convert(converter = BooleanConverter.class)
    @ColumnDefault("'Y'")
    @Comment("근무 시간 종료후 진행중인방 종료")
    private Boolean issueAutoCloseEnabled;

    /**
     * 상담 지연 상태 사용
     */
    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "enabled", column = @Column(name = "issueDelayEnabled" , length = 1)),
            @AttributeOverride(name = "minute", column = @Column(name = "issueDelayMinute"))
    })
    private SystemEnv.EnabledMinute issueDelay;

    /**
     * 채팅방 파일전송
     */
    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "enabled", column = @Column(name = "issueFileEnabled" , length = 1)),
            @AttributeOverride(name = "fileMimeType", column = @Column(name = "issueFileMimeType"))
    })
    private SystemEnv.EnableFileMimeType issueFileMimeType;

    /**
     * 알림톡 발송 자동승인
     */
    @Column(length = 1)
    @Convert(converter = BooleanConverter.class)
    @ColumnDefault("'Y'")
    @Comment("알림톡 발송 자동 승인")
    private Boolean alertTalkAutoSendEnable;

    /**
     * 친구톡 발송 자동승인
     */
    @Column(length = 1)
    @Convert(converter = BooleanConverter.class)
    @ColumnDefault("'Y'")
    @Comment("친구톡 발송 자동승인")
    private Boolean friendTalkAutoSendEnable;
}
