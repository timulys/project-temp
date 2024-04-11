package com.kep.portal.model.dto.customer;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIncludeProperties;
import com.kep.portal.model.entity.customer.Customer;
import com.kep.portal.model.entity.member.Member;
import com.kep.portal.model.entity.product.Product;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CustomerInterestedDto {

    private Long id;

    @JsonIncludeProperties({"id","name"})
    private Customer customer;

    @JsonIncludeProperties({"id","nickname"})
    private Member member;

    @JsonIncludeProperties({"id","name"})
    private Product product;

    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "Asia/Seoul")
    private LocalDate created;
}
