package com.example.demo.application.mapper;

import com.example.demo.application.dto.RatingResponse;
import com.example.demo.application.dto.WaitingCreateResponse;
import com.example.demo.application.dto.WaitingResponse;
import com.example.demo.domain.model.Period;
import com.example.demo.domain.model.Waiting;
import org.springframework.stereotype.Component;

/**
 * Waiting 도메인 모델과 DTO 간의 변환을 담당하는 Mapper.
 */
@Component
public class WaitingDtoMapper {
    
    /**
     * Waiting 도메인 모델을 WaitingCreateResponse DTO로 변환
     */
    public WaitingCreateResponse toCreateResponse(Waiting waiting) {
        return new WaitingCreateResponse(
                waiting.id(),
                waiting.popup().title(),
                waiting.waitingPersonName(),
                waiting.peopleCount(),
                waiting.contactEmail(),
                waiting.waitingNumber(),
                waiting.registeredAt()
        );
    }
    
    /**
     * Waiting 도메인 모델을 WaitingResponse DTO로 변환
     */
    public WaitingResponse toResponse(Waiting waiting) {
        return new WaitingResponse(
                waiting.id(),
                waiting.popup().id(),
                waiting.popup().title(),
                waiting.popup().thumbnails().isEmpty() ? null : waiting.popup().thumbnails().getFirst(),
                waiting.popup().location().region1depthName() + ", " + waiting.popup().location().region2depthName(),
                new RatingResponse(
                        waiting.popup().rating().averageStar(),
                        waiting.popup().rating().reviewCount()
                ),
                formatPeriod(waiting.popup().period()),
                waiting.waitingNumber(),
                waiting.status().name()
        );
    }
    
    /**
     * 기간을 클라이언트 표시용 포맷으로 변환
     */
    private String formatPeriod(Period period) {
        // TODO: 실제 기간 포맷팅 로직 구현
        return "6월 10일 ~ 6월 20일";
    }
} 