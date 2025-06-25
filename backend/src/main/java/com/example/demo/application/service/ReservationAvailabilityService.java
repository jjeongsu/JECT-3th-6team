package com.example.demo.application.service;

import com.example.demo.application.dto.TimeAvailability;
import com.example.demo.application.port.out.PopupLoadPort;
import com.example.demo.application.port.out.ReservationLoadPort;
import com.example.demo.domain.model.DateTimeSlot;
import com.example.demo.domain.model.Popup;
import com.example.demo.domain.model.TimeRange;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Stream;

/**
 * 예약 가능 시간 조회 애플리케이션 서비스.
 */
@Service
@RequiredArgsConstructor
public class ReservationAvailabilityService {

    private final PopupLoadPort popupLoadPort;
    private final ReservationLoadPort reservationLoadPort;

    public List<TimeAvailability> getAvailability(Long popupId, LocalDate date, int peopleCount) {
        if (peopleCount <= 0) {
            throw new IllegalArgumentException("요청 인원은 1명 이상이어야 합니다.");
        }

        Popup popup = popupLoadPort.findById(popupId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 팝업입니다."));

        TimeRange hours = popup.openingHours();

        // 영업 시작 시간부터 1시간 단위로 각 팝업에 해당 시간과 날짜에 예약이 가능한지 확인합니다.
        // TODO 팝업의 예약이 1시간 단위로 할 수 있다는 가정에 따른 코드입니다. 관련 정책 설정과 적용 시점 결정이 필요합니다.
        return Stream.iterate(hours.start(), t -> !t.isAfter(hours.end()), t -> t.plusHours(1))
                .map(time -> {
                    DateTimeSlot slot = new DateTimeSlot(date, time);
                    int capacity = popup.capacityOf(slot);
                    int booked = reservationLoadPort.countBookedPeople(popupId, slot);

                    int totalPeopleCount = booked + peopleCount;
                    boolean available = capacity > 0 && totalPeopleCount <= capacity;
                    return new TimeAvailability(time.toString(), available);
                })
                .toList();
    }
} 