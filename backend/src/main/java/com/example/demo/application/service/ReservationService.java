package com.example.demo.application.service;

import com.example.demo.application.dto.ReservationCreateResponse;
import com.example.demo.application.port.out.PopupLoadPort;
import com.example.demo.application.port.out.ReservationLoadPort;
import com.example.demo.application.port.out.ReservationSavePort;
import com.example.demo.domain.model.DateTimeSlot;
import com.example.demo.domain.model.Popup;
import com.example.demo.domain.model.Reservation;
import com.example.demo.domain.service.ReservationDomainService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

/**
 * 예약 생성/관리 애플리케이션 서비스
 */
@Service
@RequiredArgsConstructor
@Transactional
public class ReservationService {

    private final PopupLoadPort popupLoadPort;
    private final ReservationLoadPort reservationLoadPort;
    private final ReservationSavePort reservationSavePort;
    private final ReservationDomainService reservationDomainService;

    /**
     * 예약 생성
     *
     * @param popupId 팝업 ID
     * @param memberId 회원 ID
     * @param reserverName 예약자 이름
     * @param numberOfPeople 예약 인원
     * @param email 예약자 이메일
     * @param reservationDatetime 예약 일시
     * @return 생성된 예약 정보
     */
    public ReservationCreateResponse createReservation(
            long popupId,
            long memberId,
            String reserverName, 
            int  numberOfPeople,
            String email, 
            LocalDateTime reservationDatetime
    ) {
        
        validateInput(reserverName, numberOfPeople, email, reservationDatetime);
        
        Popup popup = popupLoadPort.findById(popupId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 팝업입니다. popupId: " + popupId));
        
        DateTimeSlot slot = new DateTimeSlot(reservationDatetime.toLocalDate(), reservationDatetime.toLocalTime());
        
        int alreadyBooked = reservationLoadPort.countBookedPeople(popupId, slot);
        
        Reservation reservation = reservationDomainService.createReservation(
                popup, memberId, slot, alreadyBooked, numberOfPeople, reserverName, email);
        
        Reservation savedReservation = reservationSavePort.save(reservation);
        
        return new ReservationCreateResponse(
                savedReservation.id(),
                savedReservation.popupId(),
                savedReservation.reserverName(),
                savedReservation.peopleCount(),
                savedReservation.email(),
                LocalDateTime.of(savedReservation.slot().date(), savedReservation.slot().time()),
                savedReservation.status().name()
        );
    }
    
    private void validateInput(String reserverName, int numberOfPeople, String email, LocalDateTime reservationDatetime) {
        if (reserverName == null || reserverName.trim().isEmpty()) {
            throw new IllegalArgumentException("예약자 이름은 필수입니다.");
        }
        if (numberOfPeople <= 0) {
            throw new IllegalArgumentException("예약 인원은 1명 이상이어야 합니다.");
        }
        if (email == null || email.trim().isEmpty()) {
            throw new IllegalArgumentException("이메일은 필수입니다.");
        }
        if (reservationDatetime == null) {
            throw new IllegalArgumentException("예약 일시는 필수입니다.");
        }
        if (reservationDatetime.isBefore(LocalDateTime.now())) {
            throw new IllegalArgumentException("과거 시간으로는 예약할 수 없습니다.");
        }
    }
} 