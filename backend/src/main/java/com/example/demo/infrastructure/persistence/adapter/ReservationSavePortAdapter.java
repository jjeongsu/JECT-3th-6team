package com.example.demo.infrastructure.persistence.adapter;

import com.example.demo.application.port.out.ReservationSavePort;
import com.example.demo.domain.model.DateTimeSlot;
import com.example.demo.domain.model.Reservation;
import com.example.demo.infrastructure.persistence.entity.ReservationEntity;
import com.example.demo.infrastructure.persistence.entity.ReservationStatus;
import com.example.demo.infrastructure.persistence.entity.popup.PopupTimeSlotEntity;
import com.example.demo.infrastructure.persistence.repository.ReservationRepository;
import com.example.demo.infrastructure.persistence.repository.popup.PopupTimeSlotRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class ReservationSavePortAdapter implements ReservationSavePort {
    private final ReservationRepository reservationRepository;
    private final PopupTimeSlotRepository popupTimeSlotRepository;
    @Override
    public Reservation save(Reservation reservation) {
        DateTimeSlot slot = reservation.slot();
        PopupTimeSlotEntity timeSlotEntity = popupTimeSlotRepository.findByPopupIdAndSlotDateAndSlotTime(
                reservation.popupId(),
                slot.date(),
                slot.time()
        ).orElseThrow(() -> new IllegalArgumentException("예약할 수 없는 시간입니다."));

        ReservationEntity reservationEntity = ReservationEntity.builder()
                .memberId(reservation.memberId())
                .popupId(reservation.popupId())
                .popupTimeSlotId(timeSlotEntity.getId())
                .reserverName("") // TODO 예약자 이름 관련 도메인 기능 추가
                .numberOfPeople(reservation.peopleCount())
                .email("") // TODO 예약에 이메일 관련 도메인 기능 추가. 회원의 이메일 혹은 입력으로부터 이메일
                .status(ReservationStatus.valueOf(reservation.status().name())) // TODO 매핑 로직 변경 필요
                .build();

        ReservationEntity savedEntity = reservationRepository.save(reservationEntity);
        return reservation.withId(savedEntity.getId());
    }
}
