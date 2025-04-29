package com.kep.portal.client.gift;

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
import org.springdoc.api.annotations.ParameterObject;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.cloud.openfeign.SpringQueryMap;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import java.util.List;


@FeignClient(name = "gift-service", url = "${spring.cloud.discovery.client.simple.instances.gift-service[0].uri}")
public interface GiftServiceClient {

    @PostMapping("/api/v1/gifts/find")
    GiftResponseDto<? super List<KakaoPostOrderGiftResponseV3>> findOrderGifts(
            @RequestHeader("X-Kakao-AK") @NotBlank final String kakaoApiToken,
            @Valid @RequestBody KakaoPostOrderGiftRequestV3 request);

    @PostMapping("/api/v1/gifts/cancel")
    GiftResponseDto<? super List<KakaoPostCancelGiftResponseV1>> cancelOrderGift(
            @RequestHeader("X-Kakao-AK") @NotBlank final String kakaoApiToken,
            @RequestBody @Valid KakaoPostCancelGiftRequestV1 request);

    @PostMapping("/api/v1/gifts")
    GiftResponseDto<? super KakaoPostOrderResponseV1> send(
            @RequestHeader("X-Kakao-AK") @NotBlank final String kakaoApiToken,
            @Valid @RequestBody KakaoPostOrderRequestV1 request);

    @GetMapping("/api/v1/gifts/complete")
    GiftResponseDto<? super KakaoGetOrderCompleteResponseV3> getOrderComplete(
            @RequestHeader("X-Kakao-AK") @NotBlank final String kakaoApiToken,
            @SpringQueryMap @ParameterObject @Valid KakaoGetOrderCompleteRequestV3 params);

    @GetMapping("/api/v1/gifts/status")
    GiftResponseDto<? super KakaoGetOrderReserveStatusResponseV1> getOrderReserveStatus(
            @RequestHeader("X-Kakao-AK") @NotBlank final String kakaoApiToken,
            @SpringQueryMap @ParameterObject @Valid KakaoGetOrderReserveStatusRequestV1 params);





    @GetMapping("/api/v1/cash/balance")
    GiftResponseDto<? super KakaoGetCashBalanceResponseV1> getCashBalance(
            @RequestHeader("X-Kakao-AK") @NotBlank final String kakaoApiToken);






    @GetMapping("/api/v1/templates")
    GiftResponseDto<? super KakaoGetTemplateResponseV1> getTemplates(
            @RequestHeader("X-Kakao-AK") @NotBlank final String kakaoApiToken,
            @SpringQueryMap @ParameterObject @Valid KakaoGetTemplateRequestV1 params);

    @GetMapping("/api/v1/templates/{templateToken}")
    GiftResponseDto<? super KakaoGetTemplateDtlResponseV1> getTemplate(
            @RequestHeader("X-Kakao-AK") @NotBlank final String kakaoApiToken,
            @PathVariable("templateToken") @NotBlank final String templateToken);

    @GetMapping("/api/v1/templates/{templateToken}/send-history")
    GiftResponseDto<? super KakaoGetOrderDailyResponseV1> getSendHistoryDaily(
            @RequestHeader("X-Kakao-AK") @NotBlank final String kakaoApiToken,
            @PathVariable("templateToken") @NotBlank final String templateToken,
            @SpringQueryMap @ParameterObject @Valid KakaoGetOrderDailyRequestV1 params);
}

