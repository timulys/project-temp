/**
 * 자동메시지 history entity
 *
 *  @생성일자     / 만든사람			/ 수정내용
 *  2023.05.25 / asher.shin	/ 신규
 */
package com.kep.portal.model.entity.member;

import lombok.*;
import org.hibernate.annotations.Comment;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.Positive;
import java.time.LocalDateTime;

@Entity
@DynamicInsert
@DynamicUpdate
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class MemberAutoMessage {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Comment("PK")
    @Positive
    private Long id;

    @Comment("직원 일정 PK")
    private Long scheduleId;

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Comment("일정 날짜")
    private LocalDateTime sendDate;

    @Comment("자동메시지 전송 직원번호")
    private Long memberId;

    @Comment("스케줄 타입번호")
    private Long sendType;

    @Comment("성공실패여부")
    private boolean result;

}
