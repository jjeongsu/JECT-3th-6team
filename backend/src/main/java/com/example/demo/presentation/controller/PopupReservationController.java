package com.example.demo.presentation.controller;

import com.example.demo.application.service.ReservationAvailabilityService;
import com.example.demo.presentation.dto.ReservationTimeSlotDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

/**
 * 팝업 예약 관련 API 컨트롤러
 */
@RestController
@RequestMapping("/api/popups")
@RequiredArgsConstructor
public class PopupReservationController {

    private final ReservationAvailabilityService reservationAvailabilityService;

    /**
     * 사전 예약 가능한 시간대 조회
     *
     * @param popupId 팝업 ID
     * @param date 원하는 예약일 (YYYY-MM-DD 형식)
     * @param peopleCount 원하는 예약 인원
     * @return 예약 가능한 시간대 목록
     */
    @GetMapping("/{popupId}/reservations/available-times")
    public ResponseEntity<List<ReservationTimeSlotDto>> getAvailableTimes(
            @PathVariable Long popupId,
            @RequestParam LocalDate date,
            @RequestParam int peopleCount) {
        return ResponseEntity.ok(reservationAvailabilityService.getAvailability(popupId, date, peopleCount)
                .stream()
                .map(it -> new ReservationTimeSlotDto(it.time(), it.available()))
                .toList()
        );
    }
} 