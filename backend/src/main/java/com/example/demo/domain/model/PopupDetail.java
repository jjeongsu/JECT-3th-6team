package com.example.demo.domain.model;

import java.util.List;

/**
 * 팝업 상세 도메인 모델.
 * 팝업의 기본 정보와 상세 정보, 브랜딩, 위치, 기간 등을 포함한다.
 */
public record PopupDetail(
    Long id,
    String title,
    List<String> thumbnails,
    int dDay,
    Rating rating,
    SearchTags searchTags,
    Location location,
    Period period,
    BrandStory brandStory,
    PopupDetailInfo popupDetail
) {}
