package com.kep.portal.service.ai.impl;

import com.kep.core.model.dto.ResponseDto;
import com.kep.core.model.enums.MessageCode;
import com.kep.portal.model.dto.openai.response.GetChatResponseDto;
import com.kep.portal.model.entity.issue.IssueExtra;
import com.kep.portal.model.entity.issue.IssueLog;
import com.kep.portal.repository.customer.GuestRepository;
import com.kep.portal.repository.issue.IssueExtraRepository;
import com.kep.portal.repository.issue.IssueLogRepository;
import com.kep.portal.repository.issue.IssueRepository;
import com.kep.portal.service.ai.OpenAssistantService;
import com.kep.portal.util.AssistantUtils;
import com.kep.portal.util.MessageSourceUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class OpenAssistantServiceImpl implements OpenAssistantService {
    // Autowired Components
    private final AssistantUtils assistantUtils;
    private final MessageSourceUtil messageUtil;
    private final GuestRepository guestRepository;
    private final IssueRepository issueRepository;
    private final IssueLogRepository issueLogRepository;
    private final IssueExtraRepository issueExtraRepository;

    @Override
    public ResponseEntity<? super GetChatResponseDto> findSummaryAiAssistant(Long issueId) {
        boolean existedByIssueId = issueRepository.existsById(issueId);
        if(!existedByIssueId) return ResponseDto.databaseErrorMessage(messageUtil.getMessage(MessageCode.NOT_EXISTED_DATA));

        List<IssueLog> issueLogList = issueLogRepository.findAllByIssueId(issueId);
        if (issueLogList.size() == 0) return ResponseDto.databaseErrorMessage(messageUtil.getMessage(MessageCode.NOT_EXISTED_DATA));

        StringBuilder question = new StringBuilder();
        issueLogList.stream().forEach(issueLog -> question.append(issueLog.getPayload()));
        question.append("상담원의 입장에서 위 고객과의 대화 내용들 중 핵심을 뽑아서 3줄 이하로 줄바꿈해서 요약해줘.\n");
        question.append("'[요약]' 이라는 문구 아래 3줄로 요약한 내용을 붙여줘.\n");
        question.append("3줄 요약 이외에 추가적으로 고객의 핵심 요구사항 키워드를 '[핵심 키워드]' 문구 아래에 표시해줘.\n");
        question.append("그외 추가적으로 그 밑에 상담사의 답변 중 개선이 필요했던 부분을 '[개선사항]' 문구 아래에 안내해줘.\n");
        question.append("그리고 다음 분류 카테고리를 참고해서 대화 내용과 가장 잘 맞는 카테고리를 찾아서 '[카테고리 추천]'이라는 문구 아래에 표시해줘.\n");
        question.append("카테고리의 기준은 <분류>에 작성되어 있는 내용 안에서만 선택해야해.\n" +
                "\n" +
                "<분류예시>\n" +
                "1단계(대분류) > 2단계(중분류)> 3단계(소분류)\n" +
                "주문 및 배송 관련 > 주문 관리 > 주문 조회 및 관리\n" +
                "\n" +
                "<분류>\n" +
                "1단계(대분류) > 2단계(중분류)> 3단계(소분류)\n" +
                "주문 및 배송 관련 > 주문 관리 > 주문 조회 및 관리\n" +
                "주문 및 배송 관련 > 주문 관리 > 주문 변경 및 취소\n" +
                "주문 및 배송 관련 > 배송 > 배송 조회 및 관리\n" +
                "주문 및 배송 관련 > 배송 > 배송 옵션\n" +
                "주문 및 배송 관련 > 교환 및 환불 > 교환 및 환불 절차 안내\n" +
                "주문 및 배송 관련 > 교환 및 환불 > 반품 정책 안내\n" +
                "제품 및 서비스 지원 > 제품 관련 > 제품 지원\n" +
                "제품 및 서비스 지원 > 제품 관련 > 제품 재고 확인\n" +
                "제품 및 서비스 지원 > 제품 관련 > 부품 신청\n" +
                "제품 및 서비스 지원 > 서비스 관련 > IKEA 서비스\n" +
                "제품 및 서비스 지원 > 서비스 관련 > 헤이오더 (전화 주문 서비스)\n" +
                "제품 및 서비스 지원 > 서비스 관련 > 기프트 카드 잔액 확인\n" +
                "계정 및 기타 문의 > 계정 관리 > 비밀번호 재설정\n" +
                "계정 및 기타 문의 > 계정 관리 > 개인정보 처리방침\n" +
                "계정 및 기타 문의 > 자주 묻는 질문 > 자주 묻는 질문 찾기\n" +
                "계정 및 기타 문의 > 분실물 > 분실물 찾기");
        String answer;
        try {
            answer = assistantUtils.chatWithAssistant(String.valueOf(question));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return GetChatResponseDto.success(answer, messageUtil.success());
    }

    @Override
    public ResponseEntity<? super GetChatResponseDto> findAllSummaryAiAssistant(Long guestId) {
        boolean existedByGuestId = guestRepository.existsById(guestId);
        if (!existedByGuestId) return ResponseDto.noSearchData(messageUtil.getMessage(MessageCode.NO_SEARCH_DATA));

        List<IssueExtra> issueExtraList = issueExtraRepository.findAllByGuestIdAndSummaryNotNullOrderBySummaryModifiedDesc(guestId);
        if (issueExtraList == null || issueExtraList.isEmpty())
            return ResponseDto.databaseErrorMessage(messageUtil.getMessage(MessageCode.DATABASE_ERROR));

        StringBuilder question = new StringBuilder();
        issueExtraList.forEach(issueExtra -> question.append(issueExtra.getSummaryModified() + " : " + issueExtra.getSummary() + "\n"));
        question.append("\n");
        question.append("위 요약 내용을 일자별로 한 줄로 더 요약해줘. 양식은 'yyyy-MM-dd HH:mm:ss : 요약내용'형태를 지켜서 만들어줘");

        String answer;
        try {
            answer = assistantUtils.chatWithAssistant(String.valueOf(question));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return GetChatResponseDto.success(answer, messageUtil.success());
    }
}
