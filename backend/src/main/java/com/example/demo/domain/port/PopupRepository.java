package com.example.demo.domain.port;

import com.example.demo.domain.model.PopupDetail;

import java.util.Optional;

/**
 * 팝업 저장소 포트 인터페이스.
 * 팝업 정보의 조회를 담당한다.
 */
public interface PopupRepository {
    
    /**
     * ID로 팝업 정보를 조회한다.
     * 
     * @param id 팝업 ID
     * @return 팝업 정보 (없으면 empty)
     */
    Optional<PopupDetail> findById(Long id);
} 