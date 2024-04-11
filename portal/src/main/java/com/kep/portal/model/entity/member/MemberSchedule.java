/**
 *  멤버 일정관리 Entity
 * @수정일자	  / 수정자			 / 수정내용
 * 2023.05.17 / asher.shin	 / 신규
 */
package com.kep.portal.model.entity.member;

import com.kep.portal.model.entity.customer.Customer;
import lombok.*;
import org.hibernate.annotations.Comment;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.springframework.format.annotation.DateTimeFormat;

import javax.annotation.Nullable;
import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZonedDateTime;

@Entity
@Table(indexes = {
        @Index(name = "IDX_MEMBER_SCHEDULE__DATE", columnList = "startDateTime , endDateTime")
        , @Index(name = "IDX_MEMBER_SCHEDULE__TYPE", columnList = "scheduleType")
        , @Index(name = "IDX_MEMBER_SCHEDULE__COMPLETED", columnList = "completed")
})
@DynamicInsert
@DynamicUpdate
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class MemberSchedule {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Comment("PK")
    @Positive
    private Long id;

    @Comment("상담원 PK")
    @Positive
    @NotNull
    private Long memberId;

    @ManyToOne
    @Comment("CUSTOMER PK")
    @JoinColumn(name="customerId" , foreignKey = @ForeignKey(name = "FK_MEMBER_SCHEDULE__CUSTOMER"), updatable = false)
    private Customer customer;

    @Comment("일정제목")
    @NotNull
    private String title;

    @Comment("일정내용")
    private String content;

    @Comment("약속장소")
    private String address;

    @Comment("약속장소 상세")
    private String addressDetail;


    @Comment("일정타입")
    @Enumerated(EnumType.STRING)
    @NotNull
    private ScheduleType scheduleType;

    @Comment("일정 시작날짜")
    @NotNull
    private ZonedDateTime startDateTime;

    @Comment("일정 마지막날짜")
    @NotNull
    private ZonedDateTime endDateTime;

    @Comment("일정관리 등록 알림톡 전송 여부")
    private Boolean alarmMessageYn;

    @Comment("일정관리 등록 알림톡 전송 템플릿 PK")
    private Long alarmTemplateId;

    @Comment("일정관리 10분전 전송 여부")
    private Boolean beforeTenAlarmMessageYn;

    @Comment("일정관리 10분점 전송 템플릿 PK")
    private Long beforeTenAlarmTemplateId;

    @Comment("등록 시간")
    private ZonedDateTime created;

    @Comment("수정 시간")
    private ZonedDateTime modified;

    @Comment("일정 완료 여부")
    private boolean completed;

}
