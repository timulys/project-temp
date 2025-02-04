//package com.kep.portal.model.entity.customer;
//
//import com.kep.portal.model.entity.member.Member;
//import lombok.AccessLevel;
//import lombok.Builder;
//import lombok.Getter;
//import org.hibernate.annotations.Comment;
//import org.hibernate.annotations.DynamicInsert;
//import org.hibernate.annotations.DynamicUpdate;
//
//import javax.persistence.*;
//
///**
// * 고객 배정 담당
// * TODO :: 지정 담당자 / 서브 담당자 요건 적용 시 주석 제거 20250204 volka
// */
//@DynamicInsert
//@DynamicUpdate
//@Getter
//@Entity
//public class CustomerAssignMember {
//
//    @Id @GeneratedValue(strategy = GenerationType.AUTO)
//    private Long id;
//
//    @OneToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "customerId", unique = true, foreignKey = @ForeignKey(name = "FK_CUSTOMER_ASSIGN_MEMBER__CUSTOMER"), nullable = false)
//    @Comment("고객ID")
//    private Customer customer;
//
//    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "mainMemberId", foreignKey = @ForeignKey(name = "FK_CUSTOMER_ASSIGN_MEMBER__MEMBER"), nullable = false)
//    @Comment("메인 담당자")
//    private Member mainMember;
//
//    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "subMemberId", foreignKey = @ForeignKey(name = "FK_CUSTOMER_ASSIGN_MEMBER__MEMBER_SUB"), nullable = true)
//    @Comment("서브 담당자")
//    private Member subMember;
//
//
//    protected CustomerAssignMember() {}
//
//    @Builder(access = AccessLevel.PRIVATE)
//    private CustomerAssignMember(Long id, Customer customer, Member mainMember, Member subMember) {
//        this.id = id;
//        this.customer = customer;
//        this.mainMember = mainMember;
//        this.subMember = subMember;
//    }
//
//
//    public static CustomerAssignMember create(Long id, Customer customer, Member mainMember, Member subMember) {
//        return CustomerAssignMember.builder()
//                .id(id)
//                .customer(customer)
//                .mainMember(mainMember)
//                .subMember(subMember)
//                .build();
//    }
//
//    public void modifyMainMember(Member mainMember) {
//        this.mainMember = mainMember;
//    }
//}
