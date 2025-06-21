package com.example.demo.domain.model;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

/**
 * 예약 시간 슬롯 값 객체 (HH:mm 형식)
 *
 * @param time 예약 시간
 */
public record TimeSlot(LocalTime time) {

    public static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("HH:mm");

    public TimeSlot {
        if (time == null) {
            throw new IllegalArgumentException("시간(time)은 null일 수 없습니다.");
        }
    }

    /**
     * 문자열(HH:mm)로부터 TimeSlot 생성
     */
    public static TimeSlot of(String timeString) {
        return new TimeSlot(LocalTime.parse(timeString, FORMATTER));
    }

    /**
     * HH:mm 형식의 문자열로 반환
     */
    public String asString() {
        return time.format(FORMATTER);
    }
} 