package com.kep.portal.service.issue.event;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.kep.core.model.dto.channel.ChannelEnvDto;
import com.kep.core.model.dto.issue.*;
import com.kep.core.model.dto.issue.payload.IssuePayload;
import com.kep.core.model.dto.system.SystemEnvEnum;
import com.kep.portal.model.dto.notification.*;
import com.kep.portal.client.PlatformClient;
import com.kep.portal.config.property.CoreProperty;
import com.kep.portal.config.property.PortalProperty;
import com.kep.portal.config.property.SocketProperty;
import com.kep.portal.config.property.SystemMessageProperty;
import com.kep.portal.model.dto.notification.NotificationInfoDto;
import com.kep.portal.model.entity.channel.ChannelEndAuto;
import com.kep.portal.model.entity.env.CounselInflowEnv;
import com.kep.portal.model.entity.issue.*;
import com.kep.portal.model.entity.member.Member;
import com.kep.portal.model.type.IssueStorageType;
import com.kep.portal.scheduler.SendDelayFirstReplyJob;
import com.kep.portal.service.assign.AssignConsumer;
import com.kep.portal.service.channel.ChannelEnvService;
import com.kep.portal.service.issue.IssueLogService;
import com.kep.portal.service.issue.IssueService;
import com.kep.portal.service.issue.IssueStorageService;
import com.kep.portal.service.notification.NotificationService;
import com.kep.portal.util.SecurityUtils;
import com.querydsl.core.Tuple;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import javax.annotation.Nullable;
import javax.annotation.Resource;
import javax.transaction.Transactional;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

import static com.kep.portal.model.entity.channel.QChannelEndAuto.channelEndAuto;
import static com.kep.portal.model.entity.issue.QIssue.issue;

/**
 * 시스템 이벤트, 로직 도중 호출되므로 Exception 던지지 말 것
 */
@Slf4j
@Service
@Transactional
public class EventBySystemService {

	@Resource
	private ChannelEnvService channelEnvService;
	@Resource
	private CoreProperty coreProperty;
	@Resource
	private SocketProperty socketProperty;
	@Resource
	private PortalProperty portalProperty;
	@Resource
	private SimpMessagingTemplate simpMessagingTemplate;
	@Resource
	private PlatformClient platformClient;
	@Resource
	private IssueService issueService;
	@Resource
	private IssueStorageService issueStorageService;
	@Resource
	private IssueMapper issueMapper;
	@Resource
	private IssueLogService issueLogService;
	@Resource
	private IssueLogMapper issueLogMapper;
	@Resource
	private ObjectMapper objectMapper;

	@Resource
	private SecurityUtils securityUtils;

	@Resource
	private SystemMessageProperty systemMessageProperty;

	@Resource
	private NotificationService notificationService;

	// ////////////////////////////////////////////////////////////////////////
	// 시스템 관리 > 상담 시작 설정 적용
	// ////////////////////////////////////////////////////////////////////////
	/**
	 * 자동메세지, 배정대기 (상담접수) 안내 상담직원이 최대상담건수 진행 중으로 즉시 배정이 어려워 상담톡이 '배정대기' 상태로 시작된 경우
	 * 아래의 메시지를 발송할 지 결정합니다.
	 */
	public void sendOpened(@NotNull Issue issue) {

		log.info("SEND OPENED, ISSUE: {}", issue.getId());

		ChannelEnvDto channelEnv = channelEnvService.getByChannel(issue.getChannel());
		try {
			log.info("SEND OPENED, CONFIG: {}", channelEnv.getStart().getUnable());
			boolean enabled = channelEnv.getStart().getUnable().getEnabled();
			if (enabled) {
				IssuePayload issuePayload = channelEnv.getStart().getUnable().getMessage();
				String payload = objectMapper.writeValueAsString(issuePayload);
				IssueLog issueLog = saveSystemMessage(issue, payload);
				// 플랫폼으로 이벤트 전송
				platformClient.message(issueMapper.map(issue), issueLogMapper.map(issueLog));
			}
		} catch (Exception e) {
			log.error(e.getLocalizedMessage(), e);
		}
	}

