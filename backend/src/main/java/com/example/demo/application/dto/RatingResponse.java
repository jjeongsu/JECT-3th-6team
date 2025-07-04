package com.example.demo.application.dto;

/**
 * 평점 정보를 담는 응답 DTO
 */
public record RatingResponse(
    Double averageStar,
    Integer reviewCount
) {} 