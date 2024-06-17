/**
 * 다중 파일 업로드를 위한 테이블(공지사항) 
 * @수정일자	  / 수정자			 / 수정내용 
 * 2023.03.28 / philip.lee7	 / 다중 파일 업로드를 위한 테이블(공지사항) Entity 신규
 */
package com.kep.portal.model.entity.notice;

import com.kep.portal.model.entity.upload.Upload;
import lombok.*;
import org.hibernate.annotations.Comment;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;
import javax.validation.constraints.Positive;

@Entity
@DynamicInsert
@DynamicUpdate
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class NoticeUpload {


	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Comment("PK")
	@Positive
	private Long id;
	
	@ManyToOne
	@JoinColumn(name = "notice_id")
	@Comment("공지 PK")
	private Notice notice;
	
	@OneToOne
	@JoinColumn(name="upload_id", nullable = true)
	private Upload upload;
}
