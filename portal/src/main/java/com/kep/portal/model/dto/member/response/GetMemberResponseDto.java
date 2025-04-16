package com.kep.portal.model.dto.member.response;

import com.kep.core.model.dto.ResponseDto;
import com.kep.core.model.dto.common.ResponseCode;
import com.kep.core.model.dto.member.MemberDto;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.ToString;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@Getter
@ToString
public class GetMemberResponseDto extends ResponseDto {
    @Schema(description = "고객 정보")
    private final MemberDto member;

    private GetMemberResponseDto(MemberDto member, String message) {
        super(ResponseCode.SUCCESS, message);
        this.member = member;
    }

    public static ResponseEntity<GetMemberResponseDto> success(MemberDto member, String message) {
        GetMemberResponseDto result = new GetMemberResponseDto(member, message);
        return ResponseEntity.status(HttpStatus.OK).body(result);
    }
}
