package com.kep.platform.service.kakao.alert;

import com.kep.core.model.dto.platform.kakao.KakaoBizMessageTemplatePayload;
import com.kep.core.model.dto.platform.kakao.KakaoBizMessageTemplateType;
import com.kep.core.model.dto.platform.kakao.KakaoBizTemplateResponse;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Slf4j
class KakaoBizTalkTemplateServiceTest {

    @Resource
    private KakaoBizTalkTemplateService kakaoBizTalkTemplateService;

    @Test
    void create() {
        KakaoBizTemplateResponse<List<KakaoBizTemplateResponse.TemplateCategory>> category = kakaoBizTalkTemplateService.category(System.currentTimeMillis());
        log.info("category = {}", category);
    }
}