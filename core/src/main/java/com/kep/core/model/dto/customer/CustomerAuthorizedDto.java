package com.kep.core.model.dto.customer;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.kep.core.model.dto.platform.AuthorizeType;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import java.time.ZonedDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CustomerAuthorizedDto {

    @Positive
    @Schema(description = "고객 인가 아이디")
    private Long id;

    @Positive
    @JsonIgnore
    @Schema(description = "고객 아이디")
    private Long customerId;

    @Schema(description = "인가 타입(kakao_sync)")
    private AuthorizeType type;

    @Schema(description = "플랫폼 사용자 아이디")
    private String platformUserId;

    @Schema(description = "생성일시")
    private ZonedDateTime created;

}
