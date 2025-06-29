package com.example.demo.application.port.out;

import com.example.demo.domain.model.UserPopupHistory;

import java.util.List;

/**
 * 사용자 팝업 내역 조회용 포트
 */
public interface UserPopupHistoryLoadPort {

    /**
     * 사용자의 팝업 예약/방문 내역을 조회한다.
     * 커서 기반 페이징을 사용하여 데이터를 가져온다.
     *
     * @param userId 사용자 ID
     * @param size   조회할 항목 수
     * @param cursor 커서 값 (null일 경우 첫 페이지)
     * @return 팝업 내역 목록 (도메인 모델)
     */
    List<UserPopupHistory> findUserPopupHistory(Long userId, Integer size, String cursor);

    /**
     * 다음 페이지가 존재하는지 확인한다.
     *
     * @param userId    사용자 ID
     * @param size      조회할 항목 수
     * @param lastCursor 마지막 커서 값
     * @return 다음 페이지 존재 여부
     */
    boolean hasNextPage(Long userId, Integer size, String lastCursor);

    /**
     * 현재 결과의 마지막 항목을 기반으로 다음 커서를 생성한다.
     *
     * @param lastItem 마지막 항목 (도메인 모델)
     * @return 다음 커서 값
     */
    String generateNextCursor(UserPopupHistory lastItem);
} 