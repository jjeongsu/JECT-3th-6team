package com.example.demo.domain.model;

/**
 * 리뷰 평점 도메인 모델.
 * 평균 별점과 리뷰 수를 나타낸다.
 */
public record Rating(
    double averageStar,
    int reviewCount
) {
}