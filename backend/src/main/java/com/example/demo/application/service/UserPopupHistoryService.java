package com.example.demo.application.service;

import com.example.demo.application.dto.PopupHistoryItemResult;
import com.example.demo.application.dto.UserPopupHistoryQuery;
import com.example.demo.application.dto.UserPopupHistoryResult;
import com.example.demo.application.port.out.UserPopupHistoryLoadPort;
import com.example.demo.domain.model.Category;
import com.example.demo.domain.model.UserPopupHistory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 사용자 팝업 내역 조회 애플리케이션 서비스
 */
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserPopupHistoryService {

    private final UserPopupHistoryLoadPort userPopupHistoryLoadPort;

    /**
     * 사용자의 팝업 예약/방문 내역을 조회한다.
     *
     * @param query 조회 쿼리 정보
     * @return 사용자의 팝업 내역 조회 결과
     */
    public UserPopupHistoryResult getUserPopupHistory(UserPopupHistoryQuery query) {
        // 입력 유효성 검증은 DTO 생성자에서 수행됨
        
        // 도메인 모델로 팝업 내역 조회
        List<UserPopupHistory> domainResults = userPopupHistoryLoadPort.findUserPopupHistory(
                query.userId(),
                query.size(),
                query.cursor()
        );

        // 도메인 모델을 애플리케이션 DTO로 변환
        List<PopupHistoryItemResult> contents = domainResults.stream()
                .map(this::convertToPopupHistoryItemResult)
                .toList();

        // 다음 커서 및 다음 페이지 존재 여부 계산
        String nextCursor = null;
        boolean hasNext = false;

        if (!domainResults.isEmpty()) {
            UserPopupHistory lastItem = domainResults.get(domainResults.size() - 1);
            nextCursor = userPopupHistoryLoadPort.generateNextCursor(lastItem);
            hasNext = userPopupHistoryLoadPort.hasNextPage(query.userId(), query.size(), nextCursor);
            
            // 마지막 페이지인 경우 nextCursor를 null로 설정
            if (!hasNext) {
                nextCursor = null;
            }
        }

        return new UserPopupHistoryResult(contents, nextCursor, hasNext);
    }
    
    /**
     * 도메인 모델을 애플리케이션 DTO로 변환
     */
    private PopupHistoryItemResult convertToPopupHistoryItemResult(UserPopupHistory domainModel) {
        // 카테고리 목록을 문자열 목록으로 변환
        List<String> categoryNames = domainModel.getPopupCategories().stream()
                .map(Category::name)
                .toList();
        
        return new PopupHistoryItemResult(
                domainModel.getReservationId(),
                domainModel.getPopupId(),
                domainModel.getPopupTitle(),
                "", // TODO: 팝업 이미지 URL - 도메인 모델에 추가 필요 (현재는 빈 문자열로 임시 처리)
                domainModel.getReservationStatus().name(),
                domainModel.getPopupType().name(),
                categoryNames,
                domainModel.reviewWritten()
        );
    }
} 