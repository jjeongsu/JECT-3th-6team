package com.example.demo.domain.model;

import java.time.LocalDate;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * 날짜·시간대별 최대 수용 인원 정보를 보관하는 값 객체.
 * 내부적으로 <LocalDate, <TimeSlot, Integer>> 구조를 래핑한다.
 * null-safe 조회 및 불변성을 제공한다.
 */
public final class CapacitySchedule {

    private final Map<LocalDate, Map<TimeSlot, Integer>> data;

    public CapacitySchedule(Map<LocalDate, Map<TimeSlot, Integer>> source) {
        if (source == null || source.isEmpty()) {
            throw new IllegalArgumentException("예약 수용 인원 일정은 비어 있을 수 없습니다.");
        }
        // 깊은 복사 및 검증
        Map<LocalDate, Map<TimeSlot, Integer>> copy = new HashMap<>();
        for (var dateEntry : source.entrySet()) {
            LocalDate date = dateEntry.getKey();
            Map<TimeSlot, Integer> slotMap = dateEntry.getValue();
            if (slotMap == null || slotMap.isEmpty()) {
                throw new IllegalArgumentException("각 날짜의 수용 인원 정보는 비어 있을 수 없습니다.");
            }
            Map<TimeSlot, Integer> slotCopy = new HashMap<>();
            for (var slotEntry : slotMap.entrySet()) {
                Integer cap = slotEntry.getValue();
                if (cap == null || cap <= 0) {
                    throw new IllegalArgumentException("각 시간대의 최대 수용 인원은 1 이상이어야 합니다.");
                }
                slotCopy.put(slotEntry.getKey(), cap);
            }
            copy.put(date, Collections.unmodifiableMap(slotCopy));
        }
        this.data = Collections.unmodifiableMap(copy);
    }

    public boolean isEmpty() {
        return data.isEmpty();
    }

    public java.util.Set<Map.Entry<LocalDate, Map<TimeSlot, Integer>>> entries() {
        return data.entrySet();
    }

    public Map<TimeSlot, Integer> get(LocalDate date) {
        return data.get(date);
    }

    /**
     * null-safe 조회.
     */
    public int capacityOf(LocalDate date, TimeSlot slot) {
        Map<TimeSlot, Integer> bySlot = data.get(date);
        if (bySlot == null) return 0;
        return bySlot.getOrDefault(slot, 0);
    }
} 