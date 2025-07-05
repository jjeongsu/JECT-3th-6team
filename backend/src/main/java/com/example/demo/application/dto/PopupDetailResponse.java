package com.example.demo.application.dto;

import com.example.demo.application.dto.popup.BrandStoryResponse;
import com.example.demo.application.dto.popup.LocationResponse;
import com.example.demo.application.dto.popup.PeriodResponse;
import com.example.demo.application.dto.popup.PopupDetailInfoResponse;
import com.example.demo.application.dto.popup.RatingResponse;
import com.example.demo.application.dto.popup.SearchTagsResponse;
import java.util.List;

public record PopupDetailResponse(
    Long id,
    List<String> thumbnails,
    int dDay,
    RatingResponse rating,
    String title,
    SearchTagsResponse searchTags,
    LocationResponse location,
    PeriodResponse period,
    BrandStoryResponse brandStory,
    PopupDetailInfoResponse popupDetail
) {}