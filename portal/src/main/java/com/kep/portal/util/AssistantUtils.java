package com.kep.portal.util;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.RequiredArgsConstructor;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public final class AssistantUtils {
    @Value("${openai.api.key}")
    private String apiKey;
    @Value("${openai.assistant.id}")
    private String assistantId;
    @Value("${openai.base-url}")
    private String baseUrl;

    private final ObjectMapper objectMapper;
    private final CloseableHttpClient httpClient = HttpClients.createDefault();

    public String chatWithAssistant(String userMessage) throws Exception {
        String threadId = createThread();
        addMessage(threadId, userMessage);
        String runId = createRun(threadId);
        waitForRunCompletion(threadId, runId);
        return fetchLatestAssistantReply(threadId);
    }

    private String createThread() throws IOException {
        HttpPost request = new HttpPost(baseUrl + "/threads");
        setHeaders(request);
        request.setEntity(new StringEntity("{}", ContentType.APPLICATION_JSON));
        return executeForId(request);
    }

    private void addMessage(String threadId, String content) throws IOException {
        HttpPost request = new HttpPost(baseUrl + "/threads/" + threadId + "/messages");
        setHeaders(request);

        ObjectNode messageJson = objectMapper.createObjectNode();
        messageJson.put("role", "user");
        messageJson.put("content", content);
        request.setEntity(new StringEntity(messageJson.toString(), ContentType.APPLICATION_JSON));

        httpClient.execute(request).close();
    }

    private String createRun(String threadId) throws IOException {
        HttpPost request = new HttpPost(baseUrl + "/threads/" + threadId + "/runs");
        setHeaders(request);

        ObjectNode runJson = objectMapper.createObjectNode();
        runJson.put("assistant_id", assistantId);
        request.setEntity(new StringEntity(runJson.toString(), ContentType.APPLICATION_JSON));

        return executeForId(request);
    }

    private void waitForRunCompletion(String threadId, String runId) throws Exception {
        String status = "";
        while (!"completed".equals(status)) {
            HttpGet request = new HttpGet(baseUrl + "/threads/" + threadId + "/runs/" + runId);
            setHeaders(request);

            try (CloseableHttpResponse response = httpClient.execute(request)) {
                String json = EntityUtils.toString(response.getEntity());
                status = objectMapper.readTree(json).get("status").asText();
            }

            Thread.sleep(1000);
        }
    }

    private String fetchLatestAssistantReply(String threadId) throws IOException {
        HttpGet request = new HttpGet(baseUrl + "/threads/" + threadId + "/messages");
        setHeaders(request);

        try (CloseableHttpResponse response = httpClient.execute(request)) {
            String json = EntityUtils.toString(response.getEntity());
            JsonNode messages = objectMapper.readTree(json).get("data");
            for (JsonNode msg : messages) {
                if ("assistant".equals(msg.get("role").asText())) {
                    return msg.get("content").get(0).get("text").get("value").asText();
                }
            }
        }

        return "답변을 찾을 수 없습니다.";
    }

    private String executeForId(HttpPost request) throws IOException {
        try (CloseableHttpResponse response = httpClient.execute(request)) {
            String json = EntityUtils.toString(response.getEntity());
            return objectMapper.readTree(json).get("id").asText();
        }
    }

    private void setHeaders(HttpRequestBase request) {
        request.setHeader("Authorization", "Bearer " + apiKey);
        request.setHeader("Content-Type", "application/json");
        request.setHeader("OpenAI-Beta", "assistants=v2");
    }
}
