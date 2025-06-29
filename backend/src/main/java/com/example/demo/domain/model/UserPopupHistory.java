package com.example.demo.domain.model;

import java.util.List;

/**
 * 사용자 팝업 내역 도메인 모델
 * 
 * @param reservation     예약 정보
 * @param popup          팝업 정보  
 * @param reviewWritten  리뷰 작성 여부
 */
public record UserPopupHistory(
        Reservation reservation,
        Popup popup,
        boolean reviewWritten
) {
    public UserPopupHistory {
        if (reservation == null) {
            throw new IllegalArgumentException("예약 정보는 필수 값입니다.");
        }
        if (popup == null) {
            throw new IllegalArgumentException("팝업 정보는 필수 값입니다.");
        }
    }
    
    /**
     * 예약 ID 반환
     */
    public Long getReservationId() {
        return reservation.id();
    }
    
    /**
     * 팝업 ID 반환  
     */
    public Long getPopupId() {
        return popup.id();
    }
    
    /**
     * 팝업 제목 반환
     */
    public String getPopupTitle() {
        return popup.name();
    }
    
    /**
     * 예약 상태 반환
     */
    public ReservationStatus getReservationStatus() {
        return reservation.status();
    }
    
    /**
     * 팝업 타입 반환
     */
    public PopupType getPopupType() {
        return popup.type();
    }
    
    /**
     * 팝업 카테고리 목록 반환
     */
    public List<Category> getPopupCategories() {
        return popup.categories();
    }
} 