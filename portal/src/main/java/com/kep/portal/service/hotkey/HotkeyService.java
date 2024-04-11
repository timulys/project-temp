/**
 * Hotkey Service 신규
 *
 *  @생성일자      / 만든사람      / 수정내용
 *  2023.03.28 / asher.shin   / 신규
 */
package com.kep.portal.service.hotkey;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import javax.annotation.Resource;
import javax.transaction.Transactional;

import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

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
    private HotkeyRepository hotkeyrepository;

    @Resource
    private HotkeyMapper hotkeyMapper;	
    /**
     * 직원의 자주사용하는 문구 리스트 받기
     * [2023.03.28 / asher.shin / 자주사용하는 리스트 추가]
     */
    public List<HotkeyDto> getListHotkeyByMember(Long memberId) {
        
         List<Hotkey> hotkeyList = hotkeyrepository.findByMemberIdOrderBySortAsc(memberId);

		return hotkeyMapper.map(hotkeyList);
         
        
    }
    
    /**
     * 
     *[2023.03.28 / asher.shin / 자주사용하는 문구 저장/수정] 
     * @return
     */
    public List<HotkeyDto> store(HotkeyDto hotkeysDto,Long memberId) {
        
    	 Assert.notNull(hotkeysDto.getHotkeyList(), "List is null");
		 Assert.isTrue(memberId.equals(securityUtils.getMemberId()),"request MemberId not equal loginId");


    	 Hotkey entity = null;
		 Long index = 0L;
    	 for(HotkeyDto dto : hotkeysDto.getHotkeyList()) {
    		 
    		 if(dto.getId() == null) {
    			entity = Hotkey.builder()
				 		.firstHotKey(dto.getFirstHotKey())
				 		.secondHotKey(dto.getSecondHotKey())
				 		.hotkeyCode(dto.getHotkeyCode())
				 		.content(dto.getContent())
				 		.memberId(securityUtils.getMemberId())
				 		.modified(ZonedDateTime.now())
				 		.enabled(true)
						.sort(++index)
				 		.created(ZonedDateTime.now())
				 		.build();
    		 } else {
    			 entity = hotkeyrepository.findById(dto.getId()).orElse(null);
    			 Assert.notNull(entity,"hotkeyEntity is null");
    			 Assert.isTrue(entity.getMemberId().equals(memberId),"different MemberId");
				 entity.setSort(++index);
    			 entity.setContent(dto.getContent());
    			 entity.setModified(ZonedDateTime.now());
    			 entity.setEnabled(dto.isEnabled());
    		 }
    		   
    		 hotkeyrepository.save(entity);
    	 }
    	 
    	 return this.getListHotkeyByMember(securityUtils.getMemberId());

        
    }
    
}
