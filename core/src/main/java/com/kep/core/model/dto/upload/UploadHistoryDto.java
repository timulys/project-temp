package com.kep.core.model.dto.upload;

import com.fasterxml.jackson.annotation.JsonIncludeProperties;
import com.kep.core.model.dto.customer.GuestDto;
import com.kep.core.model.dto.member.MemberDto;
import com.kep.core.model.dto.subject.IssueCategoryDto;
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
public class UploadHistoryDto {

    @Positive
    private Long id;

    @Positive
    private Long issueId;

    private IssueCategoryDto issueCategory;

    private GuestDto guest;


    @NotNull
    private String questName;

    @NotNull
    private String fileName;

    @NotNull
    private String mimeType;

    @NotNull
    private Long size;

    @NotNull
    private String url;

    @NotNull
    private Long teamId;

    @NotNull
    @JsonIncludeProperties({"id","username","nickname"})
    private MemberDto creator;

    private ZonedDateTime created;
}
