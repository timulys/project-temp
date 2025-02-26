package com.kep.core.model.dto.platform.kakao.bizTalk.request.vo;

/**
 * 버튼 링크 타입
 * <li> AC : 채널추가(TemplateMessageType의 값이 AD, MI일때 자동 생성됨)
 * <li> WL : 웹링크 - name 필수, linkMo 필수, linkPc 선택
 * <li> AL : 앱링크 - name 필수, linkAnd/linkIos/linkMo 중 2가지 이상 필수, linkPc 선택
 * <li> BK : 봇키워드 - name 필수
 * <li> MD : 메시지전달 - name 필수
 * <li> BC : 상담톡전환 - name 필수
 * <li> BF : 비즈니스폼 biz_form_key or biz_form_id 하나 필수 입력 (친구톡 key만 사용가능, 알림톡 id만 사용가능)
 * <li> BT : 봇전환 - name 필수
 * <li> DS : 배송조회 - name 필수
 * <li> P1 : 이미지 보안 전송 플러그인
 * <li> P2 : 개인정보이용 플러그인
 * <li> P3 : 원클릭 결제 플러그인
 */
public enum ButtonType {
    AC,
    WL,
    AL,
    DS,
    BK,
    MD,
    BC,
    BT,
    P1,
    P2,
    P3
}