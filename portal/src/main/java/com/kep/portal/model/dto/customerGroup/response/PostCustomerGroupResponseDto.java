package com.kep.portal.model.dto.customerGroup.response;

import com.kep.core.model.dto.ResponseDto;
import com.kep.core.model.dto.common.ResponseCode;
import com.kep.core.model.dto.common.ResponseMessage;
import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@Getter
public class PostCustomerGroupResponseDto extends ResponseDto {
    private PostCustomerGroupResponseDto() {
        super(ResponseCode.SUCCESS, ResponseMessage.SUCCESS);
    }

    public static ResponseEntity<PostCustomerGroupResponseDto> success() {
        PostCustomerGroupResponseDto result = new PostCustomerGroupResponseDto();
        return ResponseEntity.status(HttpStatus.OK).body(result);
    }

    public static ResponseEntity<ResponseDto> existedGroupName() {
        ResponseDto result = new ResponseDto(ResponseCode.DUPLICATED_DATA, ResponseMessage.DUPLICATED_DATA);
        return ResponseEntity.status(HttpStatus.CONFLICT).body(result);
    }
}
