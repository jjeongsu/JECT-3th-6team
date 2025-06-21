package com.example.demo.domain.model;

/**
 * 예약 정책 값 객체
 *
 * @param minPeople               최소 예약 인원
 * @param maxPeople               최대 예약 인원
 */
public record ReservationPolicy(
        int minPeople,
        int maxPeople
) {
    public ReservationPolicy {
        if (minPeople <= 0) {
            throw new IllegalArgumentException("최소 인원(minPeople)은 1 이상이어야 합니다.");
        }
        if (maxPeople < minPeople) {
            throw new IllegalArgumentException("최대 인원(maxPeople)은 최소 인원보다 작을 수 없습니다.");
        }
    }

    public boolean canReserve(int peopleCount) {
        return peopleCount >= minPeople && peopleCount <= maxPeople;
    }
} 