package com.kep.portal.service.notice.impl;

import com.kep.core.model.dto.ResponseDto;
import com.kep.core.model.dto.member.MemberDto;
import com.kep.core.model.dto.upload.UploadDto;
import com.kep.core.model.enums.MessageCode;
import com.kep.portal.config.property.SocketProperty;
import com.kep.portal.model.dto.notice.NoticeOpenType;
import com.kep.portal.model.dto.notice.request.*;
import com.kep.portal.model.dto.notice.response.*;
import com.kep.portal.model.entity.member.MemberMapper;
import com.kep.portal.model.entity.notice.Notice;
import com.kep.portal.model.entity.notice.NoticeRead;
import com.kep.portal.model.entity.notice.NoticeReadPk;
import com.kep.portal.model.entity.notice.NoticeUpload;
import com.kep.portal.model.entity.upload.Upload;
import com.kep.portal.model.entity.upload.UploadMapper;
import com.kep.portal.repository.member.MemberRepository;
import com.kep.portal.repository.notice.NoticeReadRepository;
import com.kep.portal.repository.notice.NoticeRepository;
import com.kep.portal.repository.notice.NoticeUploadRepository;
import com.kep.portal.repository.upload.UploadRepository;
import com.kep.portal.service.notice.NoticeService;
import com.kep.portal.service.upload.UploadService;
import com.kep.portal.util.CommonUtils;
import com.kep.portal.util.MessageSourceUtil;
import com.kep.portal.util.SecurityUtils;
import com.kep.portal.util.UploadUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.transaction.Transactional;
import java.io.File;
import java.time.ZonedDateTime;
import java.util.List;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class NoticeServiceImpl implements NoticeService {
    /** Autowired Components  **/
    private final MemberMapper memberMapper;
    private final SecurityUtils securityUtils;
    private final SocketProperty socketProperty;
    private final MessageSourceUtil messageUtil;
    private final UploadRepository uploadRepository;
    private final NoticeRepository noticeRepository;
    private final MemberRepository memberRepository;
    private final NoticeReadRepository noticeReadRepository;
    private final NoticeUploadRepository noticeUploadRepository;

    private final SimpMessagingTemplate simpMessagingTemplate;

    private final UploadUtils uploadUtils;
    private final UploadMapper uploadMapper;
    private final UploadService uploadService;

    /** V2 Methods **/
    /**
     * 공지사항 등록
     * @param dto
     * @param files
     * @return
     */
    @Override
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
    @Override
    public ResponseEntity<? super GetNoticeListResponseDto> getNoticeList(GetNoticeListRequestDto dto) {
        return null;
    }

    /**
     * 공지사항 상세 조회
     * @param noticeId
     * @return
     */
    @Override
    public ResponseEntity<? super GetNoticeResponseDto> getNotice(Long noticeId) {
        boolean existedByNoticeId = noticeRepository.existsById(noticeId);
        if (!existedByNoticeId) return ResponseDto.noSearchData(messageUtil.getMessage(MessageCode.NO_SEARCH_DATA));

        Notice notice = noticeRepository.findById(noticeId).get();

        boolean existedByNoticeDtoGetMemberId = memberRepository.existsById(notice.getCreator());
        if (!existedByNoticeDtoGetMemberId) return ResponseDto.noSearchData(messageUtil.getMessage(MessageCode.NO_SEARCH_DATA));

        MemberDto memberDto = memberRepository.findById(notice.getCreator()).map(member -> memberMapper.map(member)).get();
        NoticeResponseDto noticeResponseDto = NoticeResponseDto.from(notice);
        noticeResponseDto.setCreatorInfo(memberDto);

        // 공지사항 읽은 시간 체크
        NoticeReadPk noticeReadPk = new NoticeReadPk(securityUtils.getMemberId(), noticeId);
        boolean isRead = noticeReadRepository.existsByNoticeReadPk(noticeReadPk);
        if (!isRead) noticeReadRepository.save(new NoticeRead(noticeReadPk, ZonedDateTime.now()));

        return GetNoticeResponseDto.success(noticeResponseDto, messageUtil.success());
    }

    /**
     * 공지사항 수정
     * @param dto
     * @param files
     * @return
     */
    @Override
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
    @Override
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

    /**
     * 공지사항 삭제(V2)
     * : 사용 안함 처리 로직(Patch)
     * @param dto
     * @return
     */
    @Override
    public ResponseEntity<? super PatchNoticeDisableResponseDto> updateNoticeDisable(PatchNoticeDisableRequestDto dto) {
        if (dto.getNoticeIdList() == null)
            return ResponseDto.validationFailedMessage(messageUtil.getMessage(MessageCode.VALIDATION_FAILED));

        List<Notice> noticeList = noticeRepository.findAllByIdIn(dto.getNoticeIdList());
        noticeList.forEach(notice -> notice.setEnabled(false));

        dto.setNoticeOpenType(NoticeOpenType.all);
        simpMessagingTemplate.convertAndSend(socketProperty.getNoticePath(), dto);

        return PatchNoticeDisableResponseDto.success(messageUtil.success());
    }

    /**
     * 공지사항 첨부 파일 삭제(V2)
     * TODO : 임시 메소드 -> 현재 사용하지 않음, 추후 비즈니스 식별 이후 호출하면 됨
     * @param noticeUploadId
     */
    @Override
    public ResponseEntity<? super DeleteNoticeFileResponseDto> deleteNoticeFile(Long noticeUploadId) {
        boolean existedByNoticeUploadId = noticeUploadRepository.existsById(noticeUploadId);
        if (!existedByNoticeUploadId) return ResponseDto.databaseErrorMessage(messageUtil.getMessage(MessageCode.DATABASE_ERROR));

        noticeUploadRepository.findById(noticeUploadId).ifPresent(noticeUpload -> {
            noticeUploadRepository.delete(noticeUpload);
            uploadService.delete(uploadMapper.map(noticeUpload.getUpload()));
        });

        return DeleteNoticeFileResponseDto.success(messageUtil.success());
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
