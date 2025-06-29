package com.example.demo.domain.model;

import java.util.List;
import java.util.Map;
/**
 * 팝업스토어 도메인 엔티티
 *
 * @param id                팝업 ID (Long)
 * @param name              팝업 이름
 * @param location          주소
 * @param capacitySchedule  날짜별 시간대별 최대 수용 인원 맵
 * @param openingHours      영업 시간 범위
 * @param categories        팝업 카테고리 목록
 * @param type              팝업 타입
 */
public record Popup(
        Long id,
        String name,
        Address location,
        CapacitySchedule capacitySchedule,
        TimeRange openingHours,
        List<Category> categories,
        PopupType type
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
        for (Map.Entry<DateTimeSlot, Integer> entry : capacitySchedule.entries()) {
            if (entry.getValue() <= 0) {
                throw new IllegalArgumentException("각 슬롯의 최대 수용 인원은 1 이상이어야 합니다.");
            }
        }
        if (openingHours == null) {
            throw new IllegalArgumentException("영업 시간은 필수 값입니다.");
        }
        if (categories == null) {
            throw new IllegalArgumentException("카테고리 목록은 필수 값입니다.");
        }
        if (type == null) {
            throw new IllegalArgumentException("팝업 타입은 필수 값입니다.");
        }
    }

    /**
     * 특정 날짜와 시간대에 대한 최대 수용 인원을 반환합니다.
     * 정보가 없으면 0을 반환합니다.
     */
    public int capacityOf(DateTimeSlot slot) {
        return capacitySchedule.capacityOf(slot);
    }
} 