package com.example.demo.presentation.dto;

import java.util.List;

/**
 * 팝업 예약/방문 내역 항목 DTO
 *
 * @param reservationId      예약 ID
 * @param popupId           팝업 ID
 * @param popupTitle        팝업 제목
 * @param popupImageUrl     팝업 대표 이미지 URL
 * @param reservationStatus 예약 상태
 * @param popupType         팝업 유형
 * @param popupCategories   팝업이 속한 카테고리 목록
 * @param reviewWritten     리뷰 작성 여부
 */
public record PopupHistoryItem(
        Long reservationId,
        Long popupId,
        String popupTitle,
        String popupImageUrl,
        String reservationStatus,
        String popupType,
        List<String> popupCategories,
        Boolean reviewWritten
) {
} 