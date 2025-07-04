package com.example.demo.application.dto;

/**
 * 현장 대기 신청 요청 DTO (popupId 포함)
 */
public record WaitingCreateRequest(
    Long popupId,
    String name,
    Integer peopleCount,
    String email
) {} 