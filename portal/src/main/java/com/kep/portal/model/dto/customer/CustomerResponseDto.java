
/**
 * 고객 response Dto
 *  @생성일자      / 만든사람		 	/ 수정내용
 * 	 2023.04.12 / asher.shin   / 신규
 */
package com.kep.portal.model.dto.customer;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.kep.core.model.dto.customer.CustomerDto;
import com.kep.portal.model.entity.customer.CustomerAnniversary;
import com.kep.portal.model.entity.customer.CustomerContact;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Comment;
import org.hibernate.validator.constraints.URL;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CustomerResponseDto {


    //PK
    private Long id;

    //연락처
    private List<CustomerContact> contacts;

    //이름
    private String name;

    //생일
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "Asia/Seoul")
    private Date birthday;

    //핸드폰 번호
    private String phoneNumber;

    //주소
    private String address;

    //프로필 URL
    private String profile;

    //즐겨찾기 여부
    private boolean favorites;
    
    // 카카오싱크에서 받는 suid
    private String platformUserId;
    
    // 고객번호
    private String custNo;

    // 실행번호
    private String cntrtNum;
    
    // 카카오싱크 고유Id
    private String custCi;
    
}
