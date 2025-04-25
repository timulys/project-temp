package com.kep.core.model.dto.platform.kakao.gift.constant;

/**
 * 선포비 템플릿 상태
 * REGISTERED: 대기중,ALIVE: 발송가능, EXPIRED: 만료, CLOSED: 정지
 *
 * @author volka
 */
public enum KakaoTemplateStatus {
//    UNLIMITED, // 무제한
//    LIMITED // 제한

    REGISTERED, // 대기중
    ALIVE, // 발송가능
    EXPIRED, // 만료
    CLOSED, // 정지
}
