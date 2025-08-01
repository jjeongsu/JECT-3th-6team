package com.example.demo.presentation.controller;

import com.example.demo.application.dto.PopupDetailResponse;
import com.example.demo.application.dto.popup.PopupCursorResponse;
import com.example.demo.application.dto.popup.PopupFilterRequest;
import com.example.demo.application.dto.popup.PopupMapRequest;
import com.example.demo.application.dto.popup.PopupMapResponse;
import com.example.demo.application.service.PopupService;
import com.example.demo.common.security.UserPrincipal;
import com.example.demo.presentation.ApiResponse;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/popups")
@RequiredArgsConstructor
public class PopupController {

    private final PopupService popupService;

    @GetMapping("/map")
    public ApiResponse<List<PopupMapResponse>> getPopupsOnMap(PopupMapRequest request) {
        List<PopupMapResponse> response = popupService.getPopupsOnMap(request);
        return new ApiResponse<>("지도 내 팝업 조회가 성공했습니다.", response);
    }
  
    @GetMapping
    public ResponseEntity<ApiResponse<PopupCursorResponse>> getPopups(@Valid PopupFilterRequest request){
        PopupCursorResponse response = popupService.getFilteredPopups(request);
        return ResponseEntity.ok(new ApiResponse<>("팝업 목록 조회에 성공했습니다.", response));
    }

    @GetMapping("/{popupId}")
    public ResponseEntity<ApiResponse<PopupDetailResponse>> getPopupDetail(@PathVariable Long popupId,@AuthenticationPrincipal UserPrincipal principal) {
        if (principal == null) {
            throw new IllegalStateException("인증된 사용자 정보를 가져올 수 없습니다.");
        }
        PopupDetailResponse popupDetail = popupService.getPopupDetail(popupId, principal.getId());
        return ResponseEntity.ok(new ApiResponse<>("팝업 상세 조회가 성공했습니다.", popupDetail));
    }
}
