package com.example.demo.infrastructure.persistence.mapper;

import com.example.demo.domain.model.PopupDetail;
import com.example.demo.infrastructure.persistence.entity.popup.PopupEntity;
import com.example.demo.infrastructure.persistence.entity.popup.PopupType;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Nested;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class PopupMapperTest {

    @Nested
    @DisplayName("toDomain 테스트")
    class Test01 {

        @Test
        @DisplayName("정상적인 PopupEntity를 PopupDetail 도메인 모델로 변환 테스트")
        public void test01() {
            // given
            LocalDate startDate = LocalDate.now();
            LocalDate endDate = LocalDate.now().plusDays(30);
            
            PopupEntity entity = PopupEntity.builder()
                .id(1L)
                .title("테스트 팝업")
                .popupLocationId(1L)
                .type(PopupType.EXPERIENTIAL)
                .startDate(startDate)
                .endDate(endDate)
                .build();

            // when
            PopupDetail result = new PopupMapper().toDomain(entity);

            // then
            assertNotNull(result);
            assertEquals(1L, result.id());
            assertEquals("테스트 팝업", result.title());
            assertNull(result.thumbnails()); // 현재 구현에서는 null
            assertEquals(0, result.dDay()); // 현재 구현에서는 0
            assertNull(result.rating()); // 현재 구현에서는 null
            assertNull(result.searchTags()); // 현재 구현에서는 null
            assertNotNull(result.location()); // Location 객체는 생성됨
            assertNotNull(result.period()); // Period 객체는 생성됨
            assertEquals(startDate, result.period().startDate());
            assertEquals(endDate, result.period().endDate());
            assertNull(result.brandStory()); // 현재 구현에서는 null
            assertNull(result.popupDetail()); // 현재 구현에서는 null
        }

        @Test
        @DisplayName("null 값이 포함된 PopupEntity 변환 테스트")
        public void test02() {
            // given
            PopupEntity entity = PopupEntity.builder()
                .id(1L)
                .title(null)
                .popupLocationId(1L)
                .type(PopupType.EXPERIENTIAL)
                .startDate(null)
                .endDate(null)
                .build();

            // when
            PopupDetail result = new PopupMapper().toDomain(entity);

            // then
            assertNotNull(result);
            assertEquals(1L, result.id());
            assertNull(result.title());
            assertNull(result.period().startDate());
            assertNull(result.period().endDate());
        }

        @Test
        @DisplayName("빈 문자열이 포함된 PopupEntity 변환 테스트")
        public void test03() {
            // given
            LocalDate startDate = LocalDate.now();
            LocalDate endDate = LocalDate.now().plusDays(30);
            
            PopupEntity entity = PopupEntity.builder()
                .id(1L)
                .title("")
                .popupLocationId(1L)
                .type(PopupType.EXPERIENTIAL)
                .startDate(startDate)
                .endDate(endDate)
                .build();

            // when
            PopupDetail result = new PopupMapper().toDomain(entity);

            // then
            assertNotNull(result);
            assertEquals(1L, result.id());
            assertEquals("", result.title());
            assertEquals(startDate, result.period().startDate());
            assertEquals(endDate, result.period().endDate());
        }

        @Test
        @DisplayName("Location 객체 생성 확인 테스트")
        public void test04() {
            // given
            PopupEntity entity = PopupEntity.builder()
                .id(1L)
                .title("테스트 팝업")
                .popupLocationId(1L)
                .type(PopupType.EXPERIENTIAL)
                .startDate(LocalDate.now())
                .endDate(LocalDate.now().plusDays(30))
                .build();

            // when
            PopupDetail result = new PopupMapper().toDomain(entity);

            // then
            assertNotNull(result.location());
            assertNull(result.location().addressName());
            assertNull(result.location().region1depthName());
            assertNull(result.location().region2depthName());
            assertNull(result.location().region3depthName());
            assertEquals(0.0, result.location().longitude());
            assertEquals(0.0, result.location().latitude());
        }

        @Test
        @DisplayName("다른 팝업 타입의 PopupEntity 변환 테스트")
        public void test05() {
            // given
            LocalDate startDate = LocalDate.now();
            LocalDate endDate = LocalDate.now().plusDays(30);
            
            PopupEntity entity = PopupEntity.builder()
                .id(2L)
                .title("전시 팝업")
                .popupLocationId(2L)
                .type(PopupType.EXHIBITION)
                .startDate(startDate)
                .endDate(endDate)
                .build();

            // when
            PopupDetail result = new PopupMapper().toDomain(entity);

            // then
            assertNotNull(result);
            assertEquals(2L, result.id());
            assertEquals("전시 팝업", result.title());
            assertEquals(startDate, result.period().startDate());
            assertEquals(endDate, result.period().endDate());
        }
    }
} 