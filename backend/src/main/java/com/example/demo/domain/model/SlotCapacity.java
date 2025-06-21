package com.example.demo.domain.model;

import java.time.LocalDate;

/**
 * 날짜-시간대-수용 인원 단위를 나타내는 값 객체.
 */
public record SlotCapacity(
        LocalDate date,
        TimeSlot slot,
        int capacity
) {} 