package com.example.demo.application.dto;

/**
 * 시간대별 예약 가능 여부 DTO.
 *
 * @param time      HH:mm 형식 문자열
 * @param available true = 예약 가능, false = 불가
 */
public record TimeAvailability(
        String time,
        boolean available
) {} 