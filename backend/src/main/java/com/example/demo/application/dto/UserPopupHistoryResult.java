package com.example.demo.application.dto;

import java.util.List;

/**
 * 사용자 팝업 내역 조회 결과 DTO
 *
 * @param contents   조회된 팝업 내역 목록
 * @param nextCursor 다음 페이지를 조회할 때 사용할 커서 값 (마지막 페이지일 경우 null)
 * @param hasNext    다음 페이지가 존재하는지 여부
 */
public record UserPopupHistoryResult(
        List<PopupHistoryItemResult> contents,
        String nextCursor,
        Boolean hasNext
) {
} 