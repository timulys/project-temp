package com.kep.core.model.dto.system;

public class SystemEnvEnum {

    /**
     * 사용 유무
     */
    public enum SwitchStatusType {
        on //사용
        ,off // 미사용
    }

    /**
     * 파일 전송 타입
     */
    public enum FileMimeType {
        all //전체
        , image //이미지
    }

    /**
     * 상담 유입경로 사용대상
     */
    public enum InflowPathType {
        official //공식 채널
        , unlimited // 제한없음
    }

    /**
     * 상담 종료
     */
    public enum IssueEndType {
        /**
         * 종료 예고 사용
         */
        notice,
        /**
         * 즉시 종료
         */
        instant
    }

    /**
     * 고객 연결 방식
     */
    public enum CustomerConnection {
        basic,category,custom
    }

    /**
     * 상담직원 배정 방식
     */
    public enum MemberAssign {
        basic , category , custom
    }

}
