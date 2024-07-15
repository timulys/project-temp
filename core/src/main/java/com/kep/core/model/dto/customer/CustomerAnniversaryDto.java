package com.kep.core.model.dto.customer;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import java.time.LocalDate;

@Schema(description = "고객 기념일")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CustomerAnniversaryDto {

    @Positive
    @Schema(description = "고객 기념일 아이디")
    private Long id;

    @Positive
    @JsonIgnore
    @Schema(description = "고객아이디")
    private Long customerId;

    @Schema(description = "기념일 타입 : (birthday , marriage , join)")
    private AnniversaryType type;

    @Schema(description = "기념명")
    private String anniversary;

}
