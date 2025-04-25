package com.kep.core.model.dto.platform.kakao.gift.constant;

/**
 * 선물 취소 사유
 *
 * @author volka
 */
public enum KakaoGiftCancelReason {
    USER, //	사용자 취소 (선물 거절 등)
    CLIENT, //	고객사 취소
    API, //	고객사 선물취소 API를 통한 취소
    ADMIN, //	선물하기 for Biz 서비스 어드민 취소
    SYSTEM, //	수신자 발송 실패, 네트워크 오류 등으로 인한 시스템 자동취소
}
