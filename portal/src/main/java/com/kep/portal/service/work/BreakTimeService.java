package com.kep.portal.service.work;

import com.kep.portal.config.property.PortalProperty;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.time.LocalTime;

@Slf4j
@Service
public class BreakTimeService {

    @Resource
    private PortalProperty portalProperty;

    /**
     * todo 고도화 때 컬럼화 하여 화면에서 설정 가능하도록 개선할 예정
     * 현재 breaktime(휴식시간)인지 여부에 대한 체크를 위해서 추가
     * true : 휴식시간 (breaktime : O) 인 경우
     * false : 상담시간 (breaktime : X) 인 경우
     */
    public boolean inBreakTime() {
        LocalTime now = LocalTime.now();
        log.info(">>> getBreaktime : START, {} ~ {}, NOW: {}", portalProperty.getBreakTimeStart(), portalProperty.getBreakTimeEnd(), now);
        if (now.isAfter(portalProperty.getBreakTimeStart()) && now.isBefore(portalProperty.getBreakTimeEnd())) {
            return true;
        }
        return false;
    }

}
