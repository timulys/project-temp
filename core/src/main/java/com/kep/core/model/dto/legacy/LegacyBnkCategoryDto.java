package com.kep.core.model.dto.legacy;

import java.util.List;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * BNK 기간계 시스템 카테고리 정보를 나타냅니다.
 * gubun 값 (L, M, S)에 기반하여 상호작용
 * 
 * - L: 협업선택.
 * - M: 현업이관업무 (L)에서 파생된 하위 카테고리.
 * - S: 현업이관부서 (L) 및 중간 (M) 모두에서 파생된 하위 카테고리.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LegacyBnkCategoryDto {

    @JsonProperty("gubun")
    private String gubun;

    @JsonProperty("fld_cd")
    private String fldCd;

    @JsonProperty("wrk_seq")
    private String wrkSeq;

    //fld_dept_cd =>bnk 소분류 값
    @JsonProperty("fld_dept_cd")
    private String fldDeptCd;

    //bnk 상담요약 라디오 박스 데이터
    @JsonProperty("deal_gubun")
    private DealGubun dealGubun;

    private List<GubunData> dataList;

    // DealGubun class definition
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class DealGubun {
        @JsonProperty("name")
        private String name;
        
        @JsonProperty("value")
        private String value;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class GubunData {
        @JsonProperty("name")
        private String name;

        @JsonProperty("value")
        private String value;
    }

    @Data
    @EqualsAndHashCode(callSuper = true)
    @AllArgsConstructor
    @Builder
    public static class GubunLData extends GubunData {}

    @Data
    @EqualsAndHashCode(callSuper = true)
    @AllArgsConstructor
    @Builder
    public static class GubunMData extends GubunData {}

    @Data
    @EqualsAndHashCode(callSuper = true)
    @AllArgsConstructor
    @Builder
    public static class GubunSData extends GubunData {}
}
