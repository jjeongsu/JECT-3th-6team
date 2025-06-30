package com.example.demo.domain.model;

import java.util.List;

/**
 * 검색 태그 도메인 모델.
 * 팝업 타입과 카테고리 이름 리스트를 포함한다.
 */
public record SearchTags(
    String type,
    List<String> categoryNames
) {}