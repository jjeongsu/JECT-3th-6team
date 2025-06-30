package com.example.demo.application.dto;

import java.time.LocalDateTime;

/**
 * 예약 생성 요청 DTO (애플리케이션 레이어)
 */
public record ReservationCreateInput(
        long popupId,
        long memberId,
        String reserverName,
        int numberOfPeople,
        String email,
        LocalDateTime reservationDatetime
) {
} 