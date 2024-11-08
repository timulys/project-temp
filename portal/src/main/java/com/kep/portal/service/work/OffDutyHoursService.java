package com.kep.portal.service.work;

import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.annotation.Resource;
import javax.transaction.Transactional;

import com.kep.portal.model.entity.branch.Branch;
import org.springframework.stereotype.Service;

import com.kep.core.model.dto.work.OffDutyHoursDto;
import com.kep.core.model.dto.work.WorkType;
import com.kep.portal.model.entity.work.BranchOffDutyHours;
import com.kep.portal.model.entity.work.MemberOffDutyHours;
import com.kep.portal.model.entity.work.OffDutyHours;
import com.kep.portal.model.entity.work.OffDutyHoursMapper;
import com.kep.portal.repository.work.BranchOffDutyHoursRepository;
import com.kep.portal.repository.work.MemberOffDutyHoursRepository;
import com.kep.portal.util.SecurityUtils;
import com.kep.portal.util.ZonedDateTimeUtil;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@Transactional
public class OffDutyHoursService {
	@Resource
	private BranchOffDutyHoursRepository branchOffDutyHoursRepository;
	@Resource
	private MemberOffDutyHoursRepository memberOffDutyHoursRepository;
	@Resource
	private SecurityUtils securityUtils;
	@Resource
	private OffDutyHoursMapper offDutyHoursMapper;

	/**
	 * 근무 예외 목록
	 * 
	 * @return
	 */
	public List<OffDutyHoursDto> findAll(WorkType.Cases cases, Long casesId) {
		log.info("근무 예외 목록 조회: Cases - {}, Cases ID - {}",cases, casesId);
		if (WorkType.Cases.branch.equals(cases)) {
			return this.branchFindAll(casesId);
		}
		return null;
	}

	/**
	 * 근무 예외 목록 TODO: 올해만(?)
	 * 
	 * @param branchId
	 * @return
	 */
	private List<OffDutyHoursDto> branchFindAll(Long branchId) {
		List<OffDutyHours> offDutyHoursList = branchOffDutyHoursRepository.findAllByBranchIdOrderByStartCreatedAsc(branchId).stream().peek(item -> {
			item.setCases(WorkType.Cases.branch);
			item.setCasesId(branchId);
		}).collect(Collectors.toList());
		return offDutyHoursMapper.map(offDutyHoursList);
	}

	/**
	 * branch 근무 예외 시간 등록
	 * 
	 * @param dto
	 * @return
	 */
	private OffDutyHoursDto branchCreate(OffDutyHoursDto dto) {
		log.info("브랜치 근무 예외 시간 등록 결과 : {}", dto);
		Long memberId = securityUtils.getMemberId();
		BranchOffDutyHours branchOffDutyHours = null;
		if (dto.getId() != null) {
			branchOffDutyHours = branchOffDutyHoursRepository.findById(dto.getId()).orElse(null);
		}

		if (branchOffDutyHours == null) {
			// KICA-415 금일 등록 된 요일이 있을 경우 브랜치 일정 등록 안되는 로직 주석 처리
			/*
			Long branchId = securityUtils.getBranchId();
			List<OffDutyHours> branchOffDutyHoursList = this.getOffDutyHours(branchId);
			if(!branchOffDutyHoursList.isEmpty()){
				return null;
			}*/
			branchOffDutyHours = BranchOffDutyHours.builder()
					.created(ZonedDateTime.now())
					.creator(memberId)
					.modified(ZonedDateTime.now())
					.modifier(memberId).enabled(dto.getEnabled())
					.contents(dto.getContents())
					.startCreated(ZonedDateTimeUtil.stringToDateTime(dto.getStartCreated()))
					.endCreated(ZonedDateTimeUtil.stringToDateTime(dto.getEndCreated()))
					.branchId(dto.getBranchId())
			.build();
		} else {
			branchOffDutyHours.setModified(ZonedDateTime.now());
			branchOffDutyHours.setModifier(memberId);
			branchOffDutyHours.setEnabled(dto.getEnabled());
			branchOffDutyHours.setContents(dto.getContents());
			branchOffDutyHours.setStartCreated(ZonedDateTimeUtil.stringToDateTime(dto.getStartCreated()));
			branchOffDutyHours.setEndCreated(ZonedDateTimeUtil.stringToDateTime(dto.getEndCreated()));
		}

		branchOffDutyHours.setCases(dto.getCases());
		branchOffDutyHours.setCasesId(dto.getBranchId());
		return offDutyHoursMapper.map(branchOffDutyHoursRepository.save(branchOffDutyHours));
	}

