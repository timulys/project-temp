package com.kep.portal.model.dto.subject;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;


/**
 * FIXME ::  이슈 해결 KICA-430 용 DTO . 나중에 지워주세여
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class IssueCategoryOneDto {

    private Integer maxDepth;
    private List<IssueCategoryWithChannelDto> categories;
}
