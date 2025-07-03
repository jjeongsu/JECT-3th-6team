package com.example.demo.infrastructure.persistence.adapter;

import com.example.demo.application.mapper.PopupDetailMapper;
import com.example.demo.domain.model.BrandStory;
import com.example.demo.domain.model.DayOfWeekInfo;
import com.example.demo.domain.model.Location;
import com.example.demo.domain.model.Period;
import com.example.demo.domain.model.PopupDetail;
import com.example.demo.domain.model.PopupDetailInfo;
import com.example.demo.domain.model.Rating;
import com.example.demo.domain.model.SearchTags;
import com.example.demo.domain.model.Sns;
import com.example.demo.domain.port.PopupLoadPort;
import com.example.demo.infrastructure.persistence.entity.popup.PopupCategoryEntity;
import com.example.demo.infrastructure.persistence.entity.popup.PopupContentEntity;
import com.example.demo.infrastructure.persistence.entity.popup.PopupImageEntity;
import com.example.demo.infrastructure.persistence.entity.popup.PopupImageType;
import com.example.demo.infrastructure.persistence.entity.popup.PopupLocationEntity;
import com.example.demo.infrastructure.persistence.entity.popup.PopupReviewEntity;
import com.example.demo.infrastructure.persistence.repository.PopupCategoryRepository;
import com.example.demo.infrastructure.persistence.repository.PopupContentRepository;
import com.example.demo.infrastructure.persistence.repository.PopupImageRepository;
import com.example.demo.infrastructure.persistence.repository.PopupLocationRepository;
import com.example.demo.infrastructure.persistence.repository.PopupRepository;
import com.example.demo.infrastructure.persistence.repository.PopupReviewRepository;
import com.example.demo.infrastructure.persistence.repository.PopupSocialRepository;
import com.example.demo.infrastructure.persistence.repository.PopupWeeklyScheduleRepository;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class PopupLoadAdapter implements PopupLoadPort {
    private final PopupImageRepository popupImageRepository;
    private final PopupCategoryRepository popupCategoryRepository;
    private final PopupContentRepository popupContentRepository;
    private final PopupLocationRepository popupLocationRepository;
    private final PopupReviewRepository popupReviewRepository;
    private final PopupSocialRepository popupSocialRepository;
    private final PopupWeeklyScheduleRepository popupWeeklyScheduleRepository;
    private final PopupDetailMapper popupDetailMapper;
    private final PopupRepository popupRepository;

    @Override
    public Optional<PopupDetail> findDetailById(Long popupId) {
        return popupRepository.findById(popupId).map(popup -> {

            List<String> thumbnails = popupImageRepository.findAllByPopupIdAndTypeOrderBySortOrderAsc(popupId, PopupImageType.MAIN)
                .stream()
                .map(PopupImageEntity::getUrl)
                .toList();

            List<Integer> ratingValues = popupReviewRepository.findAllByPopupId(popupId)
                .stream()
                .map(PopupReviewEntity::getRating)
                .toList();
            Rating rating = Rating.from(ratingValues);

            List<String> categories = popupCategoryRepository.findAllByPopupId(popupId)
                .stream()
                .map(PopupCategoryEntity::getName)
                .toList();
            SearchTags searchTags = new SearchTags(popup.getType().name(), categories);

            PopupLocationEntity loc = popupLocationRepository.findById(popup.getPopupLocationId())
                .orElseThrow(() -> new IllegalStateException("존재하지 않는 팝업 위치입니다."));
            Location location = new Location(
                loc.getAddressName(),
                loc.getRegion1DepthName(),
                loc.getRegion2DepthName(),
                loc.getRegion3DepthName() != null ? loc.getRegion3DepthName() : "",
                loc.getLongitude(),
                loc.getLatitude()
            );

            Period period = new Period(popup.getStartDate(), popup.getEndDate());
            int dDay = (int) ChronoUnit.DAYS.between(LocalDate.now(), popup.getEndDate());

            List<String> brandImages = popupImageRepository.findAllByPopupIdAndTypeOrderBySortOrderAsc(popupId, PopupImageType.DESCRIPTION)
                .stream()
                .map(PopupImageEntity::getUrl)
                .toList();

            List<Sns> snsList = popupSocialRepository.findAllByPopupIdOrderBySortOrderAsc(popupId)
                .stream()
                .map(s -> new Sns(s.getIconUrl(), s.getLinkUrl()))
                .toList();
            BrandStory brandStory = new BrandStory(brandImages, snsList);

            List<DayOfWeekInfo> dayOfWeeks = popupWeeklyScheduleRepository.findAllByPopupId(popupId)
                .stream()
                .map(s -> new DayOfWeekInfo(
                    s.getDayOfWeek().name(),
                    s.getOpenTime() + " ~ " + s.getCloseTime()
                ))
                .toList();

            List<String> descriptions = popupContentRepository.findAllByPopupIdOrderBySortOrderAsc(popupId)
                .stream()
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
        });
    }

}
