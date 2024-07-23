package com.kep.portal.controller.statisctics;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import javax.annotation.Resource;
import javax.validation.Valid;

import com.kep.core.model.dto.issue.IssueDto;
import com.kep.portal.model.entity.statistics.IssueStstisticsDto;
import com.kep.portal.service.team.TeamService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springdoc.api.annotations.ParameterObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.kep.core.model.dto.ApiResult;
import com.kep.core.model.dto.ApiResultCode;
import com.kep.core.model.dto.branch.BranchDto;
import com.kep.core.model.type.QueryParam;
import com.kep.portal.model.dto.statistics.GuestWaitingTimeAverageDto;
import com.kep.portal.model.dto.statistics.GuestWaitingTimeDto;
import com.kep.portal.model.dto.statistics.ReplyStatusDetailDto;
import com.kep.portal.model.dto.statistics.ReplyStatusDto;
import com.kep.portal.model.dto.statistics.StatisticsSearchDto;
import com.kep.portal.model.dto.statistics.TodaySummaryDto;
import com.kep.portal.service.statistics.StatisticsService;
import com.kep.portal.util.ZonedDateTimeUtil;

import lombok.extern.slf4j.Slf4j;

@Tag(name = "통계 API", description = "/api/v1/statistics")
@Slf4j
@RestController
@RequestMapping("/api/v1/statistics")
public class StatiscticsController {
	@Resource
	private StatisticsService statisticsService;

	@Value("${application.portal.dashboad.search-interval-minutes:120}")
	private int searchInterval;

