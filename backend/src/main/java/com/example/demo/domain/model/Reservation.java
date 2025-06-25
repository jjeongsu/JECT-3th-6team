package com.example.demo.domain.model;

import java.util.Objects;

/**
 * 예약 도메인 엔티티
 *
 * @param id          예약 ID
 * @param popupId     팝업 ID
 * @param memberId    회원 ID
 * @param slot        예약 일시 슬롯
 * @param peopleCount 예약 인원
 * @param status      예약 상태
 */
public record Reservation(
        Long id,
        Long popupId,
        Long memberId,
        DateTimeSlot slot,
        int peopleCount,
        ReservationStatus status
) {
    public Reservation {
        Objects.requireNonNull(popupId, "팝업 ID는 필수 값입니다.");
        Objects.requireNonNull(memberId, "회원 ID는 필수 값입니다.");
        Objects.requireNonNull(slot, "예약 일시는 필수 값입니다.");
        if (peopleCount <= 0) {
            throw new IllegalArgumentException("예약 인원은 1명 이상이어야 합니다.");
        }
        Objects.requireNonNull(status, "예약 상태는 필수 값입니다.");
    }

    /**
     * 예약 생성 팩토리 메서드 (REQUESTED 상태)
     */
    public static Reservation create(Long popupId, Long memberId, DateTimeSlot slot, int peopleCount) {
        return new Reservation(null, popupId, memberId, slot, peopleCount, ReservationStatus.RESERVED);
    }

    /**
     * 상태 변경
     */
    public Reservation withStatus(ReservationStatus newStatus) {
        return new Reservation(id, popupId, memberId, slot, peopleCount, newStatus);
    }
} 