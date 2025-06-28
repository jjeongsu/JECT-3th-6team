package com.example.demo.domain.model;

public record Rating(
    double averageStar,
    int reviewCount
) {}