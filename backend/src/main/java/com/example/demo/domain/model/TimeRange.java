package com.example.demo.domain.model;

import java.time.LocalTime;
import java.util.Objects;

/**
 * 영업 시간 범위 값 객체
 *
 * @param start 시작 시간
 * @param end   종료 시간 (start 이전일 수 없음)
 */
public record TimeRange(LocalTime start, LocalTime end) {
    public TimeRange {
        Objects.requireNonNull(start, "시작 시간(start)은 필수 값입니다.");
        Objects.requireNonNull(end, "종료 시간(end)은 필수 값입니다.");
        if (start.isAfter(end)) {
            throw new IllegalArgumentException("시작 시간은 종료 시간보다 이후일 수 없습니다.");
        }
    }

    public boolean contains(LocalTime time) {
        return !(time.isBefore(start) || time.isAfter(end));
    }
} 