package com.example.demo.presentation.dto;

/**
 * 예약 시간대 정보 DTO
 *
 * @param time 예약 가능한 시간 (HH:MM 형식)
 * @param value 해당 시간에 예약 가능한지 여부 (true: 가능, false: 불가능)
 */
public record ReservationTimeSlotDto(
        String time,
        Boolean value
) {
} 