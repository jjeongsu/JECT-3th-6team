package com.example.demo.presentation.dto;

import java.time.LocalDateTime;

/**
 * 예약 신청 응답 DTO
 */
public record ReservationCreateResponse(
        long reservationId,
        long popupId,
        String reserverName,
        int numberOfPeople,
        String email,
        LocalDateTime reservationDatetime,
        String status
) {
} 