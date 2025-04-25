package com.kep.portal.controller.gift;

import com.kep.core.model.dto.ApiResultCode;
import com.kep.core.model.dto.ResponseDto;
import com.kep.core.model.dto.platform.kakao.gift.GiftResponseDto;
import com.kep.core.model.dto.platform.kakao.gift.vo.cancel.KakaoPostCancelGiftRequestV1;
import com.kep.core.model.dto.platform.kakao.gift.vo.cancel.KakaoPostCancelGiftResponseV1;
import com.kep.core.model.dto.platform.kakao.gift.vo.cash.KakaoGetCashBalanceResponseV1;
import com.kep.core.model.dto.platform.kakao.gift.vo.complete.KakaoGetOrderCompleteRequestV3;
import com.kep.core.model.dto.platform.kakao.gift.vo.complete.KakaoGetOrderCompleteResponseV3;
import com.kep.core.model.dto.platform.kakao.gift.vo.daily.KakaoGetOrderDailyRequestV1;
import com.kep.core.model.dto.platform.kakao.gift.vo.daily.KakaoGetOrderDailyResponseV1;
import com.kep.core.model.dto.platform.kakao.gift.vo.receive.KakaoPostOrderGiftRequestV3;
import com.kep.core.model.dto.platform.kakao.gift.vo.receive.KakaoPostOrderGiftResponseV3;
import com.kep.core.model.dto.platform.kakao.gift.vo.reserve.KakaoGetOrderReserveStatusRequestV1;
import com.kep.core.model.dto.platform.kakao.gift.vo.reserve.KakaoGetOrderReserveStatusResponseV1;
import com.kep.core.model.dto.platform.kakao.gift.vo.send.KakaoPostOrderRequestV1;
import com.kep.core.model.dto.platform.kakao.gift.vo.send.KakaoPostOrderResponseV1;
import com.kep.core.model.dto.platform.kakao.gift.vo.template.KakaoGetTemplateDtlResponseV1;
import com.kep.core.model.dto.platform.kakao.gift.vo.template.KakaoGetTemplateRequestV1;
import com.kep.core.model.dto.platform.kakao.gift.vo.template.KakaoGetTemplateResponseV1;
import com.kep.portal.client.gift.GiftServiceClient;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;
import org.springdoc.api.annotations.ParameterObject;
import org.springframework.cloud.openfeign.SpringQueryMap;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import java.util.List;

@Tag(name = "선포비 선물 API")
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/api/v1/kakao/gift")
@RestController
public class KakaoGiftController {

    private final GiftServiceClient giftServiceClient;

    @Tag(name = "선포비 선물 API")
    @Operation(summary = "선물 상세 조회")
    @PostMapping("/find")
    @ResponseStatus(HttpStatus.OK)
    public KakaoGiftResponseDto<? super List<KakaoPostOrderGiftResponseV3>> findOrderGifts(
            @Parameter(in = ParameterIn.HEADER, description = "카카오 인증 API KEY")
            @RequestHeader("X-Kakao-AK") @NotBlank final String kakaoApiToken,
            @Valid @RequestBody KakaoPostOrderGiftRequestV3 request
    ) {
        return KakaoGiftResponseDto.success(giftServiceClient.findOrderGifts(kakaoApiToken, request));
    }

    @Tag(name = "선포비 선물 API")
    @Operation(summary = "선물 취소 요청")
    @PutMapping
    @ResponseStatus(HttpStatus.OK)
    public KakaoGiftResponseDto<? super List<KakaoPostCancelGiftResponseV1>> cancelOrderGift(
            @Parameter(in = ParameterIn.HEADER, description = "카카오 인증 API KEY")
            @RequestHeader("X-Kakao-AK") @NotBlank final String kakaoApiToken,
            @RequestBody @Valid KakaoPostCancelGiftRequestV1 request
    ) {
        return KakaoGiftResponseDto.success(giftServiceClient.cancelOrderGift(kakaoApiToken, request));
    }

    @Tag(name = "선포비 선물 API")
    @Operation(summary = "선물 발송 요청")
    @PostMapping
    @ResponseStatus(HttpStatus.OK)
    public KakaoGiftResponseDto<? super KakaoPostOrderResponseV1> send(
            @Parameter(in = ParameterIn.HEADER, description = "카카오 인증 API KEY")
            @RequestHeader("X-Kakao-AK") @NotBlank final String kakaoApiToken,
            @Valid @RequestBody KakaoPostOrderRequestV1 request
    ) {
        return KakaoGiftResponseDto.success(giftServiceClient.send(kakaoApiToken, request));
    }

