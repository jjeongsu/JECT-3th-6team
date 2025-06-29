package com.example.demo.application.dto;

/**
 * 사용자 팝업 내역 조회 쿼리 DTO
 *
 * @param userId  사용자 ID
 * @param size    한 페이지에 조회할 항목 수
 * @param cursor  다음 페이지를 조회하기 위한 커서 값
 */
public record UserPopupHistoryQuery(
        Long userId,
        Integer size,
        String cursor
) {
    public UserPopupHistoryQuery {
        if (userId == null) {
            throw new IllegalArgumentException("사용자 ID는 필수 값입니다.");
        }
        if (size == null || size <= 0) {
            throw new IllegalArgumentException("조회할 항목 수는 1 이상이어야 합니다.");
        }
        if (size > 100) {
            throw new IllegalArgumentException("조회할 항목 수는 100 이하여야 합니다.");
        }
    }
} 