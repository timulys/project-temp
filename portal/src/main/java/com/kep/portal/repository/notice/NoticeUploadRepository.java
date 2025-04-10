/**
 * notice_upload 테이블 Repository
 * @수정일자	  / 수정자			 / 수정내용 
 * 2023.03.28 / philip.lee7	 / 공지사항 업로드 테이블 Repository 신규
 */

package com.kep.portal.repository.notice;

import java.util.List;
import java.util.Optional;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

import com.kep.portal.model.entity.notice.Notice;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import com.kep.portal.model.entity.notice.NoticeUpload;

public interface NoticeUploadRepository extends JpaRepository<NoticeUpload, Long>{
	@Override
	@EntityGraph(attributePaths = {"upload"})
	Optional<NoticeUpload> findById(@NotNull @Positive Long id);

	List<NoticeUpload> findAllByNoticeId(Long noticeId);
	void deleteAllByNoticeId(Long noticeId);

	@NotNull
	@Positive Long notice(Notice notice);
}
