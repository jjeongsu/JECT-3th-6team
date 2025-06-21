package com.example.demo.presentation.controller;

import com.example.demo.presentation.dto.ReservationAvailableTimesResponse;
import com.example.demo.presentation.dto.ReservationTimeSlotDto;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 팝업 예약 관련 API 컨트롤러
 */
@RestController
@RequestMapping("/api/popups")
public class PopupReservationController {

    /**
     * 사전 예약 가능한 시간대 조회
     *
     * @param popupId 팝업 ID
     * @param date 원하는 예약일 (YYYY-MM-DD 형식)
     * @param peopleCount 원하는 예약 인원
     * @return 예약 가능한 시간대 목록
     */
    @GetMapping("/{popupId}/reservations/available-times")
    public ResponseEntity<ReservationAvailableTimesResponse> getAvailableTimes(
            @PathVariable Long popupId,
            @RequestParam String date,
            @RequestParam Integer peopleCount) {
        
        // TODO: 애플리케이션 서비스 호출하여 예약 가능한 시간대 조회
        // 현재는 임시 데이터 반환
        List<ReservationTimeSlotDto> sampleTimes = List.of(
                new ReservationTimeSlotDto("11:00", true),
                new ReservationTimeSlotDto("12:00", false),
                new ReservationTimeSlotDto("13:00", true),
                new ReservationTimeSlotDto("14:00", true),
                new ReservationTimeSlotDto("15:00", false)
        );
        
        ReservationAvailableTimesResponse response = new ReservationAvailableTimesResponse(sampleTimes);
        return ResponseEntity.ok(response);
    }
} 