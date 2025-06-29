package com.example.demo.application.port.out;

import com.example.demo.domain.model.DateTimeSlot;
import com.example.demo.domain.model.Reservation;

import java.util.List;

/**
 * 예약 현황 조회용 포트(출력 포트).
 * Application Service → Infrastructure 로 예약 집계 데이터를 요청할 때 사용된다.
 */
public interface ReservationLoadPort {

    /**
     * 특정 팝업의 특정 시간대에 예약된 총 인원 수를 조회한다.
     *
     * @param popupId 팝업 ID
     * @param slot    날짜/시간 슬롯
     * @return 예약된 총 인원 수
     */
    int countBookedPeople(Long popupId, DateTimeSlot slot);

    /**
     * 사용자별 예약 내역을 최신순으로 조회한다 (커서 기반 페이징).
     *
     * @param userId 사용자 ID
     * @param size   조회할 항목 수
     * @param cursor 커서 값 (null일 경우 첫 페이지)
     * @return 예약 도메인 모델 목록
     */
    List<Reservation> findUserReservations(Long userId, Integer size, String cursor);

    /**
     * 커서 이후 추가 예약 데이터 존재 여부를 확인한다.
     *
     * @param userId 사용자 ID
     * @param cursor 커서 값
     * @return 추가 데이터 존재 여부
     */
    boolean hasMoreUserReservations(Long userId, String cursor);
} 