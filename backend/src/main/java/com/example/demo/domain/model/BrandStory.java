package com.example.demo.domain.model;

import java.util.List;

/**
 * 브랜드 스토리 도메인 모델.
 * 브랜드 이미지 URL 리스트와 SNS 정보 리스트를 포함한다.
 */
public record BrandStory(
    List<String> imageUrls,
    List<Sns> sns
) {}
