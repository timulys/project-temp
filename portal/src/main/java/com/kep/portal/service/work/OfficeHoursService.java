package com.kep.portal.service.work;

import java.text.ParseException;
import java.time.ZonedDateTime;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.Resource;
import javax.transaction.Transactional;
import javax.validation.constraints.NotNull;

import org.springframework.stereotype.Component;

import com.kep.core.model.dto.work.OfficeHoursDto;
import com.kep.core.model.dto.work.OfficeWorkDto;
import com.kep.core.model.dto.work.WorkType;
import com.kep.portal.model.entity.team.Team;
import com.kep.portal.model.entity.work.BranchOfficeHours;
import com.kep.portal.model.entity.work.MemberOfficeHours;
import com.kep.portal.model.entity.work.OfficeHours;
import com.kep.portal.model.entity.work.OfficeHoursMapper;
import com.kep.portal.model.entity.work.TeamOfficeHours;
import com.kep.portal.repository.assign.BranchOfficeHoursRepository;
import com.kep.portal.repository.assign.MemberOfficeHoursRepository;
import com.kep.portal.repository.assign.TeamOfficeHoursRepository;
import com.kep.portal.util.OfficeHoursTimeUtils;
import com.kep.portal.util.SecurityUtils;

import lombok.extern.slf4j.Slf4j;

/**
 * 상담시간 설정 Seconds 0-59 , - * / Minutes 0-59 , - * / Hours 0-23 , - * /
 * Day-of-month 1-31 , - * ?(필드자체를 무스) / L W Month 0-11 or JAN-DEC , - * /
 * Day-of-Week 1-7 or SUN-SAT , - * ? / L # Year (Optional) empty, 1970-2199 , -
 * * /
 */
@Slf4j
@Component
@Transactional
public class OfficeHoursService {

	@Resource
	private BranchOfficeHoursRepository branchOfficeHoursRepository;

	@Resource
	private MemberOfficeHoursRepository memberOfficeHoursRepository;

	@Resource
	private TeamOfficeHoursRepository teamOfficeHoursRepository;

	@Resource
	private SecurityUtils securityUtils;

	@Resource
	private OfficeHoursMapper officeHoursMapper;

	/**
	 * 근무 시간 체크
	 * 
	 * @param start
	 * @param end
	 * @return
	 * @throws ParseException
	 */
	public boolean isOfficeHours(@NotNull String start, @NotNull String end, @NotNull String dayOfWeek) {
		boolean isDayOfWeek = OfficeHoursTimeUtils.isDayOfWeek(dayOfWeek);
		log.info("DAY OF WEEK: {}", isDayOfWeek);
		if (isDayOfWeek) {
			Map<String, String> startTime = OfficeHoursTimeUtils.hours(start);
			Map<String, String> endTime = OfficeHoursTimeUtils.hours(end);

			Date startDate = OfficeHoursTimeUtils.dateTime(startTime.get("hours"), startTime.get("minutes"));
			Date endDate = OfficeHoursTimeUtils.dateTime(endTime.get("hours"), endTime.get("minutes"));

			log.info("START_DATE:{},END_DATE:{}", startDate, endDate);

			Date nowDate = new Date();

			boolean isStart = nowDate.equals(startDate);
			boolean isStartDate = nowDate.after(startDate);
			boolean isEndDate = nowDate.before(endDate);

			log.info("IS START:{} , START DATE:{} , END DATE:{}", isStart, isStartDate, isEndDate);

			if ((isStart || isStartDate) && isEndDate) {
				return true;
			}
		}
		return false;
	}

	public boolean isOfficeHours(@NotNull OfficeHours officeHours) {

		return isOfficeHours(officeHours.getStartCounselTime(), officeHours.getEndCounselTime(), officeHours.getDayOfWeek());
	}

	public List<OfficeHours> branchs(@NotNull Set<Long> branchIds) {
		return branchOfficeHoursRepository.findAllByBranchIdIn(branchIds);
	}

	/**
	 * 브랜치 근무시간 설정
	 * 
	 * @param dto
	 * @param branchId
	 * @return
	 * @throws Exception
	 */
	@Transactional
	public OfficeHoursDto branch(@NotNull OfficeWorkDto dto, @NotNull Long branchId) {
		String startHour = OfficeHoursTimeUtils.hours(dto.getOfficeHours().getStartCounselHours(), dto.getOfficeHours().getStartCounselMinutes());
		String endHour = OfficeHoursTimeUtils.hours(dto.getOfficeHours().getEndCounselHours(), dto.getOfficeHours().getEndCounselMinutes());
		String dayOfWeek = OfficeHoursTimeUtils.dayOfWeek(dto.getOfficeHours().getDayOfWeek());

		BranchOfficeHours branchOfficeHours = branchOfficeHoursRepository.findByBranchId(branchId);
		BranchOfficeHours officeHours;
		if (branchOfficeHours == null) {
			officeHours = branchOfficeHoursRepository.save(BranchOfficeHours.builder().branchId(branchId).created(ZonedDateTime.now()).creator(securityUtils.getMemberId()).startCounselTime(startHour)
					.endCounselTime(endHour).dayOfWeek(dayOfWeek).modified(ZonedDateTime.now()).modifier(securityUtils.getMemberId()).build());
		} else {
			officeHours = branchOfficeHours;
			branchOfficeHours.setStartCounselTime(startHour);
			branchOfficeHours.setEndCounselTime(endHour);
			branchOfficeHours.setDayOfWeek(dayOfWeek);
			branchOfficeHours.setModified(ZonedDateTime.now());
			branchOfficeHours.setModifier(securityUtils.getMemberId());
			branchOfficeHoursRepository.save(branchOfficeHours);
		}

		officeHours.setCases(WorkType.Cases.branch);
		officeHours.setCasesId(officeHours.getBranchId());
		return officeHoursMapper.map(officeHours);

	}

