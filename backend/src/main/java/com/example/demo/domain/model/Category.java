package com.example.demo.domain.model;

/**
 * 카테고리 값 객체
 * 
 * @param id   카테고리 ID
 * @param name 카테고리 이름
 */
public record Category(
        Long id,
        String name
) {
    public Category {
        if (id == null) {
            throw new IllegalArgumentException("카테고리 ID는 필수 값입니다.");
        }
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("카테고리 이름은 비어 있을 수 없습니다.");
        }
    }
} 