package com.example.demo.infrastructure.persistence.adapter;

import com.example.demo.domain.model.*;
import com.example.demo.infrastructure.persistence.entity.WaitingEntity;
import com.example.demo.infrastructure.persistence.mapper.WaitingEntityMapper;
import com.example.demo.infrastructure.persistence.repository.WaitingJpaRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageRequest;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class WaitingRepositoryAdapterTest {

    @Mock
    private WaitingJpaRepository waitingJpaRepository;

    @Mock
    private WaitingEntityMapper waitingEntityMapper;

    @InjectMocks
    private WaitingRepositoryAdapter waitingRepositoryAdapter;

    @Nested
    @DisplayName("save 테스트")
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

        private final Waiting validWaiting = new Waiting(
            null, validPopup, "홍길동", validMember,
            "hong@example.com", 2, 1, WaitingStatus.RESERVED, LocalDateTime.now()
        );

        @Test
        @DisplayName("정상적인 대기 정보 저장 테스트")
        public void test01() {
            // given
            WaitingEntity entity = WaitingEntity.builder()
                .popupId(1L)
                .memberId(1L)
                .waitingPersonName("홍길동")
                .contactEmail("hong@example.com")
                .peopleCount(2)
                .waitingNumber(1)
                .status(WaitingStatus.RESERVED)
                .build();

            WaitingEntity savedEntity = WaitingEntity.builder()
                .id(1L)
                .popupId(1L)
                .memberId(1L)
                .waitingPersonName("홍길동")
                .contactEmail("hong@example.com")
                .peopleCount(2)
                .waitingNumber(1)
                .status(WaitingStatus.RESERVED)
                .build();

            Waiting expectedWaiting = new Waiting(
                1L, validPopup, "홍길동", validMember,
                "hong@example.com", 2, 1, WaitingStatus.RESERVED, LocalDateTime.now()
            );

            when(waitingEntityMapper.toEntity(validWaiting)).thenReturn(entity);
            when(waitingJpaRepository.save(entity)).thenReturn(savedEntity);
            when(waitingEntityMapper.toDomain(savedEntity, validWaiting.popup(), validWaiting.member()))
                .thenReturn(expectedWaiting);

            // when
            Waiting result = waitingRepositoryAdapter.save(validWaiting);

            // then
            assertNotNull(result);
            assertEquals(1L, result.id());
            assertEquals("홍길동", result.waitingPersonName());
            assertEquals("hong@example.com", result.contactEmail());
            assertEquals(2, result.peopleCount());
            assertEquals(1, result.waitingNumber());
            assertEquals(WaitingStatus.RESERVED, result.status());

            // verify
            verify(waitingEntityMapper).toEntity(validWaiting);
            verify(waitingJpaRepository).save(entity);
            verify(waitingEntityMapper).toDomain(savedEntity, validWaiting.popup(), validWaiting.member());
        }

        @Test
        @DisplayName("저장 실패 시 예외 발생 테스트")
        public void test02() {
            // given
            WaitingEntity entity = WaitingEntity.builder()
                .popupId(1L)
                .memberId(1L)
                .waitingPersonName("홍길동")
                .contactEmail("hong@example.com")
                .peopleCount(2)
                .waitingNumber(1)
                .status(WaitingStatus.RESERVED)
                .build();

            when(waitingEntityMapper.toEntity(validWaiting)).thenReturn(entity);
            when(waitingJpaRepository.save(entity)).thenThrow(new RuntimeException("저장 실패"));

            // when & then
            assertThrows(RuntimeException.class, () -> waitingRepositoryAdapter.save(validWaiting));

            // verify
            verify(waitingEntityMapper).toEntity(validWaiting);
            verify(waitingJpaRepository).save(entity);
            verify(waitingEntityMapper, never()).toDomain(any(), any(), any());
        }
    }

    @Nested
    @DisplayName("findByQuery 테스트")
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

        @Test
        @DisplayName("RESERVED_FIRST_THEN_DATE_DESC 정렬로 조회 테스트")
        public void test01() {
            // given
            WaitingQuery query = new WaitingQuery(1L, 10, null, null, WaitingQuery.SortOrder.RESERVED_FIRST_THEN_DATE_DESC);
            
            WaitingEntity entity1 = WaitingEntity.builder()
                .id(1L)
                .popupId(1L)
                .memberId(1L)
                .waitingPersonName("홍길동")
                .contactEmail("hong@example.com")
                .peopleCount(2)
                .waitingNumber(1)
                .status(WaitingStatus.RESERVED)
                .build();

            WaitingEntity entity2 = WaitingEntity.builder()
                .id(2L)
                .popupId(1L)
                .memberId(1L)
                .waitingPersonName("김철수")
                .contactEmail("kim@example.com")
                .peopleCount(3)
                .waitingNumber(2)
                .status(WaitingStatus.COMPLETED)
                .build();

            List<WaitingEntity> entities = List.of(entity1, entity2);

            Waiting waiting1 = new Waiting(1L, validPopup, "홍길동", validMember, "hong@example.com", 2, 1, WaitingStatus.RESERVED, LocalDateTime.now());
            Waiting waiting2 = new Waiting(2L, validPopup, "김철수", validMember, "kim@example.com", 3, 2, WaitingStatus.COMPLETED, LocalDateTime.now());

            when(waitingJpaRepository.findByMemberIdOrderByStatusReservedFirstThenCreatedAtDesc(1L, WaitingStatus.RESERVED, PageRequest.of(0, 10)))
                .thenReturn(entities);
            when(waitingEntityMapper.toDomain(entity1, null, null)).thenReturn(waiting1);
            when(waitingEntityMapper.toDomain(entity2, null, null)).thenReturn(waiting2);

            // when
            List<Waiting> result = waitingRepositoryAdapter.findByQuery(query);

            // then
            assertNotNull(result);
            assertEquals(2, result.size());
            assertEquals(1L, result.getFirst().id());
            assertEquals(2L, result.getLast().id());

            // verify
            verify(waitingJpaRepository).findByMemberIdOrderByStatusReservedFirstThenCreatedAtDesc(1L, WaitingStatus.RESERVED, PageRequest.of(0, 10));
            verify(waitingEntityMapper).toDomain(entity1, null, null);
            verify(waitingEntityMapper).toDomain(entity2, null, null);
        }

        @Test
        @DisplayName("DATE_DESC 정렬로 조회 테스트")
        public void test02() {
            // given
            WaitingQuery query = new WaitingQuery(1L, 10, null, null, WaitingQuery.SortOrder.DATE_DESC);
            
            WaitingEntity entity = WaitingEntity.builder()
                .id(1L)
                .popupId(1L)
                .memberId(1L)
                .waitingPersonName("홍길동")
                .contactEmail("hong@example.com")
                .peopleCount(2)
                .waitingNumber(1)
                .status(WaitingStatus.RESERVED)
                .build();

            List<WaitingEntity> entities = List.of(entity);
            Waiting waiting = new Waiting(1L, validPopup, "홍길동", validMember, "hong@example.com", 2, 1, WaitingStatus.RESERVED, LocalDateTime.now());

            when(waitingJpaRepository.findByMemberIdOrderByCreatedAtDesc(1L, PageRequest.of(0, 10)))
                .thenReturn(entities);
            when(waitingEntityMapper.toDomain(entity, null, null)).thenReturn(waiting);

            // when
            List<Waiting> result = waitingRepositoryAdapter.findByQuery(query);

            // then
            assertNotNull(result);
            assertEquals(1, result.size());
            assertEquals(1L, result.getFirst().id());

            // verify
            verify(waitingJpaRepository).findByMemberIdOrderByCreatedAtDesc(1L, PageRequest.of(0, 10));
            verify(waitingEntityMapper).toDomain(entity, null, null);
        }

        @Test
        @DisplayName("기본 정렬로 조회 테스트")
        public void test03() {
            // given
            WaitingQuery query = new WaitingQuery(1L, 10, null, null, WaitingQuery.SortOrder.RESERVED_FIRST_THEN_DATE_DESC);
            
            WaitingEntity entity = WaitingEntity.builder()
                .id(1L)
                .popupId(1L)
                .memberId(1L)
                .waitingPersonName("홍길동")
                .contactEmail("hong@example.com")
                .peopleCount(2)
                .waitingNumber(1)
                .status(WaitingStatus.RESERVED)
                .build();

            List<WaitingEntity> entities = List.of(entity);
            Waiting waiting = new Waiting(1L, validPopup, "홍길동", validMember, "hong@example.com", 2, 1, WaitingStatus.RESERVED, LocalDateTime.now());

            when(waitingJpaRepository.findByMemberIdOrderByStatusReservedFirstThenCreatedAtDesc(1L, WaitingStatus.RESERVED, PageRequest.of(0, 10)))
                .thenReturn(entities);
            when(waitingEntityMapper.toDomain(entity, null, null)).thenReturn(waiting);

            // when
            List<Waiting> result = waitingRepositoryAdapter.findByQuery(query);

            // then
            assertNotNull(result);
            assertEquals(1, result.size());

            // verify
            verify(waitingJpaRepository).findByMemberIdOrderByStatusReservedFirstThenCreatedAtDesc(1L, WaitingStatus.RESERVED, PageRequest.of(0, 10));
            verify(waitingEntityMapper).toDomain(entity, null, null);
        }

        @Test
        @DisplayName("빈 결과 조회 테스트")
        public void test04() {
            // given
            WaitingQuery query = new WaitingQuery(1L, 10, null, null, WaitingQuery.SortOrder.RESERVED_FIRST_THEN_DATE_DESC);
            
            List<WaitingEntity> emptyEntities = List.of();

            when(waitingJpaRepository.findByMemberIdOrderByStatusReservedFirstThenCreatedAtDesc(1L, WaitingStatus.RESERVED, PageRequest.of(0, 10)))
                .thenReturn(emptyEntities);

            // when
            List<Waiting> result = waitingRepositoryAdapter.findByQuery(query);

            // then
            assertNotNull(result);
            assertTrue(result.isEmpty());

            // verify
            verify(waitingJpaRepository).findByMemberIdOrderByStatusReservedFirstThenCreatedAtDesc(1L, WaitingStatus.RESERVED, PageRequest.of(0, 10));
            verify(waitingEntityMapper, never()).toDomain(any(), any(), any());
        }
    }

    @Nested
    @DisplayName("getNextWaitingNumber 테스트")
    class Test03 {

        @Test
        @DisplayName("기존 대기 번호가 존재하는 경우 테스트")
        public void test01() {
            // given
            Long popupId = 1L;
            Integer maxWaitingNumber = 5;

            when(waitingJpaRepository.findMaxWaitingNumberByPopupId(popupId))
                .thenReturn(Optional.of(maxWaitingNumber));

            // when
            Integer result = waitingRepositoryAdapter.getNextWaitingNumber(popupId);

            // then
            assertEquals(6, result);

            // verify
            verify(waitingJpaRepository).findMaxWaitingNumberByPopupId(popupId);
        }

        @Test
        @DisplayName("기존 대기 번호가 없는 경우 테스트")
        public void test02() {
            // given
            Long popupId = 1L;

            when(waitingJpaRepository.findMaxWaitingNumberByPopupId(popupId))
                .thenReturn(Optional.empty());

            // when
            Integer result = waitingRepositoryAdapter.getNextWaitingNumber(popupId);

            // then
            assertEquals(1, result);

            // verify
            verify(waitingJpaRepository).findMaxWaitingNumberByPopupId(popupId);
        }

        @Test
        @DisplayName("대기 번호 조회 실패 시 예외 발생 테스트")
        public void test03() {
            // given
            Long popupId = 1L;

            when(waitingJpaRepository.findMaxWaitingNumberByPopupId(popupId))
                .thenThrow(new RuntimeException("조회 실패"));

            // when & then
            assertThrows(RuntimeException.class, () -> waitingRepositoryAdapter.getNextWaitingNumber(popupId));

            // verify
            verify(waitingJpaRepository).findMaxWaitingNumberByPopupId(popupId);
        }
    }
} 