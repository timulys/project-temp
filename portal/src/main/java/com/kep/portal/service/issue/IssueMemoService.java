/**
 * IssueMemo Service 
 *
 *  @생성일자      / 만든사람		 	/  수정내용
 * 	2023.04.04 / philip.lee7    /  신규
 */

package com.kep.portal.service.issue;

import java.time.ZonedDateTime;
import java.util.List;

import javax.annotation.Resource;
import javax.transaction.Transactional;

import ch.qos.logback.classic.net.SimpleSocketServer;
import com.kep.portal.config.property.SocketProperty;
import com.kep.portal.model.entity.issue.IssueMapper;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import com.kep.core.model.dto.issue.IssueMemoDto;
import com.kep.portal.model.entity.issue.Issue;
import com.kep.portal.model.entity.issue.IssueMemo;
import com.kep.portal.model.entity.issue.IssueMemoMapper;
import com.kep.portal.model.entity.member.MemberMapper;
import com.kep.portal.repository.issue.IssueMemoRepository;
import com.kep.portal.repository.issue.IssueRepository;
import com.kep.portal.repository.member.MemberRepository;
import com.kep.portal.util.SecurityUtils;

import lombok.extern.slf4j.Slf4j;


@Service
@Transactional
@Slf4j
public class IssueMemoService {
	
	@Resource
	private IssueMemoRepository issueMemoRepository;
	
	@Resource
	private IssueRepository issueRepository;
	
	@Resource
	private IssueMemoMapper issueMemoMapper;
	
	@Resource
	private SecurityUtils securityUtils;
	
	@Resource
	private MemberRepository memberRepository;
	
	@Resource
	private MemberMapper memberMapper;
	
	@Resource
	private SimpMessagingTemplate simpMessagingTemplate;

	@Resource
	private SocketProperty socketProperty;

	@Resource
	private IssueMapper issueMapper;

	public List<IssueMemoDto> getMemoList(Long issueId) {
		
		Assert.notNull(issueId,"id is null");
		
		Issue issue = issueRepository.findById(issueId).orElse(null);
		
		Assert.notNull(issue,"issue entity is null");
		
		List<IssueMemo> entities = issueMemoRepository.findAllByIssueIdAndDeletedOrderByCreatedAsc(issue.getId(),false);
		
		
		return setMemberInfo(issueMemoMapper.map(entities));
	}
	

	/**
	 * 이슈메모 저장
	 * @param IssueMemoDto
	 * @return List
	 * @throws 
	 *
	 * @수정일자	  / 수정자		 	/ 수정내용
	 * 2023.04.04 / philip.lee7 / 신규
	 * 2023.04.10 / philip.lee7 / 저장 시 Issue 테이블에 memo pk 저장
	 */
	public List<IssueMemoDto> save(IssueMemoDto dto){
		
		Assert.notNull(dto,"dto is null");
		
		Assert.isTrue(dto.getMemberId().equals(securityUtils.getMemberId()),"memberId is not equal");
		
		IssueMemo entity = IssueMemo.builder().issueId(dto.getIssueId())
											 .memberId(securityUtils.getMemberId())
											 .guestId(dto.getGuestId())
											 .content(dto.getContent())
											 .created(ZonedDateTime.now())
											 .deleted(false)
											 .modified(ZonedDateTime.now())
											 .build();
		IssueMemo issueMemo = issueMemoRepository.save(entity);

		Assert.notNull(issueMemo,"save is failed");

		//메모 저장 후 issue 테이블에 메모 PK 저장 START
		Issue issue = issueRepository.findById(dto.getIssueId()).orElse(null);

		Assert.notNull(issue,"Issue is null");

		issue.setLastIssueMemo(issueMemo);

		issueRepository.save(issue);
		//메모 저장 후 issue 테이블에 메모 PK 저장 END

		simpMessagingTemplate.convertAndSend(socketProperty.getIssuePath(), issueMapper.map(issue));

		
		List<IssueMemo> entities = issueMemoRepository.findAllByIssueIdAndDeletedOrderByCreatedAsc(issueMemo.getIssueId(),false);
		
		return setMemberInfo(issueMemoMapper.map(entities));
	}
	

	/**
	 * 이슈메모 수정
	 * @param IssueMemoDto
	 * @return List
	 * @throws 
	 *
	 * @수정일자	  / 수정자		 	/ 수정내용
	 * 2023.04.04 / philip.lee7 / 신규
	 */
	public List<IssueMemoDto> update(IssueMemoDto dto){
		
		Assert.notNull(dto,"dto is null");
		
		IssueMemo entity  = issueMemoRepository.findById(dto.getId()).orElse(null);

		Assert.notNull(entity,"entity is null");
		
		entity.setContent(dto.getContent());
		entity.setMemberId(securityUtils.getMemberId());
		entity.setModified(ZonedDateTime.now());
		
		
		issueMemoRepository.save(entity);
		
		Assert.notNull(entity,"save is failed");
		
		
		List<IssueMemo> entities = issueMemoRepository.findAllByIssueIdAndDeletedOrderByCreatedAsc(entity.getIssueId(),false);
		
		return setMemberInfo(issueMemoMapper.map(entities));
	}
	

	/**
	 * 이슈메모 삭제
	 * @param IssueMemoDto
	 * @return List
	 * @throws 
	 *
	 * @수정일자	  / 수정자		 	/ 수정내용
	 * 2023.04.04 / philip.lee7 / 신규
	 * 2023.04.07 / philip.lee7	/  ids 파라미터추가(다중삭제 필요)
	 */
	public List<IssueMemoDto> delete(IssueMemoDto dto){
		
		Assert.notNull(dto,"dto is null");

		for(Long id : dto.getIds()) {
			IssueMemo entity = issueMemoRepository.findById(id).orElse(null);

			Assert.notNull(entity, "entity is null");

			Assert.isTrue(entity.getMemberId().equals(securityUtils.getMemberId()), "register Member not equal request Member");


			entity.setDeleted(true);
			entity.setModified(ZonedDateTime.now());


			issueMemoRepository.save(entity);

		}
		
		List<IssueMemo> entities = issueMemoRepository.findAllByIssueIdAndDeletedOrderByCreatedAsc(dto.getIssueId(), false);
			//메모 저장 후 issue 테이블에 메모 PK 저장 START
		Issue issue = null;
		if(entities.size()>0){

			issue = issueRepository.findById(dto.getIssueId()).orElse(null);

			Assert.notNull(issue,"Issue is null");

			issue.setLastIssueMemo(entities.get(entities.size()-1));

			issueRepository.save(issue);

		} else {
			issue = issueRepository.findById(dto.getIssueId()).orElse(null);

			Assert.notNull(issue,"Issue is null");

			issue.setLastIssueMemo(null);

			issueRepository.save(issue);
		}
		//메모 저장 후 issue 테이블에 메모 PK 저장 END


		simpMessagingTemplate.convertAndSend(socketProperty.getIssuePath(), issueMapper.map(issue));


		return setMemberInfo(issueMemoMapper.map(entities));
	}
	

	/**
	 * 멤버조회
	 * @param IssueMemoDto
	 * @return List
	 * @throws 
	 *
	 * @수정일자	  / 수정자		 	/ 수정내용
	 * 2023.04.04 / philip.lee7 / 신규
	 */
	public List<IssueMemoDto> setMemberInfo(List<IssueMemoDto>  entities) {
		entities.stream().forEach(t->t.setMember(memberMapper.map(memberRepository.findById(t.getMemberId()).orElse(null))));
		
		return entities;
	}
}
