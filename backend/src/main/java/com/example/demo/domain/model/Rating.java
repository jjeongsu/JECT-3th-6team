package com.example.demo.domain.model;

import java.util.List;

/**
 * 리뷰 평점 도메인 모델.
 * 평균 별점과 리뷰 수를 나타낸다.
 */
public record Rating(
    double averageStar,
    int reviewCount
) {
    public static Rating from(List<Integer> ratings) {
        if (ratings == null || ratings.isEmpty()) {
            return new Rating(0.0, 0);
        }
        double average = ratings.stream()
            .mapToInt(Integer::intValue)
            .average()
            .orElse(0.0);
        average = Math.round(average * 10.0) / 10.0;
        return new Rating(average, ratings.size());
    }
}