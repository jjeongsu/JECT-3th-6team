package com.example.demo.infrastructure.persistence.mapper;

import com.example.demo.domain.model.*;
import com.example.demo.infrastructure.persistence.entity.WaitingEntity;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class WaitingMapperTest {

    @Nested
    @DisplayName("toDomain 테스트")
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
        @DisplayName("정상적인 WaitingEntity를 Waiting 도메인 모델로 변환 테스트")
        public void test01() {
            // given
            LocalDateTime createdAt = LocalDateTime.now();
            WaitingEntity entity = createTestWaitingEntity(1L, 1L, "홍길동", 1L, "hong@example.com", 2, 1, WaitingStatus.RESERVED, createdAt);

            // when
            Waiting result = new WaitingMapper().toDomain(entity, validPopup, validMember);

            // then
            assertNotNull(result);
            assertEquals(1L, result.id());
            assertEquals(validPopup, result.popup());
            assertEquals("홍길동", result.waitingPersonName());
            assertEquals(validMember, result.member());
            assertEquals("hong@example.com", result.contactEmail());
            assertEquals(2, result.peopleCount());
            assertEquals(1, result.waitingNumber());
            assertEquals(WaitingStatus.RESERVED, result.status());
            assertEquals(createdAt, result.registeredAt());
        }

        @Test
        @DisplayName("null popup과 member로 변환 테스트")
        public void test02() {
            // given
            LocalDateTime createdAt = LocalDateTime.now();
            WaitingEntity entity = createTestWaitingEntity(1L, 1L, "홍길동", 1L, "hong@example.com", 2, 1, WaitingStatus.RESERVED, createdAt);

            // when
            Waiting result = new WaitingMapper().toDomain(entity, null, null);

            // then
            assertNotNull(result);
            assertEquals(1L, result.id());
            assertNull(result.popup());
            assertEquals("홍길동", result.waitingPersonName());
            assertNull(result.member());
            assertEquals("hong@example.com", result.contactEmail());
            assertEquals(2, result.peopleCount());
            assertEquals(1, result.waitingNumber());
            assertEquals(WaitingStatus.RESERVED, result.status());
        }

        @Test
        @DisplayName("다른 상태의 WaitingEntity 변환 테스트")
        public void test03() {
            // given
            LocalDateTime createdAt = LocalDateTime.now();
            WaitingEntity entity = createTestWaitingEntity(1L, 1L, "김철수", 1L, "kim@example.com", 3, 2, WaitingStatus.COMPLETED, createdAt);

            // when
            Waiting result = new WaitingMapper().toDomain(entity, validPopup, validMember);

            // then
            assertNotNull(result);
            assertEquals(1L, result.id());
            assertEquals("김철수", result.waitingPersonName());
            assertEquals("kim@example.com", result.contactEmail());
            assertEquals(3, result.peopleCount());
            assertEquals(2, result.waitingNumber());
            assertEquals(WaitingStatus.COMPLETED, result.status());
        }

        // 테스트용 WaitingEntity 생성 헬퍼 메서드
        private WaitingEntity createTestWaitingEntity(Long id, Long popupId, String waitingPersonName, Long memberId, 
                                                    String contactEmail, Integer peopleCount, Integer waitingNumber, 
                                                    WaitingStatus status, LocalDateTime createdAt) {
            WaitingEntity entity = WaitingEntity.builder()
                .id(id)
                .popupId(popupId)
                .waitingPersonName(waitingPersonName)
                .memberId(memberId)
                .contactEmail(contactEmail)
                .peopleCount(peopleCount)
                .waitingNumber(waitingNumber)
                .status(status)
                .build();

            // Reflection을 사용하여 createdAt 필드 설정
            try {
                Field createdAtField = entity.getClass().getSuperclass().getDeclaredField("createdAt");
                createdAtField.setAccessible(true);
                createdAtField.set(entity, createdAt);
            } catch (Exception e) {
                throw new RuntimeException("테스트 데이터 생성 실패", e);
            }
            return entity;
        }
    }

    @Nested
    @DisplayName("toEntity 테스트")
    class Test02 {
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

        private final Waiting validWaiting = new Waiting(
            1L, validPopup, "홍길동", validMember,
            "hong@example.com", 2, 1, WaitingStatus.RESERVED, LocalDateTime.now()
        );

        @Test
        @DisplayName("정상적인 Waiting 도메인 모델을 WaitingEntity로 변환 테스트")
        public void test01() {
            // when
            WaitingEntity result = new WaitingMapper().toEntity(validWaiting);

            // then
            assertNotNull(result);
            assertEquals(1L, result.getPopupId());
            assertEquals(1L, result.getMemberId());
            assertEquals("홍길동", result.getWaitingPersonName());
            assertEquals("hong@example.com", result.getContactEmail());
            assertEquals(2, result.getPeopleCount());
            assertEquals(1, result.getWaitingNumber());
            assertEquals(WaitingStatus.RESERVED, result.getStatus());
        }

        @Test
        @DisplayName("다른 상태의 Waiting 도메인 모델 변환 테스트")
        public void test02() {
            // given
            Waiting completedWaiting = new Waiting(
                1L, validPopup, "김철수", validMember,
                "kim@example.com", 3, 2, WaitingStatus.COMPLETED, LocalDateTime.now()
            );

            // when
            WaitingEntity result = new WaitingMapper().toEntity(completedWaiting);

            // then
            assertNotNull(result);
            assertEquals(1L, result.getPopupId());
            assertEquals(1L, result.getMemberId());
            assertEquals("김철수", result.getWaitingPersonName());
            assertEquals("kim@example.com", result.getContactEmail());
            assertEquals(3, result.getPeopleCount());
            assertEquals(2, result.getWaitingNumber());
            assertEquals(WaitingStatus.COMPLETED, result.getStatus());
        }

        @Test
        @DisplayName("다른 인원수의 Waiting 도메인 모델 변환 테스트")
        public void test03() {
            // given
            Waiting largeGroupWaiting = new Waiting(
                1L, validPopup, "박영희", validMember,
                "park@example.com", 6, 5, WaitingStatus.RESERVED, LocalDateTime.now()
            );

            // when
            WaitingEntity result = new WaitingMapper().toEntity(largeGroupWaiting);

            // then
            assertNotNull(result);
            assertEquals(1L, result.getPopupId());
            assertEquals(1L, result.getMemberId());
            assertEquals("박영희", result.getWaitingPersonName());
            assertEquals("park@example.com", result.getContactEmail());
            assertEquals(6, result.getPeopleCount());
            assertEquals(5, result.getWaitingNumber());
            assertEquals(WaitingStatus.RESERVED, result.getStatus());
        }
    }
} 