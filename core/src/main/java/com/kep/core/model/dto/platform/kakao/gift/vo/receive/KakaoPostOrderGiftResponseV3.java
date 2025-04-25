package com.kep.core.model.dto.platform.kakao.gift.vo.receive;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.kep.core.model.dto.platform.kakao.gift.GiftExternalResponse;
import com.kep.core.model.dto.platform.kakao.gift.constant.KakaoGiftStatus;
import com.kep.core.model.dto.platform.kakao.gift.constant.KakaoItemType;
import com.kep.core.model.dto.platform.kakao.gift.constant.KakaoReceiverType;
import com.kep.core.model.dto.platform.kakao.gift.constant.KakaoVoucherStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.URL;

import javax.validation.constraints.NotNull;

import static io.swagger.v3.oas.annotations.media.Schema.RequiredMode.REQUIRED;

/**
 * 선물 상세 조회 V3 응답
 * TODO :: volka
 * TODO :: 모든 요청과 응답은 불변으로 사용.
 * TODO :: -> 현재 올웨이즈의 Jackson의 JSON 직렬화/역직렬화는 생성자를 이용한 직렬화/역직렬화를 사용하지 않으므로 @Data로 임시 사용 (중계 모듈은 record 사용중)
 * TODO :: -> 추후 선물하기 비즈니스가 정해질 때 올웨이즈에 맞게 리팩토링 하여 사용
 */
@Schema(description = "선물 상세 조회 V3 응답")
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class KakaoPostOrderGiftResponseV3 implements GiftExternalResponse {
    @Schema(description = "선물 정보")
    private Gift gift;
    @Schema(description = "상품 정보")
    private Product product;

    @Schema(name = "PostOrderGiftResponseV3.Gift", description = "선물 정보")
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @EqualsAndHashCode(callSuper = false)
    @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
    public static class Gift {
        @Schema(description = "순번", requiredMode = REQUIRED)
        @NotNull
        private Integer sequence;
        @Schema(description = "고객사의 수신자 식별값")
        private String externalKey;
        @Schema(description = "수신자 유형 (PHONE 고정)", requiredMode = REQUIRED)
        @NotNull
        private KakaoReceiverType receiverType;
        @Schema(description = "수신자명")
        private String name;
        @Schema(description = "수신자 유형에 맞는 ID (폰번호)", requiredMode = REQUIRED)
        private String receiverId;
        @Schema(description = "선물ID", requiredMode = REQUIRED)
        @NotNull
        private Long giftTraceId;
        @Schema(description = "선포비 주문 번호", requiredMode = REQUIRED)
        @NotNull
        private Long orderTraceId;
        @Schema(description = "외부 주문 번호", requiredMode = REQUIRED)
        @NotNull
        private Long externalOrderId;
        @Schema(description = "상품 가격", requiredMode = REQUIRED)
        @NotNull
        private Long productPrice;
        @Schema(description = "선물 상태", requiredMode = REQUIRED)
        @NotNull
        private KakaoGiftStatus giftStatus;
        @Schema(description = "교환권 상태")
        private KakaoVoucherStatus voucherStatus;
        @Schema(description = "교환권 만료일시")
        private String expiredAt;
        @Schema(description = "선물함 랜딩 URL", requiredMode = REQUIRED)
        @NotNull
        @URL
        private String giftUrl;
    }

    @Schema(name = "PostOrderGiftResponseV3.Product", description = "상품 정보")
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @EqualsAndHashCode(callSuper = false)
    @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
    public static class Product {
        @Schema(description = "상품명", requiredMode = REQUIRED)
        private String productName;
        @Schema(description = "아이템타입(VOUCHER=교환권,SHIPPING=배송상품)", requiredMode = REQUIRED)
        private KakaoItemType itemType;
        @Schema(description = "브랜드명", requiredMode = REQUIRED)
        private String brandName;
        @Schema(description = "브랜드 이미지 URL", requiredMode = REQUIRED)
        private String brandImageUrl;
        @Schema(description = "상품 이미지 URL", requiredMode = REQUIRED)
        private String productImageUrl;
        @Schema(description = "상품 썸네일 URL", requiredMode = REQUIRED)
        private String productThumbImageUrl;
        @Schema(description = "상품 가격", requiredMode = REQUIRED)
        private Long productPrice;
    }
}
