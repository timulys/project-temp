/**
 * notice_read 테이블 Repository
 * @수정일자	  / 수정자			 / 수정내용 
 * 2023.03.28 / philip.lee7	 / 직원별 공지사항 읽기 여부 Repository 신규
 */

package com.kep.portal.repository.notice;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.kep.portal.model.entity.notice.NoticeRead;
import com.kep.portal.model.entity.notice.NoticeReadPk;


@Repository
public interface NoticeReadRepository extends JpaRepository<NoticeRead, NoticeReadPk>{
	
	int countByNoticeReadPk(NoticeReadPk noticeReadPk);
	
}
