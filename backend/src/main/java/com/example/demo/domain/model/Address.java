package com.example.demo.domain.model;

/**
 * 주소 값 객체
 *
 * @param city     도시/시
 * @param district 구/군/동 등 행정 구역
 * @param detail   상세 주소
 */
public record Address(
        String city,
        String district,
        String detail
) {
    public Address {
        if (city == null || city.isBlank()) {
            throw new IllegalArgumentException("도시는 비어 있을 수 없습니다.");
        }
        if (district == null || district.isBlank()) {
            throw new IllegalArgumentException("구/군/동은 비어 있을 수 없습니다.");
        }
        if (detail == null || detail.isBlank()) {
            throw new IllegalArgumentException("상세 주소는 비어 있을 수 없습니다.");
        }
    }
} 