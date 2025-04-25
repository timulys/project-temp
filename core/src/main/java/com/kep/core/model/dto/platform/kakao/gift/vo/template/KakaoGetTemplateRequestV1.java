package com.kep.core.model.dto.platform.kakao.gift.vo.template;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.kep.core.model.dto.platform.kakao.gift.GiftExternalQueryRequest;
import com.kep.core.model.dto.platform.kakao.gift.constant.KakaoTemplateStatus;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;

/**
 * TODO :: volka
 * TODO :: 모든 요청과 응답은 불변으로 사용.
 * TODO :: -> 현재 올웨이즈의 Jackson의 JSON 직렬화/역직렬화는 생성자를 이용한 직렬화/역직렬화를 사용하지 않으므로 @Data로 임시 사용 (중계 모듈은 record 사용중)
 * TODO :: -> 추후 선물하기 비즈니스가 정해질 때 올웨이즈에 맞게 리팩토링 하여 사용
 */
@Schema(description = "템플릿 목록 조회 V1 요청 VO")
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class KakaoGetTemplateRequestV1 implements GiftExternalQueryRequest {
    @Parameter(in = ParameterIn.QUERY, description = "템플릿 상태")
    private KakaoTemplateStatus status;
    @Parameter(in = ParameterIn.QUERY, description = "템플릿 토큰")
    private String token;
    @Parameter(in = ParameterIn.QUERY, description = "템플릿명")
    private String name;
    @Parameter(in = ParameterIn.QUERY, description = "상품명")
    private String productName;
    @Parameter(in = ParameterIn.QUERY, description = "페이지 (default 0)")
    private @PositiveOrZero
    @Max(Integer.MAX_VALUE) Integer page;
    @Parameter(in = ParameterIn.QUERY, description = "페이지 사이즈 (default 20)")
    private @Positive
    @Min(1) Integer size;

    @Override
    public MultiValueMap<String, String> toMap() {
        MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
        if (status != null) map.add("status", status.name());
        if (token != null && !token.isEmpty()) map.add("token", token);
        if (name != null && !name.isEmpty()) map.add("name", name);
        if (productName != null && !productName.isEmpty()) map.add("productName", productName);
        if (page >= 0) map.add("page", String.valueOf(page));
        if (size > 0) map.add("size", String.valueOf(size));

        return map;
    }

}
