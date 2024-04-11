package com.kep.portal.model.entity.work;

import lombok.*;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.Comment;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;


/**
 *  @수정일자	  / 수정자		 	/ 수정내용
 *  2023.05.31 / asher.shin / 근무외상담 컬럼 추가
 */
@Entity
@DynamicInsert
@DynamicUpdate
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class MemberOfficeHours extends OfficeHours {

    @Positive
    @NotNull
    @Column(unique = true)
    private Long memberId;


    @Comment("근무 시간 외 상담 여부")
    private Boolean offDutyCounselYn;


}
