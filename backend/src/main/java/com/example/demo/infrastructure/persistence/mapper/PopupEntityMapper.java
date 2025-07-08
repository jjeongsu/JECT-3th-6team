package com.example.demo.infrastructure.persistence.mapper;

import com.example.demo.domain.model.*;
import com.example.demo.infrastructure.persistence.entity.popup.*;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * PopupDetail 도메인 모델과 PopupEntity 간의 변환을 담당하는 Mapper.
 */
@Component
public class PopupEntityMapper {

    public Location toLocation(PopupLocationEntity loc) {
        return new Location(
                loc.getAddressName(),
                loc.getRegion1DepthName(),
                loc.getRegion2DepthName(),
                loc.getRegion3DepthName(),
                loc.getLongitude(),
                loc.getLatitude()
        );
    }

    public SearchTags toSearchTags(String type, List<PopupCategoryEntity> categories) {
        List<String> categoryNames = categories.stream()
                .map(PopupCategoryEntity::getName)
                .toList();
        return new SearchTags(type, categoryNames);
    }

    public Rating toRating(List<PopupReviewEntity> reviews) {
        List<Integer> ratings = reviews.stream()
                .map(PopupReviewEntity::getRating)
                .toList();
        return Rating.from(ratings);
    }

    public BrandStory toBrandStory(List<PopupImageEntity> images, List<PopupSocialEntity> socials) {
        List<String> imageUrls = images.stream()
                .map(PopupImageEntity::getUrl)
                .toList();

        List<Sns> snsList = socials.stream()
                .map(s -> new Sns(s.getIconUrl(), s.getLinkUrl()))
                .toList();

        return new BrandStory(imageUrls, snsList);
    }

    public List<DayOfWeekInfo> toDayOfWeekInfos(List<PopupWeeklyScheduleEntity> schedules) {
        return schedules.stream()
                .map(s -> new DayOfWeekInfo(
                        s.getDayOfWeek().name(),
                        s.getOpenTime() + " ~ " + s.getCloseTime()
                ))
                .toList();
    }
}