package com.example.demo.presentation.dto;

import java.util.List;

/**
 * 예약 가능한 시간대 조회 응답 DTO
 *
 * @param times 예약 가능한 시간 목록
 */
public record ReservationAvailableTimesResponse(
        List<ReservationTimeSlotDto> times
) {
} 