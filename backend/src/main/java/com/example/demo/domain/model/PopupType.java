package com.example.demo.domain.model;

/**
 * 팝업스토어 타입
 * 
 * @param EXPERIENTIAL 체험형 팝업
 * @param PRE_ORDER 선주문형 팝업
 */
public enum PopupType {
    EXPERIENTIAL("체험형"),
    PRE_ORDER("선주문형");
    
    private final String description;
    
    PopupType(String description) {
        this.description = description;
    }
    
    public String getDescription() {
        return description;
    }
} 