package com.kep.core.model.dto.platform.kakao.gift.vo.complete;

import com.kep.core.model.dto.platform.kakao.gift.GiftExternalQueryRequest;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

/**
 * 완료 주문 조회 V3 요청 (GET)
 *
 * TODO :: volka
 * TODO :: 모든 요청과 응답은 불변으로 사용.
 * TODO :: -> 현재 올웨이즈의 Jackson의 JSON 직렬화/역직렬화는 생성자를 이용한 직렬화/역직렬화를 사용하지 않으므로 @Data로 임시 사용 (중계 모듈은 record 사용중)
 * TODO :: -> 추후 선물하기 비즈니스가 정해질 때 올웨이즈에 맞게 리팩토링 하여 사용
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
@Schema(description = "완료 주문 조회 V3 요청")
public class KakaoGetOrderCompleteRequestV3 implements GiftExternalQueryRequest {
    @Parameter(in = ParameterIn.QUERY, description = "reserve_trace_id로 조회시 발송 요청 단위에 대해 유일하기 때문에 단건 조회")
    private String reserve_trace_id;
    @Parameter(in = ParameterIn.QUERY, description = "external_order_id로 조회시 발송 실패 건이 포함되므로 다건 조회될 수 있습니다. 단, 주문 성공건은 유일하게 한건만 존재합니다.")
    private String external_order_id;

    @Override
    public MultiValueMap<String, String> toMap() {
        MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
        if(external_order_id != null && !external_order_id.isEmpty()) map.add("external_order_id", external_order_id);
        if(reserve_trace_id != null && !reserve_trace_id.isEmpty()) map.add("reserve_trace_id", reserve_trace_id);
        return map;
    }
}

