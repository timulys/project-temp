package com.kep.core.model.dto.platform.kakao.gift.constant;

/**
 * 카카오 선물하기 API 응답코드
 *
 * @author volka
 */
public enum KakaoResponseCode {
    INTERNAL_ERROR(-10000, "common", "일시적인 장애입니다. 다시 시도해 주세요."),
    VALIDATION_ERROR(-10001, "common", "유효하지 않은 요청입니다."),
    FORBIDDEN(-10003, "common", "사용자의 접근이 허용되지 않습니다."),
    NOT_FOUND_URL(-10015, "common", "존재하지 않는 주소가 호출되었습니다."),
    AUTH_REQUIRED(-11000, "authentication", "인증정보가 올바르지 않습니다. 확인 후 다시 시도해 주세요."),
    TOO_MANY_REQUEST(-10018, "common", "현재 요청이 많습니다. 잠시후 다시 시도해 주세요."),
    API_INVALID_REQUEST(-12003, "common_api", "잘못된 요청이 전달되었습니다. 요청정보를 확인해주세요."),
    API_FORBIDDEN_ERROR(-12004, "common_api", "사용자의 접근이 허용되지 않습니다."),
    API_SERVER_INTERNAL_ERROR(-12005, "common_api", "open-api quota 확인 오류"),
    ORDER_TEMPLATE_NOT_FOUND(-22501, "order_template", "존재하지 않는 템플릿입니다. 다시 확인해주세요."),
    NOT_ALIVE_TEMPLATE(-22505, "order_template", "사용할 수 없는 상태의 템플릿입니다. 템플릿을 확인해주세요."),
    NOT_ALLOWED_IP(-22506, "order_template", ""),
    TEMPLATE_ORDER_RESERVE_NOT_FOUND(-22509, "order_template", "해당 템플릿으로 주문한 내역을 찾을 수 없습니다."),
    ;

    private final int code;
    private final String category;
    private final String description;

    KakaoResponseCode(int code, String category, String description) {
        this.code = code;
        this.category = category;
        this.description = description;
    }

    public int getCode() {
        return code;
    }

    public String getCategory() {
        return category;
    }

    public String getDescription() {
        return description;
    }
}
