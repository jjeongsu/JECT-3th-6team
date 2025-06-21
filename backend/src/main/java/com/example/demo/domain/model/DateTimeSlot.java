package com.example.demo.domain.model;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

/**
 * 날짜 + 시간 슬롯 값 객체.
 *
 * @param date 날짜 (YYYY-MM-DD)
 * @param time 시간 (HH:mm)
 */
public record DateTimeSlot(LocalDate date, LocalTime time) {

    public static final DateTimeFormatter TIME_FORMATTER = DateTimeFormatter.ofPattern("HH:mm");

    public DateTimeSlot {
        if (date == null) {
            throw new IllegalArgumentException("날짜는 필수 값입니다.");
        }
        if (time == null) {
            throw new IllegalArgumentException("시간은 필수 값입니다.");
        }
    }

    public static DateTimeSlot of(LocalDate date, String timeStr) {
        return new DateTimeSlot(date, LocalTime.parse(timeStr, TIME_FORMATTER));
    }

    public String timeString() {
        return time.format(TIME_FORMATTER);
    }
} 