	/**
	 * 자동메세지, 배정대기 (상담접수, Issue.status == open) / 상담대기 (배정완료, Issue.status == assign) 중 고객메세지 자동응답
	 */
	public void sendReplyWhenOpenedAndAssigned(@NotNull Issue issue) {

		log.info("SEND REPLY WHEN OPENED AND ASSIGNED, ISSUE: {}", issue.getId());

		ChannelEnvDto channelEnv = channelEnvService.getByChannel(issue.getChannel());
		try {
			// FIXME : Absence는 상담원 부재로 메시지를 보낼 수 없는 경우에 해당하는데 현 시점에 이 메시지가 맞는지는 확인이 필요함
			log.info("SEND REPLY WHEN OPENED AND ASSIGNED, CONFIG: {}", channelEnv.getStart().getAbsence());
			boolean enabled = channelEnv.getStart().getAbsence().getEnabled();
			if (enabled) {
				IssuePayload issuePayload = channelEnv.getStart().getAbsence().getMessage();
				String payload = objectMapper.writeValueAsString(issuePayload);
				IssueLog issueLog = saveSystemMessage(issue, payload);
				sendToPlatformAndSocket(issue, issueLog);
			}
		} catch (Exception e) {
			log.error(e.getLocalizedMessage(), e);
		}
	}

	/**
	 * 자동메세지, 상담대기 (배정완료, Issue.status == assign) 안내, Called by
	 * {@link AssignConsumer}

	 */
	public void sendAssigned(@NotNull Issue issue) {
		log.info("SEND ASSIGNED, ISSUE: {}", issue.getId());
		ChannelEnvDto channelEnv = channelEnvService.getByChannel(issue.getChannel());
		try {
			log.info("SEND ASSIGNED, CONFIG: {}", channelEnv.getStart().getWaiting());
			boolean enabled = channelEnv.getStart().getWaiting().getEnabled();
			if (enabled) {
				IssuePayload issuePayload = channelEnv.getStart().getWaiting().getMessage();
				String payload = objectMapper.writeValueAsString(issuePayload);
				IssueLog issueLog = saveSystemMessage(issue, payload);
				sendToPlatformAndSocket(issue, issueLog);
			}
		} catch (Exception e) {
			log.error(e.getLocalizedMessage(), e);
		}
	}

	/**
	 * 상담시작 인사말 (공통 / 상담사), 플랫폼/소켓 전송은 안함
	 */
	@Nullable
	public IssueLog saveWelcome(@NotNull Issue issue) {

		log.info("SAVE WELCOME, ISSUE: {}", issue.getId());

		ChannelEnvDto channelEnv = channelEnvService.getByChannel(issue.getChannel());
		try {
			log.info("SEND WELCOME, CONFIG: {}", channelEnv.getStart().getWelcom());
			boolean enabled = channelEnv.getStart().getWelcom().getEnabled();
			IssuePayload issuePayload = null;
			// TODO: 현재 시스템 인사말 설정이 비활성화 된 경우 user가 활성화 한 인사말이 작동되도록 되어있음 우선 순위 확인 필요해보임
			if (enabled) {
				issuePayload = channelEnv.getStart().getWelcom().getMessage();
			} else if (Objects.nonNull(issue.getMember()) && issue.getMember().getUsedMessage()) {
				Member member = issue.getMember();
				issuePayload = member.getFirstMessage();
			}

			if (issuePayload != null) {
				String payload = objectMapper.writeValueAsString(issuePayload);

				if (issue.getMember() != null && !ObjectUtils.isEmpty(issue.getMember().getNickname())) {
					// TODO: 파라미터 정리 필요
					payload = payload.replace("{{상담직원명}}", issue.getMember().getNickname());
				}
				return saveSystemMessage(issue, payload);
			}
		} catch (Exception e) {
			log.error(e.getLocalizedMessage(), e);
		}

		return null;
	}

