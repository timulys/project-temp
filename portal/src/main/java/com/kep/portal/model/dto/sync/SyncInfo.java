package com.kep.portal.model.dto.sync;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.ZonedDateTime;

/**
 * 카카오 싱크 유저 정보
 */

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SyncInfo {
    public long id;
    public ZonedDateTime connectedAt;
    public KakaoAccount kakaoAccount;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class KakaoAccount{
        public boolean nameNeedsAgreement;
        public boolean hasEmail;
        public boolean emailNeedsAgreement;
        public boolean isEmailValid;
        public boolean isEmailVerified;
        public String email;
        public boolean hasPhoneNumber;
        public boolean phoneNumberNeedsAgreement;
        public boolean hasAgeRange;
        public boolean ageRangeNeedsAgreement;
        public boolean hasBirthday;
        public boolean birthdayNeedsAgreement;
        public boolean hasGender;
        public boolean genderNeedsAgreement;
    }
}
