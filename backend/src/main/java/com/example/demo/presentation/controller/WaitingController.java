package com.example.demo.presentation.controller;

import com.example.demo.application.dto.WaitingRequest;
import com.example.demo.application.dto.WaitingCreateRequest;
import com.example.demo.application.dto.WaitingCreateResponse;
import com.example.demo.application.dto.VisitHistoryCursorResponse;
import com.example.demo.application.service.WaitingService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


import java.util.Map;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class WaitingController {

    private final WaitingService waitingService;

    /**
     * 현장 대기 신청
     */
    @PostMapping("/popups/{popupId}/waitings")
    public ResponseEntity<Map<String, Object>> createWaiting(
            @PathVariable Long popupId,
            @RequestBody WaitingRequest request
    ) {
        WaitingCreateRequest createRequest = new WaitingCreateRequest(
                popupId,
                request.name(),
                request.peopleCount(),
                request.email()
        );
        WaitingCreateResponse response = waitingService.createWaiting(createRequest);
        
        return ResponseEntity.ok(Map.of(
            "message", "성공적으로 대기가 등록되었습니다.",
            "data", response
        ));
    }

    /**
     * 내 방문/예약 내역 조회 (무한 스크롤)
     */
    @GetMapping("/me/visits")
    public ResponseEntity<Map<String, Object>> getVisitHistory(
            @RequestParam(defaultValue = "10") Integer size,
            @RequestParam(required = false) Long lastWaitingId,
            @RequestParam(required = false) String status
    ) {
        VisitHistoryCursorResponse response = waitingService.getVisitHistory(size, lastWaitingId, status);
        
        return ResponseEntity.ok(Map.of(
            "message", "성공적으로 조회되었습니다.",
            "data", response
        ));
    }
} 