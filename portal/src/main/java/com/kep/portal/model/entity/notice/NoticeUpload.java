/**
 * 다중 파일 업로드를 위한 테이블(공지사항) 
 * @수정일자	  / 수정자			 / 수정내용 
 * 2023.03.28 / philip.lee7	 / 다중 파일 업로드를 위한 테이블(공지사항) Entity 신규
 */
package com.kep.portal.model.entity.notice;

import javax.persistence.ConstraintMode;
import javax.persistence.Entity;
import javax.persistence.ForeignKey;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.validation.constraints.Positive;

import org.hibernate.annotations.Comment;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import com.kep.portal.model.entity.upload.Upload;

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
