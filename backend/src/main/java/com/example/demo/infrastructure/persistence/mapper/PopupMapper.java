package com.example.demo.infrastructure.persistence.mapper;

import com.example.demo.domain.model.Location;
import com.example.demo.domain.model.Period;
import com.example.demo.domain.model.PopupDetail;
import com.example.demo.infrastructure.persistence.entity.popup.PopupEntity;
import org.springframework.stereotype.Component;

/**
 * PopupDetail 도메인 모델과 PopupEntity 간의 변환을 담당하는 Mapper.
 */
@Component
public class PopupMapper {
    
    /**
     * PopupEntity를 PopupDetail 도메인 모델로 변환한다.
     * 
     * @param entity PopupEntity
     * @return PopupDetail 도메인 모델
     */
    public PopupDetail toDomain(PopupEntity entity) {
        // PopupEntity의 실제 구조에 맞게 변환
        // 현재는 기본적인 정보만 변환하고, 나머지는 null로 설정
        return new PopupDetail(
                entity.getId(),
                entity.getTitle(),
                null, // thumbnails
                0, // dDay (계산 필요)
                null, // rating
                null, // searchTags
                new Location(null, null, null, null, 0.0, 0.0), // location 정보는 별도 엔티티에서 조회 필요
                new Period(entity.getStartDate(), entity.getEndDate()),
                null, // brandStory
                null  // popupDetail
        );
    }
} 