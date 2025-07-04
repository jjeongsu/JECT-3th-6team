package com.example.demo.application.dto;

/**
 * 현장 대기 신청 요청 DTO
 */
public record WaitingRequest(
    String name,
    Integer peopleCount,
    String email
) {} 