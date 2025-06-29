package com.example.demo.infrastructure.persistence.repository.popup;

import com.example.demo.infrastructure.persistence.entity.popup.PopupCategoryEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PopupCategoryRepository extends JpaRepository<PopupCategoryEntity, Long> {
    
    /**
     * 팝업 ID로 카테고리 목록 조회
     */
    List<PopupCategoryEntity> findByPopupId(Long popupId);
    
    /**
     * 여러 팝업 ID들로 카테고리 목록 조회
     */
    List<PopupCategoryEntity> findByPopupIdIn(List<Long> popupIds);
}
