package com.kep.portal.controller.sm;

import com.kep.core.model.dto.ApiResult;
import com.kep.core.model.dto.ApiResultCode;
import com.kep.core.model.dto.issue.payload.IssuePayload;
import com.kep.portal.config.property.PortalProperty;
import com.kep.portal.model.entity.channel.Channel;
import com.kep.portal.service.channel.ChannelService;
import com.kep.portal.service.sm.SystemMessageService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

@Tag(name = "시스템 메시지 API", description = "/api/v1/sm")
@Slf4j
@RestController
@RequestMapping("/api/v1/sm")
public class SystemMessageController {


    @Resource
    private PortalProperty portalProperty;
    @Resource
    private SystemMessageService systemMessageService;

    @Resource
    private ChannelService channelService;

    /**
     * SB-SA-007
     * 메세지 타입에 맞는 시스템 메시지 조회
     * @param channelId
     * @param messageType
     * @return
     */
    @Tag(name = "시스템 메시지 API")
    @Operation(summary = "메세지 타입에 맞는 시스템 메시지 조회", description = "SB-SA-007")
    @GetMapping("{channelId}/{messageType}")
    public ResponseEntity<ApiResult<Map>> get(
            @Parameter(description = "채널 아이디", in = ParameterIn.PATH, required = true)
            @PathVariable(value = "channelId") Long channelId,
            @Parameter(description = "메시지 타입", in = ParameterIn.PATH, required = true)
            @PathVariable(value = "messageType") String messageType
    ) {
        try {
            Channel channel = channelService.findById(channelId);
            Assert.notNull(channel , "not found channel , id:"+channelId);
            ApiResult response = ApiResult.builder()
                    .code(ApiResultCode.succeed)
                    .payload(systemMessageService.getSystemMessage(channel.getServiceKey(), messageType))
                    .build();

            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            ApiResult response = ApiResult.builder()
                    .code(ApiResultCode.failed)
                    .message(e.getLocalizedMessage())
                    .build();
            response.setError("<<SB-SA-007-SM>>");

            return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
        }

    }

    /**
     * SB-SA-007
     * 메시지 타입 전체 조회
     * @param channelId
     * @return
     */
    @Tag(name = "시스템 메시지 API")
    @Operation(summary = "메시지 타입 전체 조회", description = "SB-SA-007")
    @GetMapping(value = "/{id}")
    public ResponseEntity<ApiResult<List<IssuePayload>>> get(
            @Parameter(description = "채널 아이디", in = ParameterIn.PATH, required = true)
            @PathVariable(value = "id") Long channelId) {

        Channel channel = channelService.findById(channelId);
        Assert.notNull(channel ,"not found channel , id:"+channelId);
        List<IssuePayload> issuePayloads = systemMessageService.getSystemMessage(channel.getServiceKey());
        ApiResult<List<IssuePayload>> response = ApiResult.<List<IssuePayload>>builder()
                .code(ApiResultCode.succeed)
                .payload(issuePayloads)
                .build();

        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
