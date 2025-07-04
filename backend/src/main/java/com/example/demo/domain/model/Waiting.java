package com.example.demo.domain.model;

import java.time.LocalDateTime;

/**
 * 대기 도메인 모델.
 * 팝업에 대한 대기 정보를 나타낸다.
 */
public record Waiting(
    Long id,
    PopupDetail popup,
    Member member,
    String contactEmail,
    Integer peopleCount,
    Integer waitingNumber,
    WaitingStatus status,
    LocalDateTime registeredAt
) {
    
    /**
     * 대기 정보를 생성한다.
     * 
     * @param id 대기 ID
     * @param popup 팝업 정보
     * @param name 대기자 이름
     * @param peopleCount 대기 인원수
     * @param email 대기자 이메일
     * @param waitingNumber 대기 번호
     * @param status 대기 상태
     * @param registeredAt 등록 시간
     * @throws IllegalArgumentException 유효하지 않은 인원수인 경우
     */
    public Waiting {
        if (peopleCount == null || peopleCount < 1 || peopleCount > 6) {
            throw new IllegalArgumentException("대기 인원수는 1명 이상 6명 이하여야 합니다. 현재: " + peopleCount);
        }
    }
} 