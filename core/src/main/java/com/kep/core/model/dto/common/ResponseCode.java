package com.kep.core.model.dto.common;

public interface ResponseCode {
    // Http Status 20x
    String SUCCESS = "SUC";

    // Http Status 40x
    String VALIDATION_FAILED = "VLF";
    String NOT_EXISTED_DATA = "NED";
    String NOT_EXISTED_MEMBER = "NEM";
    String NOT_EXISTED_CUSTOMER = "NEC";
    String NOT_EXISTED_CUSTOMER_GROUP = "NEG";
    String DUPLICATED_DATA = "DPD";
    String NO_PERMISSION = "NPE";

    // Http Status 50x
    String DATABASE_ERROR = "DBE";
    String BZM_CALL_FAILED = "BCF";
    String ALIM_TALK_CALL_FAILED = "ATF";
    String FRIEND_TALK_CALL_FAILED = "FTF";
    String SERVER_ERROR = "SVE";
}
