package com.kep.core.model.dto.member;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ManagerDto {

    @Positive
    private Long id;

    @NotEmpty
    private String username;

    @NotEmpty
    private String nickname;

    @NotNull
    private Boolean enabled;
}
