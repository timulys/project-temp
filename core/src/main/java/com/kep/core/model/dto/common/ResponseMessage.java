package com.kep.core.model.dto.common;

public interface ResponseMessage {
    // Http Status 20x
    String SUCCESS = "Success";

    // Http Status 40x
    String VALIDATION_FAILED = "Validation Failed";
    String NOT_EXISTED_DATA = "Not Existed Data";
    String NO_PERMISSION = "Do not have permission";

    // Http Status 50x
    String DATABASE_ERROR = "Database Error";
    String BZM_CALL_FAILED = "BizMessageCenter Call Failed";
    String ALIM_TALK_CALL_FAILED = "AlimTalk Service Call Failed";
    String FRIEND_TALK_CALL_FAILED = "FriendTalk Service Call Failed";
    String FRIEND_TALK_UNAVAILABLE_TIME = "FriendTalk Service Unavailable Time";
    String SERVER_ERROR = "Server Error";
}
