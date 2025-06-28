package com.example.demo.application.mapper;

import com.example.demo.application.dto.PopupRawData;
import com.example.demo.domain.model.BrandStory;
import com.example.demo.domain.model.DayOfWeekInfo;
import com.example.demo.domain.model.Location;
import com.example.demo.domain.model.Period;
import com.example.demo.domain.model.PopupDetail;
import com.example.demo.domain.model.PopupDetailInfo;
import com.example.demo.domain.model.Rating;
import com.example.demo.domain.model.SearchTags;
import com.example.demo.domain.model.Sns;
import com.example.demo.infrastructure.persistence.entity.popup.PopupCategoryEntity;
import com.example.demo.infrastructure.persistence.entity.popup.PopupContentEntity;
import com.example.demo.infrastructure.persistence.entity.popup.PopupEntity;
import com.example.demo.infrastructure.persistence.entity.popup.PopupImageEntity;
import com.example.demo.infrastructure.persistence.entity.popup.PopupLocationEntity;
import com.example.demo.infrastructure.persistence.entity.popup.PopupReviewEntity;
import java.util.List;
import org.springframework.stereotype.Component;

@Component
public class PopupDetailMapper {

    public PopupDetail toDomain(PopupRawData raw) {

        PopupEntity popup = raw.popup();

        // 1. 썸네일
        List<String> thumbnails = raw.mainImages().stream()
            .map(PopupImageEntity::getUrl)
            .toList();

        // 2. D-day
        int dDay = (int) java.time.temporal.ChronoUnit.DAYS.between(
            java.time.LocalDate.now(), popup.getEndDate());

        // 3. Rating
        List<Integer> ratings = raw.reviews().stream()
            .map(PopupReviewEntity::getRating)
            .toList();
        double average = ratings.isEmpty() ? 0.0 :
            Math.round(ratings.stream().mapToInt(r -> r).average().orElse(0.0) * 10.0) / 10.0;
        Rating rating = new Rating(average, ratings.size());

        // 4. SearchTags
        List<String> categories = raw.categories().stream()
            .map(PopupCategoryEntity::getName)
            .toList();
        SearchTags searchTags = new SearchTags(popup.getType().name(), categories);

        // 5. Location
        PopupLocationEntity loc = raw.location();
        Location location = new Location(
            loc.getAddressName(),
            loc.getRegion1DepthName(),
            loc.getRegion2DepthName(),
            loc.getRegion3DepthName() != null ? loc.getRegion3DepthName() : "",
            loc.getLongitude(),
            loc.getLatitude()
        );

        // 6. Period
        Period period = new Period(popup.getStartDate(), popup.getEndDate());

        // 7. BrandStory
        List<String> brandImages = raw.descriptionImages().stream()
            .map(PopupImageEntity::getUrl)
            .toList();

        List<Sns> snsList = raw.socials().stream()
            .map(s -> new Sns(s.getIconUrl(), s.getLinkUrl()))
            .toList();
        BrandStory brandStory = new BrandStory(brandImages, snsList);

        // 8. PopupDetailInfo
        List<DayOfWeekInfo> dayOfWeeks = raw.schedules().stream()
            .map(sch -> new DayOfWeekInfo(
                sch.getDayOfWeek().name(),
                sch.getOpenTime() + " ~ " + sch.getCloseTime()
            ))
            .toList();

        List<String> descriptions = raw.contents().stream()
            .map(PopupContentEntity::getContentText)
            .toList();

        PopupDetailInfo popupDetailInfo = new PopupDetailInfo(dayOfWeeks, descriptions);

        return new PopupDetail(
            popup.getId(),
            popup.getTitle(),
            thumbnails,
            dDay,
            rating,
            searchTags,
            location,
            period,
            brandStory,
            popupDetailInfo
        );
    }
}
