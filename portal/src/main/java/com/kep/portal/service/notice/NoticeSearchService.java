package com.kep.portal.service.notice;

import com.kep.core.model.dto.ResponseDto;
import com.kep.core.model.dto.member.MemberDto;
import com.kep.core.model.dto.upload.UploadDto;
import com.kep.core.model.enums.MessageCode;
import com.kep.portal.config.property.SocketProperty;
import com.kep.portal.model.dto.notice.NoticeOpenType;
import com.kep.portal.model.dto.notice.request.*;
import com.kep.portal.model.dto.notice.response.*;
import com.kep.portal.model.entity.branch.Branch;
import com.kep.portal.model.entity.member.MemberMapper;
import com.kep.portal.model.entity.notice.*;
import com.kep.portal.model.entity.team.Team;
import com.kep.portal.model.entity.upload.Upload;
import com.kep.portal.model.entity.upload.UploadMapper;
import com.kep.portal.repository.member.MemberRepository;
import com.kep.portal.repository.notice.NoticeReadRepository;
import com.kep.portal.repository.notice.NoticeRepository;
import com.kep.portal.repository.notice.NoticeUploadRepository;
import com.kep.portal.repository.upload.UploadRepository;
import com.kep.portal.service.branch.BranchService;
import com.kep.portal.service.notification.NotificationService;
import com.kep.portal.service.team.TeamService;
import com.kep.portal.service.upload.UploadService;
import com.kep.portal.util.CommonUtils;
import com.kep.portal.util.MessageSourceUtil;
import com.kep.portal.util.SecurityUtils;
import com.kep.portal.util.UploadUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.transaction.Transactional;
import javax.validation.constraints.NotNull;
import java.io.File;
import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class NoticeSearchService {
	@Resource
	private NoticeRepository noticeRepository;

	@Resource
	private NoticeMapper noticeMapper;

	@Resource
	private MemberMapper memberMapper;

	@Resource
	private MemberRepository memberRepository;

	@Resource
	private SecurityUtils securityUtils;

	@Resource
	private BranchService branchService;

	@Resource
	private TeamService teamService;

	/**
	 * 상담관리 > 공지사항 목록 조회
	 *
	 *  @생성일자      / 만든사람		 	/ 수정내용
	 *	2023.04.04 / philip.lee7   / memberId 파라미터 추가
	 */
	public Page<NoticeResponseDto> getMangerList(String keyword, String type,@NotNull Pageable pageable) {
		Page<Notice> noticepPage =  noticeRepository.searchMangerList(keyword, type, securityUtils.getBranchId(), securityUtils.getMemberId(), pageable);

		// 조회된 목록으로 frontend에 필요한 데이터 가공
		List<NoticeResponseDto> dtos = new ArrayList<>();
		settingList(dtos, noticepPage.getContent());

		return new PageImpl<>(dtos, noticepPage.getPageable(), noticepPage.getTotalElements());
	}

	/**
	 * 공통 팝업 > 공지사항 목록 조회
	 */
	public Page<NoticeResponseDto> getList(String keyword, String type,@NotNull Pageable pageable, Boolean fixation) {

		// [2023.03.28 / philip.lee7 / MemberId 추가]
		Page<Notice> noticepPage =  noticeRepository.searchList(keyword, type, securityUtils.getBranchId(),securityUtils.getMemberId(), pageable , fixation);

		// 조회된 목록으로 frontend에 필요한 데이터 가공
		List<NoticeResponseDto> dtos = new ArrayList<>();
		settingList(dtos, noticepPage.getContent());

		return new PageImpl<>(dtos, noticepPage.getPageable(), noticepPage.getTotalElements());
	}

	/**
	 * 미확인 공지사항 목록 조회
	 * @return Long(Unread Count)
	 */
	public Long unreadNotice() {
		return noticeRepository.unreadNotice(securityUtils.getBranchId(), securityUtils.getMemberId() , securityUtils.getTeamId() ) ;
	}

	/**
	 * 리스트 정보 추출
	 *  2023.04.06 / philip.lee7   / newNotice 제외
	 */
	public void settingList(List<NoticeResponseDto> dtos, List<Notice> list){

		List<Long> branchId = list.stream().map(Notice::getBranchId)
				.collect(Collectors.toList());

		List<Branch> branches = branchService.findAllById(branchId);

		List<Long> teamId = list.stream().map(Notice::getTeamId)
				.collect(Collectors.toList());

		List<Team> teams = teamService.findAllById(teamId);

		for (Notice notice : list) {
			NoticeResponseDto dto = noticeMapper.mapResponse(notice);


			Optional<String> findBranchName = branches.stream().filter(item->item.getId().equals(notice.getBranchId()))
					.map(Branch::getName).findFirst();

			if(findBranchName.isPresent()){
				String branchName = findBranchName.get();
				dto.setBranchName(branchName);
			}


			Optional<String> findTeamName = teams.stream().filter(item->item.getId().equals(notice.getTeamId()))
					.map(Team::getName).findFirst();

			if(findTeamName.isPresent()){
				String teamName = findTeamName.get();
				dto.setTeamName(teamName);
			}

			if(notice.getOpenType() != null){
				dto.setOpenType(notice.getOpenType());
			}

			// 등록자 이름 정보 조회
			MemberDto memberDto = memberMapper.map(memberRepository.findById(notice.getCreator()).orElse(null));
			dto.setCreatorInfo(memberDto);
			// 오늘을 포함한 1주일(7일)으로 계산하기 위하여 날짜 계산 후 처리
			LocalDate weekDate = ZonedDateTime.now().minusDays(7).toLocalDate();
			dto.setNewNotice(weekDate.isBefore(dto.getCreated().toLocalDate()));

			dtos.add(dto);
		}
	}
}
