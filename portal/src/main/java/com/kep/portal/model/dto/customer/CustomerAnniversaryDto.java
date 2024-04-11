
/**
 *
 *  @생성일자      / 만든사람		 	/ 수정내용
 * 	 2023.04.12 / asher.shin   / 고객 기념일 DTO 추가 신규
 */
package com.kep.portal.model.dto.customer;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.kep.portal.model.entity.customer.Anniversary;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.ZonedDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CustomerAnniversaryDto {


    private Long id;

    private CustomerResponseDto customer;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate anniversaryDay;


    private Anniversary anniversary;

    private Long creator;

    private ZonedDateTime created;

    private Long modifier;

    private ZonedDateTime modified;

}
