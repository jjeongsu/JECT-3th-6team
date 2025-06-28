package com.example.demo.infrastructure.persistence.adapter;

import com.example.demo.application.port.out.PopupLoadPort;
import com.example.demo.domain.model.CapacitySchedule;
import com.example.demo.domain.model.DateTimeSlot;
import com.example.demo.domain.model.Popup;
import com.example.demo.infrastructure.persistence.entity.popup.PopupTimeSlotEntity;
import com.example.demo.infrastructure.persistence.repository.popup.PopupRepository;
import com.example.demo.infrastructure.persistence.repository.popup.PopupTimeSlotRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Map;
import java.util.Optional;

import static java.util.stream.Collectors.toMap;

@Repository
@RequiredArgsConstructor
public class PopupLoadPortAdapter implements PopupLoadPort {
    private final PopupRepository popupRepository;
    private final PopupTimeSlotRepository popupTimeSlotRepository;
    
    @Override
    public Optional<Popup> findById(Long popupId) {
        Map<DateTimeSlot, Integer> data = popupTimeSlotRepository.findAllByPopupId(popupId)
                .stream()
                .collect(toMap(
                        it -> new DateTimeSlot(it.getSlotDate(), it.getSlotTime()),
                        PopupTimeSlotEntity::getAvailableCount
                ));

        return popupRepository.findById(popupId)
                .map(it -> {
                    return new Popup(
                            it.getId(),
                            it.getTitle(),
                            null, // TODO 관련 인프라 구성 요소 추가 필요
                            new CapacitySchedule(data),
                            null // TODO 도메인에 요일 별 영업 시간 기능 구현 안되어있는 부분 수정 필요
                    );
                });
    }
}
