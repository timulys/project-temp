package com.kep.portal.model.dto.member.response;

import com.kep.core.model.dto.ResponseDto;
import com.kep.core.model.dto.common.ResponseCode;
import com.kep.core.model.dto.member.MemberDto;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.ToString;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Collections;
import java.util.List;

@Getter
@ToString
public class GetMemberListResponseDto extends ResponseDto {
    @Schema(description = "상담원 목록")
    private final List<MemberDto> memberList;

    private GetMemberListResponseDto(List<MemberDto> memberList, String message) {
        super(ResponseCode.SUCCESS, message);
        this.memberList = memberList;
    }

    public static ResponseEntity<GetMemberListResponseDto> success(List<MemberDto> memberList, String message) {
        GetMemberListResponseDto result = new GetMemberListResponseDto(memberList, message);
        return ResponseEntity.status(HttpStatus.OK).body(result);
    }

    public static ResponseEntity<GetMemberListResponseDto> noSelectableManager(String message) {
        GetMemberListResponseDto result = new GetMemberListResponseDto(Collections.emptyList(), message);
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(result);
    }
}
