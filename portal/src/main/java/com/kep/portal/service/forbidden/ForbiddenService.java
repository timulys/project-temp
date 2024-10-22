/**
 * Forbidden Service 추가
 *
 * @수정일자	  / 수정자			 	/ 수정내용
 * 2023.03.28 / asher.shin    	/ 신규
 */
package com.kep.portal.service.forbidden;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kep.core.model.exception.BizException;
import com.kep.portal.model.dto.forbidden.ForbiddenDto;
import com.kep.portal.model.entity.forbidden.Forbidden;
import com.kep.portal.model.entity.forbidden.ForbiddenMapper;
import com.kep.portal.repository.forbidden.ForbiddenRepository;
import com.kep.portal.util.SecurityUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import javax.annotation.Resource;
import javax.transaction.Transactional;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.regex.Pattern;
import java.util.stream.IntStream;

@Service
@Transactional
@Slf4j
public class ForbiddenService {
	
	@Resource
	private ForbiddenRepository forbiddenRepository;
	
	@Resource
	private ForbiddenMapper forbiddenMapper;
	
	@Resource
	private SecurityUtils securityUtils;


	@Resource
	private ObjectMapper objectMapper;

	private static final Pattern forbiddenRegexPattern = Pattern.compile("^[ㄱ-ㅎㅏ-ㅣ가-힣a-zA-Z0-9]*$");
	
	/**
	 * 금칙어 리스트 가져오기
	 */
	public List<ForbiddenDto> getList(){
		
		return forbiddenMapper.map(forbiddenRepository.findAll(Sort.by(Sort.Direction.ASC,"word")));
	}
	
	
	/**
	 * 금칙어 저장
	 * @param dto
	 * @return
	 */
	public List<ForbiddenDto> save(ForbiddenDto dto) {
		
		Assert.notNull(dto,"dto is Null");
		Assert.notNull(dto.getWord(),"word is Null");

		String word = dto.getWord();

		if (!forbiddenRegexPattern.matcher(word).matches()) throw new BizException("forbidden words can be korean, english, numbers");
		if (forbiddenRepository.findByWord(word).isPresent()) throw new BizException("already exists word");

		//특수문제 제거
//		String word = dto.getWord().replaceAll("[^ㄱ-ㅎㅏ-ㅣ가-힣a-zA-Z0-9]" , "");

		forbiddenRepository.save(Forbidden.builder().word(word)
							.memberId(securityUtils.getMemberId())
							.created(ZonedDateTime.now()).build());

		forbiddenRepository.flush();

		return this.getList();
		
	}
	
	/**
	 * 금칙어 삭제
	 * @param dto
	 */
	public List<ForbiddenDto> delete(ForbiddenDto dto) {
			
		
		Forbidden forbidden = forbiddenRepository.findById(dto.getId()).orElse(null);
		
		Assert.notNull(forbidden,"entity is null");
		
		forbiddenRepository.delete(forbidden);
		
		return this.getList();
	}
	
	/**
	 * 금칙어 마스킹 처리
	 * @param content
	 * @return
	 */
	public String changeForbiddenToMasking(String content) {

		if(content == null)
			return content;
		
		List<Forbidden> forbiddenWordList = forbiddenRepository.findAll();

		for( Forbidden forbidden: forbiddenWordList) {
			content = content.replaceAll(forbidden.getWord(),wordLengthMasking(forbidden.getWord()));
		}
		
		return content;
	}
	
	public String wordLengthMasking(String word) {
		StringBuffer buffer = new StringBuffer();
		
		IntStream.range(0, word.length()).forEach(i->buffer.append("*"));
		
		return buffer.toString();
	}
}
