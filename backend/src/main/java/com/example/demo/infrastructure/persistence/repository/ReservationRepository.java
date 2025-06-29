package com.example.demo.infrastructure.persistence.repository;

import com.example.demo.infrastructure.persistence.entity.ReservationEntity;
import com.example.demo.infrastructure.persistence.entity.ReservationStatus;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ReservationRepository extends JpaRepository<ReservationEntity, Long> {

    List<ReservationEntity> findAllByPopupTimeSlotIdAndStatus(Long popupTimeSlotId, ReservationStatus status);

    /**
     * 사용자별 예약 내역을 최신순으로 조회 (첫 페이지)
     */
    @Query("""
        SELECT r FROM ReservationEntity r 
        WHERE r.memberId = :memberId 
        ORDER BY r.id DESC
        """)
    List<ReservationEntity> findUserReservationHistory(@Param("memberId") Long memberId, Pageable pageable);

    /**
     * 사용자별 예약 내역을 커서 기반으로 조회 (다음 페이지)
     */
    @Query("""
        SELECT r FROM ReservationEntity r 
        WHERE r.memberId = :memberId 
        AND r.id < :cursor 
        ORDER BY r.id DESC
        """)
    List<ReservationEntity> findUserReservationHistoryAfterCursor(
            @Param("memberId") Long memberId, 
            @Param("cursor") Long cursor, 
            Pageable pageable);

    /**
     * 커서 이후 추가 데이터 존재 여부 확인
     */
    @Query("""
        SELECT COUNT(r) > 0 FROM ReservationEntity r 
        WHERE r.memberId = :memberId 
        AND r.id < :cursor
        """)
    boolean hasMoreReservationsAfterCursor(@Param("memberId") Long memberId, @Param("cursor") Long cursor);
}
