package com.example.demo.infrastructure.persistence.adapter;

import com.example.demo.application.port.out.ReservationLoadPort;
import com.example.demo.domain.model.DateTimeSlot;
import com.example.demo.infrastructure.persistence.entity.ReservationEntity;
import com.example.demo.infrastructure.persistence.entity.ReservationStatus;
import com.example.demo.infrastructure.persistence.repository.ReservationRepository;
import com.example.demo.infrastructure.persistence.repository.popup.PopupTimeSlotRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class ReservationLoadPortAdapter implements ReservationLoadPort {
    private final PopupTimeSlotRepository popupTimeSlotRepository;
    private final ReservationRepository reservationRepository;

    @Override
    public int countBookedPeople(Long popupId, DateTimeSlot slot) {
        // TODO join 이용한 쿼리로 성능 개선 가능
        return popupTimeSlotRepository
                .findByPopupIdAndSlotDateAndSlotTime(popupId, slot.date(), slot.time())
                .map(timeSlot -> reservationRepository
                        .findAllByPopupTimeSlotIdAndStatus(timeSlot.getId(), ReservationStatus.RESERVED)
                        .stream()
                        .mapToInt(ReservationEntity::getNumberOfPeople)
                        .sum())
                .orElse(0);
    }
}
