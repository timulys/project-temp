package com.kep.core.model.dto.platform.kakao.gift.constant;

/**
 * 카카오 선물하기 선물 상태
 *
 * @author volka
 */
public enum KakaoGiftStatus {
    REGISTERED,
    FAILED_SEND_BIZMESSAGE,
    SYSTEM_ERROR,
    COMPLETED,
    CANCELED,
    FAILOVER,
    CASHBACKED
}
