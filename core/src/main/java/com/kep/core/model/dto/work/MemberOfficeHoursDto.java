package com.kep.core.model.dto.work;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import javax.validation.constraints.Positive;
import java.time.ZonedDateTime;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MemberOfficeHoursDto {

    @Positive
    private Long id;

    @Positive
    @Schema(description = "유저 구분 아이디")
    private Long memberId;

    @Schema(description = "근무 시간 외 상담 여부")
    private Boolean offDutyCounselYn;

    @Schema(description = "근무 시작 시간")
    private String startCounselTime;
    @Schema(description = "근무 종료 시간")
    private String endCounselTime;
    @Schema(description = "상담 가능 요일")
    private String dayOfWeek;

    @Positive
    @Schema(description = "수정자")
    private Long modifier;
    @Schema(description = "수정 일시")
    private ZonedDateTime modified;

    @Positive
    @Schema(description = "생성자")
    public Long creator;
    @Schema(description = "생성 일시")
    public ZonedDateTime created;

}