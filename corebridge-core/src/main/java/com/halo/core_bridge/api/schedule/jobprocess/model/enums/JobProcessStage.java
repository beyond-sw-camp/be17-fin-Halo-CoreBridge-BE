package com.halo.core_bridge.api.schedule.jobprocess.model.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum JobProcessStage {

    APPLICATION("지원서 검토", 1),            // Resume screening
    PHONE_SCREEN("전화 인터뷰", 2),         // Phone / HR screen
    CODING_TEST("코딩 테스트", 3),          // Technical test / Take-home assignment
    TECH_INTERVIEW_1("1차 기술 인터뷰", 4),  // First technical interview
    TECH_INTERVIEW_2("2차 기술 인터뷰", 5),  // Second technical interview
    CULTURE_FIT("컬처핏 인터뷰", 6),        // Culture-fit / HR/Team interview
    FINAL_INTERVIEW("최종 인터뷰", 7),       // Executive / final round
    OFFER("오퍼 확정", 8),                  // Offer stage
    ONBOARDING("입사 확정/온보딩", 9);       // Employee onboarding

    private final String displayName;
    private final int order;
}