	@Tag(name = "통계 API")
	@Operation(summary = "이슈 전체 조회")
	@GetMapping
	public ResponseEntity<ApiResult<List<IssueStstisticsDto>>> index(
			@Parameter(description = "기준일")
			@RequestParam(name = "date", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate date){


		log.info("DATE : {}",date);
		List<IssueStstisticsDto> issueStstistics = statisticsService.getAllIssues(date);
		ApiResult<List<IssueStstisticsDto>> response = ApiResult.<List<IssueStstisticsDto>>builder()
				.code(ApiResultCode.succeed)
				.payload(issueStstistics)
				.build();

		return new ResponseEntity<>(response , HttpStatus.OK);
	}

	/**
	 * 모든 브랜치 조회
	 *
	 * @return branch 목록
	 */
	@Tag(name = "통계 API")
	@Operation(summary = "모든 브랜치 조회")
	@GetMapping("/branches")
	public ResponseEntity<ApiResult<List<BranchDto>>> getAllBranchs() {
		List<BranchDto> data = statisticsService.getAllBranches();
		ApiResult<List<BranchDto>> response = ApiResult.<List<BranchDto>>builder().code(ApiResultCode.succeed).payload(data).build();
		return new ResponseEntity<>(response, HttpStatus.OK);
	}

	/**
	 * 오늘 현황 요약
	 * @param today : yyyymmdd 형태
	 * @param branchId
	 * @return 오늘 현황 요약 : 상담원수, 고객수, 상담중, 대기, 완료(놓침 제외), 놓침
	 */
	@Tag(name = "통계 API")
	@Operation(summary = "금일 상담통계 요약 현황", description = "오늘 현황 요약 : 상담원수, 고객수, 상담중, 대기, 완료(놓침 제외), 놓침")
	@GetMapping("/today_summary")
	public ResponseEntity<ApiResult<TodaySummaryDto>> getTodaySummary(
			@Parameter(description = "조회 기준일(yyyyMMdd)")
			@RequestParam(name = "today", required = false) String today,
			@Parameter(description = "브랜치 아이디")
			@RequestParam(name = "branch_id", required = false) Long branchId,
			@Parameter(description = "팀 아이디")
			@RequestParam(name = "team_id", required = false) Long teamId) {
		if (today == null)
			today = ZonedDateTimeUtil.getTodayWithNoTime();

		TodaySummaryDto data = statisticsService.getTodaySummary(today, branchId , teamId , null);
		ApiResult<TodaySummaryDto> response = ApiResult.<TodaySummaryDto>builder().code(ApiResultCode.succeed).payload(data).build();
		return new ResponseEntity<>(response, HttpStatus.OK);
	}

	/**
	 * 상담사 오늘 현황 요약
	 *
	 * @param memberId : 멤버 ID
	 * @param today    : yyyymmdd 형태
	 * @return 오늘 현황 요약 : 상담중, 대기, 완료(놓침 제외), 지연
	 */
	@Tag(name = "통계 API")
	@Operation(summary = "상담사 기준일 현황 요약")
	@GetMapping("/today_summary_member")
	public ResponseEntity<ApiResult<TodaySummaryDto>> getTodaySummaryOfMember(
			@Parameter(description = "조회 기준일 (yyyyMMdd)")
			@RequestParam(name = "today", required = false) String today
			,@Parameter(description = "사용자 아이디", required = true)
			@RequestParam(name = "member_id") Long memberId) {

		if (today == null)
			today = ZonedDateTimeUtil.getTodayWithNoTime();

		TodaySummaryDto data = statisticsService.getTodaySummary(today, null , null , memberId);
		ApiResult<TodaySummaryDto> response = ApiResult.<TodaySummaryDto>builder().code(ApiResultCode.succeed).payload(data).build();
		return new ResponseEntity<>(response, HttpStatus.OK);
	}

	/**
	 * 최근 3주의 주별 평균 대기 시간 목록
	 *
	 * @param branchId
	 * @return 주별 대기 시간들
	 */
	@Tag(name = "통계 API")
	@Operation(summary = "최근 3주의 주별 평균 대기 시간 목록")
	@GetMapping("/wait_time_3weeks_averages")
	public ResponseEntity<ApiResult<List<GuestWaitingTimeAverageDto>>> getAverageReplyTimesGroupBy3Week(
			@Parameter(description = "브랜치 아이디")
			@RequestParam(name = "branch_id", required = false) Long branchId
	){

		List<GuestWaitingTimeAverageDto> data =
				statisticsService.getAverageReplyTimesGroupBy3Week(branchId);;

		ApiResult<List<GuestWaitingTimeAverageDto>> response = ApiResult.<List<GuestWaitingTimeAverageDto>>builder().code(ApiResultCode.succeed).payload(data).build();
		return new ResponseEntity<>(response, HttpStatus.OK);
	}

	/**
	 * 최근 3주 평균 대기 시간
	 *
	 * @param branchId
	 * @return 평균 응답 시간
	 */
	@Tag(name = "통계 API")
	@Operation(summary = "최근 3주 평균 대기 시간")
	@GetMapping("/wait_time_3weeks_overall_average")
	public ResponseEntity<ApiResult<GuestWaitingTimeAverageDto>> getOverallAverageReplyTimesGroupBy3Week(
			@Parameter(description = "브랜치 아이디")
			@RequestParam(name = "branch_id", required = false) Long branchId
	) throws Exception {
		GuestWaitingTimeAverageDto data = null;

		if (branchId == null)
			data = statisticsService.getAverageReplyTimesBy3Week();
		else
			data = statisticsService.getAverageReplyTimesBy3Week(branchId);

		ApiResult<GuestWaitingTimeAverageDto> response = ApiResult.<GuestWaitingTimeAverageDto>builder().code(ApiResultCode.succeed).payload(data).build();
		return new ResponseEntity<>(response, HttpStatus.OK);
	}

	/**
	 * 최근 3개월의 월별 대기 시간 목록
	 *
	 * @param branchId
	 * @return 월별 평균 응답 시간들
	 */
	@Tag(name = "통계 API")
	@Operation(summary = "최근 3개월의 월별 대기 시간 목록")
	@GetMapping("/wait_time_3months_averages")
	public ResponseEntity<ApiResult<List<GuestWaitingTimeAverageDto>>> getAverageReplyTimesGroupByUnits(
			@Parameter(description = "브랜치 아이디")
			@RequestParam(name = "branch_id", required = false) Long branchId
	) throws Exception {
		List<GuestWaitingTimeAverageDto> data = null;
		if (branchId == null)
			data = statisticsService.getAverageReplyTimesGroupBy3Month();
		else
			data = statisticsService.getAverageReplyTimesGroupBy3Month(branchId);

		ApiResult<List<GuestWaitingTimeAverageDto>> response = ApiResult.<List<GuestWaitingTimeAverageDto>>builder().code(ApiResultCode.succeed).payload(data).build();
		return new ResponseEntity<>(response, HttpStatus.OK);
	}

	/**
	 * 최근 3개월 평균 대기 시간
	 *
	 * @param unit - week(default), month
	 * @return 평균 응답 시간
	 */
	@Tag(name = "통계 API")
	@Operation(summary = "최근 3개월 평균 대기 시간")
	@GetMapping("/wait_time_3months_overall_average")
	public ResponseEntity<ApiResult<GuestWaitingTimeAverageDto>> getOverallAverageReplyTimesGroupBy3Month(
			@Parameter(description = "브랜치 아이디")
			@RequestParam(name = "branch_id", required = false) Long branchId
	) throws Exception {
		GuestWaitingTimeAverageDto data = null;
		if (branchId == null)
			data = statisticsService.getAverageReplyTimesBy3Month();
		else
			data = statisticsService.getAverageReplyTimesBy3Month(branchId);

		ApiResult<GuestWaitingTimeAverageDto> response = ApiResult.<GuestWaitingTimeAverageDto>builder().code(ApiResultCode.succeed).payload(data).build();
		return new ResponseEntity<>(response, HttpStatus.OK);
	}

	/**
	 * 최근 2시간 10분 간격의 대기 시간 전체 데이터 조회
	 * 
	 * @param searchDto
	 * @return - issue Id , branch Id, 고객 인입 일시, 대화 시작 일시, 고객 인입 일시 그룹(10분 간격), 고객
	 *         상담 대기 시간(초)
	 */
	@Tag(name = "통계 API")
	@Operation(summary = "최근 2시간 10분 간격의 대기 시간 전체 데이터 조회")
	@GetMapping("/wait_time_in_2hours")
	public ResponseEntity<ApiResult<List<GuestWaitingTimeDto>>> getWatingTimes(@ParameterObject @QueryParam @Valid StatisticsSearchDto searchDto) throws Exception {
		String startTime = searchDto.getStartTime();
		String endTime = searchDto.getEndTime();

		if (startTime.length() >= 12)
			startTime = startTime.substring(0, 11) + "0";

		if (endTime.length() >= 12)
			endTime = endTime.substring(0, 11) + "0";

		List<GuestWaitingTimeDto> data = null;
		log.info("startTime : {} endTime : {}", startTime, endTime);

		if (searchDto.getBranchId() != null)
			data = statisticsService.getGuestWatingTimesSearch(startTime, endTime, searchDto.getBranchId());
		else
			data = statisticsService.getGuestWatingTimesSearch(startTime, endTime);

//빈 시간대 채우기.
		/*
		 * List<String> timeTable = makeTimeTable(startTime, endTime);// 전체 시간
		 * 테이블(10분간격) 만들기 List<GuestWaitingTimeDto> dataToShow = new ArrayList<>();
		 * boolean found = false; for (String t : timeTable) { for (GuestWaitingTimeDto
		 * d : data) { if (d.getEntryTimeGroup().equals(t)) { dataToShow.add(d); found =
		 * true; break; } }
		 *
		 * if (!found)
		 * dataToShow.add(GuestWaitingTimeDto.builder().entryTimeGroup(t).waitingTime(0L
		 * ).build());
		 *
		 * found = false; }
		 */

		ApiResult<List<GuestWaitingTimeDto>> response = ApiResult.<List<GuestWaitingTimeDto>>builder().code(ApiResultCode.succeed).payload(data).build();
		return new ResponseEntity<>(response, HttpStatus.OK);
	}

	/**
	 * 대기 시간 금일 평균
	 * 
	 * @param today - yyyyMMdd
	 * @return 조회된 시간별 응답 현황 : today, 평균 대기 시간
	 */
	@Tag(name = "통계 API")
	@Operation(summary = "대기 시간 금일 평균")
	@GetMapping("/wait_time_today_average")
	public ResponseEntity<ApiResult<GuestWaitingTimeAverageDto>> getReplyStatusesOfToday(
			@Parameter(description = "조회 기준일(yyyyMMdd)")
			@RequestParam(name = "today", required = false) String today,
			@Parameter(description = "브랜치 아이디")
			@RequestParam(name = "branch_id", required = false) Long branchId
	) throws Exception {
		if (today == null || today.length() != 8)
			today = ZonedDateTimeUtil.getTodayWithNoTime();

		List<GuestWaitingTimeDto> data = null;
		if (branchId == null)
			data = statisticsService.getGuestWatingTimesSearch(today + "0000", today + "2400");
		else
			data = statisticsService.getGuestWatingTimesSearch(today + "0000", today + "2400", branchId);

		Long average = 0L;
		Long count = 0L;
		Long sum = 0L;

		for (GuestWaitingTimeDto d : data) {
			sum += d.getWaitingTime();
			count++;
		}

		if (count > 0)
			average = sum / count;

		GuestWaitingTimeAverageDto result = GuestWaitingTimeAverageDto.builder().dateRange(today).waitingTimeAverage(average).build();
		ApiResult<GuestWaitingTimeAverageDto> response = ApiResult.<GuestWaitingTimeAverageDto>builder().code(ApiResultCode.succeed).payload(result).build();
		return new ResponseEntity<>(response, HttpStatus.OK);
	}

	/**
	 * 시간별 응답 현황 조회
	 * 
	 * @param startTime - yyyyMMddHH
	 * @param endTime   - yyyyMMddHH
	 * @return 조회된 시간별 응답 현황 : 인입건수, 응답건수, 상담소요시간, 고객 인입 일시 그룹(10분 간격)
	 */
	@Tag(name = "통계 API")
	@Operation(summary = "시간별 응답 현황 조회")
	@GetMapping("/reply_status")
	public ResponseEntity<ApiResult<List<ReplyStatusDto>>> getReplyStatuses(
			@Parameter(description = "조회 시작일시 (yyyyMMddHH)")
			@RequestParam(name = "start_time") String startDateHourMinute,
			@Parameter(description = "조회 종료일시 (yyyyMMddHH)")
			@RequestParam(name = "end_time") String endDateHourMinute,
			@Parameter(description = "브랜치 아이디")
			@RequestParam(name = "branch_id", required = false) Long branchId) throws Exception {
		List<ReplyStatusDto> data = null;
		String startTime = startDateHourMinute.substring(0, 11) + "0";
		String endTime = endDateHourMinute.substring(0, 11) + "0";

		if (branchId == null)
			data = statisticsService.getReplyStatusesSearch(startTime, endTime);
		else
			data = statisticsService.getReplyStatusesSearch(startTime, endTime, branchId);

		List<String> timeTable = makeTimeTable(startTime, endTime);// 전체 시간 테이블(10분간격) 만들기
		List<ReplyStatusDto> dataToShow = new ArrayList<>();

		boolean found = false;
		for (String t : timeTable) {
			for (ReplyStatusDto d : data) {
				if (d.getTimeGroup().equals(t)) {
					dataToShow.add(d);
					found = true;
					break;
				}
			}

			if (!found)
				dataToShow.add(ReplyStatusDto.builder().timeGroup(t).entryCount(0L).replyCount(0L).averageCounselTime(0L).build());

			found = false;
		}
		
		ApiResult<List<ReplyStatusDto>> response = ApiResult.<List<ReplyStatusDto>>builder().code(ApiResultCode.succeed).payload(dataToShow).build();
		return new ResponseEntity<>(response, HttpStatus.OK);
	}

	/**
	 * 시간별 응답현황 상세 조회
	 * 
	 * @param timeGroup
	 * @return
	 */
	@Tag(name = "통계 API")
	@Operation(summary = "시간별 응답현황 상세 조회")
	@GetMapping("/reply_status_detail")
	public ResponseEntity<ApiResult<List<ReplyStatusDetailDto>>> getReplyStatusDetailDto(
			@Parameter(description = "팀 그룹", required = true)
			@RequestParam(name = "time_group") String timeGroup,
			@Parameter(description = "브랜치 아이디")
			@RequestParam(name = "branch_id", required = false) Long branchId) {
		List<ReplyStatusDetailDto> data = null;

		if (branchId == null)
			data = statisticsService.getReplyStatusDetailDto(timeGroup);
		else
			data = statisticsService.getReplyStatusDetailDto(timeGroup, branchId);
		ApiResult<List<ReplyStatusDetailDto>> response = ApiResult.<List<ReplyStatusDetailDto>>builder().code(ApiResultCode.succeed).payload(data).build();
		return new ResponseEntity<>(response, HttpStatus.OK);
	}

	/**
	 * 시작시간과 종료시간 사이의 날짜 및 시간을 yyyyMMddHHmm 형태로의 Map List로 만들어 전달.
	 *
	 * @param startTime - 시작시간
	 * @param endTime   - 종료시간
	 * @return (key : yyyyMMddHHmm, value : 0) 형태
	 */
	/*
	 * private Map<String, Long> makeTimeTable(String startTime, String endTime) {
	 * final SimpleDateFormat format = new SimpleDateFormat("yyyyMMddHHmm");
	 * SortedMap<String, Long> timeTable = new TreeMap<String, Long>();
	 *
	 * try { Calendar endCalendar = Calendar.getInstance();
	 * endCalendar.setTime(format.parse(endTime));
	 *
	 * Calendar calendar = Calendar.getInstance();
	 * calendar.setTime(format.parse(startTime));
	 *
	 * while (calendar.before(endCalendar)) {
	 * timeTable.put(format.format(calendar.getTime()), 0L);
	 * calendar.add(Calendar.MINUTE, 10); } } catch (Exception e) {
	 * System.out.println(e.getMessage()); } return timeTable; }
	 */

	private List<String> makeTimeTable(String startTime, String endTime) {
		final SimpleDateFormat format = new SimpleDateFormat("yyyyMMddHHmm");
		List<String> timeTable = new ArrayList<>();

		try {
			Calendar endCalendar = Calendar.getInstance();
			endCalendar.setTime(format.parse(endTime));

			Calendar calendar = Calendar.getInstance();
			calendar.setTime(format.parse(startTime));

			while (calendar.before(endCalendar)) {
				timeTable.add(format.format(calendar.getTime()));
				calendar.add(Calendar.MINUTE, 10);
			}
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
		return timeTable;
	}

//	@GetMapping("/test")
//	public String test() throws Exception {
//		statisticsService.batchReplyStatus();
//		return "-------------";
//	}
}
