package com.example.demo.application.service;

import com.example.demo.domain.model.notification.Notification;
import com.example.demo.domain.model.notification.ScheduledNotification;
import com.example.demo.domain.model.notification.ScheduledNotificationTrigger;
import com.example.demo.domain.model.waiting.Waiting;
import com.example.demo.domain.model.waiting.WaitingDomainEvent;
import com.example.demo.domain.model.waiting.WaitingEventType;
import com.example.demo.domain.port.NotificationPort;
import com.example.demo.domain.port.ScheduledNotificationPort;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

/**
 * 웨이팅 관련 알림 정책을 관리하는 서비스.
 * 알림 생성 규칙과 스케줄링 로직을 담당한다.
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class WaitingNotificationService {

    private final NotificationPort notificationPort;
    private final ScheduledNotificationPort scheduledNotificationPort;

    // === 알림 정책 상수 ===
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("MM.dd");
    private static final DateTimeFormatter DAY_FORMATTER = DateTimeFormatter.ofPattern("E");
    private static final int AVERAGE_WAITING_TIME_MINUTES = 15; // 팀당 평균 대기 시간

    /**
     * 웨이팅 생성 시 알림 정책 실행.
     * 1. 즉시 알림 (WAITING_CONFIRMED) 발송
     * 2. 미래 알림들 스케줄링
     */
    @Transactional
    public void processWaitingCreatedNotifications(Waiting waiting) {
        // 1. 즉시 알림 발송
        sendWaitingConfirmedNotification(waiting);

        // 2. 미래 알림들 스케줄링
        List<ScheduledNotification> scheduledNotifications = createScheduledNotifications(waiting);
        scheduledNotifications.forEach(scheduledNotificationPort::save);

        log.info("웨이팅 알림 처리 완료 - 웨이팅 ID: {}, 스케줄된 알림: {}개",
                waiting.id(), scheduledNotifications.size());
    }

    /**
     * 웨이팅 확정 알림 즉시 발송 (WAITING_CONFIRMED 정책)
     */
    private void sendWaitingConfirmedNotification(Waiting waiting) {
        String content = generateWaitingConfirmedContent(waiting);

        WaitingDomainEvent event = new WaitingDomainEvent(waiting, WaitingEventType.WAITING_CONFIRMED);
        Notification notification = Notification.builder()
                .member(waiting.member())
                .event(event)
                .content(content)
                .build();

        notificationPort.save(notification);
        log.debug("웨이팅 확정 알림 발송 - 웨이팅 ID: {}", waiting.id());
    }

    /**
     * 미래 알림들 스케줄 생성
     */
    private List<ScheduledNotification> createScheduledNotifications(Waiting waiting) {
        List<ScheduledNotification> schedules = new ArrayList<>();

        LocalDateTime estimatedEnterTime = calculateEstimatedEnterTime(waiting);

        // 1. 입장 시작 알림 (ENTER_NOW 정책)
        schedules.add(createEnterNowSchedule(waiting, estimatedEnterTime));

        // 2. 입장 시간 초과 알림 (ENTER_TIME_OVER 정책) 
        schedules.add(createEnterTimeOverSchedule(waiting, estimatedEnterTime));

        // 3. 리뷰 요청 알림 (REVIEW_REQUEST 정책)
        schedules.add(createReviewRequestSchedule(waiting, estimatedEnterTime));

        // 4. 3팀 전 알림 (ENTER_3TEAMS_BEFORE 정책) - 동적 트리거
        schedules.add(createEnter3TeamsBeforeSchedule(waiting));

        return schedules;
    }

    /**
     * 입장 시작 알림 스케줄 생성 (ENTER_NOW 정책)
     * 정책: 예상 입장 시간에 "지금 매장으로 입장 부탁드립니다" 알림
     */
    private ScheduledNotification createEnterNowSchedule(Waiting waiting, LocalDateTime enterTime) {
        WaitingDomainEvent event = new WaitingDomainEvent(waiting, WaitingEventType.ENTER_NOW);
        Notification notification = Notification.builder()
                .member(waiting.member())
                .event(event)
                .content("지금 매장으로 입장 부탁드립니다. 즐거운 시간 보내세요!")
                .build();

        return new ScheduledNotification(notification, ScheduledNotificationTrigger.WAITING_ENTER_NOW);
    }

    /**
     * 입장 시간 초과 알림 스케줄 생성 (ENTER_TIME_OVER 정책)
     * 정책: 입장 시간 + 5분 후 "입장 시간이 초과되었습니다" 알림
     */
    private ScheduledNotification createEnterTimeOverSchedule(Waiting waiting, LocalDateTime enterTime) {
        WaitingDomainEvent event = new WaitingDomainEvent(waiting, WaitingEventType.ENTER_TIME_OVER);
        Notification notification = Notification.builder()
                .member(waiting.member())
                .event(event)
                .content("입장 시간이 초과되었습니다. 빠른 입장 부탁드립니다! 입장이 지연될 경우 웨이팅이 취소될 수 있습니다.")
                .build();

        return new ScheduledNotification(notification, ScheduledNotificationTrigger.WAITING_ENTER_TIME_OVER);
    }

    /**
     * 리뷰 요청 알림 스케줄 생성 (REVIEW_REQUEST 정책)
     * 정책: 입장 시간 + 2시간 후 "방문하신 팝업은 어떠셨나요?" 알림
     */
    private ScheduledNotification createReviewRequestSchedule(Waiting waiting, LocalDateTime enterTime) {
        WaitingDomainEvent event = new WaitingDomainEvent(waiting, WaitingEventType.REVIEW_REQUEST);
        Notification notification = Notification.builder()
                .member(waiting.member())
                .event(event)
                .content("방문하신 팝업은 어떠셨나요? 소중한 후기를 남겨주세요!")
                .build();

        return new ScheduledNotification(notification, ScheduledNotificationTrigger.WAITING_REVIEW_REQUEST);
    }

    /**
     * 3팀 전 알림 스케줄 생성 (ENTER_3TEAMS_BEFORE 정책)
     * 정책: 대기 순번이 4번째가 되었을 때 "앞으로 3팀 남았습니다" 알림
     */
    private ScheduledNotification createEnter3TeamsBeforeSchedule(Waiting waiting) {
        WaitingDomainEvent event = new WaitingDomainEvent(waiting, WaitingEventType.ENTER_3TEAMS_BEFORE);
        Notification notification = Notification.builder()
                .member(waiting.member())
                .event(event)
                .content("앞으로 3팀 남았습니다! 순서가 다가오니 매장 근처에서 대기해주세요!")
                .build();

        return new ScheduledNotification(notification, ScheduledNotificationTrigger.WAITING_ENTER_3TEAMS_BEFORE);
    }

    // === 알림 정책 유틸리티 메서드들 ===

    /**
     * 예상 입장 시간 계산 정책
     * 정책: 현재 시간 + (앞의 대기팀 수 × 15분)
     */
    private LocalDateTime calculateEstimatedEnterTime(Waiting waiting) {
        int teamsAhead = waiting.waitingNumber() - 1;
        int estimatedWaitMinutes = teamsAhead * AVERAGE_WAITING_TIME_MINUTES;
        return waiting.registeredAt().plusMinutes(estimatedWaitMinutes);
    }

    /**
     * 웨이팅 확정 알림 내용 생성 정책
     * 정책: "MM.dd (요일) N인 웨이팅이 완료되었습니다. 현재 대기 번호를 확인해주세요!"
     */
    private String generateWaitingConfirmedContent(Waiting waiting) {
        LocalDateTime registeredAt = waiting.registeredAt();
        String dateText = registeredAt.format(DATE_FORMATTER);
        String dayText = registeredAt.format(DAY_FORMATTER);
        int peopleCount = waiting.peopleCount();

        return String.format("%s (%s) %d인 웨이팅이 완료되었습니다. 현재 대기 번호를 확인해주세요!",
                dateText, dayText, peopleCount);
    }
}