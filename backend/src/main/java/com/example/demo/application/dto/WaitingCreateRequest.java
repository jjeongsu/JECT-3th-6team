package com.example.demo.application.dto;

/**
 * 현장 대기 신청 요청 DTO (popupId 포함)
 */
public record WaitingCreateRequest(
    Long popupId,
    Long memberId,
    String name,
    Integer peopleCount,
    String contactEmail
) {
    
    /**
     * 대기 신청 요청을 생성한다.
     * 
     * @param popupId 팝업 ID
     * @param memberId 회원 ID
     * @param name 대기자 이름
     * @param peopleCount 대기 인원수
     * @param contactEmail 연락 이메일
     * @throws IllegalArgumentException 유효하지 않은 인원수인 경우
     */
    public WaitingCreateRequest {
        if (peopleCount == null || peopleCount < 1 || peopleCount > 6) {
            throw new IllegalArgumentException("대기 인원수는 1명 이상 6명 이하여야 합니다. 현재: " + peopleCount);
        }
    }
} 