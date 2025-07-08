package com.example.demo.application.dto;

import java.time.LocalDateTime;

/**
 * 현장 대기 신청 응답 DTO
 */
public record WaitingCreateResponse(
    Long waitingId,
    String popupName,
    String name,
    Integer peopleCount,
    String email,
    Integer waitingNumber,
    LocalDateTime registeredAt
) {} 