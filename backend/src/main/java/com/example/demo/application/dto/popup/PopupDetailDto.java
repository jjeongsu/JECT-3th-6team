package com.example.demo.application.dto.popup;

import java.util.List;

public record PopupDetailDto(
    List<DayOfWeekInfoDto> dayOfWeeks,
    List<String> descriptions
) {
}
