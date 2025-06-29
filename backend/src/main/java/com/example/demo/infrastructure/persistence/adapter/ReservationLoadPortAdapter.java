package com.example.demo.infrastructure.persistence.adapter;

import com.example.demo.application.port.out.ReservationLoadPort;
import com.example.demo.domain.model.DateTimeSlot;
import com.example.demo.domain.model.Reservation;
import com.example.demo.domain.model.ReservationStatus;
import com.example.demo.infrastructure.persistence.entity.ReservationEntity;
import com.example.demo.infrastructure.persistence.entity.popup.PopupTimeSlotEntity;
import com.example.demo.infrastructure.persistence.repository.ReservationRepository;
import com.example.demo.infrastructure.persistence.repository.popup.PopupTimeSlotRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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
                        .findAllByPopupTimeSlotIdAndStatus(timeSlot.getId(), com.example.demo.infrastructure.persistence.entity.ReservationStatus.RESERVED)
                        .stream()
                        .mapToInt(ReservationEntity::getNumberOfPeople)
                        .sum())
                .orElse(0);
    }

    @Override
    public List<Reservation> findUserReservations(Long userId, Integer size, String cursor) {
        Pageable pageable = PageRequest.of(0, size);
        
        // 예약 엔티티 조회
        List<ReservationEntity> reservationEntities;
        if (cursor == null) {
            reservationEntities = reservationRepository.findUserReservationHistory(userId, pageable);
        } else {
            Long cursorId = Long.parseLong(cursor);
            reservationEntities = reservationRepository.findUserReservationHistoryAfterCursor(userId, cursorId, pageable);
        }

        if (reservationEntities.isEmpty()) {
            return List.of();
        }

        // TimeSlot ID 목록 추출
        List<Long> timeSlotIds = reservationEntities.stream()
                .map(ReservationEntity::getPopupTimeSlotId)
                .distinct()
                .toList();

        // 팝업 시간 슬롯 정보 일괄 조회
        List<PopupTimeSlotEntity> timeSlotEntities = popupTimeSlotRepository.findAllById(timeSlotIds);
        Map<Long, PopupTimeSlotEntity> timeSlotMap = timeSlotEntities.stream()
                .collect(Collectors.toMap(PopupTimeSlotEntity::getId, entity -> entity));

        // 도메인 모델로 변환
        return reservationEntities.stream()
                .map(entity -> convertToReservationDomain(entity, timeSlotMap))
                .toList();
    }

    @Override
    public boolean hasMoreUserReservations(Long userId, String cursor) {
        if (cursor == null) {
            return false;
        }
        
        Long cursorId = Long.parseLong(cursor);
        return reservationRepository.hasMoreReservationsAfterCursor(userId, cursorId);
    }

    /**
     * ReservationEntity를 Reservation 도메인 모델로 변환
     */
    private Reservation convertToReservationDomain(ReservationEntity entity, Map<Long, PopupTimeSlotEntity> timeSlotMap) {
        PopupTimeSlotEntity timeSlotEntity = timeSlotMap.get(entity.getPopupTimeSlotId());
        
        DateTimeSlot dateTimeSlot = new DateTimeSlot(
                timeSlotEntity.getSlotDate(),
                timeSlotEntity.getSlotTime()
        );
        
        return new Reservation(
                entity.getId(),
                entity.getPopupId(),
                entity.getMemberId(),
                dateTimeSlot,
                entity.getNumberOfPeople(),
                entity.getReserverName(),
                entity.getEmail(),
                convertReservationStatus(entity.getStatus())
        );
    }

    /**
     * 예약 상태 변환
     */
    private ReservationStatus convertReservationStatus(
            com.example.demo.infrastructure.persistence.entity.ReservationStatus entityStatus) {
        return switch (entityStatus) {
            case RESERVED -> ReservationStatus.RESERVED;
            case VISITED -> ReservationStatus.VISITED;
        };
    }
}
