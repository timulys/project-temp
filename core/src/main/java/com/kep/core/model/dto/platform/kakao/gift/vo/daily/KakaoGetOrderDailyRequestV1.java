package com.kep.core.model.dto.platform.kakao.gift.vo.daily;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.kep.core.model.dto.platform.kakao.gift.GiftExternalQueryRequest;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import javax.validation.constraints.*;

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
public class KakaoGetOrderDailyRequestV1 implements GiftExternalQueryRequest {
    @Parameter(in = ParameterIn.QUERY)
    @NotNull
    @PositiveOrZero
    private Integer page;
    @Parameter(in = ParameterIn.QUERY)
    private @NotNull
    @Positive
    @Min(1)
    @Max(1000) Integer size;
    @Parameter(in = ParameterIn.QUERY, description = "날짜 (yyyyMMdd)")
    @NotBlank
    private String date;

    @Override
    public MultiValueMap<String, String> toMap() {
        MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
        map.add("page", String.valueOf(page));
        map.add("size", String.valueOf(size));
        map.add("date", date);
        return map;
    }
}
