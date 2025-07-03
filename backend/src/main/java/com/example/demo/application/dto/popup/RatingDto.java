package com.example.demo.application.dto.popup;

import com.example.demo.domain.model.Rating;

public record RatingDto(
    double averageStar,
    int reviewCount
) {
}
