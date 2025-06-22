package com.example.demo.application.port.out;

import com.example.demo.domain.model.DateTimeSlot;

/**
 * 예약 현황 조회용 포트(출력 포트).
 * Application Service → Infrastructure 로 예약 집계 데이터를 요청할 때 사용된다.
 */
public interface ReservationLoadPort {

    /**
     * 특정 팝업·시간 슬롯에 대해 이미 예약된 총 인원 수를 반환한다.
     *
     * @param popupId 팝업 ID
     * @param slot    날짜·시간 슬롯
     * @return 예약된 인원 수 (없으면 0)
     */
    int countBookedPeople(Long popupId, DateTimeSlot slot);
} 