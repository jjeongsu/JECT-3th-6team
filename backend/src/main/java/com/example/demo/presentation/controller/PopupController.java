package com.example.demo.presentation.controller;

import com.example.demo.application.dto.PopupDetailResponse;
import com.example.demo.application.service.PopupDetailService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/popups")
@RequiredArgsConstructor
public class PopupController {

    private final PopupDetailService popupDetailService;

    @GetMapping("/{popupId}")
    public ResponseEntity<PopupDetailResponse> getPopupDetail(@PathVariable Long popupId) {
        return ResponseEntity.ok(popupDetailService.getPopupDetail(popupId));
    }
}
