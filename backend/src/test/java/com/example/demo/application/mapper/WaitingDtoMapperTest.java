package com.example.demo.application.mapper;

import com.example.demo.application.dto.WaitingCreateResponse;
import com.example.demo.application.dto.WaitingResponse;
import com.example.demo.domain.model.*;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class WaitingDtoMapperTest {

    @Nested
    @DisplayName("toCreateResponse 테스트")
    class Test01 {
        // 테스트용 유효한 데이터
        private final PopupDetail validPopup = new PopupDetail(
            1L, "테스트 팝업", List.of("thumbnail1.jpg"), 5,
            new Rating(4.5, 100), new SearchTags("팝업", List.of("테스트")),
            new Location("서울시 강남구", "서울특별시", "강남구", "역삼동", 127.0012, 37.5665),
            new Period(LocalDate.now(), LocalDate.now().plusDays(30)),
            new BrandStory(List.of("brand1.jpg"), List.of()),
            new PopupDetailInfo(List.of(), List.of("상세 설명"))
        );

        private final Member validMember = new Member(1L, "테스트 사용자", "test@example.com");

        @Test
        @DisplayName("정상적인 Waiting을 WaitingCreateResponse로 변환 테스트")
        public void test01() {
            // given
            LocalDateTime registeredAt = LocalDateTime.now();
            Waiting waiting = new Waiting(
                1L, validPopup, "홍길동", validMember,
                "hong@example.com", 2, 1, WaitingStatus.RESERVED, registeredAt
            );

            // when
            WaitingCreateResponse result = new WaitingDtoMapper().toCreateResponse(waiting);

            // then
            assertNotNull(result);
            assertEquals(1L, result.waitingId());
            assertEquals("테스트 팝업", result.popupName());
            assertEquals("홍길동", result.name());
            assertEquals(2, result.peopleCount());
            assertEquals("hong@example.com", result.email());
            assertEquals(1, result.waitingNumber());
            assertEquals(registeredAt, result.registeredAt());
        }

        @Test
        @DisplayName("다른 상태의 Waiting을 WaitingCreateResponse로 변환 테스트")
        public void test02() {
            // given
            LocalDateTime registeredAt = LocalDateTime.now();
            Waiting waiting = new Waiting(
                2L, validPopup, "김철수", validMember,
                "kim@example.com", 3, 2, WaitingStatus.COMPLETED, registeredAt
            );

            // when
            WaitingCreateResponse result = new WaitingDtoMapper().toCreateResponse(waiting);

            // then
            assertNotNull(result);
            assertEquals(2L, result.waitingId());
            assertEquals("테스트 팝업", result.popupName());
            assertEquals("김철수", result.name());
            assertEquals(3, result.peopleCount());
            assertEquals("kim@example.com", result.email());
            assertEquals(2, result.waitingNumber());
            assertEquals(registeredAt, result.registeredAt());
        }
    }

    @Nested
    @DisplayName("toResponse 테스트")
    class Test02 {
        // 테스트용 유효한 데이터
        private final PopupDetail validPopup = new PopupDetail(
            1L, "테스트 팝업", List.of("thumbnail1.jpg", "thumbnail2.jpg"), 5,
            new Rating(4.5, 100), new SearchTags("팝업", List.of("테스트")),
            new Location("서울시 강남구", "서울특별시", "강남구", "역삼동", 127.0012, 37.5665),
            new Period(LocalDate.now(), LocalDate.now().plusDays(30)),
            new BrandStory(List.of("brand1.jpg"), List.of()),
            new PopupDetailInfo(List.of(), List.of("상세 설명"))
        );

        private final Member validMember = new Member(1L, "테스트 사용자", "test@example.com");

        @Test
        @DisplayName("정상적인 Waiting을 WaitingResponse로 변환 테스트")
        public void test01() {
            // given
            LocalDateTime registeredAt = LocalDateTime.now();
            Waiting waiting = new Waiting(
                1L, validPopup, "홍길동", validMember,
                "hong@example.com", 2, 1, WaitingStatus.RESERVED, registeredAt
            );

            // when
            WaitingResponse result = new WaitingDtoMapper().toResponse(waiting);

            // then
            assertNotNull(result);
            assertEquals(1L, result.waitingId());
            assertEquals(1L, result.popupId());
            assertEquals("테스트 팝업", result.popupName());
            assertEquals("thumbnail1.jpg", result.popupImageUrl()); // 첫 번째 썸네일
            assertEquals("서울특별시, 강남구", result.location());
            assertNotNull(result.rating());
            assertEquals(4.5, result.rating().averageStar());
            assertEquals(100, result.rating().reviewCount());
            assertEquals("6월 10일 ~ 6월 20일", result.period()); // 하드코딩된 값
            assertEquals(1, result.waitingNumber());
            assertEquals("RESERVED", result.status());
        }

        @Test
        @DisplayName("썸네일이 없는 Waiting을 WaitingResponse로 변환 테스트")
        public void test02() {
            // given
            PopupDetail popupWithoutThumbnails = new PopupDetail(
                2L, "썸네일 없는 팝업", List.of(), 5,
                new Rating(3.5, 50), new SearchTags("팝업", List.of("테스트")),
                new Location("서울시 강남구", "서울특별시", "강남구", "역삼동", 127.0012, 37.5665),
                new Period(LocalDate.now(), LocalDate.now().plusDays(30)),
                new BrandStory(List.of("brand1.jpg"), List.of()),
                new PopupDetailInfo(List.of(), List.of("상세 설명"))
            );

            LocalDateTime registeredAt = LocalDateTime.now();
            Waiting waiting = new Waiting(
                2L, popupWithoutThumbnails, "김철수", validMember,
                "kim@example.com", 3, 2, WaitingStatus.COMPLETED, registeredAt
            );

            // when
            WaitingResponse result = new WaitingDtoMapper().toResponse(waiting);

            // then
            assertNotNull(result);
            assertEquals(2L, result.waitingId());
            assertEquals(2L, result.popupId());
            assertEquals("썸네일 없는 팝업", result.popupName());
            assertNull(result.popupImageUrl()); // 썸네일이 없으므로 null
            assertEquals("서울특별시, 강남구", result.location());
            assertNotNull(result.rating());
            assertEquals(3.5, result.rating().averageStar());
            assertEquals(50, result.rating().reviewCount());
            assertEquals("6월 10일 ~ 6월 20일", result.period());
            assertEquals(2, result.waitingNumber());
            assertEquals("COMPLETED", result.status());
        }

        @Test
        @DisplayName("다른 지역의 Waiting을 WaitingResponse로 변환 테스트")
        public void test03() {
            // given
            PopupDetail popupInGangnam = new PopupDetail(
                3L, "강남 팝업", List.of("gangnam.jpg"), 5,
                new Rating(4.8, 200), new SearchTags("팝업", List.of("강남")),
                new Location("서울시 강남구", "서울특별시", "강남구", "청담동", 127.0012, 37.5665),
                new Period(LocalDate.now(), LocalDate.now().plusDays(30)),
                new BrandStory(List.of("brand1.jpg"), List.of()),
                new PopupDetailInfo(List.of(), List.of("상세 설명"))
            );

            LocalDateTime registeredAt = LocalDateTime.now();
            Waiting waiting = new Waiting(
                3L, popupInGangnam, "박영희", validMember,
                "park@example.com", 4, 3, WaitingStatus.RESERVED, registeredAt
            );

            // when
            WaitingResponse result = new WaitingDtoMapper().toResponse(waiting);

            // then
            assertNotNull(result);
            assertEquals(3L, result.waitingId());
            assertEquals(3L, result.popupId());
            assertEquals("강남 팝업", result.popupName());
            assertEquals("gangnam.jpg", result.popupImageUrl());
            assertEquals("서울특별시, 강남구", result.location());
            assertNotNull(result.rating());
            assertEquals(4.8, result.rating().averageStar());
            assertEquals(200, result.rating().reviewCount());
            assertEquals("6월 10일 ~ 6월 20일", result.period());
            assertEquals(3, result.waitingNumber());
            assertEquals("RESERVED", result.status());
        }
    }
} 