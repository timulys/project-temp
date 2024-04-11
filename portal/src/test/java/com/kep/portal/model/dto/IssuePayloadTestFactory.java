package com.kep.portal.model.dto;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.kep.core.model.dto.issue.payload.IssuePayload;
import lombok.extern.slf4j.Slf4j;
import org.jeasy.random.EasyRandom;
import org.jeasy.random.EasyRandomParameters;
import org.jeasy.random.TypePredicates;

import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
public class IssuePayloadTestFactory {

	private static EasyRandom easyRandom;

	static {
		EasyRandomParameters parameters = new EasyRandomParameters();
		parameters.stringLengthRange(1, 30);
		// TODO: 4000 바이트 넘어가는 경우 생김, CLOB 고려
		parameters.collectionSizeRange(1, 2);
		parameters.excludeType(TypePredicates.ofType(Object.class));
		easyRandom = new EasyRandom(parameters);
	}

	public static IssuePayload getPayload() {

		IssuePayload issuePayload = easyRandom.nextObject(IssuePayload.class);
		for (IssuePayload.Chapter chapter : issuePayload.getChapters()) {
			chapter.setSections(easyRandom.objects(IssuePayload.Section.class, 3)
					.collect(Collectors.toList()));
//			for (IssuePayload.Section section : chapter.getSections()) {
//				IssuePayload.Section section = easyRandom.nextObject(IssuePayload.Section.class);
//				log.info("section params: {}", section.getParams());
//				chapter.getSections().add(section);
//			}
		}

		return issuePayload;
	}

	public static Map<String, Object> getMap(ObjectMapper objectMapper) throws Exception {

		IssuePayload issuePayload = getPayload();
		String json = objectMapper.writeValueAsString(issuePayload);
		return objectMapper.readValue(json, new TypeReference<Map<String, Object>>() {});
	}
}
