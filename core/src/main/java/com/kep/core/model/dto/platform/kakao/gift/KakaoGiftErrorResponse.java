package com.kep.core.model.dto.platform.kakao.gift;


import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * 카카오 선물하기 요청 실패시 공통 응답 VO
 *
 * TODO :: volka
 * TODO :: 모든 요청과 응답은 불변으로 사용.
 * TODO :: -> 현재 올웨이즈의 Jackson의 JSON 직렬화/역직렬화는 생성자를 이용한 직렬화/역직렬화를 사용하지 않으므로 @Data로 임시 사용 (중계 모듈은 record 사용중)
 * TODO :: -> 추후 선물하기 비즈니스가 정해질 때 올웨이즈에 맞게 리팩토링 하여 사용
 *
 * @author volka
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class KakaoGiftErrorResponse {
    @Schema(description = "선포비 HTTP statusCode")
    private Integer statusCode;
    @Schema(description = "에러 카테고리")
    private String errorCategory;
    @Schema(description = "에러코드값")
    private String errorCode;
    @Schema(description = "에러명")
    private String errorName;
    @Schema(description = "에러 메시지")
    private String errorMessage;
    @Schema(description = "요청값 검증 실패 필드 목록")
    private Object validation;
}