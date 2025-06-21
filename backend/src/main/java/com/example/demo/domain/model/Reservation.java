package com.example.demo.domain.model;

import java.time.LocalDate;
import java.util.Objects;

/**
 * 예약 도메인 엔티티
 *
 * @param id          예약 ID
 * @param popupId     팝업 ID
 * @param memberId    회원 ID
 * @param date        예약 날짜
 * @param timeSlot    예약 시간 슬롯
 * @param peopleCount 예약 인원
 * @param status      예약 상태
 */
public record Reservation(
        Long id,
        Long popupId,
        Long memberId,
        LocalDate date,
        TimeSlot timeSlot,
        int peopleCount,
        ReservationStatus status
) {
    public Reservation {
        Objects.requireNonNull(popupId, "팝업 ID는 필수 값입니다.");
        Objects.requireNonNull(memberId, "회원 ID는 필수 값입니다.");
        Objects.requireNonNull(date, "예약 날짜는 필수 값입니다.");
        Objects.requireNonNull(timeSlot, "예약 시간은 필수 값입니다.");
        if (peopleCount <= 0) {
            throw new IllegalArgumentException("예약 인원은 1명 이상이어야 합니다.");
        }
        Objects.requireNonNull(status, "예약 상태는 필수 값입니다.");
    }

    /**
     * 예약 생성 팩토리 메서드 (REQUESTED 상태)
     */
    public static Reservation create(Long popupId, Long memberId, LocalDate date, TimeSlot timeSlot, int peopleCount) {
        return new Reservation(null, popupId, memberId, date, timeSlot, peopleCount, ReservationStatus.RESERVED);
    }

    /**
     * 상태 변경
     */
    public Reservation withStatus(ReservationStatus newStatus) {
        return new Reservation(id, popupId, memberId, date, timeSlot, peopleCount, newStatus);
    }
} 