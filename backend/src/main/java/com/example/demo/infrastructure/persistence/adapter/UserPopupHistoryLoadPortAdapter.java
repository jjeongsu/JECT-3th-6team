package com.example.demo.infrastructure.persistence.adapter;

import com.example.demo.application.port.out.PopupLoadPort;
import com.example.demo.application.port.out.ReservationLoadPort;
import com.example.demo.application.port.out.UserPopupHistoryLoadPort;
import com.example.demo.domain.model.Popup;
import com.example.demo.domain.model.Reservation;
import com.example.demo.domain.model.UserPopupHistory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 사용자 팝업 내역 조회 포트 구현체
 */
@Repository
@RequiredArgsConstructor
public class UserPopupHistoryLoadPortAdapter implements UserPopupHistoryLoadPort {

    private final ReservationLoadPort reservationLoadPort;
    private final PopupLoadPort popupLoadPort;

    @Override
    public List<UserPopupHistory> findUserPopupHistory(Long userId, Integer size, String cursor) {
        // 사용자 예약 내역 조회 (ReservationLoadPort 사용)
        List<Reservation> reservations = reservationLoadPort.findUserReservations(userId, size, cursor);

        if (reservations.isEmpty()) {
            return List.of();
        }

        // 팝업 ID 목록 추출
        List<Long> popupIds = reservations.stream()
                .map(Reservation::popupId)
                .distinct()
                .toList();

        // 팝업 정보 일괄 조회 (PopupLoadPort 재사용)
        List<Popup> popups = popupLoadPort.findByIds(popupIds);
        Map<Long, Popup> popupMap = popups.stream()
                .collect(Collectors.toMap(Popup::id, popup -> popup));

        // 도메인 모델로 변환
        return reservations.stream()
                .map(reservation -> {
                    Popup popup = popupMap.get(reservation.popupId());
                    
                    // TODO: 리뷰 작성 여부 조회 로직 추가 필요 (현재는 false로 고정)
                    boolean reviewWritten = false;
                    
                    return new UserPopupHistory(reservation, popup, reviewWritten);
                })
                .toList();
    }

    @Override
    public boolean hasNextPage(Long userId, Integer size, String lastCursor) {
        return reservationLoadPort.hasMoreUserReservations(userId, lastCursor);
    }

    @Override
    public String generateNextCursor(UserPopupHistory lastItem) {
        return lastItem.getReservationId().toString();
    }
} 