package com.example.demo.presentation.dto;

/**
 * 예약 가능한 시간대 조회 요청 DTO
 *
 * @param date 원하는 예약일 (YYYY-MM-DD 형식)
 * @param peopleCount 원하는 예약 인원
 */
public record ReservationAvailableTimesRequest(
        String date,
        Integer peopleCount
) {
} 