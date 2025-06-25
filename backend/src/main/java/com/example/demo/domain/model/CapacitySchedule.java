package com.example.demo.domain.model;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * 날짜·시간대별 최대 수용 인원 정보를 보관하는 값 객체.
 * 내부적으로 <DateTimeSlot, Integer> 구조를 래핑한다.
 * null-safe 조회 및 불변성을 제공한다.
 */
public final class CapacitySchedule {

    private final Map<DateTimeSlot, Integer> data;

    public CapacitySchedule(Map<DateTimeSlot, Integer> source) {
        if (source == null || source.isEmpty()) {
            throw new IllegalArgumentException("예약 수용 인원 일정은 비어 있을 수 없습니다.");
        }
        Map<DateTimeSlot, Integer> copy = new HashMap<>();
        for (var entry : source.entrySet()) {
            DateTimeSlot slot = entry.getKey();
            Integer cap = entry.getValue();
            if (cap == null || cap <= 0) {
                throw new IllegalArgumentException("각 슬롯의 최대 수용 인원은 1 이상이어야 합니다.");
            }
            copy.put(slot, cap);
        }
        this.data = Collections.unmodifiableMap(copy);
    }

    public boolean isEmpty() {
        return data.isEmpty();
    }

    public java.util.Set<Map.Entry<DateTimeSlot, Integer>> entries() {
        return data.entrySet();
    }

    /**
     * null-safe 조회.
     */
    public int capacityOf(DateTimeSlot slot) {
        return data.getOrDefault(slot, 0);
    }
} 