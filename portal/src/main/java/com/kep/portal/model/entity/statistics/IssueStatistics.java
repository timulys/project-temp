package com.kep.portal.model.entity.statistics;

import com.kep.portal.model.dto.statistics.IssueStatisticsStatus;
import lombok.*;
import org.hibernate.annotations.Comment;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;
import javax.validation.constraints.Positive;
import java.time.LocalDate;

@Entity
@DynamicInsert
@DynamicUpdate
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Table(
        indexes = {
                @Index(name = "IDX_ISSUE_STATISTICS_BRANCH_TEAM_DATE", columnList = "branchId,teamId,memberId"),
                @Index(name = "IDX_ISSUE_STATISTICS_CREATED", columnList = "created")
        },
        uniqueConstraints = {
                @UniqueConstraint(name = "UK_ISSUE_STATISTICS_STATUS_CREATED", columnNames = { "issueId", "status","created" })
        }
)
public class IssueStatistics {

    @Id
    @Positive
    @Comment("PK")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Positive
    @Column(nullable = false,updatable = false)
    @Comment("issue PK")
    private Long issueId;

    @Positive
    @Comment("branch PK")
    private Long branchId;

    @Positive
    @Comment("team PK")
    private Long teamId;

    @Positive
    @Comment("member PK")
    private Long memberId;

    @Comment("open : 생성 , ing : 진행중 , close : 종료")
    @Column(nullable = false,updatable = false)
    @Enumerated(EnumType.STRING)
    private IssueStatisticsStatus status;

    @Comment("생성일")
    private LocalDate created;

}
