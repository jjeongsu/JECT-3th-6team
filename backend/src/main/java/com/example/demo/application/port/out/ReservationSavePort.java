package com.example.demo.application.port.out;

import com.example.demo.domain.model.Reservation;

/**
 * 예약 저장(생성)용 포트.
 */
public interface ReservationSavePort {

    /**
     * 예약을 저장하고, 저장된 예약(식별자 포함)을 반환한다.
     *
     * @param reservation 예약 도메인 객체
     * @return 저장된 예약
     */
    Reservation save(Reservation reservation);
} 