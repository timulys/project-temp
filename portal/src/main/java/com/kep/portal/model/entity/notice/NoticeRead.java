/**
 * 직원별 공지사항 읽기 여부 체크 테이블
 * @수정일자	  / 수정자			 / 수정내용 
 * 2023.03.28 / philip.lee7	 / 직원별 공지사항 읽기 여부 체크 테이블 Entity 신규
 */
package com.kep.portal.model.entity.notice;

import java.time.ZonedDateTime;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;

import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@DynamicInsert
@DynamicUpdate
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class NoticeRead  {
	
	@EmbeddedId
	private NoticeReadPk noticeReadPk;
	
	private ZonedDateTime readAt;

}
