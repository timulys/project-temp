package com.kep.core.model.dto.platform.kakao.gift.constant;

/**
 * 템플릿 주문 상태 목록
 */
public enum KakaoReserveOrderStatus {
    WAIT,
    PROCESSING,
    ORDER_CREATE_FAILED,
    ORDER_CREATE_FAILED_BUSY,
    ORDER_CREATED,
    GIFT_CREATED,
    GIFT_ENDED,
    CANCELED,
    ORDER_TEMPLATE_NOT_FOUND,
    NOT_ENOUGH_CASH_BALANCE,
    INVALID_RECEIVER,
    CHANGE_ORDER_TEMPLATE_SNAPSHOT,
    EXCEED_BUDGET,
    DUPLICATE_TEMPLATE_ORDER
}
