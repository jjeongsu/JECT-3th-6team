package com.example.demo.infrastructure.persistence.repository;

import com.example.demo.infrastructure.persistence.entity.ReservationEntity;
import com.example.demo.infrastructure.persistence.entity.ReservationStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ReservationRepository extends JpaRepository<ReservationEntity, Long> {

    List<ReservationEntity> findAllByPopupTimeSlotIdAndStatus(Long popupTimeSlotId, ReservationStatus status);

}
