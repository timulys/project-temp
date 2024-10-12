package com.kep.core.model.dto.system;

public enum SystemEventHistoryActionType {

    login //로그인
    , member_create //상담원 생성
    , member_update //상담원 수정
    , member_password //상담원 비밀번호 변경
    , system_counsel_work //근무 조건 설정
    , system_counsel_set //상담 환경 설정
    , system_channel //채널 설정 변경
    , system_counsel_auto_message //채널 설정 변경
    , system_counsel_distribution //상담 배분 설정
    , system_counsel_off_duty //근무시간 예외
    , system_counsel_member_max //최대 상담 건수 개별 설정
    , schedule_member_status_sync // 스케줄러를 활용한 사용자 온/오프라인 sync
}