    @Tag(name = "선포비 선물 API")
    @Operation(summary = "완료 주문 조회")
    @GetMapping("/complete")
    @ResponseStatus(HttpStatus.OK)
    public KakaoGiftResponseDto<? super KakaoGetOrderCompleteResponseV3> getOrderComplete(
            @Parameter(in = ParameterIn.HEADER, description = "카카오 인증 API KEY")
            @RequestHeader("X-Kakao-AK") @NotBlank final String kakaoApiToken,
            @SpringQueryMap @ParameterObject @Valid KakaoGetOrderCompleteRequestV3 params
    ) {
        return KakaoGiftResponseDto.success(giftServiceClient.getOrderComplete(kakaoApiToken, params));
    }

    @Tag(name = "선포비 선물 API")
    @Operation(summary = "주문 상태 조회(발송 요청 주문 상태 확인)")
    @GetMapping("/status")
    @ResponseStatus(HttpStatus.OK)
    public KakaoGiftResponseDto<? super KakaoGetOrderReserveStatusResponseV1> getOrderReserveStatus(
            @Parameter(in = ParameterIn.HEADER, description = "카카오 인증 API KEY")
            @RequestHeader("X-Kakao-AK") @NotBlank final String kakaoApiToken,
            @SpringQueryMap @ParameterObject @Valid KakaoGetOrderReserveStatusRequestV1 params
    ) {
        return KakaoGiftResponseDto.success(giftServiceClient.getOrderReserveStatus(kakaoApiToken, params));
    }





    @Tag(name = "선포비 선물 API")
    @Operation(summary = "잔여 캐시 확인")
    @GetMapping("/balance")
    @ResponseStatus(HttpStatus.OK)
    public KakaoGiftResponseDto<? super KakaoGetCashBalanceResponseV1> getCashBalance(
            @Parameter(in = ParameterIn.HEADER, description = "카카오 인증 API KEY")
            @RequestHeader("X-Kakao-AK") @NotBlank final String kakaoApiToken
    ) {
        return KakaoGiftResponseDto.success(giftServiceClient.getCashBalance(kakaoApiToken));
    }





    @Tag(name = "선포비 선물 API")
    @GetMapping("/templates")
    @ResponseStatus(HttpStatus.OK)
    public KakaoGiftResponseDto<? super KakaoGetTemplateResponseV1> getTemplates(
            @Parameter(in = ParameterIn.HEADER, description = "카카오 인증 API KEY")
            @RequestHeader("X-Kakao-AK") @NotBlank final String kakaoApiToken,
            @ParameterObject @Valid KakaoGetTemplateRequestV1 params
    ) {
//        return KakaoGiftResponseDto.success(giftServiceClient.getTemplates(kakaoApiToken, params);
        return KakaoGiftResponseDto.success(giftServiceClient.getTemplates(kakaoApiToken, params));
    }

    @Tag(name = "선포비 선물 API")
    @GetMapping("/templates/{templateToken}")
    @ResponseStatus(HttpStatus.OK)
    public KakaoGiftResponseDto<? super KakaoGetTemplateDtlResponseV1> getTemplates(
            @Parameter(in = ParameterIn.HEADER, description = "카카오 인증 API KEY")
            @RequestHeader("X-Kakao-AK") @NotBlank final String kakaoApiToken,
            @PathVariable("templateToken") @NotBlank final String templateToken
    ) {
        return KakaoGiftResponseDto.success(giftServiceClient.getTemplate(kakaoApiToken, templateToken));
    }

    @Tag(name = "선포비 선물 API")
    @GetMapping("/templates/{templateToken}/send-history")
    @ResponseStatus(HttpStatus.OK)
    public KakaoGiftResponseDto<? super KakaoGetOrderDailyResponseV1> getSendHistoryDaily(
            @Parameter(in = ParameterIn.HEADER, description = "카카오 인증 API KEY")
            @RequestHeader("X-Kakao-AK") @NotBlank final String kakaoApiToken,
            @PathVariable("templateToken") @NotBlank final String templateToken,
            @ParameterObject @Valid KakaoGetOrderDailyRequestV1 params
    ) {
        return KakaoGiftResponseDto.success(giftServiceClient.getSendHistoryDaily(kakaoApiToken, templateToken, params));
    }

    @Getter
    @ToString
    public static class KakaoGiftResponseDto<T> extends ResponseDto {

        private final T data;

        private KakaoGiftResponseDto(String responseCode, String message, T data) {
            super(responseCode, message);
            this.data = data;
        }

        public static <T> KakaoGiftResponseDto<T> success(GiftResponseDto<T> giftResponseDto) {
            return new KakaoGiftResponseDto<>(ApiResultCode.succeed.name(), ApiResultCode.succeed.name(), giftResponseDto.getData());
        }
    }
}
