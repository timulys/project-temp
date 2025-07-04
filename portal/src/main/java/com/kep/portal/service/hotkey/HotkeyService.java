/**
 * Hotkey Service 신규
 *
 *  @생성일자      / 만든사람      / 수정내용
 *  2023.03.28 / asher.shin   / 신규
 */
package com.kep.portal.service.hotkey;

import java.util.List;

import javax.annotation.Resource;
import javax.transaction.Transactional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.kep.portal.model.dto.hotkey.HotkeyDto;
import com.kep.portal.model.entity.hotkey.Hotkey;
import com.kep.portal.model.entity.hotkey.HotkeyMapper;
import com.kep.portal.repository.hotkey.HotkeyRepository;
import com.kep.portal.util.SecurityUtils;

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

    public Page<HotkeyDto> getListHotkeyByMember(Pageable pageable) {

			Long memberId = securityUtils.getMemberId();

			List<Hotkey> hotkeyList = hotkeyRepository.findByMemberIdOrderBySortAsc(memberId, pageable);

			return new PageImpl<>(hotkeyMapper.map(hotkeyList));
    }

    /**
     * 
     *[2023.03.28 / asher.shin / 자주사용하는 문구 저장/수정] 
     * @return
     */
		@Transactional
    public List<HotkeyDto> store(Long memberId, HotkeyDto hotkeysDto) {
		Long index = 0L;
		for(HotkeyDto dto : hotkeysDto.getHotkeyList()) {
				Hotkey entity  = Hotkey.fromDto(dto, memberId);
				hotkeyRepository.save(entity);
		}

    	return this.getListHotkeyByMember(memberId);
    }

		public List<HotkeyDto> overwriteStore(HotkeyDto hotkeysDto) {
			Long memberId = securityUtils.getMemberId();
			hotkeyRepository.deleteByMemberId(memberId);
			hotkeyRepository.flush();

			return store(memberId, hotkeysDto);
		}

		public List<HotkeyDto> appendStore(HotkeyDto hotkeysDto) {
			Long memberId = securityUtils.getMemberId();

			return store(memberId, hotkeysDto);
		}

		@Transactional
		public void modifyStore(HotkeyDto hotkeysDto) {
			Long memberId = securityUtils.getMemberId();
			Hotkey entity  = Hotkey.fromDto(hotkeysDto, memberId);
			entity.setId(hotkeysDto.getId());
			hotkeyRepository.save(entity);
		}

	@Transactional
	public void deleteStore(Long id) {
		hotkeyRepository.deleteById(id);
	}

}
