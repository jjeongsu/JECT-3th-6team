package com.example.demo.application.dto.popup;

import java.util.List;

public record BrandStoryDto(
    List<String> imageUrls,
    List<SnsDto> sns
) {}