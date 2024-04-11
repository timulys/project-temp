package com.kep.portal.model.entity.upload;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.kep.portal.model.entity.customer.Guest;
import com.kep.portal.model.entity.member.Member;
import com.kep.portal.model.entity.subject.IssueCategory;
import lombok.*;
import org.hibernate.annotations.Comment;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import java.time.ZonedDateTime;

@Entity
@DynamicInsert
@DynamicUpdate
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class UploadHistory {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Comment("PK")
    @Positive
    private Long id;

    @Comment("이슈 PK")
    @Positive
    private Long issueId;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "issueCategoryId", foreignKey = @ForeignKey(name="FK_UPLOAD_HISTORY__ISSUE_CATEGORY"))
    @Comment("분류 PK")
    @JsonIgnoreProperties({"parent", "path"})
    private IssueCategory issueCategory;


    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "guestId", foreignKey = @ForeignKey(name="FK_UPLOAD_HISTORY__GUEST"))
    @Comment("고객 정보")
    @NotNull
    private Guest guest;

    @Comment("파일 이름")
    @NotNull
    private String fileName;

    @Comment("확장자")
    @NotNull
    private String mimeType;

    @Comment("용량")
    @Column(name = "file_size")
    @NotNull
    private Long size;

    @Comment("파일 url")
    @NotNull
    private String url;

    @Comment("팀 PK")
    @NotNull
    private Long teamId;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "creator", foreignKey = @ForeignKey(name="FK_UPLOAD_HISTORY__CREATOR"))
    @Comment("생성자 PK")
    private Member creator;

    @Comment("생성일")
    private ZonedDateTime created;

    @PrePersist
    public void prePersist() {
        if (created == null) {
            created = ZonedDateTime.now();
        }
    }



}
