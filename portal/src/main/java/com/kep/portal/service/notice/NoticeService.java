package com.kep.portal.service.notice;

import com.kep.core.model.dto.ResponseDto;
import com.kep.core.model.dto.member.MemberDto;
import com.kep.core.model.dto.upload.UploadDto;
import com.kep.core.model.enums.MessageCode;
import com.kep.portal.config.property.SocketProperty;
import com.kep.portal.model.dto.notice.NoticeDto;
import com.kep.portal.model.dto.notice.NoticeOpenType;
import com.kep.portal.model.dto.notice.request.GetNoticeListRequestDto;
import com.kep.portal.model.dto.notice.request.PatchNoticeFixationRequestDto;
import com.kep.portal.model.dto.notice.request.PatchNoticeRequestDto;
import com.kep.portal.model.dto.notice.request.PostNoticeRequestDto;
import com.kep.portal.model.dto.notice.response.*;
import com.kep.portal.model.dto.notice.NoticeUploadDto;
import com.kep.portal.model.entity.branch.Branch;
import com.kep.portal.model.entity.member.Member;
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
import org.springframework.util.Assert;
import org.springframework.util.ObjectUtils;
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
public class NoticeService {
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
	private NotificationService notificationService;

	// [2023.03.28 / philip.lee7 / 변수추가 START]
	@Resource
	private NoticeUploadRepository noticeUploadRepository;

	@Resource
	private NoticeReadRepository noticeReadRepository;

	@Resource
	private UploadService uploadService;

	@Resource
	private UploadMapper uploadMapper;

	@Resource
	private UploadUtils uploadUtils;
	// [2023.03.28 / philip.lee7 / 변수추가 END]

	@Resource
	private BranchService branchService;

	@Resource
	private TeamService teamService;

	@Resource
	private SocketProperty socketProperty;

	@Resource
	private SimpMessagingTemplate simpMessagingTemplate;

	/** Autowired Components **/
	private final MessageSourceUtil messageUtil;
    private final UploadRepository uploadRepository;

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

	public void destory(NoticeDto noticeDto){

		List<Notice> entities = noticeRepository.findAllByIdIn(noticeDto.getIds());

		List<Notice> notices = new ArrayList<>();
		for (Notice entity : entities){
			entity.setEnabled(false);
			notices.add(entity);
		}

		if(!notices.isEmpty()){
			noticeRepository.saveAll(notices);
			// 삭제의 경우 branch 여부를 확인 하기는 어려움 ( 다건 같이 삭제 가능 )
			// 해당 이슈로 인하여 noticeDto에 branch 오픈 범위 전체 공개로 소켓전송
			noticeDto.setOpenType(NoticeOpenType.all);
			simpMessagingTemplate.convertAndSend(socketProperty.getNoticePath() , noticeDto);
		}
	}

	/**
	 * 상담관리 > 공지사항 상세, 공통화면 > 공지사항 상세
	 */
	public NoticeResponseDto getDetail(@NotNull Long id){
		Notice notice = noticeRepository.findById(id).orElse(null);

		// [2023.03.28 / philip.lee7 / readNoticeFlag  추가]
		Assert.notNull(notice,"notice is null");

		this.readNoticeFlag(id);

		NoticeResponseDto dto = noticeMapper.mapResponse(notice);
		dto.setCreatorInfo(getMemberInfo(notice.getCreator()));

		return dto;
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

	public Page<NoticeResponseDto> fixations(Pageable pageable){
		Page<Notice> notices = noticeRepository.findAllByBranchIdAndEnabledAndFixation(securityUtils.getBranchId() , true , true , pageable);
		List<NoticeResponseDto> dtos = new ArrayList<>();
		settingList(dtos, notices.getContent());
		return new PageImpl<>(dtos, notices.getPageable(), notices.getTotalElements());
	}

	/**
	 * 미확인 공지사항 목록 조회
	 * @return Long(Unread Count)
	 */
	public Long unreadNotice() {
		// FIXME : 미확인 공지사항 목록 조회
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
			dto.setCreatorInfo(getMemberInfo(notice.getCreator()));
			// 오늘을 포함한 1주일(7일)으로 계산하기 위하여 날짜 계산 후 처리
			LocalDate weekDate = ZonedDateTime.now().minusDays(7).toLocalDate();
			dto.setNewNotice(weekDate.isBefore(dto.getCreated().toLocalDate()));

			dtos.add(dto);
		}
	}

