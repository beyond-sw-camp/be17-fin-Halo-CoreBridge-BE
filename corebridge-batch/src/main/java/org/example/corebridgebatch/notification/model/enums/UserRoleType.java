package org.example.corebridgebatch.notification.model.enums;

import lombok.Getter;

@Getter
public enum UserRoleType {

    ROLE_ADMIN("관리자"),
    ROLE_APPLICANT("지원자"),
    ROLE_RECRUITER("채용 담당자"),
    ROLE_INTERVIEWER("면접관");

    private final String displayName;

    UserRoleType(String displayName) {
        this.displayName = displayName;
    }

    public static String getDisplayName(String code) {

        for (UserRoleType roleType : values()) {
            if (roleType.name().equals(code)) {
                return roleType.getDisplayName();
            }
        }

        return null;
    }
}
