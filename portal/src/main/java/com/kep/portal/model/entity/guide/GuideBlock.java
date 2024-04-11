package com.kep.portal.model.entity.guide;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.kep.core.model.dto.issue.payload.IssuePayload;
import lombok.*;
import org.hibernate.annotations.*;

import javax.persistence.*;
import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.Table;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

@Entity
@Table(indexes = {
//        @Index(name = "IDX_GUIDE_BLOCK__MESSAGE", columnList = "guideId,messageCondition"),
        @Index(name = "IDX_GUIDE_BLOCK__FILE", columnList = "guideId,fileCondition")
})
@DynamicInsert
@DynamicUpdate
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class GuideBlock {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Positive
    @Comment("PK")
    private Long id;

    private Long guideId;

    @Positive
    @Comment("필수 블록 PK")
    private Long requireId;

    @Comment("프로세스 타입에서 사용될 이름")
    private String blockName;

    @NotEmpty
    @Lob
    @Basic
    @Nationalized
    @Comment("GuidePayload toString")
    private String payload;

    @Comment("블록에서 사용하는 콘텐츠 개수")
    @ColumnDefault("0")
    @NotNull
    private Integer contentCount;

    @Comment("GuidePayload condition message")
    private String messageCondition;

    @Comment("GuidePayload condition filename")
    private String fileCondition;
}