	/**
	 * 이름 정보 호출
	 */
	public MemberDto getMemberInfo(Long memberId) {
		return memberMapper.map(memberRepository.findById(memberId).orElse(null));
	}


	/**
	 * 상담관리 > 공지사항 일괄삭제/일괄상단고정/일괄고정해제 처리
	 * @return NoticeDto
	 * 2023.03.28 / philip.lee7		/ store -> changeFixationAndEanbled 로 분리
	 */
	public NoticeDto changeFixationAndEnabled(NoticeDto noticeDto) {
		Notice notice = noticeMapper.map(noticeDto);

		if(!ObjectUtils.isEmpty(noticeDto.getIds())){
			for(Long id : noticeDto.getIds()){
				notice = noticeRepository.findById(id).orElse(null);

				Assert.notNull(notice, "notice can not be null");

				notice.setId(id);
				notice.setModifier(securityUtils.getMemberId());
				notice.setModified(ZonedDateTime.now());

				if(null != noticeDto.getEnabled()){
					notice.setEnabled(noticeDto.getEnabled());

					//TODO:삭제 시 파일 제거가 필요한가 확인후 작업

				} else if(null != noticeDto.getFixation()){
					notice.setFixation(noticeDto.getFixation());
				}

				noticeRepository.save(notice);
			}
		// 공지사항 등록/수정인 경우
		}

		return noticeMapper.map(notice);
	}


	/**
	 * 공지글 읽기체크
	 * @param id
	 * @return count
	 * 2023.03.28 / philip.lee7		/ 신규
	 */
	public int readNoticeFlag( @NotNull Long id ) {
		NoticeReadPk pk = new NoticeReadPk();
		pk.setMember_id(securityUtils.getMemberId());
		pk.setNotice_id(id);
		int count = noticeReadRepository.countByNoticeReadPk(pk);
		if(count ==0) {
			noticeReadRepository.save(new NoticeRead().builder().noticeReadPk(pk).readAt(ZonedDateTime.now()).build());
		}
		return count ;
	}

	/**
	 * 공지사항 파일삭제
	 * @return boolean
	 * 2023.03.28 / philip.lee7		/ 신규
	 */
	public boolean filedeleteOne(NoticeUploadDto dto) {

		Assert.notNull(dto,"dto is null");

		NoticeUpload noticeUpload = noticeUploadRepository.findById(dto.getId()).orElse(null);

		Assert.notNull(noticeUpload,"entity is null");

		noticeUploadRepository.delete(noticeUpload);

		return uploadService.delete(uploadMapper.map(noticeUpload.getUpload()));
	}

	/** V2 Methods **/
	// TODO : 추후 Interface와 Implement Class 분리 예정
	/**
	 * 공지사항 등록
	 * @param dto
	 * @param files
	 * @return
	 */
	public ResponseEntity<? super PostNoticeResponseDto> createNotice(PostNoticeRequestDto dto, List<MultipartFile> files) {
		Notice notice = Notice.create(dto.getTitle(), dto.getContent(), dto.getOpenType(), dto.getTeamId(), dto.getFixation());
		notice.setCreator(securityUtils.getMemberId());
		notice.setCreated(ZonedDateTime.now());
		notice.setBranchId(securityUtils.getBranchId());
		notice.setTeamId(dto.getTeamId());
		notice.setEnabled(true);

		Notice saveNotice = noticeRepository.save(notice);
		if (files != null && !files.isEmpty()) {
			uploadNoticeFiles(saveNotice, files);
		}

		return PostNoticeResponseDto.success(messageUtil.success());
	}