	// ////////////////////////////////////////////////////////////////////////
	// 시스템 관리 > 상담 종료 설정 적용
	// ////////////////////////////////////////////////////////////////////////
	/**
	 * 근무 외 시간 상담 접수 사용
	 */
	public void sendOfficeHours(@NotNull Issue issue) {

		log.info("SEND OFFICE HOURS, ISSUE: {}", issue.getId());

		ChannelEnvDto channelEnv = channelEnvService.getByChannel(issue.getChannel());
		try {
			log.info("SEND OFFICE HOURS, CONFIG: {}", channelEnv.getEnd().getRegister());

			boolean enabled = channelEnv.getEnd().getRegister().getEnabled();
			 //on 일시 설정된 메시지
			if(enabled) {
				IssuePayload issuePayload = channelEnv.getEnd().getRegister().getMessage();
				String payload = objectMapper.writeValueAsString(issuePayload);
				IssueLog issueLog = saveSystemMessage(issue, payload);
				if(issue.getAssignCount() < 2){
					platformClient.message(issueMapper.map(issue), issueLogMapper.map(issueLog));
				}

			} else { //off 시스템 메시지
				this.sendDisabledAndClose(issue);
			}
		} catch (Exception e) {
			log.error(e.getLocalizedMessage(), e);
		}
	}

	/**
	 * 상담대기 중 상담직원응답 지연 안내, Called by Scheduler ({@link SendDelayFirstReplyJob})
	 */
	public void sendDelayFirstReply(@NotNull Issue issue, @NotNull @Valid ChannelEnvDto channelEnv) {

		log.info("SEND DELAY FIRST REPLY, ISSUE: {}, CONFIG: {}", issue.getId(), channelEnv.getEnd().getMemberDelay());

		try {
			IssuePayload issuePayload = channelEnv.getEnd().getMemberDelay().getMessage();
			String payload = objectMapper.writeValueAsString(issuePayload);
			IssueLog issueLog = saveSystemMessage(issue, payload);
			sendToPlatformAndSocket(issue, issueLog);

		} catch (Exception e) {
			log.error(e.getLocalizedMessage(), e);
		}
	}

	private void sendToPlatformAndSocket(@NotNull Issue issue, @NotNull IssueLog issueLog) {

		issueService.joinIssueSupport(issue);
		IssueDto issueDto = issueMapper.map(issue);
		IssueLogDto issueLogDto = issueLogMapper.map(issueLog);

		try {
			// 플랫폼으로 이벤트 전송
			platformClient.message(issueMapper.map(issue), issueLogDto);
			// 소켓으로 이슈 전송
			simpMessagingTemplate.convertAndSend(socketProperty.getIssuePath(), issueDto);
			// 소켓으로 이벤트 전송
			simpMessagingTemplate.convertAndSend(socketProperty.getIssuePath() + "." + issue.getId() + "." + "message", issueLogDto);
		} catch (Exception e) {
			log.error(e.getLocalizedMessage(), e);
		}
	}

	/**
	 * 고객응답 지연 자동종료 사용, Called by Scheduler (SendDelayGuestJob) Issue.status ==
	 * IssueStatus.reply
	 */
	@Deprecated
	public void sendDelayGuest(@NotNull Issue issue, @NotNull @Valid ChannelEnvDto channelEnv) {

		log.info("SEND DELAY GUEST, ISSUE: {}, CONFIG: {}", issue.getId(), channelEnv.getEnd().getGuestDelay());

		try {
			IssuePayload issuePayload = channelEnv.getEnd().getGuestDelay().getMessage();
			String payload = objectMapper.writeValueAsString(issuePayload);
			IssueLog issueLog = saveSystemMessage(issue, payload);
			sendToPlatformAndSocket(issue, issueLog);
		} catch (Exception e) {
			log.error(e.getLocalizedMessage(), e);
		}
	}

