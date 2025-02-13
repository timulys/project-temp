package com.kep.portal.model.entity.issue;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.Comment;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import java.time.ZonedDateTime;

/**
 * 상담 요약(후처리) 카테고리
 *
 * @author volka
 */
@DynamicInsert
@DynamicUpdate
@Getter
@Entity
public class IssueSummaryCategory {

    @Id @GeneratedValue(strategy = GenerationType.AUTO)
    @Comment("PK")
    @Positive
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent")
    @Comment("부모 카테고리")
    private IssueSummaryCategory parent;

    @Column(nullable = false, length = 100, unique = true)
    @NotBlank
    private String name;

    @Column(nullable = false)
    @Positive
    private Integer sort;

    @Column(nullable = false)
    @Positive
    private Integer depth;

    @ColumnDefault("true")
    @Column(nullable = false, length = 1)
    private Boolean enabled;


    @Column(nullable = false, updatable = false)
    private Long creator;
    @Column(nullable = false, updatable = false)
    private ZonedDateTime createdAt;

    @Column(nullable = false)
    private Long modifier;
    @Column(nullable = false)
    private ZonedDateTime modifiedAt;

    protected IssueSummaryCategory() {}

    @PrePersist
    private void prePersist() {
        ZonedDateTime now = ZonedDateTime.now();
        this.createdAt = now;
        this.modifiedAt = now;
    }

    @PreUpdate
    private void preUpdate() {
        this.modifiedAt = ZonedDateTime.now();
    }

    @Builder(access = AccessLevel.PRIVATE)
    private IssueSummaryCategory(Long id, IssueSummaryCategory parent, String name, Integer sort, Integer depth, Boolean enabled, Long creator, Long modifier) {
        this.id = id;
        this.parent = parent;
        this.name = name;
        this.sort = sort;
        this.depth = depth;
        this.enabled = enabled;
        this.creator = creator;
        this.modifier = modifier;
    }

    public static IssueSummaryCategory create(Long id, IssueSummaryCategory parent, @NotBlank String name, @NotNull Integer sort, @NotNull Integer depth, @NotNull Boolean enabled, @NotNull Long memberId) {
        return IssueSummaryCategory.builder()
                .id(id)
                .parent(parent)
                .name(name)
                .sort(sort)
                .depth(depth)
                .enabled(enabled)
                .creator(id == null ? memberId : null)
                .modifier(memberId)
                .build();
    }


    public void modify(@NotBlank String name, @NotNull Integer sort, @NotNull Boolean enabled, @NotNull Long memberId) {
        this.name = name;
        this.sort = sort;
        this.enabled = enabled;
        this.modifier = memberId;
    }

}
