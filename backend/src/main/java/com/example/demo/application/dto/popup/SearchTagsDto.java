package com.example.demo.application.dto.popup;

import java.util.List;

public record SearchTagsDto(
    String type,
    List<String> category
) {
}
