package com.example.demo.domain.model;

import java.time.LocalDate;
import java.util.Map;
/**
 * 팝업스토어 도메인 엔티티
 *
 * @param id                팝업 ID (Long)
 * @param name              팝업 이름
 * @param location          주소
 * @param capacitySchedule  날짜별 시간대별 최대 수용 인원 맵
 * @param openingHours      영업 시간 범위
 * @param reservationPolicy 예약 정책
 */
public record Popup(
        Long id,
        String name,
        Address location,
        CapacitySchedule capacitySchedule,
        TimeRange openingHours,
        ReservationPolicy reservationPolicy
) {
    public Popup {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("팝업 이름은 비어 있을 수 없습니다.");
        }
        if (location == null) {
            throw new IllegalArgumentException("주소는 필수 값입니다.");
        }
        if (capacitySchedule == null || capacitySchedule.isEmpty()) {
            throw new IllegalArgumentException("예약 수용 인원 일정은 비어 있을 수 없습니다.");
        }
        for (Map.Entry<LocalDate, Map<TimeSlot, Integer>> dateEntry : capacitySchedule.entries()) {
            if (dateEntry.getValue() == null || dateEntry.getValue().isEmpty()) {
                throw new IllegalArgumentException("각 날짜의 수용 인원 정보는 비어 있을 수 없습니다.");
            }
            for (Map.Entry<TimeSlot, Integer> slotEntry : dateEntry.getValue().entrySet()) {
                if (slotEntry.getValue() == null || slotEntry.getValue() <= 0) {
                    throw new IllegalArgumentException("각 시간대의 최대 수용 인원은 1 이상이어야 합니다.");
                }
            }
        }
        if (openingHours == null) {
            throw new IllegalArgumentException("영업 시간은 필수 값입니다.");
        }
        if (reservationPolicy == null) {
            throw new IllegalArgumentException("예약 정책은 필수 값입니다.");
        }
    }

    /**
     * 특정 날짜와 시간대에 대한 최대 수용 인원을 반환합니다.
     * 정보가 없으면 0을 반환합니다.
     */
    public int capacityOf(LocalDate date, TimeSlot timeSlot) {
        return capacitySchedule.capacityOf(date, timeSlot);
    }
} 