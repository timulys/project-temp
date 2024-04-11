package com.kep.portal.model.entity.guide;


import lombok.*;
import org.hibernate.annotations.ColumnDefault;
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
@Table(indexes = {
        @Index(name = "IDX_GUIDE_LOG__GUIDE_ISSUE_BLOCK", columnList = "issueId,guideId,blockId")
})
public class GuideLog {

    @Id
    @Positive
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Comment("PK")
    private Long id;

    @NotNull
    @Comment("가이드 PK")
    private Long guideId;

    @NotNull
    @Comment("Issue PK")
    private Long issueId;

    @NotNull
    @Comment("가이드 블록 PK")
    private Long blockId;

    @NotNull
    @Comment("가이드 콘텐츠 Index")
    @ColumnDefault("0")
    private Long contentId;

    @NotNull
    @Comment("생성자 PK")
    private Long creator;
    @NotNull
    @Comment("생성일")
    private ZonedDateTime created;

    @PrePersist
    public void prePersist() {
        if (created == null) {
            created = ZonedDateTime.now();
        }
    }
}