	/**
	 * 고객응답 지연 자동종료 메세지, Called by Scheduler (SendDelayGuestJob) Issue.status ==
	 * IssueStatus.reply
	 */
	@Deprecated
	public IssueLog getDelayGuest(@NotNull Issue issue, @NotNull @Valid ChannelEnvDto channelEnv) {

		log.info("SAVE DELAY GUEST, ISSUE: {}, CONFIG: {}", issue.getId(), channelEnv.getEnd().getGuestDelay());

		try {
			IssuePayload issuePayload = channelEnv.getEnd().getGuestDelay().getMessage();
			String payload = objectMapper.writeValueAsString(issuePayload);
			return saveSystemMessage(issue, payload);
		} catch (Exception e) {
			log.error(e.getLocalizedMessage(), e);
		}

		return null;
	}

	/**
	 * 고객응답 지연 자동종료 예고 사용, Called by Scheduler (SendWarningDelayGuestJob)
	 * Issue.status == IssueStatus.reply
	 */
	public void sendWarningDelayGuest(@NotNull Issue issue, @NotNull @Valid ChannelEnvDto channelEnv) {

		log.info("SEND WARNING DELAY GUEST, ISSUE: {}, CONFIG: {}", issue.getId(), channelEnv.getEnd().getGuestNoticeDelay());

		try {
			IssuePayload issuePayload = channelEnv.getEnd().getGuestNoticeDelay().getMessage();
			String payload = objectMapper.writeValueAsString(issuePayload);
			IssueLog issueLog = saveSystemMessage(issue, payload);
			sendToPlatformAndSocket(issue, issueLog);
		} catch (Exception e) {
			log.error(e.getLocalizedMessage(), e);
		}
	}

	/**
	 * 고객 인증 요청 안내 메시지, Called By Operator
	 */
	public void sendSync(Issue issue, CounselInflowEnv counselInflowEnv) {
		String payload = "";
        try {
			log.info("Send Customer Kakao-Sync, Issue ID : {}", issue.getId());

			// FIXME : 간이로 인증을 요청하는 버튼 메시지 템플릿 생성
			// TODO : 추후 channel_env로 DB화 진행
			String template = "{\n" +
					"  \"version\" : \"0.1\",\n" +
					"  \"chapters\" : [ {\n" +
					"    \"sections\" : [ {\n" +
					"      \"type\" : \"text\",\n" +
					"      \"data\" : \"인증이 필요한 서비스입니다.\\n아래 버튼을 눌러 인증해주세요.\"\n" +
					"    } ]\n" +
					"  } ]\n" +
					"}";

			IssuePayload issuePayload = objectMapper.readValue(template, IssuePayload.class);

			String url = coreProperty.getAuthorizedUri()
					+ "?state=path_" + counselInflowEnv.getParams()
					+ "__mid_" + issue.getMember().getId()
					+ "__issue_" + issue.getId();
			log.info("Send Customer Kakao-Sync, Sync URL : {}", url);

			payload = this.getLinkBtnAddPayload(issuePayload, "고객 인증", url);
			IssueLog issueLog = saveSystemMessage(issue, payload);
			sendToPlatformAndSocket(issue, issueLog);
		} catch (Exception e) {
			log.error(e.getLocalizedMessage(), e);
        }
    }

