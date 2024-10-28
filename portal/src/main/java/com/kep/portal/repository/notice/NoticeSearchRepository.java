/**
 * notice_read 테이블 Repository
 * @수정일자	  / 수정자			 / 수정내용 
 * 2023.03.28 / philip.lee7	 / searchList 함수 파라메타 추가
 */

package com.kep.portal.repository.notice;

import javax.validation.constraints.NotNull;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.kep.portal.model.entity.notice.Notice;

import java.util.List;

public interface NoticeSearchRepository {
	
	/**
	 * 상담관리 > 공지사항 목록 조회
	 *
	 *  @생성일자      / 만든사람		 	/ 수정내용
	 * 	2023.04.04 / philip.lee7   / memberId 파라미터 추가
	 */
    Page<Notice> searchMangerList(String keyword, String type,@NotNull Long branchId ,@NotNull Long memberId, @NotNull Pageable pageable);

    Page<Notice> searchList(String keyword, String type, @NotNull Long branchId, @NotNull Long memberId, @NotNull Pageable pageable, Boolean fixation);

	Long unreadNotice(@NotNull Long branchId, @NotNull Long memberId , @NotNull Long teamId);
}
