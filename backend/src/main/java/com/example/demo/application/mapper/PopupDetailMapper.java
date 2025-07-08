package com.example.demo.application.mapper;

import com.example.demo.application.dto.PopupDetailResponse;
import com.example.demo.application.dto.popup.*;
import com.example.demo.domain.model.*;
import org.springframework.stereotype.Component;

@Component
public class PopupDetailMapper {

    public PopupDetailResponse toResponse(PopupDetail popup) {
        return new PopupDetailResponse(
            popup.id(),
            popup.thumbnails(),
            popup.dDay(),
            toRatingDto(popup.rating()),
            popup.title(),
            toSearchTagsDto(popup.searchTags()),
            toLocationDto(popup.location()),
            toPeriodDto(popup.period()),
            toBrandStoryDto(popup.brandStory()),
            toPopupDetailDto(popup.popupDetail())
        );
    }

    private RatingResponse toRatingDto(Rating rating) {
        if (rating == null) return null;
        return new RatingResponse(rating.averageStar(), rating.reviewCount());
    }

    private SearchTagsResponse toSearchTagsDto(SearchTags tags) {
        if (tags == null) return null;
        return new SearchTagsResponse(tags.type(), tags.categoryNames());
    }

    private LocationResponse toLocationDto(Location loc) {
        if (loc == null) return null;
        return new LocationResponse(
            loc.addressName(),
            loc.region1depthName(),
            loc.region2depthName(),
            loc.region3depthName(),
            loc.longitude(),
            loc.latitude()
        );
    }

    private PeriodResponse toPeriodDto(Period period) {
        if (period == null) return null;
        return new PeriodResponse(
            period.startDate().toString(),
            period.endDate().toString()
        );
    }

    private BrandStoryResponse toBrandStoryDto(BrandStory brandStory) {
        if (brandStory == null) return null;
        return new BrandStoryResponse(
            brandStory.imageUrls(),
            brandStory.sns().stream()
                .map(this::toSnsDto)
                .toList()
        );
    }
    private SnsResponse toSnsDto(Sns sns) {
        return new SnsResponse(sns.icon(), sns.url());
    }

    private PopupDetailInfoResponse toPopupDetailDto(PopupDetailInfo detail) {
        if (detail == null) return null;
        return new PopupDetailInfoResponse(
            detail.dayOfWeeks().stream()
                .map(this::toDayOfWeekInfoDto)
                .toList(),
            detail.descriptions()
        );
    }
    private DayOfWeekInfoResponse toDayOfWeekInfoDto(DayOfWeekInfo d) {
        return new DayOfWeekInfoResponse(
            d.dayOfWeek(),
            d.value()
        );
    }
}