	/**
	 * 상담종료 안내 메세지, Called by Operator
	 */
	public void sendClose(@NotNull Issue issue, @NotNull ChannelEnvDto channelEnv) {

		log.info("SEND CLOSE, ISSUE: {}", issue.getId());

		try {
			log.info("SEND CLOSE, ISSUE: {}, CONFIG: {}", issue.getId(), channelEnv.getEnd().getGuide());
			// 종료 안내 이벤트 발송, 발송 이력 있으면 스킵
			// TODO: 정책, 종료 안내 계속 발송?
			IssueStorage sendCloseIssueStorage = issueStorageService.findOne(issue.getId(), IssueStorageType.send_close);
			if (sendCloseIssueStorage != null) {
				
				// [2023.05.10 / philip.lee / 강제종료 시간차 조건추가]
				if(!sendCloseIssueStorage.getModified().isBefore(issue.getModified())){
					log.info("SEND CLOSE, ALREADY PROCESSED: ISSUE: {}", issue.getId());
					return;
				} else {
						log.info("SEND CLOSE, DELETE PROCESSED @@@ : {}", sendCloseIssueStorage);
						issueStorageService.delete(sendCloseIssueStorage);
				}
			}


			// 종료 버튼 추가 된 메세지 발송
			String payload = this.getTextBtnAddPayload(channelEnv.getEnd().getGuide().getMessage() , systemMessageProperty.getConsultationTalk().getButton().getClose() );

			IssueLog issueLog = saveSystemMessage(issue, payload,securityUtils.getMemberId());
			sendToPlatformAndSocket(issue, issueLog);

			// 종료 안내 이벤트 발송 이력 저장
			issueStorageService.save(IssueStorage.builder().issueId(issue.getId()).type(IssueStorageType.send_close).modified(ZonedDateTime.now()).enabled(true).build());

			// 종료 예고 사용시, 종료 예고 이벤트 저장, CloseWarnedIssueJob 스케줄러에서 종료
			boolean enabled = SystemEnvEnum.IssueEndType.notice.equals(channelEnv.getEnd().getGuide().getType());
			if (enabled) {
				IssueStorage issueStorage = issueStorageService.findOne(issue.getId(), IssueStorageType.send_warning_close);
				if (issueStorage == null) {
					issueStorage = IssueStorage.builder().issueId(issue.getId()).type(IssueStorageType.send_warning_close).enabled(true).build();
				}
				issueStorage.setModified(ZonedDateTime.now()); // 스케줄러에서 시간제한 판별 기준
				issueStorage = issueStorageService.save(issueStorage);
				log.info("SEND WARNING CLOSE, STORAGE: {}", issueStorage);
			}
		} catch (Exception e) {
			log.error(e.getLocalizedMessage(), e);
		}
	}

	// ////////////////////////////////////////////////////////////////////////
	// 시스템 관리 > 상담 관리/제한 적용
	// ////////////////////////////////////////////////////////////////////////
	/**
	 * 상담 불가 안내 (상담 종료)
	 */
	public void sendDisabledAndClose(@NotNull Issue issue) {

		log.info("SEND DISABLED, ISSUE: {}", issue.getId());

		ChannelEnvDto channelEnv = channelEnvService.getByChannel(issue.getChannel());
		try {
			log.info("SEND DISABLED, CONFIG: {}", channelEnv.getImpossibleMessage());
			IssuePayload issuePayload = channelEnv.getImpossibleMessage();
			String payload = objectMapper.writeValueAsString(issuePayload);
			log.info("SEND DISABLED, PAYLOAD: {}", payload);
//            IssueLog issueLog = saveSystemMessage(issue, payload);
//            // 플랫폼으로 이벤트 전송
//            platformClient.message(issueMapper.map(issue), issueLogMapper.map(issueLog));
			this.close(issue, issuePayload, false);
		} catch (Exception e) {
			log.error(e.getLocalizedMessage(), e);
		}
	}