	/**
	 * 회원 근무시간 예외
	 * 
	 * @param dto
	 * @return
	 */
	private OffDutyHoursDto memberCreate(OffDutyHoursDto dto) {

		Long memberId = securityUtils.getMemberId();
		MemberOffDutyHours memberOffDutyHours = null;
		if (dto.getId() != null) {
			memberOffDutyHours = memberOffDutyHoursRepository.findById(dto.getId()).orElse(null);
		}

		if (memberOffDutyHours == null) {
			memberOffDutyHours = MemberOffDutyHours.builder().created(ZonedDateTime.now()).creator(memberId).modified(ZonedDateTime.now()).modifier(memberId).enabled(dto.getEnabled())
					.contents(dto.getContents()).startCreated(ZonedDateTimeUtil.stringToDateTime(dto.getStartCreated())).endCreated(ZonedDateTimeUtil.stringToDateTime(dto.getEndCreated()))
					.memberId(dto.getMemberId()).build();
		} else {
			memberOffDutyHours.setModified(ZonedDateTime.now());
			memberOffDutyHours.setModifier(memberId);
			memberOffDutyHours.setEnabled(dto.getEnabled());
			memberOffDutyHours.setContents(dto.getContents());
			memberOffDutyHours.setStartCreated(ZonedDateTimeUtil.stringToDateTime(dto.getStartCreated()));
			memberOffDutyHours.setEndCreated(ZonedDateTimeUtil.stringToDateTime(dto.getEndCreated()));
		}

		memberOffDutyHours.setCases(dto.getCases());
		memberOffDutyHours.setCasesId(dto.getMemberId());
		return offDutyHoursMapper.map(memberOffDutyHoursRepository.save(memberOffDutyHours));
	}

	public OffDutyHoursDto create(OffDutyHoursDto dto, Long casesId) {
		OffDutyHoursDto offDutyHoursDto = null;
		if (dto.getCases().equals(WorkType.Cases.branch)) {
			dto.setBranchId(casesId);
			offDutyHoursDto = this.branchCreate(dto);
		}
		if (dto.getCases().equals(WorkType.Cases.member)) {
			dto.setMemberId(casesId);
			offDutyHoursDto = this.memberCreate(dto);
		}
		return offDutyHoursDto;
	}

	/**
	 * 브랜치 근무 예외 삭제
	 * 
	 * @param id
	 * @return
	 */
	private boolean branchDelete(Long id) {

		BranchOffDutyHours branchOffDutyHours = branchOffDutyHoursRepository.findById(id).orElse(null);

		// TODO : 등록자만 삭제 ??
		if (branchOffDutyHours != null) {
			branchOffDutyHoursRepository.deleteById(id);
			return true;
		}
		return false;
	}

	/**
	 * 회원 근무예외 삭제
	 * 
	 * @param id
	 * @return
	 */
	private boolean memberDelete(Long id) {
		MemberOffDutyHours memberOffDutyHours = memberOffDutyHoursRepository.findById(id).orElse(null);

		// TODO : 등록자만 삭제 ??
		if (memberOffDutyHours != null) {
			memberOffDutyHoursRepository.deleteById(id);
			return true;
		}
		return false;

	}

	public boolean delete(OffDutyHoursDto dto) {
		boolean result = false;
		if (dto.getCases().equals(WorkType.Cases.branch)) {
			result = this.branchDelete(dto.getId());
		}
		if (dto.getCases().equals(WorkType.Cases.member)) {
			result = this.memberDelete(dto.getId());
		}
		return result;
	}

	public List<OffDutyHours> getOffDutyHours(Long branchId) {
		Map<String,ZonedDateTime> today = ZonedDateTimeUtil.getTodayDateTime(LocalDate.now());
		return branchOffDutyHoursRepository.findAllByBranchIdAndStartCreatedGreaterThanEqualAndEndCreatedLessThanEqual( branchId , today.get("start") , today.get("end") );
	}

}
