package com.example.demo.application.dto;

/**
 * 방문 내역 조회에서 사용할 Waiting 객체 응답 DTO
 */
public record WaitingResponse(
    Long waitingId,
    Long popupId,
    String popupName,
    String popupImageUrl,
    String location,
    RatingResponse rating,
    String period,
    Integer waitingNumber,
    String status
) {} 