	/**
	 * 배정대기 건수 제한
	 */
	public void sendOverAssignedAndClose(@NotNull Issue issue, @NotNull ChannelEnvDto channelEnv) {

		log.info("SEND OVER ASSIGNED, ISSUE: {}", issue.getId());

		try {
			log.info("SEND OVER ASSIGNED, CONFIG: {}", channelEnv.getAssignStandby());
			// TODO: 화면 변경 반영 예정, 커스텀 메세지 사용 여부로 변경
			boolean enabled = channelEnv.getAssignStandby().getEnabled();
			if (enabled) {
				IssuePayload issuePayload = channelEnv.getAssignStandby().getMessage();
				String payload = objectMapper.writeValueAsString(issuePayload);
				IssueLog issueLog = saveSystemMessage(issue, payload);
				if(issue.getAssignCount() < 2) {
					platformClient.message(issueMapper.map(issue), issueLogMapper.map(issueLog));
				}
			} else {
				this.sendDisabledAndClose(issue);
			}
		} catch (Exception e) {
			log.error(e.getLocalizedMessage(), e);
		}
	}

	/**
	 * 고객 상담평가 요청 메세지 저장, 플랫폼/소켓 전송은 안함
	 */
	@Nullable
	public IssueLog saveEvaluation(@NotNull Issue issue) {

		log.info("SAVE EVALUATION, ISSUE: {}", issue.getId());

		ChannelEnvDto channelEnv = channelEnvService.getByChannel(issue.getChannel());
		try {
			log.info("BUILD EVALUATION, CONFIG: {}", channelEnv.getEvaluation());
			boolean enabled = channelEnv.getEvaluation().getEnabled();
			if (enabled) {
				String payload = this.getLinkBtnAddPayload(channelEnv.getEvaluation().getMessage(), systemMessageProperty.getConsultationTalk().getButton().getEvaluation() , portalProperty.getEvaluationLinkUrl()) ;
				return saveSystemMessage(issue, payload);
			}
		} catch (Exception e) {
			log.error(e.getLocalizedMessage(), e);
		}

		return null;
	}

	// ////////////////////////////////////////////////////////////////////////
	// 공통
	// ////////////////////////////////////////////////////////////////////////
	/**
	 * 시스템 메세지 {@link Issue}, {@link IssueLog}에 저장
	 */
	private IssueLog saveSystemMessage(@NotNull Issue issue, @NotNull String payload , Long memberId) {
		log.info("SAVE SYSTEM MESSAGE, PAYLOAD: {}", payload);

		// 시스템 메세지 저장
		IssueLog issueLog = IssueLog.builder().issueId(issue.getId()).status(IssueLogStatus.send)
				               .payload(payload).creator(memberId).created(ZonedDateTime.now()).build();
		issueLog = issueLogService.save(issueLog, null);

		// 마지막 메세지로 저장
		issue.setLastIssueLog(issueLog);
		issue.setModified(ZonedDateTime.now());
		issueService.save(issue);

		return issueLog;
	}

	/**
	 * 상담원 아이디 추가 상위 클론
	 * @param issue
	 * @param payload
	 * @return
	 */
	private IssueLog saveSystemMessage(@NotNull Issue issue, @NotNull String payload) {
		return this.saveSystemMessage(issue , payload , portalProperty.getSystemMemberId());
	}

	/**
	 * 상담 종료 (by 시스템)
	 *
	 * @param closeIssuePayload 종료시, 같이 보낼 메세지
	 */
	public void close(@NotNull Long issueId, IssuePayload closeIssuePayload) {

		close(issueId, closeIssuePayload, true);
	}

	/**
	 * 상담 종료 (by 시스템)
	 *
	 * @param closeIssuePayload 종료시, 같이 보낼 메세지
	 */
	public void close(@NotNull Long issueId, IssuePayload closeIssuePayload, boolean evaluation) {

		// 이슈 검색
		Issue issue = issueService.findById(issueId);
		if (issue == null) {
			log.error("ISSUE NOT FOUND, ID: {}", issueId);
			return;
		}

		close(issue, closeIssuePayload, evaluation);
	}

