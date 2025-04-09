package com.kep.portal.service.notice;

import com.kep.portal.model.dto.notice.request.*;
import com.kep.portal.model.dto.notice.response.*;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface NoticeService {
    ResponseEntity<? super PostNoticeResponseDto> createNotice(PostNoticeRequestDto dto, List<MultipartFile> files);

    ResponseEntity<? super GetNoticeResponseDto> getNotice(Long noticeId);
    ResponseEntity<? super GetNoticeListResponseDto> getNoticeList(GetNoticeListRequestDto dto);

    ResponseEntity<? super PatchNoticeDisableResponseDto> updateNoticeDisable(PatchNoticeDisableRequestDto dto);
    ResponseEntity<? super PatchNoticeFixationResponseDto> updateNoticeFixation(PatchNoticeFixationRequestDto dto);
    ResponseEntity<? super PatchNoticeResponseDto> updateNotice(PatchNoticeRequestDto dto, List<MultipartFile> files);

    ResponseEntity<? super DeleteNoticeFileResponseDto> deleteNoticeFile(Long noticeUploadId);
}
