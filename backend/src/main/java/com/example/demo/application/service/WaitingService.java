package com.example.demo.application.service;

import com.example.demo.application.dto.VisitHistoryCursorResponse;
import com.example.demo.application.dto.WaitingCreateRequest;
import com.example.demo.application.dto.WaitingCreateResponse;
import com.example.demo.application.dto.WaitingResponse;
import com.example.demo.domain.model.*;
import com.example.demo.domain.port.MemberRepository;
import com.example.demo.domain.port.PopupRepository;
import com.example.demo.domain.port.WaitingRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class WaitingService {

    private final WaitingRepository waitingRepository;
    private final PopupRepository popupRepository;
    private final MemberRepository memberRepository;

    /**
     * 현장 대기 신청
     */
    @Transactional
    public WaitingCreateResponse createWaiting(WaitingCreateRequest request) {
        // 1. 팝업 존재 여부 확인
        var popup = popupRepository.findById(request.popupId())
                .orElseThrow(() -> new IllegalArgumentException("팝업을 찾을 수 없습니다: " + request.popupId()));
        
        // 2. 다음 대기 번호 조회
        Integer nextWaitingNumber = waitingRepository.getNextWaitingNumber(request.popupId());
        
        // 2. 회원 정보 조회
        Member member = memberRepository.findById(request.memberId())
                .orElseThrow(() -> new IllegalArgumentException("회원을 찾을 수 없습니다: " + request.memberId()));
        
        // 3. 대기 정보 생성
        Waiting waiting = new Waiting(
                null, // ID는 저장소에서 생성
                popup,
                request.name(),
                member,
                request.contactEmail(),
                request.peopleCount(),
                nextWaitingNumber,
                WaitingStatus.RESERVED,
                LocalDateTime.now()
        );
        
        // 4. 대기 정보 저장
        Waiting savedWaiting = waitingRepository.save(waiting);
        
        // 5. 응답 생성
        return new WaitingCreateResponse(
                savedWaiting.id(),
                savedWaiting.popup().title(),
                savedWaiting.member().name(),
                savedWaiting.peopleCount(),
                savedWaiting.contactEmail(),
                savedWaiting.waitingNumber(),
                savedWaiting.registeredAt()
        );
    }

    /**
     * 내 방문/예약 내역 조회 (무한 스크롤)
     */
    @Transactional(readOnly = true)
    public VisitHistoryCursorResponse getVisitHistory(Integer size, Long lastWaitingId, String status) {
        // TODO: 사용자 인증 확인 (현재는 임시로 memberId = 1 사용)
        Long memberId = 1L;
        
        // 1. 조회 조건 생성
        WaitingQuery query = WaitingQuery.forVisitHistory(memberId, size, lastWaitingId, status);
        
        // 2. 대기 내역 조회
        List<Waiting> waitings = waitingRepository.findByQuery(query);
        
        // 3. 다음 페이지 존재 여부 확인
        boolean hasNext = waitings.size() == size;
        Long lastId = waitings.isEmpty() ? null : waitings.getLast().id();
        
        // 4. DTO 변환
        List<WaitingResponse> waitingResponses = waitings.stream()
                .map(this::toWaitingResponse)
                .toList();
        
        return new VisitHistoryCursorResponse(waitingResponses, lastId, hasNext);
    }
    
    /**
     * Waiting 도메인 모델을 WaitingResponse DTO로 변환
     */
    private WaitingResponse toWaitingResponse(Waiting waiting) {
        return new WaitingResponse(
                waiting.id(),
                waiting.popup().id(),
                waiting.popup().title(),
                waiting.popup().thumbnails().isEmpty() ? null : waiting.popup().thumbnails().getFirst(),
                waiting.popup().location().region1depthName() + ", " + waiting.popup().location().region2depthName(),
                new com.example.demo.application.dto.RatingResponse(
                        waiting.popup().rating().averageStar(),
                        waiting.popup().rating().reviewCount()
                ),
                formatPeriod(waiting.popup().period()),
                waiting.waitingNumber(),
                waiting.status().name()
        );
    }
    
    /**
     * 기간을 클라이언트 표시용 포맷으로 변환
     */
    private String formatPeriod(Period period) {
        // TODO: 실제 기간 포맷팅 로직 구현
        return "6월 10일 ~ 6월 20일";
    }
} 