	/**
	 * 상담 종료 (by 시스템)
	 *
	 * @param closeIssuePayload 종료시, 같이 보낼 메세지
	 */
	public void close(@NotNull @Valid Issue issue, IssuePayload closeIssuePayload) {

		close(issue, closeIssuePayload, true);
	}

	/**
	 * 상담 종료 (by 시스템)
	 *
	 * @param closeIssuePayload 종료시, 같이 보낼 메세지
	 * @param evaluation        고객 상담 평가 전송 여부
	 */
	public void close(@NotNull @Valid Issue issue, IssuePayload closeIssuePayload, boolean evaluation) {

		// 이미 종료된 이슈
		if (IssueStatus.close.equals(issue.getStatus())) {
			log.warn("ISSUE IS ALREADY CLOSED, ID: {}", issue.getId());
			return;
		}

		List<IssueLogDto> closeIssueLogs = new ArrayList<>();

		try {
			// 메세지가 있는 경우 종료 이벤트와 함께 전송
			if (closeIssuePayload != null) {
				String closePayload = objectMapper.writeValueAsString(closeIssuePayload);
				IssueLog closeIssueLog = saveSystemMessage(issue, closePayload);
				closeIssueLogs.add(issueLogMapper.map(closeIssueLog));
			}

			// 고객 상담 평가 요청, 종료 이벤트와 함께 전송
			// TODO: 정책, 시스템 종료시에도 고객 팡가 요청
			if (evaluation) {
				IssueLog evaluationIssueLog = this.saveEvaluation(issue);
				if (evaluationIssueLog != null) {
					closeIssueLogs.add(issueLogMapper.map(evaluationIssueLog));
				}
			}

			// 이슈 상태 변경
			issue.setStatus(IssueStatus.close);
			issue.setCloseType(IssueCloseType.system);
			issue.setClosed(ZonedDateTime.now());
			// TODO: 정책, 종료는 상태변경 시간 업데이트 예외?
			issue.setStatusModified(ZonedDateTime.now());

			// 미답변 카운트, 시간 초기화
			issue.setAskCount(0L);
			issue.setFirstAsked(null);

			issue = issueService.save(issue);
			issueService.joinIssueSupport(issue);
			IssueDto issueDto = issueMapper.map(issue);

			// 플랫폼으로 이슈 전송
			// TODO: 종료 메세지, 무과금 메세지로? (ModeProperty 로 가능)
			if (!ObjectUtils.isEmpty(closeIssueLogs)) {
				platformClient.close(issueDto, closeIssueLogs);
				// 소켓으로 이벤트 전송
				for (IssueLogDto issueLogDto : closeIssueLogs) {
					simpMessagingTemplate.convertAndSend(socketProperty.getIssuePath() + "." + issue.getId() + "." + "message", issueLogDto);
				}
			} else {
				platformClient.close(issueDto);
			}

			// 소켓으로 이슈 전송
			simpMessagingTemplate.convertAndSend(socketProperty.getIssuePath(), issueDto);

		} catch (Exception e) {
			log.error(e.getLocalizedMessage(), e);
		}

		log.warn("CLOSE ISSUE BY SYSTEM, ID: {}", issue.getId());
		// TODO: close, 통계 등 필요한 데이터 생성
	}

	/**
	 * 텍스트가 포함 된 버튼 발송 함수
	 * 현재 : '!종료'버튼 발송에 사용 중
	 *
	 * @param sendMessage
	 * @param buttonName
	 * @return
	 * @throws JsonProcessingException
	 */
	private String getTextBtnAddPayload(IssuePayload sendMessage , String buttonName) throws JsonProcessingException {
		IssuePayload.Action action = IssuePayload.Action.builder().type(IssuePayload.ActionType.message)
				     											  .name(buttonName)
																  .build();
		return this.getActionAddPayload(sendMessage , action);
	}

