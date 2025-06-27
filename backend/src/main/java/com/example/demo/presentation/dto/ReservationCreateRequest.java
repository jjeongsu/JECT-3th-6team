package com.example.demo.presentation.dto;

import java.time.LocalDateTime;

/**
 * 예약 신청 요청 DTO
 */
public record ReservationCreateRequest(
        String reserverName,
        Integer numberOfPeople,
        String email,
        LocalDateTime reservationDatetime
) {
} 