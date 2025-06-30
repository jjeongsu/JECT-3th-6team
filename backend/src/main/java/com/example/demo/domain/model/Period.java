package com.example.demo.domain.model;

import java.time.LocalDate;

/**
 * 기간 도메인 모델.
 * 시작일과 종료일을 나타낸다.
 */
public record Period(
    LocalDate startDate,
    LocalDate endDate
) {
}