	/**
	 * URL 링크가 포함 된 버튼 발송 함수
	 * 현재 : '상담평가하기' 버튼 발송에 사용 중
	 *
	 * @param sendMessage
	 * @param buttonName
	 * @param buttonUrl
	 * @return
	 * @throws JsonProcessingException
	 */
	private String getLinkBtnAddPayload(IssuePayload sendMessage , String buttonName , String buttonUrl) throws JsonProcessingException {
		IssuePayload.Action action = IssuePayload.Action.builder().type(IssuePayload.ActionType.link)
				              								 	  .name(buttonName)
															 	  .data(buttonUrl)
																  .build();
		return this.getActionAddPayload(sendMessage , action);
	}

	/**
	 * 로직 겹치는 부분 함수화 하기 위해서 추가
	 * @param sendMessage
	 * @param action
	 * @return
	 * @throws JsonProcessingException
	 */
	private String getActionAddPayload(IssuePayload sendMessage, IssuePayload.Action action) throws JsonProcessingException {
		IssuePayload issuePayload = objectMapper.readValue(objectMapper.writeValueAsString(sendMessage), IssuePayload.class);
		issuePayload.add(IssuePayload.Section.builder().type(IssuePayload.SectionType.action).actions(Collections.singletonList(action)).build());
		String payload = objectMapper.writeValueAsString(issuePayload);
		return payload;
	}


	/**
	 * @param issueAndChannelEnvList
	 */
	public void issueCloseUseIssueAndChannelEnvList (List<Tuple> issueAndChannelEnvList) {
		for (Tuple issueAndChannelEnv : issueAndChannelEnvList) {
			// import static com.kep.portal.model.entity.channel.QChannelEndAuto.channelEndAuto
			// 서비스에서 q파일 사용 문제 발생 됨.. 고민 필요
			Issue issueEntity = issueAndChannelEnv.get(issue);
			ChannelEndAuto channelEndAutoEnity = issueAndChannelEnv.get(channelEndAuto);
			IssuePayload issuePayload = channelEndAutoEnity.getGuide().getNoticeMessage();

			this.close(issueEntity, issuePayload);
			// 1. 재사용할 때 문제가 될 수 있어보입니다.
			// 2. 명확함에 문제가 있어보입니다... loop도는 것보다는 괜찮다고 생각하여 일단 이렇게 코딩해놓았습니다.
			// 3. Q파일 호출하는 부분이 생겨서.. 애매해졌습니다.

			// 아래 로직을 개선하기 위해서 Tuple 처음 사용
            /*
            ChannelEnvDto channelEnv = channelEnvService.getByChannel(issue.getChannel());
            // 종료 (즉시 종료)
            IssuePayload issuePayload = null;

            try {
                issuePayload = channelEnv.getEnd().getGuide().getNoticeMessage();
            } catch (Exception e) {
                log.warn(e.getLocalizedMessage());
                issuePayload = new IssuePayload("고객님 다음 상담을 위해 상담을 종료합니다." +
                        " 궁금하신 내용이 생기시면 언제든 다시 문의주시기 바라며," +
                        " 오늘도 행복한 하루되세요^^");
            }
            */
		}
	}

	public void sendMemberNotification(IssueDto issueDto) {
		NotificationDto notificationDto = NotificationDto.builder().displayType(NotificationDisplayType.toast)
																   .icon(NotificationIcon.system)
																   .target(NotificationTarget.member)
																   .type(NotificationType.end_counsel)
																   .build();

		NotificationInfoDto.NotificationInfoDtoBuilder builder = NotificationInfoDto.builder()
				.senderId(securityUtils.getMemberId())
				.receiverId(issueDto.getMember().getId());

		if (issueDto.getCustomerId() != null) builder.customerId(issueDto.getCustomerId());
		if (issueDto.getGuest() != null) builder.guestId(issueDto.getGuest().getId());

		notificationService.store( builder.build() , notificationDto, securityUtils.getMemberId());
	}

}
