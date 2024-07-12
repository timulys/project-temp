/**
 * Hotkey Dto 추가
 *
 *  @생성일자      / 만든사람      / 수정내용
 *  2023.03.28 / asher.shin   / 신규
 */
package com.kep.portal.model.dto.hotkey;

import java.time.ZonedDateTime;
import java.util.List;

import com.kep.portal.model.entity.hotkey.Hotkey;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 자주사용하는 문구 Dto
 *  [2023.03.28 / asher.shin / 자주사용하는 문구 Dto]
 *
 */
@Schema(description = "자주사용하는 문구 DTO")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class HotkeyDto {
	
	/**
	 * PK
	 */
    @Schema(description = "단축키 아이디")
    private Long id;

    /**
     * 단축키 조합 첫번째
     */
    @Schema(description = "단축키 조합 첫번째")
    private String firstHotKey;

    /**
     * 단축키 조합 두번째
     */
    @Schema(description = "단축키 조합 두번째")
    private String secondHotKey;

    /**
     * 두번째 조합키의 키코드
     */
    @Schema(description = "두번째 조합키의 키코드")
    private int hotkeyCode;

    /**
     * 사용할 문구 내용
     */
    @Schema(description = "사용할 문구 내용")
    private String content;

    /**
     * 사용하는 직원 PK
     */
    @Schema(description = "사용하는 직원 PK")
    private Long memberId;

    /**
     * 사용여부
     */
    @Schema(description = "사용여부")
    private boolean enabled;

    /**
     * 등록한 날짜
     */
    @Schema(description = "등록한 날짜")
    private ZonedDateTime created;

    /**
     * 수정한 날짜
     */
    @Schema(description = "수정한 날짜")
    private ZonedDateTime modified;

    @Schema(description = "순서 인덱스")
    private Long sort;

    /**
     * 저장/수정시 사용할 리스트
     */
    @Schema(description = "저장/수정시 사용할 리스트")
    private List<HotkeyDto> hotkeyList;
	
}
