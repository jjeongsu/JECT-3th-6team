package com.example.demo.presentation.controller;

import com.example.demo.application.dto.PopupDetailResponse;
import com.example.demo.application.service.PopupDetailReadService;
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

    private final PopupDetailReadService popupDetailReadService;

    @GetMapping("/{popupId}")
    public ResponseEntity<PopupDetailResponse> getPopupDetail(@PathVariable Long popupId) {
        return ResponseEntity.ok(popupDetailReadService.getPopupDetail(popupId));
    }
}
