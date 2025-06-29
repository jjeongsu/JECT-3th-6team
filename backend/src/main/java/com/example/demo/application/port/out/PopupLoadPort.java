package com.example.demo.application.port.out;

import com.example.demo.domain.model.Popup;

import java.util.List;
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

    /**
     * 여러 ID로 팝업 엔티티들을 일괄 로드한다.
     *
     * @param popupIds 팝업 ID 목록
     * @return 팝업 도메인 객체 목록
     */
    List<Popup> findByIds(List<Long> popupIds);
} 