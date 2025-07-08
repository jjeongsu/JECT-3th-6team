package com.example.demo.domain.port;

import com.example.demo.domain.model.PopupDetail;

import java.util.Optional;

public interface PopupPort {
    /**
     * ID로 팝업 상세 정보를 조회한다.
     * 존재하지 않을 경우 Optional.empty()를 반환한다.
     *
     * @param popupId 조회할 팝업의 고유 식별자
     * @return 팝업 상세 도메인 모델 (없으면 Optional.empty())
     */
    Optional<PopupDetail> findDetailById(Long popupId);

}