	// TODO : 추후 조회 서비스 개선이 필요하면 리팩토링 작업 진행할 것
	public ResponseEntity<? super GetNoticeListResponseDto> getNoticeList(GetNoticeListRequestDto dto) {
		return null;
	}

	/**
	 * 공지사항 상세 조회
	 * @param noticeId
	 * @return
	 */
	public ResponseEntity<? super GetNoticeResponseDto> getNotice(Long noticeId) {
		boolean existedByNoticeId = noticeRepository.existsById(noticeId);
		if (!existedByNoticeId) return ResponseDto.noSearchData(messageUtil.getMessage(MessageCode.NO_SEARCH_DATA));

		Notice notice = noticeRepository.findById(noticeId).get();

		boolean existedByNoticeDtoGetMemberId = memberRepository.existsById(notice.getCreator());
		if (!existedByNoticeDtoGetMemberId) return ResponseDto.noSearchData(messageUtil.getMessage(MessageCode.NO_SEARCH_DATA));

		MemberDto memberDto = memberRepository.findById(notice.getCreator()).map(member -> memberMapper.map(member)).get();
		NoticeResponseDto noticeResponseDto = NoticeResponseDto.from(notice);
		noticeResponseDto.setCreatorInfo(memberDto);

		return GetNoticeResponseDto.success(noticeResponseDto, messageUtil.success());
	}

	/**
	 * 공지사항 수정
 	 * @param dto
	 * @param files
	 * @return
	 */
	public ResponseEntity<? super PatchNoticeResponseDto> updateNotice(PatchNoticeRequestDto dto, List<MultipartFile> files) {
		boolean existedByNoticeId = noticeRepository.existsById(dto.getId());
		if (!existedByNoticeId) return ResponseDto.noSearchData(messageUtil.getMessage(MessageCode.NO_SEARCH_DATA));

		Notice newNotice = Notice.of(dto.getId(), dto.getTitle(), dto.getContent(), dto.getOpenType(), dto.getTeamId(), dto.getFixation());
		Notice notice = noticeRepository.findById(dto.getId()).get();
		CommonUtils.copyNotEmptyProperties(newNotice, notice);

		notice.setModifier(securityUtils.getMemberId());
		notice.setModified(ZonedDateTime.now());

		if (files != null && !files.isEmpty()) {
			List<NoticeUpload> noticeUploadList = noticeUploadRepository.findAllByNoticeId(notice.getId());
			noticeUploadList.forEach(noticeUpload -> {
				uploadRepository.deleteById(noticeUpload.getUpload().getId());
			});
			noticeUploadRepository.deleteAllByNoticeId(notice.getId());

			uploadNoticeFiles(notice, files);
		}

		return PatchNoticeResponseDto.success(messageUtil.success());
	}

	/**
	 * 공지사항 고정
	 * @param dto
	 * @return
	 */
	public ResponseEntity<? super PatchNoticeFixationResponseDto> updateNoticeFixation(PatchNoticeFixationRequestDto dto) {
		for (Long noticeId : dto.getIds()) {
			noticeRepository.findById(noticeId).ifPresent(notice -> {
				notice.setModifier(securityUtils.getMemberId());
				notice.setModified(ZonedDateTime.now());
                if (notice.getFixation() != null) {
                    notice.setFixation(dto.getFixation());
                }
            });
		}

		return PatchNoticeFixationResponseDto.success(messageUtil.success());
	}

	// Notice 업로드 파일 내부 메소드
	private void uploadNoticeFiles(Notice notice, List<MultipartFile> files) {
		files.forEach(multipartFile -> {
			File file = uploadUtils.upload(multipartFile);
			log.info("Upload File : {} ", multipartFile.getOriginalFilename());
			UploadDto storeUploadDto = uploadService.store(UploadDto.builder()
					.originalName(multipartFile.getOriginalFilename())
					.mimeType(uploadUtils.getMimeType(multipartFile))
					.type("notice")
					.build(), file);
			NoticeUpload noticeUpload = NoticeUpload.builder().upload(Upload.of(storeUploadDto)).notice(notice).build();
			noticeUploadRepository.save(noticeUpload);
		});
	}
}
