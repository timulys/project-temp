/**
 * Hotkey Mapper 추가
 *
 *  @생성일자      / 만든사람      / 수정내용
 *  2023.03.28 / asher.shin   / 신규
 */
package com.kep.portal.model.entity.hotkey;

import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

import com.kep.portal.model.dto.hotkey.HotkeyDto;

import java.util.List;

@Mapper(componentModel = "spring"
		, unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface HotkeyMapper {

	Hotkey map(HotkeyDto dto);
	
	HotkeyDto map(Hotkey entity);

	List<HotkeyDto> map(List<Hotkey> entities);
}
