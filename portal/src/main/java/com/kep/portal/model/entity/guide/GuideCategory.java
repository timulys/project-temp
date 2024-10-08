package com.kep.portal.model.entity.guide;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.kep.portal.model.converter.BooleanConverter;
import com.kep.portal.model.entity.branch.Branch;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;
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
public class GuideCategory {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Positive
    @Comment("PK")
    private Long id;

    @NotEmpty
    @Comment("이름")
    private String name;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_id", foreignKey = @ForeignKey(name="FK_GUIDE_CATEGORY__PARENT"))
    @JsonBackReference
    @Comment("부모 카테고리 PK")
    private GuideCategory parent;

    @OneToMany(mappedBy = "parent", fetch = FetchType.LAZY, orphanRemoval = true, cascade = CascadeType.ALL)
    @JsonManagedReference
    private List<GuideCategory> children;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "branch_id", foreignKey = @ForeignKey(name = "FK_GUIDE_CATEGORY__BRANCH"))
    private Branch branch;

    @Column(length = 1)
    @Convert(converter = BooleanConverter.class)
    @Builder.Default
    @Comment("브랜치 전체 오픈")
    private Boolean isOpen = false;

    @NotNull
    @Positive
    @Comment("카테고리 depth")
    private Integer depth;

    @NotNull
    @Column(length = 1)
    @Convert(converter = BooleanConverter.class)
    @ColumnDefault("'Y'")
    @Comment("사용여부")
    private Boolean enabled;

    @NotNull
    @Positive
    @Comment("생성자 PK")
    private Long creator;
    @NotNull
    @Comment("생성일")
    private ZonedDateTime created;

    @Positive
    @NotNull
    @Comment("수정자 PK")
    private Long modifier;

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
