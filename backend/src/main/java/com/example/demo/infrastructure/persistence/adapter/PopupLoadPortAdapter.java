package com.example.demo.infrastructure.persistence.adapter;

import com.example.demo.application.dto.PopupRawData;
import com.example.demo.application.mapper.PopupDetailMapper;
import com.example.demo.application.port.out.PopupLoadPort;
import com.example.demo.domain.model.Popup;
import com.example.demo.domain.model.PopupDetail;
import com.example.demo.infrastructure.persistence.entity.popup.PopupImageType;
import com.example.demo.infrastructure.persistence.repository.popup.PopupCategoryRepository;
import com.example.demo.infrastructure.persistence.repository.popup.PopupContentRepository;
import com.example.demo.infrastructure.persistence.repository.popup.PopupImageRepository;
import com.example.demo.infrastructure.persistence.repository.popup.PopupLocationRepository;
import com.example.demo.infrastructure.persistence.repository.popup.PopupRepository;
import com.example.demo.infrastructure.persistence.repository.popup.PopupReviewRepository;
import com.example.demo.infrastructure.persistence.repository.popup.PopupSocialRepository;
import com.example.demo.infrastructure.persistence.repository.popup.PopupWeeklyScheduleRepository;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class PopupLoadPortAdapter implements PopupLoadPort {
    private final PopupRepository popupRepository;
    private final PopupImageRepository popupImageRepository;
    private final PopupCategoryRepository popupCategoryRepository;
    private final PopupContentRepository popupContentRepository;
    private final PopupLocationRepository popupLocationRepository;
    private final PopupReviewRepository popupReviewRepository;
    private final PopupSocialRepository popupSocialRepository;
    private final PopupWeeklyScheduleRepository popupWeeklyScheduleRepository;
    private final PopupDetailMapper popupDetailMapper;



    @Override
    public Optional<Popup> findById(Long popupId) {
        return Optional.empty();
    }

    @Override
    public Optional<PopupDetail> findDetailById(Long popupId) {
        return popupRepository.findById(popupId).map(popup -> {

            PopupRawData rawData = new PopupRawData(
                popup,
                popupImageRepository.findAllByPopupIdAndTypeOrderBySortOrderAsc(popupId, PopupImageType.MAIN),
                popupCategoryRepository.findAllByPopupId(popupId),
                popupLocationRepository.findById(popup.getPopupLocationId())
                    .orElseThrow(() -> new IllegalStateException("Location not found")),
                popupReviewRepository.findAllByPopupId(popupId),
                popupImageRepository.findAllByPopupIdAndTypeOrderBySortOrderAsc(popupId, PopupImageType.DESCRIPTION),
                popupSocialRepository.findAllByPopupIdOrderBySortOrderAsc(popupId),
                popupWeeklyScheduleRepository.findAllByPopupId(popupId),
                popupContentRepository.findAllByPopupIdOrderBySortOrderAsc(popupId)
            );


            return popupDetailMapper.toDomain(rawData);
        });
    }
}
