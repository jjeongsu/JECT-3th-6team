package com.example.demo.application.dto;

import java.time.LocalDateTime;

/**
 * 예약 생성 응답 DTO (애플리케이션 레이어)
 */
public record ReservationCreateResponse(
        Long reservationId,
        Long popupId,
        String reserverName,
        Integer numberOfPeople,
        String email,
        LocalDateTime reservationDatetime,
        String status
) {
} 