package com.kep.core.model.dto.platform.kakao.gift.constant;

/**
 * 선물 식별 ID 타입 상수
 *
 * @author volka
 */
public enum KakaoGiftIdType {

    EXTERNAL_ORDER_ID("연동사주문번호"),
    RESERVE_TRACE_ID("선포비주문번호"),
    GIFT_TRACE_ID("선포비선물번호"),
    UNDEFINED("미정의")
    ;

    private final String description;

    KakaoGiftIdType(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}
