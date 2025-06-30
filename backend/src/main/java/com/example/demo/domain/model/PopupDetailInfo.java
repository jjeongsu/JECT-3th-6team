package com.example.demo.domain.model;

import java.util.List;

/**
 * 팝업 상세 추가 정보 모델.
 * 요일별 운영 정보와 설명 텍스트 리스트를 포함한다.
 */
public record PopupDetailInfo(
    List<DayOfWeekInfo> dayOfWeeks,
    List<String> descriptions
) {}
