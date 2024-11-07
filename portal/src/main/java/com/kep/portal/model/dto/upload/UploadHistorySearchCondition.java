package com.kep.portal.model.dto.upload;


import com.kep.portal.model.entity.customer.Customer;
import com.kep.portal.model.entity.customer.Guest;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDate;
import java.util.List;

@Schema(description = "파일 업로드 히스토리 검색 조건")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UploadHistorySearchCondition {

    @Schema(description = "조회일 시작")
    private LocalDate startDate;

    @Schema(description = "조회일 종료")
    private LocalDate endDate;

    @Schema(description = "팀 아이디")
    private Long teamId;

    @Schema(description = "사용자 아이디")
    private Long memberId;

    @Schema(description = "이슈 카테고리 아이디")
    private Long issueCategoryId;

    @Schema(description = "")
    private String customerSubject;

    @Schema(description = "")
    private String customerQuery;

    @Schema(description = "브랜치 아이디")
    private Long branchId;

    /**
     * 고객검색
     */
    @Schema(description = "")
    private List<Guest> guests;
}
