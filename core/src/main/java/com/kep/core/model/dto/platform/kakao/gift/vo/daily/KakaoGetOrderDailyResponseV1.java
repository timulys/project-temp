package com.kep.core.model.dto.platform.kakao.gift.vo.daily;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.kep.core.model.dto.platform.kakao.gift.GiftExternalResponse;
import com.kep.core.model.dto.platform.kakao.gift.constant.KakaoGiftStatus;
import com.kep.core.model.dto.platform.kakao.gift.constant.KakaoReceiverType;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;


/**
 * TODO :: volka
 * TODO :: 모든 요청과 응답은 불변으로 사용.
 * TODO :: -> 현재 올웨이즈의 Jackson의 JSON 직렬화/역직렬화는 생성자를 이용한 직렬화/역직렬화를 사용하지 않으므로 @Data로 임시 사용 (중계 모듈은 record 사용중)
 * TODO :: -> 추후 선물하기 비즈니스가 정해질 때 올웨이즈에 맞게 리팩토링 하여 사용
 */
@Schema(description = "일거래 조회(템플릿 발송 내역) v1 응답 VO")
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
@JsonNaming(value = PropertyNamingStrategies.SnakeCaseStrategy.class)
public class KakaoGetOrderDailyResponseV1 implements GiftExternalResponse {
    @Schema(description = "총 선물 수")
    @NotNull
    private Long totalCount;
    @Schema(description = "마지막 페이지 여부")
    @NotNull
    private Boolean last;
    @Schema(description = "선물 발송 중인 수")
    @NotNull
    private Long giftProcessingCount;
    @Schema(description = "선물 발송 완료 수")
    @NotNull
    private Long giftSentCount;
    @Schema(description = "선물 발송 완료 수")
    @NotNull
    private Long giftFailCount;
    @Schema(description = "캐시 사용 금액")
    @NotNull
    private Long cashUseAmount;
    @Schema(description = "선물 발송 목록")
    private @Valid List<TemplateOrderDailyGift> templateOrderDailyGifts;

    @Schema(name = "GetOrderDailyResponseV1.TemplateOrderDailyGift", description = "선물 발송 정보(일거래 조회(템플릿 발송 내역 V1))")
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @EqualsAndHashCode(callSuper = false)
        @JsonNaming(value = PropertyNamingStrategies.SnakeCaseStrategy.class)
        public static class TemplateOrderDailyGift {
        @Schema(description = "순번")
        @NotNull
        private Integer sequence;
        @Schema(description = "예약 거래 번호")
        @NotNull
        private Long reserveTraceId;
        @Schema(description = "외부주문번호")
        @NotNull
        private String externalOrderId;
        @Schema(description = "고객사가 가지고 있는 수신자 식별값")
        private String externalKey;
        @Schema(description = "수신자유형")
        @NotNull
        private KakaoReceiverType receiverType;
        @Schema(description = "수신자이름")
        private String name;
        @Schema(description = "수신자 유형(RECEIVER_TYPE)에 맞는 ID e.g, 010-0000-0000")
        @NotBlank
        private String receiverId;
        @Schema(description = "선물ID")
        @NotNull
        private Long giftTraceId;
        @Schema(description = "상품가격")
        @NotNull
        private Long productPrice;
        @Schema(description = "선물상태")
        @NotNull
        private KakaoGiftStatus giftStatus;
    }
}
