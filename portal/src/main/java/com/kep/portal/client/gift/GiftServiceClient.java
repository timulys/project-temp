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
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import org.springdoc.api.annotations.ParameterObject;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.cloud.openfeign.SpringQueryMap;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import java.util.List;


@FeignClient(name = "gift-service", url = "${spring.cloud.discovery.client.simple.instances.gift-service[0].uri}")
public interface GiftServiceClient {

    @PostMapping("/api/v1/gifts/find")
    @ResponseStatus(HttpStatus.OK)
    GiftResponseDto<List<KakaoPostOrderGiftResponseV3>> findOrderGifts(
            @Parameter(in = ParameterIn.HEADER, description = "카카오 인증 API KEY")
            @RequestHeader("X-Kakao-AK") @NotBlank final String kakaoApiToken,
            @Valid @RequestBody KakaoPostOrderGiftRequestV3 request);

    @PutMapping("/api/v1/gifts")
    @ResponseStatus(HttpStatus.OK)
    GiftResponseDto<List<KakaoPostCancelGiftResponseV1>> cancelOrderGift(
            @Parameter(in = ParameterIn.HEADER, description = "카카오 인증 API KEY")
            @RequestHeader("X-Kakao-AK") @NotBlank final String kakaoApiToken,
            @RequestBody @Valid KakaoPostCancelGiftRequestV1 request);

    @PostMapping("/api/v1/gifts")
    @ResponseStatus(HttpStatus.OK)
    GiftResponseDto<KakaoPostOrderResponseV1> send(
            @Parameter(in = ParameterIn.HEADER, description = "카카오 인증 API KEY")
            @RequestHeader("X-Kakao-AK") @NotBlank final String kakaoApiToken,
            @Valid @RequestBody KakaoPostOrderRequestV1 request);

    @GetMapping("/api/v1/gifts/complete")
    @ResponseStatus(HttpStatus.OK)
    GiftResponseDto<KakaoGetOrderCompleteResponseV3> getOrderComplete(
            @Parameter(in = ParameterIn.HEADER, description = "카카오 인증 API KEY")
            @RequestHeader("X-Kakao-AK") @NotBlank final String kakaoApiToken,
            @SpringQueryMap @ParameterObject @Valid KakaoGetOrderCompleteRequestV3 params);

    @GetMapping("/api/v1/gifts/status")
    @ResponseStatus(HttpStatus.OK)
    GiftResponseDto<KakaoGetOrderReserveStatusResponseV1> getOrderReserveStatus(
            @Parameter(in = ParameterIn.HEADER, description = "카카오 인증 API KEY")
            @RequestHeader("X-Kakao-AK") @NotBlank final String kakaoApiToken,
            @SpringQueryMap @ParameterObject @Valid KakaoGetOrderReserveStatusRequestV1 params);





    @GetMapping("/api/v1/cash/balance")
    @ResponseStatus(HttpStatus.OK)
    GiftResponseDto<KakaoGetCashBalanceResponseV1> getCashBalance(
            @Parameter(in = ParameterIn.HEADER, description = "카카오 인증 API KEY")
            @RequestHeader("X-Kakao-AK") @NotBlank final String kakaoApiToken);






    @GetMapping("/api/v1/templates")
    @ResponseStatus(HttpStatus.OK)
    GiftResponseDto<KakaoGetTemplateResponseV1> getTemplates(
            @Parameter(in = ParameterIn.HEADER, description = "카카오 인증 API KEY")
            @RequestHeader("X-Kakao-AK") @NotBlank final String kakaoApiToken,
            @ParameterObject @Valid KakaoGetTemplateRequestV1 params);

    @GetMapping("/api/v1/templates/{templateToken}")
    @ResponseStatus(HttpStatus.OK)
    GiftResponseDto<KakaoGetTemplateDtlResponseV1> getTemplates(
            @Parameter(in = ParameterIn.HEADER, description = "카카오 인증 API KEY")
            @RequestHeader("X-Kakao-AK") @NotBlank final String kakaoApiToken,
            @PathVariable("templateToken") @NotBlank final String templateToken);

    @GetMapping("/api/v1/templates/{templateToken}/send-history")
    @ResponseStatus(HttpStatus.OK)
    GiftResponseDto<KakaoGetOrderDailyResponseV1> getSendHistoryDaily(
            @Parameter(in = ParameterIn.HEADER, description = "카카오 인증 API KEY")
            @RequestHeader("X-Kakao-AK") @NotBlank final String kakaoApiToken,
            @PathVariable("templateToken") @NotBlank final String templateToken,
            @ParameterObject @Valid KakaoGetOrderDailyRequestV1 params);
}
