package com.kep.core.model.dto.platform.kakao.gift.vo.receive;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.kep.core.model.dto.platform.kakao.gift.GiftExternalRequest;
import com.kep.core.model.dto.platform.kakao.gift.constant.KakaoGiftIdType;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.List;

import static io.swagger.v3.oas.annotations.media.Schema.RequiredMode.REQUIRED;

/**
 * 선물 상세 조회 V3 요청
 *
 * TODO :: volka
 * TODO :: 모든 요청과 응답은 불변으로 사용.
 * TODO :: -> 현재 올웨이즈의 Jackson의 JSON 직렬화/역직렬화는 생성자를 이용한 직렬화/역직렬화를 사용하지 않으므로 @Data로 임시 사용 (중계 모듈은 record 사용중)
 * TODO :: -> 추후 선물하기 비즈니스가 정해질 때 올웨이즈에 맞게 리팩토링 하여 사용
 *
 * @author volka
 */
@Schema(description = "[POST] 선물 상세 조회 V3")
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
@JsonNaming(value = PropertyNamingStrategies.SnakeCaseStrategy.class)
public class KakaoPostOrderGiftRequestV3 implements GiftExternalRequest {
    @Schema(description = "주문 완료 후 생성되는 수신자 단위의 식별 파라미터", requiredMode = REQUIRED)
    @Size(min = 1, max = 1000)
    @NotEmpty
    @Valid
    private List<Receiver> params;

    @Schema(name = "PostOrderGiftRequestV3.Receiver", description = "선물 상세 조회 V3 요청 쿼리 스트링")
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @EqualsAndHashCode(callSuper = false)
    @JsonNaming(value = PropertyNamingStrategies.SnakeCaseStrategy.class)
    public static class Receiver {
        @Schema(description = "선물 식별 ID 타입 [RESERVE_TRACE_ID, EXTERNAL_ORDER_ID, GIFT_TRACE_ID]", requiredMode = REQUIRED)
        @NotNull
        private KakaoGiftIdType idType;
        @Schema(description = "ID 타입별 아이디", requiredMode = REQUIRED)
        @NotNull
        private String id;
        @Schema(description = "외부 키 (RESERVE_TRACE_ID, EXTERNAL_ORDER_ID 일 경우 필수)")
        private String externalKey;
        @Schema(description = "ID 타입 미정의 여부")
        private boolean undefinedIdType;

    }
}
