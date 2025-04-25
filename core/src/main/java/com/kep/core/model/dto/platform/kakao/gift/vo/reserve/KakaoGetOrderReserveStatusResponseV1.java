package com.kep.core.model.dto.platform.kakao.gift.vo.reserve;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.kep.core.model.dto.platform.kakao.gift.GiftExternalResponse;
import com.kep.core.model.dto.platform.kakao.gift.constant.KakaoReserveOrderStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * TODO :: volka
 * TODO :: 모든 요청과 응답은 불변으로 사용.
 * TODO :: -> 현재 올웨이즈의 Jackson의 JSON 직렬화/역직렬화는 생성자를 이용한 직렬화/역직렬화를 사용하지 않으므로 @Data로 임시 사용 (중계 모듈은 record 사용중)
 * TODO :: -> 추후 선물하기 비즈니스가 정해질 때 올웨이즈에 맞게 리팩토링 하여 사용
 */
@Schema(description = "주문 상태 조회 V1 응답 VO")
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class KakaoGetOrderReserveStatusResponseV1 implements GiftExternalResponse {
    @Schema(description = "주문 상태 조회 목록")
    @Valid
    private List<TemplateReserveOrder> templateReserveOrders;

    @Schema(name = "GetOrderReserveStatusResponseV1.TemplateReserveOrder")
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @EqualsAndHashCode(callSuper = false)
    @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
    public static class TemplateReserveOrder {
        @Schema(description = "예약 거래 번호")
        private Long reserveTraceId;
        @Schema(description = "외부주문번호")
        @NotNull
        private String externalOrderId;
        @Schema(description = "템플릿 주문 상태 목록")
        @NotNull
        private KakaoReserveOrderStatus reserveOrderStatus;
    }

}
