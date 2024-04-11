package com.kep.core.model.dto.platform.kakao.profile;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.URL;

import javax.validation.constraints.Positive;
import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class KakaoCustomerDto {

    @Positive
    private String id;
    private String connectedAt;
    private String synchedAt;

    private Account kakaoAccount;

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Account {
        // 프로필 또는 닉네임 동의 항목 필요
        private boolean profileNicknameNeedsAgreement;
        // 프로필 또는 프로필 사진 동의 항목 필요
        private boolean profileImageNeedsAgreement;

        private Profile profile;

        @Data
        @Builder
        @NoArgsConstructor
        @AllArgsConstructor
        public static class Profile {
            // 프로필 또는 닉네임 동의 항목 필요
            private String nickname;
            // 프로필 또는 프로필 사진 동의 항목 필요
            @URL
            private String thumbnailImageUrl;
            @URL
            private String profileImageUrl;
            private boolean isDefaultImage;
        }
        // 이름 동의 항목 필요
        private boolean nameNeedsAgreement;
        private String name;
        // 카카오계정(이메일) 동의 항목 필요
        private boolean hasEmail;
        private String emailNeedsAgreement;
        private boolean isEmailValid;
        private boolean isEmailVerified;
        private String email;
        // 연령대 동의 항목 필요
        private boolean ageRangeNeedsAgreement;
        private String ageRange;
        // 출생 연도 동의 항목 필요
        private boolean birthyearNeedsAgreement;
        private String birthyear;
        // 생일 동의 항목 필요
        private boolean birthdayNeedsAgreement;
        private String birthday;
        private String birthdayType;
        // 성별 동의 항목 필요
        private boolean genderNeedsAgreement;
        private String gender;
        // 카카오계정(전화번호) 동의 항목 필요
        private boolean phoneNumberNeedsAgreement;
        private String phoneNumber;
        // CI(연계정보) 동의 항목 필요
        private boolean ciNeedsAgreement;
        private String ci;
        private String ciAuthenticatedAt;
    }

    private Object properties;
    private ForPartner forPartner;

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ForPartner {
        private String uuid;
    }
    
    private Map<String , Object> extra;

}
