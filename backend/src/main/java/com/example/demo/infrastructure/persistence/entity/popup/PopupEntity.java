package com.example.demo.infrastructure.persistence.entity.popup;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.LocalDate;
import lombok.Getter;

/**
 * 팝업 엔티티.
 * 팝업 기본 정보(제목, 위치, 기간, 타입 등)를 저장한다.
 */
@Entity
@Table(name = "popups")
@Getter
public class PopupEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false, updatable = false)
    private Long id;

    @Column(name = "title", nullable = false)
    private String title;

    @Column(name = "popup_location_id", nullable = false)
    private Long popupLocationId;

    @Enumerated(EnumType.STRING)
    @Column(name = "type", nullable = false)
    private PopupType type;

    @Column(name = "start_date", nullable = false)
    private LocalDate startDate;

    @Column(name = "end_date", nullable = false)
    private LocalDate endDate;


}
