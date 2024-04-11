/**
 * 자동메시지 템플릿 DTO
 *
 *  @생성일자     / 만든사람			/ 수정내용
 *  2023.05.25 / asher.shin	/ 신규
 */
package com.kep.portal.model.dto.member;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class MemberAutoMessageTemplateDto {


    private Long id;

    //템플릿 구분값 1: 즉시발송 2:당일 10분전 발송
    private int category;

    //템플릿명

    private String title;

    //템플릿 내용
    private String payload;
}
