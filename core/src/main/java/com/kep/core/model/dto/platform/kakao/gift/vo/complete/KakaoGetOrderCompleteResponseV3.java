package com.kep.core.model.dto.platform.kakao.gift.vo.complete;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.kep.core.model.dto.platform.kakao.gift.GiftExternalResponse;
import com.kep.core.model.dto.platform.kakao.gift.constant.*;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * 완료 주문 조회 V3 응답
 *
 * TODO :: volka
 * TODO :: 모든 요청과 응답은 불변으로 사용.
 * TODO :: -> 현재 올웨이즈의 Jackson의 JSON 직렬화/역직렬화는 생성자를 이용한 직렬화/역직렬화를 사용하지 않으므로 @Data로 임시 사용 (중계 모듈은 record 사용중)
 * TODO :: -> 추후 선물하기 비즈니스가 정해질 때 올웨이즈에 맞게 리팩토링 하여 사용
 */
@Schema(description = "완료 주문 조회 V3 응답")
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
@JsonNaming(value = PropertyNamingStrategies.SnakeCaseStrategy.class)
public class KakaoGetOrderCompleteResponseV3 implements GiftExternalResponse {
    @Schema(description = "예약 거래 번호")
    private @NotNull Long reserveTraceId;
    @Schema(description = "외부주문번호")
    private @NotNull String externalOrderId;
    @Schema(description = "선물 발송 요청 수")
    private @NotNull Long giftRequestCount;
    @Schema(description = "선물 발송 중인 수")
    private @NotNull Long giftProcessingCount;
    @Schema(description = "선물 발송 완료 수")
    private @NotNull Long giftSentCount;
    @Schema(description = "선물 발송 완료 수")
    private @NotNull Long giftFailCount;
    @Schema(description = "캐시 사용 금액")
    private @NotNull Long cashUseAmount;
    @Schema(description = "템플릿 선물 주문 정보 목록")
    private List<TemplateOrderGift> templateOrderGifts;
    @Schema(description = "상품 정보")
    private Product product;


    @Schema(name = "GetOrderCompleteResponseV3.TemplateOrderGift", description = "템플릿 선물 주문 정보(완료 주문 조회 V3 응답)")
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @EqualsAndHashCode(callSuper = false)
    @JsonNaming(value = PropertyNamingStrategies.SnakeCaseStrategy.class)
    public static class TemplateOrderGift {
        @Schema(description = "순번")
        @NotNull
        private Integer sequence;
        @Schema(description = "고객사가 가지고 있는 수신자 식별값")
        private String externalKey;
        @Schema(description = "수신자유형")
        @NotNull
        private KakaoReceiverType receiverType;
        @Schema(description = "수신자이름")
        private String name;
        @Schema(description = "수신자 유형(RECEIVER_TYPE)에 맞는 ID e.g, 010-0000-0000")
        private @NotBlank String receiverId;
        @Schema(description = "선물ID")
        @NotNull
        private Long giftTraceId;
        @Schema(description = "선포비 주문 번호")
        @NotNull
        private Long orderTraceId;
        @Schema(description = "외부주문번호")
        @NotNull
        private String externalOrderId;
        @Schema(description = "상품가격")
        @NotNull
        private Long productPrice;
        @Schema(description = "선물상태 [REGISTERED,FAILED_SEND_BIZMESSAGE,SYSTEM_ERROR,COMPLETED,CANCELED,FAILOVER,CASHBACKED]")
        @NotNull
        private KakaoGiftStatus giftStatus;
        @Schema(description = "교환권상태 [USED,NOT_USED,DROPPED]")
        private KakaoVoucherStatus voucherStatus;
        @Schema(description = "선물취소사유")
        private KakaoGiftCancelReason giftCancelReason;
        @Schema(description = "교환권만료일시")
        private String expiredAt;
        @Schema(description = "선물함랜딩url")
        @NotNull
        private String giftUrl;

    }

    @Schema(name = "GetOrderCompleteResponseV3.Product", description = "상품 정보 (완료 주문 조회 V3 응답)")
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @EqualsAndHashCode(callSuper = false)
    @JsonNaming(value = PropertyNamingStrategies.SnakeCaseStrategy.class)
    public static class Product {
        @Schema(description = "상품명")
        @NotBlank
        private String productName;
        @Schema(description = "아이템타입(VOUCHER=교환권,SHIPPING=배송상품)")
        @NotNull
        private KakaoItemType itemType;
        @Schema(description = "브랜드명")
        @NotNull
        private String brandName;
        @Schema(description = "브랜드이미지url")
        @NotNull
        private String brandImageUrl;
        @Schema(description = "상품이미지url")
        @NotNull
        private String productImageUrl;
        @Schema(description = "상품썸네일url")
        @NotNull
        private String productThumbImageUrl;
        @Schema(description = "상품가격")
        @NotNull
        private Long productPrice;
    }
}
