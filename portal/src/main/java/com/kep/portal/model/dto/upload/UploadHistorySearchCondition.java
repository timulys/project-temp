package com.kep.portal.model.dto.upload;


import com.kep.portal.model.entity.customer.Customer;
import com.kep.portal.model.entity.customer.Guest;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDate;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UploadHistorySearchCondition {

    private LocalDate startDate;

    private LocalDate endDate;

    private Long teamId;

    private Long memberId;

    private Long issueCategoryId;

    private String customerSubject;

    private String customerQuery;


    /**
     * 고객검색
     */
    private List<Guest> guests;
}
