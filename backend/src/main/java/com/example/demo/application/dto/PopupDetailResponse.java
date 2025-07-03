package com.example.demo.application.dto;

import com.example.demo.application.dto.popup.BrandStoryDto;
import com.example.demo.application.dto.popup.LocationDto;
import com.example.demo.application.dto.popup.PeriodDto;
import com.example.demo.application.dto.popup.PopupDetailDto;
import com.example.demo.application.dto.popup.RatingDto;
import com.example.demo.application.dto.popup.SearchTagsDto;
import java.util.List;

public record PopupDetailResponse(
    Long id,
    List<String> thumbnails,
    int dDay,
    RatingDto rating,
    String title,
    SearchTagsDto searchTags,
    LocationDto location,
    PeriodDto period,
    BrandStoryDto brandStory,
    PopupDetailDto popupDetail
) {}