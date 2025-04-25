package com.kep.core.model.dto.platform.kakao.gift;

import org.springframework.util.MultiValueMap;

/**
 * HTTP Method GET
 */
public interface GiftExternalQueryRequest extends GiftExternalRequest {
    MultiValueMap<String, String> toMap();
}
