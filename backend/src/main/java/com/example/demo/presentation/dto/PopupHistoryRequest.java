package com.example.demo.presentation.dto;

/**
 * 사용자 팝업 예약/방문 내역 조회 요청 DTO
 *
 * @param size   한 페이지에 조회할 항목 수 (기본값: 10)
 * @param cursor 다음 페이지를 조회하기 위한 커서 값
 */
public record PopupHistoryRequest(
        Integer size,
        String cursor
) {
    public PopupHistoryRequest {
        // size가 null이면 기본값 10으로 설정
        if (size == null || size <= 0) {
            size = 10;
        }
        // size 최대값 제한 (100으로 제한)
        if (size > 100) {
            size = 100;
        }
    }
    
    /**
     * 기본값이 적용된 size 반환
     */
    public int getSize() {
        return size == null ? 10 : size;
    }
} 