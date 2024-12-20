package com.kep.core.model.dto.work;

public class WorkType {

    /**
     * 근무 시간 TYPE
     */
    public enum Cases {
        branch // 시스템
        , member // 상담직원
    }

    /**
     * 근무 상태
     */
    public enum OfficeHoursStatusType {
        on //근무
        ,off // 오프
        ,rest // 휴식
        ,meal // 식사시간
    }

    /**
     * 최대 상담건수 type
     */
    public enum MaxCounselType {
        batch //일괄
        , individual //개별
    }

}
