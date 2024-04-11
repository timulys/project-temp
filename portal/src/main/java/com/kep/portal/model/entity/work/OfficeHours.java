package com.kep.portal.model.entity.work;

import com.kep.core.model.dto.work.WorkType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.ColumnDefault;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.time.ZonedDateTime;

/**
 * @수정일자	  / 수정자		 	/ 수정내용
 * 2023.05.31 / asher.shin / 근무 외 상담 여부 추가
 */
@MappedSuperclass
@Data
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
public class OfficeHours {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Positive
    private Long id;

    @NotEmpty
    public String startCounselTime;

    @NotEmpty
    public String endCounselTime;
    
    public String dayOfWeek;

    @Positive
    public Long creator;

    public ZonedDateTime created;

    @Positive
    public Long modifier;

    public ZonedDateTime modified;

    @Transient
    private WorkType.Cases cases;

    @Transient
    private Long casesId;

    @Transient
    private Boolean  offDutyCounselYn;

}
