/**
 * Hotkey Service 신규
 *
 *  @생성일자      / 만든사람      / 수정내용
 *  2023.03.28 / asher.shin   / 신규
 */
package com.kep.portal.service.hotkey;

import java.time.ZonedDateTime;
import java.util.List;

import javax.annotation.Resource;
import javax.transaction.Transactional;

import com.kep.core.model.exception.BizException;
import org.springframework.stereotype.Service;

import com.kep.portal.model.dto.hotkey.HotkeyDto;
import com.kep.portal.model.entity.hotkey.Hotkey;
import com.kep.portal.model.entity.hotkey.HotkeyMapper;
import com.kep.portal.repository.hotkey.HotkeyRepository;
import com.kep.portal.util.SecurityUtils;
import org.springframework.util.ObjectUtils;

@Service
@Transactional
public class HotkeyService {

	@Resource
	private SecurityUtils securityUtils;
	
    @Resource
    private HotkeyRepository hotkeyRepository;

    @Resource
    private HotkeyMapper hotkeyMapper;	
    /**
     * 직원의 자주사용하는 문구 리스트 받기
     * [2023.03.28 / asher.shin / 자주사용하는 리스트 추가]
     */
    public List<HotkeyDto> getListHotkeyByMember(Long memberId) {

		if (memberId == null) memberId = securityUtils.getMemberId();

		List<Hotkey> hotkeyList = hotkeyRepository.findByMemberIdOrderBySortAsc(memberId);

		return hotkeyMapper.map(hotkeyList);
    }
    
    /**
     * 
     *[2023.03.28 / asher.shin / 자주사용하는 문구 저장/수정] 
     * @return
     */
    public List<HotkeyDto> store(HotkeyDto hotkeysDto) {

		Long memberId = securityUtils.getMemberId();

		hotkeyRepository.deleteByMemberId(memberId);
		hotkeyRepository.flush();

		Long index = 0L;
		for(HotkeyDto dto : hotkeysDto.getHotkeyList()) {
				Hotkey entity  = Hotkey.builder().firstHotKey(dto.getFirstHotKey())
												 .secondHotKey(dto.getSecondHotKey())
												 .hotkeyCode(dto.getHotkeyCode())
												 .content(dto.getContent())
												 .memberId(memberId)
												 .modified(ZonedDateTime.now())
												 .enabled(true)
												 .sort(++index)
												 .created(ZonedDateTime.now())
												 .build();
				hotkeyRepository.save(entity);
		}

    	return this.getListHotkeyByMember(memberId);
    }
    
}
