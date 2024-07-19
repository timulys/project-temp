package com.kep.core.model.dto.legacy;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * FIXME :: BNK 연동 정보, 수정 필요 20240715 volka
 *
 * BNK 기간계, 고객 정보
 * 응답 클래스 
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonIgnoreProperties(ignoreUnknown = true)
public class LegacyCustomerDto {
	
	//카카오싱크 후 내려받는 데이터는 제외
	private Long id;
	private String identifier;
	private String name;
	private String phone;
	private String email;
	private String birthday;
	private String age;
	
	@JsonProperty("cust_nm")
    private String custNm;
	
	@JsonProperty("cust_no")
	private String custNo;

    @JsonProperty("clerk_jno")
    private String clerkJno;

    @JsonProperty("cust_tp")
    private String custTp;

    @JsonProperty("key_tel_no")
    private String keyTelNo;
    
    @JsonProperty("key_juso")
    private String keyJuso;
    
    @JsonProperty("cntrt_nums")
    private List<String> cntrtNums;

    @JsonProperty("cres")
    private CresData cres;
    
//    private IssueDto issueDto;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class CresData {
        @JsonProperty("cntrt_num")
        private String cntrtNum;

        @JsonProperty("goods_nm")
        private String goodsNm;

        @JsonProperty("exe_ymd")
        private String exeYmd;

        @JsonProperty("expr_ymd")
        private String exprYmd;

        @JsonProperty("status")
        private String status;

        @JsonProperty("lt_term")
        private String ltTerm;

        @JsonProperty("obtn_cst")
        private String obtnCst;

        @JsonProperty("loan_bal_amt")
        private String loanBalAmt;

        @JsonProperty("mgt_dept")
        private String mgtDept;

        @JsonProperty("real_rat")
        private String realRat;

        @JsonProperty("repay_mth")
        private String repayMth;

        @JsonProperty("nxt_pay_ymd")
        private String nxtPayYmd;

        @JsonProperty("pln_pay_amt")
        private String plnPayAmt;

        @JsonProperty("at_car_no")
        private String atCarNo;

        @JsonProperty("at_car_nm")
        private String atCarNm;

        @JsonProperty("sj_yn")
        private String sjYn;

        @JsonProperty("sj_fee")
        private String sjFee;

        @JsonProperty("lt_term_1")
        private String ltTerm1;

        @JsonProperty("prep_amt_1")
        private String prepAmt1;

        @JsonProperty("assrn_amt")
        private String assrnAmt;

        @JsonProperty("prep_amt_2")
        private String prepAmt2;

        @JsonProperty("lt_m_pay_amt")
        private String ltMPayAmt;

        @JsonProperty("payw_rat")
        private String paywRat;

        @JsonProperty("prep_amt_3")
        private String prepAmt3;

        @JsonProperty("pay_subj")
        private String paySubj;

        @JsonProperty("lt_exp")
        private String ltExp;

        @JsonProperty("prs_val")
        private String prsVal;

        @JsonProperty("insu_cd")
        private String insuCd;

        @JsonProperty("lt_km")
        private String ltKm;

        @JsonProperty("cust_add_fee")
        private String custAddFee;

        @JsonProperty("add_driver_nm")
        private String addDriverNm
;

        @JsonProperty("dpst_cnt")
        private String dpstCnt;

        @JsonProperty("rtl_calc")
        private String rtlCalc;
    }
}