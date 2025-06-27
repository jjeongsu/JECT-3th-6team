package com.example.demo.domain.service;

import com.example.demo.domain.model.*;
import org.springframework.stereotype.Component;

import java.time.LocalTime;

/**
 * 예약 관련 도메인 서비스.
 * <p>
 * 단일 엔티티에 속하지 않는 예약 비즈니스 규칙을 담당합니다.
 */
@Component
public class ReservationDomainService {

    /**
     * 예약 가능 여부 검증.
     *
     * @param popup       팝업 스토어
     * @param slot        예약 일시
     * @param alreadyBooked 현재 이미 예약된 인원 수
     * @param requestPeople 요청 인원 수
     * @return 예약 가능하면 true
     */
    public boolean canReserve(Popup popup, DateTimeSlot slot, int alreadyBooked, int requestPeople) {
        if (popup == null) {
            throw new IllegalArgumentException("팝업 정보는 필수 값입니다.");
        }
        if (slot == null) {
            throw new IllegalArgumentException("예약 일시는 필수 값입니다.");
        }
        if (requestPeople <= 0) {
            throw new IllegalArgumentException("예약 인원은 1명 이상이어야 합니다.");
        }

        LocalTime time = slot.time();

        // 영업 시간 내인지 확인
        if (!popup.openingHours().contains(time)) {
            return false;
        }

        // 날짜와 시간대별 최대 수용 인원 확인
        int capacity = popup.capacityOf(slot);
        return capacity > 0 && (alreadyBooked + requestPeople) <= capacity;
    }

    /**
     * 예약 생성.
     *
     * @param popup       팝업 스토어
     * @param memberId    회원 ID
     * @param slot        예약 일시
     * @param alreadyBooked 현재 이미 예약된 인원 수
     * @param peopleCount 인원 수
     * @param reserverName 예약자 이름
     * @param email       예약자 이메일
     * @return 생성된 예약
     */
    public Reservation createReservation(Popup popup, Long memberId, DateTimeSlot slot, int alreadyBooked, int peopleCount, String reserverName, String email) {
        if (!canReserve(popup, slot, alreadyBooked, peopleCount)) {
            throw new IllegalStateException("예약 조건을 만족하지 않습니다.");
        }
        return Reservation.create(popup.id(), memberId, slot, peopleCount, reserverName, email);
    }

    /**
     * 예약을 방문 완료 처리.
     *
     * @param reservation 예약 엔티티
     * @return 상태가 변경된 예약
     */
    public Reservation markVisited(Reservation reservation) {
        if (reservation == null) {
            throw new IllegalArgumentException("예약 정보는 필수 값입니다.");
        }
        if (reservation.status() == ReservationStatus.VISITED) {
            return reservation; // 이미 방문 처리됨
        }
        return reservation.withStatus(ReservationStatus.VISITED);
    }
}
