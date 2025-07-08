package com.example.demo.application.mapper;

import com.example.demo.application.dto.PopupDetailResponse;
import com.example.demo.application.dto.popup.*;
import com.example.demo.domain.model.*;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class PopupDtoMapperTest {
    private final PopupDtoMapper mapper = new PopupDtoMapper();

    @Test
    @DisplayName("toResponse: PopupDetail -> PopupDetailResponse 변환")
    void toResponseTest() {
        // given
        Rating rating = new Rating(4.5, 100);
        SearchTags searchTags = new SearchTags("팝업", List.of("아트", "패션"));
        Location location = new Location("서울시 강남구", "서울특별시", "강남구", "역삼동", 127.0012, 37.5665);
        Period period = new Period(LocalDate.of(2024, 1, 1), LocalDate.of(2024, 1, 31));
        Sns sns1 = new Sns("icon1.png", "http://sns1");
        Sns sns2 = new Sns("icon2.png", "http://sns2");
        BrandStory brandStory = new BrandStory(List.of("img1.jpg", "img2.jpg"), List.of(sns1, sns2));
        DayOfWeekInfo dayInfo = new DayOfWeekInfo("MONDAY", "10:00 ~ 18:00");
        PopupDetailInfo detailInfo = new PopupDetailInfo(List.of(dayInfo), List.of("상세 설명1", "상세 설명2"));
        PopupDetail popup = new PopupDetail(
                1L,
                "테스트 팝업",
                List.of("thumbnail1.jpg", "thumbnail2.jpg"),
                5,
                rating,
                searchTags,
                location,
                period,
                brandStory,
                detailInfo
        );

        // when
        PopupDetailResponse dto = mapper.toResponse(popup);

        // then
        assertThat(dto.id()).isEqualTo(1L);
        assertThat(dto.title()).isEqualTo("테스트 팝업");
        assertThat(dto.thumbnails()).containsExactly("thumbnail1.jpg", "thumbnail2.jpg");
        assertThat(dto.dDay()).isEqualTo(5);
        assertThat(dto.rating()).isEqualTo(new RatingResponse(4.5, 100));
        assertThat(dto.searchTags().type()).isEqualTo("팝업");
        assertThat(dto.searchTags().category()).containsExactly("아트", "패션");
        assertThat(dto.location()).isEqualTo(new LocationResponse("서울시 강남구", "서울특별시", "강남구", "역삼동", 127.0012, 37.5665));
        assertThat(dto.period()).isEqualTo(new PeriodResponse("2024-01-01", "2024-01-31"));
        assertThat(dto.brandStory().imageUrls()).containsExactly("img1.jpg", "img2.jpg");
        assertThat(dto.brandStory().sns()).containsExactly(
                new SnsResponse("icon1.png", "http://sns1"),
                new SnsResponse("icon2.png", "http://sns2")
        );
        assertThat(dto.popupDetail().dayOfWeeks()).containsExactly(new DayOfWeekInfoResponse("MONDAY", "10:00 ~ 18:00"));
        assertThat(dto.popupDetail().descriptions()).containsExactly("상세 설명1", "상세 설명2");
    }
} 