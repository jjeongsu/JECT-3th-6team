package com.example.demo.application.service;

import com.example.demo.application.dto.WaitingRequest;
import com.example.demo.application.dto.WaitingCreateRequest;
import com.example.demo.application.dto.WaitingCreateResponse;
import com.example.demo.application.dto.WaitingResponse;
import com.example.demo.application.dto.VisitHistoryCursorResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class WaitingService {

    /**
     * 현장 대기 신청
     */
    @Transactional
    public WaitingCreateResponse createWaiting(WaitingCreateRequest request) {
        // TODO: 실제 비즈니스 로직 구현
        // 1. 팝업 존재 여부 확인
        // 2. 사용자 인증 확인
        // 3. 대기 등록
        // 4. 대기 번호 생성
        
        return new WaitingCreateResponse(
                123L,
                "젠틀몬스터 팝업",
                request.name(),
                request.peopleCount(),
                request.email(),
                7,
                java.time.LocalDateTime.now()
        );
    }

    /**
     * 내 방문/예약 내역 조회 (무한 스크롤)
     */
    public VisitHistoryCursorResponse getVisitHistory(Integer size, Long lastWaitingId, String status) {
        // TODO: 실제 비즈니스 로직 구현
        // 1. 사용자 인증 확인
        // 2. 방문 내역 조회 (커서 기반 페이지네이션)
        // 3. 다음 페이지 존재 여부 확인
        
        return new VisitHistoryCursorResponse(
                java.util.List.of(), // TODO: 실제 데이터로 교체
                121L,
                true
        );
    }
} 