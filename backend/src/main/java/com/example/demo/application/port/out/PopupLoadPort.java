package com.example.demo.application.port.out;

import com.example.demo.domain.model.Popup;

import java.util.Optional;

/**
 * 팝업 조회용 포트.
 */
public interface PopupLoadPort {

    /**
     * ID로 팝업 엔티티를 로드한다.
     * 존재하지 않을 경우 Optional.empty()를 반환할 수 있다.
     *
     * @param popupId 팝업 ID
     * @return 팝업 도메인 객체
     */
    Optional<Popup> findById(Long popupId);
} 