package com.kep.platform.model.dto;

import org.springframework.util.ObjectUtils;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

/**
 * 카카오 상담톡 송신 이벤트 유효성 검사
 */
public class KakaoCounselSendEventValidator implements ConstraintValidator<KakaoCounselSendEventConstraint, KakaoCounselSendEvent> {

	@Override
	public void initialize(KakaoCounselSendEventConstraint event) {
	}

	@Override
	public boolean isValid(KakaoCounselSendEvent event, ConstraintValidatorContext context) {

		// 메세지 타입별 제약조건
		switch (event.getMessageType()) {
		case "TX":
			// message 필드 필수
			if (ObjectUtils.isEmpty(event.getMessage())) {
				return false;
			}
			break;

		case "IM":
			if (ObjectUtils.isEmpty(event.getImageUrl())) {
				return false;
			}
			break;

		case "FI":
		case "AU":
			break;

		case "LI":
			// message 필드 필수
			if (ObjectUtils.isEmpty(event.getMessage())) {
				return false;
			}
			// (links || autoAnswer) 필드 필수
			if ((event.getLinks() == null || event.getLinks().isEmpty())
					&& ObjectUtils.isEmpty(event.getAutoAnswer()) ) {
				return false;
			}
			break;

		default:
			return false;
		}

		// 자동응답 메세지가 있을 경우 메세지 타입은 'LI'이어야 한다
		if (!ObjectUtils.isEmpty(event.getAutoAnswer())) {
			return event.getMessageType().equals("LI");
		}

		// 링크 목록 제약조건
		if (event.getLinks() != null) {
			for (KakaoCounselSendEvent.Link link : event.getLinks()) {

				if (ObjectUtils.isEmpty(link.getName())
						|| ObjectUtils.isEmpty(link.getType())) {
					return false;
				}

				switch (link.getType()) {
				case "WL":
					if (ObjectUtils.isEmpty(link.getUrlMobile())) {
						return false;
					}
					break;

				case "AL":
					if (ObjectUtils.isEmpty(link.getUrlMobile())
							|| ObjectUtils.isEmpty(link.getSchemeAndroid())
							|| ObjectUtils.isEmpty(link.getSchemeIos())) {
						return false;
					}
					break;

				case "BK":
				case "MD":
				case "BT":
					break;

				default:
					return false;
				}
			}
		}

		return true;
	}
}
