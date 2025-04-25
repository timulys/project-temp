package com.kep.core.model.dto.platform.kakao.gift.vo.cash;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.kep.core.model.dto.platform.kakao.gift.GiftExternalResponse;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

/**
 * TODO :: volka
 * TODO :: 모든 요청과 응답은 불변으로 사용.
 * TODO :: -> 현재 올웨이즈의 Jackson의 JSON 직렬화/역직렬화는 생성자를 이용한 직렬화/역직렬화를 사용하지 않으므로 @Data로 임시 사용 (중계 모듈은 record 사용중)
 * TODO :: -> 추후 선물하기 비즈니스가 정해질 때 올웨이즈에 맞게 리팩토링 하여 사용
 */
@Schema(description = "잔여캐시 확인 V1 응답 VO")
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class KakaoGetCashBalanceResponseV1 implements GiftExternalResponse {
    @Schema(description = "유상캐시,무상캐시의 합")
    private Long totalCash;
    @Schema(description = "유상캐시")
    private Long cash;
    @Schema(description = "무상캐시")
    private Long freeCash;
    @Schema(description = "만료예정 캐시")
    private Long expiry;
}
