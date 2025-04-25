package com.kep.core.model.dto.platform.kakao.gift.vo.cancel;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.kep.core.model.dto.platform.kakao.gift.GiftExternalResponse;
import com.kep.core.model.dto.platform.kakao.gift.constant.KakaoGiftIdType;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

import static io.swagger.v3.oas.annotations.media.Schema.RequiredMode.REQUIRED;

/**
 * 선물 취소 요청 응답.
 *
 * TODO :: volka
 * TODO :: 모든 요청과 응답은 불변으로 사용.
 * TODO :: -> 현재 올웨이즈의 Jackson의 JSON 직렬화/역직렬화는 생성자를 이용한 직렬화/역직렬화를 사용하지 않으므로 @Data로 임시 사용 (중계 모듈은 record 사용중)
 * TODO :: -> 추후 선물하기 비즈니스가 정해질 때 올웨이즈에 맞게 리팩토링 하여 사용
 */
@Data
@EqualsAndHashCode(callSuper = false)
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class KakaoPostCancelGiftResponseV1 implements GiftExternalResponse {
    @Schema(description = "주문 식별자 타입", requiredMode = REQUIRED)
    private KakaoGiftIdType idType;
    @Schema(description = "주문 식별자 ID", requiredMode = REQUIRED)
    private String id;
    @Schema(description = "고객사 수신자 식별값")
    private String externalKey;
    @Schema(description = "선포비선물번호", requiredMode = REQUIRED)
    private Long giftTraceId;
    @Schema(description = "선물취소결과 메시지", requiredMode = REQUIRED)
    private String result;
}
