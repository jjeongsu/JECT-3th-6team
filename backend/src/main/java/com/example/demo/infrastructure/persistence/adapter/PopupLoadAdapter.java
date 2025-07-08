package com.example.demo.infrastructure.persistence.adapter;

import com.example.demo.domain.model.*;
import com.example.demo.domain.port.PopupLoadPort;
import com.example.demo.infrastructure.persistence.entity.popup.PopupContentEntity;
import com.example.demo.infrastructure.persistence.entity.popup.PopupImageEntity;
import com.example.demo.infrastructure.persistence.entity.popup.PopupImageType;
import com.example.demo.infrastructure.persistence.mapper.PopupEntityMapper;
import com.example.demo.infrastructure.persistence.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Optional;

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
    private final PopupRepository popupRepository;
    private final PopupEntityMapper popupEntityMapper;

    @Override
    public Optional<PopupDetail> findDetailById(Long popupId) {
        return popupRepository.findById(popupId).map(popup -> {

            List<String> thumbnails = popupImageRepository.findAllByPopupIdAndTypeOrderBySortOrderAsc(popupId, PopupImageType.MAIN)
                    .stream()
                    .map(PopupImageEntity::getUrl)
                    .toList();

            Rating rating = popupEntityMapper.toRating(
                    popupReviewRepository.findAllByPopupId(popupId)
            );

            SearchTags searchTags = popupEntityMapper.toSearchTags(
                    popup.getType().name(),
                    popupCategoryRepository.findAllByPopupId(popupId)
            );

            Location location = popupEntityMapper.toLocation(
                    popupLocationRepository.findById(popup.getPopupLocationId())
                            .orElseThrow(() -> new IllegalStateException("존재하지 않는 팝업 위치입니다."))
            );

            Period period = new Period(popup.getStartDate(), popup.getEndDate());
            int dDay = (int) ChronoUnit.DAYS.between(LocalDate.now(), popup.getEndDate());

            BrandStory brandStory = popupEntityMapper.toBrandStory(
                    popupImageRepository.findAllByPopupIdAndTypeOrderBySortOrderAsc(popupId, PopupImageType.DESCRIPTION),
                    popupSocialRepository.findAllByPopupIdOrderBySortOrderAsc(popupId)
            );

            List<DayOfWeekInfo> dayOfWeeks = popupEntityMapper.toDayOfWeekInfos(
                    popupWeeklyScheduleRepository.findAllByPopupId(popupId)
            );

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