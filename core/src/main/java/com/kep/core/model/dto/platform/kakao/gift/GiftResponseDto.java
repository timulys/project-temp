package com.kep.core.model.dto.platform.kakao.gift;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * TODO :: volka
 * TODO :: 모든 요청과 응답은 불변으로 사용.
 * TODO :: -> 현재 올웨이즈의 Jackson의 JSON 직렬화/역직렬화는 생성자를 이용한 직렬화/역직렬화를 사용하지 않으므로 @Data로 임시 사용 (중계 모듈은 record 사용중)
 * TODO :: -> 추후 선물하기 비즈니스가 정해질 때 올웨이즈에 맞게 리팩토링 하여 사용
 *
 * 카카오 선물하기 응답 DTO
 * @param <T>
 *
 * @author volka
 */
public class GiftResponseDto<T> {
    private final String code;
    private final String message;
    private final T data;

    protected GiftResponseDto(String code, String message) {
        this.code = code;
        this.message = message;
        this.data = null;
    }

    protected GiftResponseDto(String code, String message, T data) {
        this.code = code;
        this.message = message;
        this.data = data;
    }

    public static <T> GiftResponseDto<T> create(String code, String message) {
        return new GiftResponseDto<T>(code, message);
    }

    public static <T> GiftResponseDto<T> create(String code, String message, T data) {
        return new GiftResponseDto<T>(code, message, data);
    }

    public String getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }

    public T getData() {
        return data;
    }
}
