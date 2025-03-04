package com.kep.portal.model.entity.customer;

import com.kep.portal.model.dto.customerGroup.request.PostCustomerGroupRequestDto;
import com.kep.portal.model.entity.member.Member;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Comment;

import javax.persistence.*;

/**
 * 고객 그룹
 */
@Entity
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class CustomerGroup {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Comment("PK")
    private Long id;

    @Comment("그룹 이름")
    private String groupName;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", foreignKey = @ForeignKey(name = "FK_CUSTOMER_GROUP__MEMBER"))
    @Comment("관리 상담사")
    private Member member;

    public CustomerGroup(PostCustomerGroupRequestDto dto, Member member) {
        this.groupName = dto.getGroupName();
        this.member = member;
    }

    public void updateGroupName(String groupName) {
        this.groupName = groupName;
    }
}
