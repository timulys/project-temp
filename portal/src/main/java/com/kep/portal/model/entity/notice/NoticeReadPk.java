/**
 * 직원별 공지사항 읽기 여부 체크 테이블
 * @수정일자	  / 수정자			 / 수정내용 
 * 2023.03.28 / philip.lee7	 / 공지사항 읽기 여부 PK추가 신규
 */
package com.kep.portal.model.entity.notice;

import java.io.Serializable;

import javax.persistence.Embeddable;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Embeddable
public class NoticeReadPk implements Serializable{

	private Long member_id;
	
	private Long notice_id;
	
}
