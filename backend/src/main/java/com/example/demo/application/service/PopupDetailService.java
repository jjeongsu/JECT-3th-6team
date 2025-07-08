package com.example.demo.application.service;

import com.example.demo.application.dto.PopupDetailResponse;
import com.example.demo.application.mapper.PopupDtoMapper;
import com.example.demo.domain.port.PopupPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class PopupDetailService {

    private final PopupPort popupPort;
    private final PopupDtoMapper popupDtoMapper;

    @Transactional(readOnly = true)
    public PopupDetailResponse getPopupDetail(Long popupId) {
        return popupPort.findDetailById(popupId)
                .map(popupDtoMapper::toResponse)
                .orElseThrow(() -> new IllegalArgumentException("해당 팝업이 존재하지 않습니다."));
    }
}
