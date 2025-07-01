package com.example.demo.domain.model;

/**
 * 요일별 정보 모델.
 * 요일(예: MON)과 시간 값(예: 10:00 ~ 18:00)을 나타낸다.
 */
public record DayOfWeekInfo(
    String dayOfWeek,
    String value
) {}