	/**
	 * 브랜치 근무시간 조회
	 * 
	 * @param branchId
	 * @return
	 * @throws Exception
	 */
	public OfficeHoursDto branch(@NotNull Long branchId) {
		return officeHoursMapper.map(this.branchHours(branchId));
	}

	/**
	 * 브랜치 근무시간 조회
	 * 
	 * @param branchId
	 * @return
	 * @throws Exception
	 */
	public OfficeHours branchHours(@NotNull Long branchId) {
		log.info("BRANCH ID:{}", branchId);
		BranchOfficeHours branchOfficeHours = branchOfficeHoursRepository.findByBranchId(branchId);
		branchOfficeHours.setCases(WorkType.Cases.branch);
		branchOfficeHours.setCasesId(branchOfficeHours.getBranchId());
		return branchOfficeHours;
	}

	/**
	 * 회원 근무시간 조회
	 * 
	 * @param memberId
	 * @return
	 * @throws Exception
	 */
	public OfficeHours member(@NotNull Long memberId) {
		MemberOfficeHours memberOfficeHours = memberOfficeHoursRepository.findByMemberId(memberId);
		if (memberOfficeHours != null) {
			memberOfficeHours.setCases(WorkType.Cases.member);
			memberOfficeHours.setCasesId(memberOfficeHours.getMemberId());
		}

		return memberOfficeHours;
	}

	/**
	 * 회원 근무시간 설정
	 * 
	 * @수정일자 / 수정자 / 수정내용 2023.05.31 / asher.shin / 근무외상담 추가
	 */
	public MemberOfficeHours member(@NotNull OfficeHoursDto dto, @NotNull Long memberId) {

		log.info("OFFICE HOURS: {} , MEMBER ID:{}", dto, memberId);
		String startHour = (dto.getStartCounselHours() != null && dto.getStartCounselMinutes() != null) ? OfficeHoursTimeUtils.hours(dto.getStartCounselHours(), dto.getStartCounselMinutes()) : null;
		String endHour = (dto.getEndCounselHours() != null && dto.getEndCounselMinutes() != null) ? OfficeHoursTimeUtils.hours(dto.getEndCounselHours(), dto.getEndCounselMinutes()) : null;
		String dayOfWeek = (dto.getDayOfWeek() != null) ? OfficeHoursTimeUtils.dayOfWeek(dto.getDayOfWeek()) : null;

		log.info("START HOUR:{} , END HOUR:{} , DAY OF WEEK:{}", startHour, endHour, dayOfWeek);

		if (startHour == null || endHour == null || dayOfWeek == null) {
			BranchOfficeHours officeHours = branchOfficeHoursRepository.findByBranchId(securityUtils.getBranchId());
			startHour = officeHours.getStartCounselTime();
			endHour = officeHours.getEndCounselTime();
			dayOfWeek = officeHours.getDayOfWeek();
		}

		MemberOfficeHours memberOfficeHours = memberOfficeHoursRepository.findByMemberId(memberId);
		if (memberOfficeHours == null) {
			memberOfficeHours = MemberOfficeHours.builder().memberId(memberId).created(ZonedDateTime.now()).creator(securityUtils.getMemberId()).build();
		}

		memberOfficeHours.setStartCounselTime(startHour);
		memberOfficeHours.setEndCounselTime(endHour);
		memberOfficeHours.setDayOfWeek(dayOfWeek);
		// 근무외상담여부 추가,TODO:기본값 FALSE로 확인필요
		memberOfficeHours.setOffDutyCounselYn(dto.getOffDutyCounselYn() != null && dto.getOffDutyCounselYn());
		memberOfficeHours.setModified(ZonedDateTime.now());
		memberOfficeHours.setModifier(securityUtils.getMemberId());
		return memberOfficeHoursRepository.save(memberOfficeHours);

	}

	/**
	 * 팀 근무시간 설정
	 */
	@Transactional
	@Deprecated
	public TeamOfficeHours team(@NotNull OfficeHoursDto dto, @NotNull Team team, @NotNull Long memberId) throws Exception {

		String startHour = OfficeHoursTimeUtils.hours(dto.getStartCounselHours(), dto.getStartCounselMinutes());
		String endHour = OfficeHoursTimeUtils.hours(dto.getEndCounselHours(), dto.getEndCounselHours());
		String dayOfWeek = OfficeHoursTimeUtils.dayOfWeek(dto.getDayOfWeek());

		TeamOfficeHours teamOfficeHours = teamOfficeHoursRepository.findByTeamId(team.getId());

		if (teamOfficeHours.getTeamId() == null) {
			teamOfficeHours.setTeamId(team.getId());
			teamOfficeHours.setCreator(memberId);
			teamOfficeHours.setCreated(ZonedDateTime.now());
		}

		teamOfficeHours.setStartCounselTime(startHour);
		teamOfficeHours.setEndCounselTime(endHour);
		teamOfficeHours.setDayOfWeek(dayOfWeek);
		teamOfficeHours.setModified(ZonedDateTime.now());
		teamOfficeHours.setModifier(memberId);
		return teamOfficeHoursRepository.save(teamOfficeHours);

	}

}
