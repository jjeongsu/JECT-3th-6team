package com.example.demo.infrastructure.persistence.mapper;

import com.example.demo.domain.model.*;
import com.example.demo.infrastructure.persistence.entity.popup.*;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class PopupEntityMapperTest {
    private final PopupEntityMapper popupEntityMapper = new PopupEntityMapper();

    @Test
    @DisplayName("toLocation: PopupLocationEntity -> Location 변환")
    void toLocationTest() {
        PopupLocationEntity entity = PopupLocationEntity.builder()
                .addressName("서울시 강남구")
                .region1DepthName("서울특별시")
                .region2DepthName("강남구")
                .region3DepthName("역삼동")
                .longitude(127.0012)
                .latitude(37.5665)
                .build();

        Location location = popupEntityMapper.toLocation(entity);
        assertThat(location.addressName()).isEqualTo("서울시 강남구");
        assertThat(location.region1depthName()).isEqualTo("서울특별시");
        assertThat(location.region2depthName()).isEqualTo("강남구");
        assertThat(location.region3depthName()).isEqualTo("역삼동");
        assertThat(location.longitude()).isEqualTo(127.0012);
        assertThat(location.latitude()).isEqualTo(37.5665);
    }

    @Test
    @DisplayName("toSearchTags: 카테고리 리스트 -> SearchTags 변환")
    void toSearchTagsTest() {
        PopupCategoryEntity cat1 = PopupCategoryEntity.builder().name("아트").build();
        PopupCategoryEntity cat2 = PopupCategoryEntity.builder().name("패션").build();
        List<PopupCategoryEntity> categories = List.of(cat1, cat2);
        SearchTags tags = popupEntityMapper.toSearchTags("팝업", categories);
        assertThat(tags.type()).isEqualTo("팝업");
        assertThat(tags.categoryNames()).containsExactly("아트", "패션");
    }

    @Test
    @DisplayName("toRating: 리뷰 리스트 -> Rating 변환")
    void toRatingTest() {
        PopupReviewEntity r1 = PopupReviewEntity.builder().rating(5).content("").build();
        PopupReviewEntity r2 = PopupReviewEntity.builder().rating(3).content("").build();
        List<PopupReviewEntity> reviews = List.of(r1, r2);
        Rating rating = popupEntityMapper.toRating(reviews);
        assertThat(rating.averageStar()).isEqualTo(4.0);
        assertThat(rating.reviewCount()).isEqualTo(2);
    }

    @Test
    @DisplayName("toBrandStory: 이미지/SNS 리스트 -> BrandStory 변환")
    void toBrandStoryTest() {
        PopupImageEntity img1 = PopupImageEntity.builder().url("img1.jpg").build();
        PopupImageEntity img2 = PopupImageEntity.builder().url("img2.jpg").build();
        List<PopupImageEntity> images = List.of(img1, img2);

        PopupSocialEntity sns1 = PopupSocialEntity.builder().iconUrl("icon1.png").linkUrl("http://sns1").build();
        PopupSocialEntity sns2 = PopupSocialEntity.builder().iconUrl("icon2.png").linkUrl("http://sns2").build();
        List<PopupSocialEntity> socials = List.of(sns1, sns2);

        BrandStory brandStory = popupEntityMapper.toBrandStory(images, socials);
        assertThat(brandStory.imageUrls()).containsExactly("img1.jpg", "img2.jpg");
        assertThat(brandStory.sns().stream().map(Sns::icon)).containsExactly("icon1.png", "icon2.png");
        assertThat(brandStory.sns().stream().map(Sns::url)).containsExactly("http://sns1", "http://sns2");
    }

    @Test
    @DisplayName("toDayOfWeekInfos: 스케줄 리스트 -> DayOfWeekInfo 변환")
    void toDayOfWeekInfosTest() {
        PopupWeeklyScheduleEntity s1 = PopupWeeklyScheduleEntity.builder()
                .dayOfWeek(DayOfWeek.MONDAY)
                .openTime(LocalTime.of(10, 0))
                .closeTime(LocalTime.of(18, 0))
                .build();
        List<PopupWeeklyScheduleEntity> schedules = List.of(s1);
        List<DayOfWeekInfo> infos = popupEntityMapper.toDayOfWeekInfos(schedules);
        assertThat(infos).hasSize(1);
        assertThat(infos.getFirst().dayOfWeek()).isEqualTo("MONDAY");
        assertThat(infos.getFirst().value()).isEqualTo("10:00 ~ 18:00");
    }
} 