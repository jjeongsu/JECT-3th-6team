package com.example.demo.application.service;

import com.example.demo.application.dto.waiting.VisitHistoryCursorResponse;
import com.example.demo.application.dto.waiting.WaitingCreateRequest;
import com.example.demo.application.dto.waiting.WaitingCreateResponse;
import com.example.demo.application.dto.waiting.WaitingResponse;
import com.example.demo.application.mapper.WaitingDtoMapper;
import com.example.demo.domain.model.Member;
import com.example.demo.domain.model.waiting.Waiting;
import com.example.demo.domain.model.waiting.WaitingQuery;
import com.example.demo.domain.model.waiting.WaitingStatus;
import com.example.demo.domain.port.MemberPort;
import com.example.demo.domain.port.NotificationPort;
import com.example.demo.domain.port.PopupPort;
import com.example.demo.domain.port.WaitingPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import com.example.demo.domain.model.CursorResult;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class WaitingService {

    private final WaitingPort waitingPort;
    private final PopupPort popupPort;
    private final MemberPort memberPort;
    private final NotificationPort notificationPort;
    private final WaitingDtoMapper waitingDtoMapper;
    private final WaitingNotificationService waitingNotificationService;

    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("MM.dd");
    private static final DateTimeFormatter DAY_FORMATTER = DateTimeFormatter.ofPattern("E");

    /**
     * 현장 대기 신청
     */
    @Transactional
    public WaitingCreateResponse createWaiting(WaitingCreateRequest request) {
        // 1. 팝업 존재 여부 확인
        var popup = popupPort.findById(request.popupId())
                .orElseThrow(() -> new IllegalArgumentException("팝업을 찾을 수 없습니다: " + request.popupId()));

        // 2. 다음 대기 번호 조회
        Integer nextWaitingNumber = waitingPort.getNextWaitingNumber(request.popupId());

        // 3. 회원 정보 조회
        Member member = memberPort.findById(request.memberId())
                .orElseThrow(() -> new IllegalArgumentException("회원을 찾을 수 없습니다: " + request.memberId()));

        // 4. 대기 정보 생성
        Waiting waiting = new Waiting(
                null, // ID는 저장소에서 생성
                popup,
                request.name(),
                member,
                request.contactEmail(),
                request.peopleCount(),
                nextWaitingNumber,
                WaitingStatus.WAITING,
                LocalDateTime.now()
        );

        // 5. 대기 정보 저장
        Waiting savedWaiting = waitingPort.save(waiting);

        // 7. 새로운 알림 서비스로 모든 알림 처리 위임
        waitingNotificationService.processWaitingCreatedNotifications(savedWaiting);

        // 8. 응답 생성
        return waitingDtoMapper.toCreateResponse(savedWaiting);
    }

    /**
     * 웨이팅 확정 알림 내용 생성
     */
    private String generateWaitingConfirmedContent(Waiting waiting) {
        LocalDateTime registeredAt = waiting.registeredAt();
        String dateText = registeredAt.format(DATE_FORMATTER);
        String dayText = registeredAt.format(DAY_FORMATTER);
        int peopleCount = waiting.peopleCount();

        return String.format("%s (%s) %d인 웨이팅이 완료되었습니다. 현재 대기 번호를 확인해주세요!",
                dateText, dayText, peopleCount);
    }

    /**
     * 내 방문/예약 내역 조회 (무한 스크롤) 또는 단건 조회
     */
    @Transactional(readOnly = true)
    public VisitHistoryCursorResponse getVisitHistory(Long memberId, Integer size, Long lastWaitingId, String status, Long waitingId) {
        
        // waitingId가 있으면 단건 조회
        if (waitingId != null) {
            Waiting waiting = waitingPort.findByQuery(new WaitingQuery(waitingId, null, null, null, null, null))
                    .stream()
                    .findFirst()
                    .orElseThrow(() -> new IllegalArgumentException("대기 정보를 찾을 수 없습니다: " + waitingId));

            // 본인의 대기 정보인지 확인
            if (!waiting.member().id().equals(memberId)) {
                throw new IllegalArgumentException("본인의 대기 정보만 조회할 수 있습니다.");
            }

            WaitingResponse waitingResponse = waitingDtoMapper.toResponse(waiting);
            return new VisitHistoryCursorResponse(List.of(waitingResponse), waitingId, false);
        }

        // 1. 조회 조건 생성
        WaitingQuery query = WaitingQuery.forVisitHistory(memberId, size, lastWaitingId, status);

        // 2. 대기 내역 조회
        List<Waiting> waitings = waitingPort.findByQuery(query);

        // 3. 다음 페이지 존재 여부 확인
        boolean hasNext = waitings.size() == size;
        Long lastId = waitings.isEmpty() ? null : waitings.getLast().id();

        // 4. DTO 변환
        List<WaitingResponse> waitingResponses = waitings.stream()
                .map(waitingDtoMapper::toResponse)
                .toList();

        return new VisitHistoryCursorResponse(waitingResponses, lastId, hasNext);
    }
} 
