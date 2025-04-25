package com.kep.core.model.dto.platform.kakao.gift.vo.send;


import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.kep.core.model.dto.platform.kakao.gift.GiftExternalResponse;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.List;

import static io.swagger.v3.oas.annotations.media.Schema.RequiredMode.REQUIRED;

/**
 * 선물 발송 요청 응답 (선물 발송 요청은 비동기처리라 전달 보장하지 않음)
 *
 * TODO :: volka
 * TODO :: 모든 요청과 응답은 불변으로 사용.
 * TODO :: -> 현재 올웨이즈의 Jackson의 JSON 직렬화/역직렬화는 생성자를 이용한 직렬화/역직렬화를 사용하지 않으므로 @Data로 임시 사용 (중계 모듈은 record 사용중)
 * TODO :: -> 추후 선물하기 비즈니스가 정해질 때 올웨이즈에 맞게 리팩토링 하여 사용
 *
 * @author volka
 */
@Schema(description = "선물 발송 API 응답 [https://gateway-giftbiz.kakao.com/openapi/giftbiz/v1/template/order]")
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
@JsonNaming(value = PropertyNamingStrategies.SnakeCaseStrategy.class)
public class KakaoPostOrderResponseV1 implements GiftExternalResponse {
    @Schema(description = "예약 거래 번호", requiredMode = REQUIRED)
    private @NotNull Long reserveTraceId;
    @Schema(description = "템플릿 수신자 정보")
    private @Valid List<TemplateReceiver> templateReceivers;


    @Schema(name = "PostOrderResponseV1.TemplateReceiver", description = "템플릿 수신자 정보")
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @EqualsAndHashCode(callSuper = false)
    @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
        public static class TemplateReceiver {
        @Schema(description = "순번", requiredMode = REQUIRED)
        private Integer sequence;
        @Schema(description = "수신자 ID", requiredMode = REQUIRED)
        private String receiver_id;
        @Schema(description = "고객사 수신자 식별값")
        private String external_key;
        @Schema(description = "수신자명")
        private String name;
    }
}
