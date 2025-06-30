package com.example.demo.presentation.controller;

import com.example.demo.application.dto.ReservationCreateInput;
import com.example.demo.application.dto.ReservationCreateResult;
import com.example.demo.application.service.ReservationAvailabilityService;
import com.example.demo.application.service.ReservationService;
import com.example.demo.presentation.dto.ReservationCreateRequest;
import com.example.demo.presentation.dto.ReservationCreateResponse;
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
    private final ReservationService reservationService;

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

    /**
     * 팝업 사전 예약 신청
     *
     * @param popupId 팝업 ID
     * @param request 예약 신청 요청 정보
     * @return 생성된 예약 정보
     */
    @PostMapping("/{popupId}/reservations")
    public ResponseEntity<ReservationCreateResponse> createReservation(
            @PathVariable Long popupId,
            @RequestBody ReservationCreateRequest request) {
        
        // TODO: 현재는 회원 ID를 1로 고정 (인증 기능 추가 시 수정 필요)
        Long memberId = 1L;

        // 프레젠테이션 DTO를 애플리케이션 DTO로 변환
        ReservationCreateInput applicationRequest = new ReservationCreateInput(
                popupId,
                memberId,
                request.reserverName(),
                request.numberOfPeople(),
                request.email(),
                request.reservationDatetime()
        );

        ReservationCreateResult applicationResponse = reservationService.createReservation(applicationRequest);
        
        // 애플리케이션 DTO를 프레젠테이션 DTO로 변환
        ReservationCreateResponse response = new ReservationCreateResponse(
                applicationResponse.reservationId(),
                applicationResponse.popupId(),
                applicationResponse.reserverName(),
                applicationResponse.numberOfPeople(),
                applicationResponse.email(),
                applicationResponse.reservationDatetime(),
                applicationResponse.status()
        );
        
        return ResponseEntity.ok(response);
    }
} 