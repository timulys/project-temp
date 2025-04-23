package com.kep.portal.model.dto.branch.response;

import com.kep.core.model.dto.ResponseDto;
import com.kep.core.model.dto.common.ResponseCode;
import lombok.Getter;
import lombok.ToString;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@Getter
@ToString
public class PostBranchResponseDto extends ResponseDto {
    private PostBranchResponseDto(String message) {
        super(ResponseCode.SUCCESS, message);
    }

    public static ResponseEntity<PostBranchResponseDto> success(String message) {
        PostBranchResponseDto result = new PostBranchResponseDto(message);
        return ResponseEntity.status(HttpStatus.OK).body(result);
    }
}
