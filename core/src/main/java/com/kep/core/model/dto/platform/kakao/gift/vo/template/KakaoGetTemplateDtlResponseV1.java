package com.kep.core.model.dto.platform.kakao.gift.vo.template;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.kep.core.model.dto.platform.kakao.gift.GiftExternalResponse;
import com.kep.core.model.dto.platform.kakao.gift.constant.KakaoBudgetType;
import com.kep.core.model.dto.platform.kakao.gift.constant.KakaoItemType;
import com.kep.core.model.dto.platform.kakao.gift.constant.KakaoTemplateStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

/**
 * 요청 없음
 *
 * TODO :: volka
 * TODO :: 모든 요청과 응답은 불변으로 사용.
 * TODO :: -> 현재 올웨이즈의 Jackson의 JSON 직렬화/역직렬화는 생성자를 이용한 직렬화/역직렬화를 사용하지 않으므로 @Data로 임시 사용 (중계 모듈은 record 사용중)
 * TODO :: -> 추후 선물하기 비즈니스가 정해질 때 올웨이즈에 맞게 리팩토링 하여 사용
 *
 */
@Schema(description = "템플릿 상세 조회 V1 응답 VO")
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class KakaoGetTemplateDtlResponseV1 implements GiftExternalResponse {
    @Schema(description = "템플릿명")
    private @NotNull String templateName;
    @Schema(description = "템플릿 ID")
    private @NotNull Long templateTraceId;
    @Schema(description = "발송 가능 시작 일시")
    private LocalDateTime startAt;
    @Schema(description = "발송 가능 종료 일시")
    private LocalDateTime endAt;
    @Schema(description = "템플릿 상태")
    private @NotNull KakaoTemplateStatus orderTemplateStatus;
    @Schema(description = "한도 타입")
    private @NotNull KakaoBudgetType budgetType;
    @Schema(description = "발송한도수")
    private Long giftBudgetCount;
    @Schema(description = "기발송 수")
    private @NotNull Long giftSentCount;
    @Schema(description = "발송 가능 수")
    private @NotNull Long giftStockCount;
    @Schema(description = "발신자명")
    private String bmSenderName;
    @Schema(description = "메세지카드 이미지 URL")
    private String mcImageUrl;
    @Schema(description = "메세지카드 입력값")
    private String mcText;
    @Schema(description = "템플릿 상품 정보")
    private @NotNull Product product;

    @Schema(name = "GetTemplateDtlResponseV1.Product", description = "상품 정보 (템플릿 상세 조회 v1)")
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @EqualsAndHashCode(callSuper = false)
        @JsonNaming(value = PropertyNamingStrategies.SnakeCaseStrategy.class)
        public static class Product {
        @Schema(description = "상품명")
        private @NotBlank String productName;
        @Schema(description = "아이템타입(VOUCHER=교환권,SHIPPING=배송상품)")
        private @NotNull KakaoItemType itemType;
        @Schema(description = "브랜드명")
        private @NotNull String brandName;
        @Schema(description = "브랜드이미지url")
        private @NotNull String brandImageUrl;
        @Schema(description = "상품이미지url")
        private @NotNull String productImageUrl;
        @Schema(description = "상품썸네일url")
        private @NotNull String productThumbImageUrl;
        @Schema(description = "상품가격")
        private @NotNull Long productPrice;

    }
}
