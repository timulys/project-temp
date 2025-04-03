package com.kep.core.model.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum MessageCode {
    ALIM_TALK_CALL_FAILED("alim_talk_call_failed"),
    BZM_CALL_FAILED("bzm_call_failed"),
    DUPLICATED_DATA("duplicated_data"),
    DUPLICATED_CUSTOMER("duplicated_customer"),
    DATABASE_ERROR("database_error"),
    FRIEND_TALK_CALL_FAILED("friend_talk_call_failed"),
    FRIEND_TALK_UNAVAILABLE_TIME("friend_talk_unavailable_time"),
    NO_SEARCH_DATA("no_search_data"),
    NO_PERMISSION("no_permission"),
    NOT_EXISTED_DATA("not_existed_data"),
    NOT_EXISTED_MEMBER("not_existed_member"),
    NOT_EXISTED_CUSTOMER("not_existed_customer"),
    NOT_EXISTED_CUSTOMER_GROUP("not_existed_customer_group"),
    SUCCESS("success"),
    SERVER_ERROR("server_error"),
    VALIDATION_FAILED("validation_failed");

    private final String code;
}
