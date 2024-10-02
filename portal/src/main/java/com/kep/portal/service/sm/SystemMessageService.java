package com.kep.portal.service.sm;

import com.kep.core.model.dto.ApiResult;
import com.kep.core.model.dto.ApiResultCode;
import com.kep.core.model.dto.issue.payload.IssuePayload;
import com.kep.portal.config.property.SystemMessageProperty;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.netty.http.client.HttpClientRequest;

import javax.annotation.Resource;
import java.time.Duration;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Slf4j
public class SystemMessageService {
    @Resource(name = "webClientBuilder")
    private WebClient.Builder webClientBuilder;

    @Resource
    private SystemMessageProperty systemMessageProperty;
    /**
     * system message;
     * @param payloads
     * @return
     */
    public  Map<String, IssuePayload> setSystemMessage(List<IssuePayload> payloads){
        List<List<IssuePayload.Chapter>> chapters = payloads.stream().map(IssuePayload::getChapters)
                .collect(Collectors.toList());

        Map<String, IssuePayload> message = new HashMap<>();

        for (List<IssuePayload.Chapter> chapter : chapters){
            List<IssuePayload.Section> sections =
                    chapter.stream().map(IssuePayload.Chapter::getSections).collect(Collectors.toList())
                            .stream().findFirst().orElse(null);

            if(!ObjectUtils.isEmpty(sections)){
                for (IssuePayload.Section section : sections){
                    message.put(section.getExtra() , IssuePayload.builder()
                            .version(IssuePayload.CURRENT_VERSION)
                            .chapters(new IssuePayload(IssuePayload.Section.builder()
                                    .type(IssuePayload.SectionType.platform_answer)
                                    .data(section.getData())
                                    .extra(section.getExtra())
                                    .build()).getChapters())
                            .build());
                }
            }
        }
        return message;
    }

    public String getSystemMessage(String serviceKey, String messageType){

        WebClient webClient = webClientBuilder.build();
        Map response = webClient.get().uri("http://platform-service/platform/api/v1/portal/sm/"+messageType).headers(httpHeaders -> {
            httpHeaders.add("X-Service-Key", serviceKey);
        }).retrieve().bodyToMono(Map.class).block();

        return response.get("payload").toString();
    }

    /**
     * 자동메시지 ISSUE PAYLOAD 로 변환
     * @return
     */
    public List<IssuePayload> getSystemMessage(String serviceKey) {
        WebClient webClient = webClientBuilder.build();
        ApiResult<List<IssuePayload>> payloads = webClient
                .get()
                // eddie.j 메세지 수신 URL 변경 된 사항으로 수정
                .uri("http://platform-service/platform/api/v1/counsel-portal/sm")
                .accept(MediaType.APPLICATION_JSON)
                .header("X-Service-Key",serviceKey)
                .httpRequest(httpRequest -> {
                    HttpClientRequest reactorRequest = httpRequest.getNativeRequest();
                    reactorRequest.responseTimeout(Duration.ofSeconds(60));
                })
                .retrieve()
                .onStatus(HttpStatus::is4xxClientError, rs -> {
                    log.error("KAKAO CENTER SYSTEM MESSAGE , STATUS:{} , HEADERS:{}" , rs.statusCode() , rs.headers());
                    return null;
                })
                .onStatus(HttpStatus::is5xxServerError, rs -> {
                    log.error("KAKAO CENTER SYSTEM MESSAGE , STATUS:{} , HEADERS:{}" , rs.statusCode() , rs.headers());
                    return null;
                }).bodyToMono(new ParameterizedTypeReference<ApiResult<List<IssuePayload>>>() {}).block();

        if(payloads != null && ApiResultCode.succeed.equals(payloads.getCode())){
            return payloads.getPayload();
        }
        return null;
    }

    public List<IssuePayload> createSystemMessage() {
        // todo extra에 code가 아닌 IssuePayload.PlatformAnswer의 code가 보일지 나중에 체크 필요 ( 코드클릭에는 그렇게 되어있음 )
        List<IssuePayload> issuePayloads = new ArrayList<>();
        // ST 상담 시작
        issuePayloads.add(this.getDefaultSystemMessage( systemMessageProperty.getChannel().getStart().getSt().getMessage() , systemMessageProperty.getChannel().getStart().getSt().getCode() ));
        // S1 상담 불가
        issuePayloads.add(this.getDefaultSystemMessage( systemMessageProperty.getChannel().getStart().getUnable().getMessage() , systemMessageProperty.getChannel().getStart().getUnable().getCode() ));
        // S2 상담 부재
        issuePayloads.add(this.getDefaultSystemMessage( systemMessageProperty.getChannel().getStart().getAbsence().getMessage() , systemMessageProperty.getChannel().getStart().getAbsence().getCode() ));
        // S3 무응답 종료
        issuePayloads.add(this.getDefaultSystemMessage( systemMessageProperty.getChannel().getStart().getNoResponseEnd().getMessage() , systemMessageProperty.getChannel().getStart().getNoResponseEnd().getCode() ));
        // S4 상담 대기
        issuePayloads.add(this.getDefaultSystemMessage( systemMessageProperty.getChannel().getStart().getWaiting().getMessage() , systemMessageProperty.getChannel().getStart().getWaiting().getCode() ));
        return issuePayloads;
    }

    private IssuePayload getDefaultSystemMessage(String data , String extra ) {
        return new IssuePayload(IssuePayload.Section.builder().type(IssuePayload.SectionType.platform_answer)
                .data(data)
                .extra(extra)
                .build());
    }
}
