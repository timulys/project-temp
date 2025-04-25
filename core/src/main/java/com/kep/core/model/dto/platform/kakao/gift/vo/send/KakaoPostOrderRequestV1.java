package com.kep.core.model.dto.platform.kakao.gift.vo.send;


import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.kep.core.model.dto.platform.kakao.gift.GiftExternalRequest;
import com.kep.core.model.dto.platform.kakao.gift.constant.KakaoReceiverType;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.URL;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.List;

import static io.swagger.v3.oas.annotations.media.Schema.RequiredMode.REQUIRED;

/**
 * 선물 발송 요청
 *
 * TODO :: volka
 * TODO :: 모든 요청과 응답은 불변으로 사용.
 * TODO :: -> 현재 올웨이즈의 Jackson의 JSON 직렬화/역직렬화는 생성자를 이용한 직렬화/역직렬화를 사용하지 않으므로 @Data로 임시 사용 (중계 모듈은 record 사용중)
 * TODO :: -> 추후 선물하기 비즈니스가 정해질 때 올웨이즈에 맞게 리팩토링 하여 사용
 *
 * @author volka
 */
@Schema(description = "선물 발송 요청 VO")
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
@JsonNaming(value = PropertyNamingStrategies.SnakeCaseStrategy.class)
public class KakaoPostOrderRequestV1 implements GiftExternalRequest {
    @Schema(description = "템플릿 토큰", requiredMode = REQUIRED)
    private @NotBlank String templateToken;
    @Schema(description = "수신자 유형 (현재는 PHONE만 가능)", requiredMode = REQUIRED)
    private @NotNull KakaoReceiverType receiverType;
    @Schema(description = "외부 주문번호", requiredMode = REQUIRED)
    private @NotBlank
    @Size(max = 70) String externalOrderId;
    @Schema(description = "수신자 목록")
    private @NotEmpty
    @Size(min = 1)
    @Valid List<Receiver> receivers;
    @Schema(description = "발송 요청 선물이 수신자에게 정상 수신시 받을 수 있는 콜백 주소 (콜백 성공 보장 X)")
    private @URL String successCallbackUrl;
    @Schema(description = "발송 요청 선물이 수신자에게 정상 수신되지 못할 경우 받을 콜백 주소 (콜백 성공 보장 X)")
    private @URL String failCallbackUrl;
    @Schema(description = "선물이 취소될 경우 콜백 주소 (콜백 성공 보장 X)")
    private @URL String giftCallbackUrl;
    @Schema(description = "주문명 (미입력시 템플릿 생성시 입력한 템플릿명으로 주문 생성)")
    private String templateOrderName;

    @Schema(name = "SendGiftRequest.Receiver", description = "수신자 정보")
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @EqualsAndHashCode(callSuper = false)
        @JsonNaming(value = PropertyNamingStrategies.SnakeCaseStrategy.class)
        public static class Receiver {
        @Schema(description = "수신자 유형(RECEIVER_TYPE)에 따른 ID (현재는 PHONE 고정이므로 휴대폰 번호 '-' 포함)", requiredMode = REQUIRED)
        private @NotEmpty String receiverId;
        @Schema(description = "수신자 단위로 부여 가능한 외부 식별 키. 필수는 아니나 이를 이용해 선물사용여부, 취소 가능")
        private @Size(max = 70) String externalKey;
        @Schema(description = "받는 사람 이름")
        private String name;
        @Schema(description = "발신자명 (미입력 시 템플릿 생성 시 입력된 값으로 발송)")
        private String senderName;
        @Schema(description = "메시지 카드 내용 (미입력 시 템플릿 생성 시 입력된 값으로 발송)")
        private String mcText;
    }
}