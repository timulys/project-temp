package com.kep.portal.model.entity.issue;


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
public class IssueHighlight {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Comment("PK")
    @Positive
    private Long id;

    @NotNull
    @Column(unique = true)
    private String keyword;

    @Comment("생성자 member pk")
    @Positive
    @NotNull
    private Long creator;

    @Comment("생성 일시")
    @NotNull
    private ZonedDateTime created;

    @Comment("수정자 member pk")
    @Positive
    private Long modifier;

    @Comment("수정 일시")
    @NotNull
    private ZonedDateTime modified;

    @PrePersist
    @PreUpdate
    public void prePersist() {
        if (created == null) {
            created = ZonedDateTime.now();
        }
        if (modified == null) {
            modified = ZonedDateTime.now();
        }
    }
}
