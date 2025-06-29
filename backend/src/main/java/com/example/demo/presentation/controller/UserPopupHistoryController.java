package com.example.demo.presentation.controller;

import com.example.demo.application.dto.PopupHistoryItemResult;
import com.example.demo.application.dto.UserPopupHistoryQuery;
import com.example.demo.application.dto.UserPopupHistoryResult;
import com.example.demo.application.service.UserPopupHistoryService;
import com.example.demo.presentation.dto.PopupHistoryItem;
import com.example.demo.presentation.dto.PopupHistoryRequest;
import com.example.demo.presentation.dto.PopupHistoryResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 사용자 팝업 내역 관련 API 컨트롤러
 */
@RestController
@RequestMapping("/api/users/popups")
@RequiredArgsConstructor
public class UserPopupHistoryController {

    private final UserPopupHistoryService userPopupHistoryService;

    /**
     * 사용자 팝업 예약/방문 내역 조회
     *
     * @param size   한 페이지에 조회할 항목 수 (기본값: 10)
     * @param cursor 다음 페이지를 조회하기 위한 커서 값
     * @return 사용자의 팝업 예약/방문 내역 목록
     */
    @GetMapping("/history")
    public ResponseEntity<PopupHistoryResponse> getPopupHistory(
            @RequestParam(required = false) Integer size,
            @RequestParam(required = false) String cursor) {
        
        // TODO: 현재는 사용자 ID를 1로 고정 (인증 기능 추가 시 수정 필요)
        Long userId = 1L;
        
        // 요청 파라미터를 DTO로 변환
        PopupHistoryRequest request = new PopupHistoryRequest(size, cursor);
        
        // 애플리케이션 계층 쿼리 생성
        UserPopupHistoryQuery query = new UserPopupHistoryQuery(
                userId,
                request.getSize(),
                request.cursor()
        );
        
        // 애플리케이션 서비스 호출
        UserPopupHistoryResult result = userPopupHistoryService.getUserPopupHistory(query);
        
        // 애플리케이션 결과를 프레젠테이션 응답으로 변환
        List<PopupHistoryItem> contents = result.contents().stream()
                .map(this::convertToPopupHistoryItem)
                .toList();
        
        PopupHistoryResponse response = new PopupHistoryResponse(
                contents,
                result.nextCursor(),
                result.hasNext()
        );
        
        return ResponseEntity.ok(response);
    }
    
    /**
     * 애플리케이션 계층의 PopupHistoryItemResult를 프레젠테이션 계층의 PopupHistoryItem으로 변환
     */
    private PopupHistoryItem convertToPopupHistoryItem(PopupHistoryItemResult item) {
        return new PopupHistoryItem(
                item.reservationId(),
                item.popupId(),
                item.popupTitle(),
                item.popupImageUrl(),
                item.reservationStatus(),
                item.popupType(),
                item.popupCategories(),
                item.reviewWritten()
        );
    }
} 