package com.kep.portal.model.entity.guide;


import com.kep.core.model.dto.guide.GuideType;
import com.kep.portal.model.converter.BooleanConverter;
import com.kep.portal.model.converter.ListOfLongConverter;
import com.kep.portal.model.entity.branch.Branch;
import lombok.*;
import org.hibernate.annotations.Comment;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import java.time.ZonedDateTime;
import java.util.List;

@Entity
@DynamicInsert
@DynamicUpdate
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Table(indexes = {
        @Index(name = "IDX_GUIDE__BRANCH_CATEGORY", columnList = "branchId,guideCategoryId"),
        @Index(name = "IDX_GUIDE__BRANCH_NAME", columnList = "branchId,name")
})
public class Guide {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Positive
    @Comment("PK")
    private Long id;

    @NotEmpty
    @Comment("이름")
    private String name;


    @Comment("가이드를 사용할 팀 PK")
    private Long teamId;

    @ManyToOne(fetch = FetchType.LAZY)
    @Comment("가이드를 사용할 브랜치 PK")
    @JoinColumn(name = "branchId", foreignKey = @ForeignKey(name="FK_GUIDE__BRANCH"))
    private Branch branch;

    @NotNull
    @Column(length = 1)
    @Convert(converter = BooleanConverter.class)
    @Builder.Default
    @Comment("브랜치 전체 오픈")
    private Boolean isBranchOpen = false;

    @NotNull
    @Column(length = 1)
    @Convert(converter = BooleanConverter.class)
    @Builder.Default
    @Comment("팀 전체 오픈")
    private Boolean isTeamOpen = false;

    @NotNull
    @Builder.Default
    @Column(length = 1)
    @Convert(converter = BooleanConverter.class)
    @Comment("사용 상태")
    private Boolean enabled = true;

    //3depth
    @ManyToOne
    @JoinColumn(name = "guideCategoryId", foreignKey = @ForeignKey(name="FK_GUIDE__GUIDE_CATEGORY"))
    @Comment("카테고리 PK")
    @NotNull
    private GuideCategory guideCategory;

    @Enumerated(EnumType.STRING)
    @NotNull
    @Comment("타입")
    private GuideType type;

    @NotNull
    @Comment("생성자 PK")
    private Long creatorId;
    @NotNull
    @Comment("생성일")
    private ZonedDateTime created;


    @Comment("블록 리스트")
    @Convert(converter = ListOfLongConverter.class)
    private List<Long> blockIds;

    @NotNull
    @Comment("수정자 PK")
    private Long modifierId;

    @NotNull
    @Comment("수정일")
    private ZonedDateTime modified;

    @PrePersist
    public void prePersist() {
        if (created == null) {
            created = ZonedDateTime.now();
        }
        if (modified == null) {
            modified = ZonedDateTime.now();
        }
    }

    @PreUpdate
    public void preUpdate() {
        if (modified == null) {
            modified = ZonedDateTime.now();
        }
    }


}
