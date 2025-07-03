package com.example.demo.application.mapper;

import com.example.demo.application.dto.PopupDetailResponse;
import com.example.demo.application.dto.popup.BrandStoryDto;
import com.example.demo.application.dto.popup.DayOfWeekInfoDto;
import com.example.demo.application.dto.popup.LocationDto;
import com.example.demo.application.dto.popup.PeriodDto;
import com.example.demo.application.dto.popup.PopupDetailDto;
import com.example.demo.application.dto.popup.RatingDto;
import com.example.demo.application.dto.popup.SearchTagsDto;
import com.example.demo.application.dto.popup.SnsDto;
import com.example.demo.domain.model.BrandStory;
import com.example.demo.domain.model.DayOfWeekInfo;
import com.example.demo.domain.model.Location;
import com.example.demo.domain.model.Period;
import com.example.demo.domain.model.PopupDetail;
import com.example.demo.domain.model.PopupDetailInfo;
import com.example.demo.domain.model.Rating;
import com.example.demo.domain.model.SearchTags;
import com.example.demo.domain.model.Sns;
import java.time.DayOfWeek;
import java.time.format.TextStyle;
import java.util.Locale;
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

    private RatingDto toRatingDto(Rating rating) {
        if (rating == null) return null;
        return new RatingDto(rating.averageStar(), rating.reviewCount());
    }

    private SearchTagsDto toSearchTagsDto(SearchTags tags) {
        if (tags == null) return null;
        return new SearchTagsDto(tags.type(), tags.categoryNames());
    }

    private LocationDto toLocationDto(Location loc) {
        if (loc == null) return null;
        return new LocationDto(
            loc.addressName(),
            loc.region1depthName(),
            loc.region2depthName(),
            loc.region3depthName(),
            loc.longitude(),
            loc.latitude()
        );
    }

    private PeriodDto toPeriodDto(Period period) {
        if (period == null) return null;
        return new PeriodDto(
            period.startDate().toString(),
            period.endDate().toString()
        );
    }

    private BrandStoryDto toBrandStoryDto(BrandStory brandStory) {
        if (brandStory == null) return null;
        return new BrandStoryDto(
            brandStory.imageUrls(),
            brandStory.sns().stream()
                .map(this::toSnsDto)
                .toList()
        );
    }
    private SnsDto toSnsDto(Sns sns) {
        return new SnsDto(sns.icon(), sns.url());
    }

    private PopupDetailDto toPopupDetailDto(PopupDetailInfo detail) {
        if (detail == null) return null;
        return new PopupDetailDto(
            detail.dayOfWeeks().stream()
                .map(this::toDayOfWeekInfoDto)
                .toList(),
            detail.descriptions()
        );
    }
    private DayOfWeekInfoDto toDayOfWeekInfoDto(DayOfWeekInfo d) {
        return new DayOfWeekInfoDto(
            abbreviateDayOfWeek(d.dayOfWeek()),
            d.value()
        );
    }

    private String abbreviateDayOfWeek(String fullName) {
        try {
            DayOfWeek dow = DayOfWeek.valueOf(fullName.toUpperCase());
            return dow.getDisplayName(TextStyle.SHORT, Locale.ENGLISH).toUpperCase();
        } catch (IllegalArgumentException e) {
            return fullName;
        }
    }
}
