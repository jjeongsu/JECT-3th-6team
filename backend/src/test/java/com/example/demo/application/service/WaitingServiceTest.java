package com.example.demo.application.service;

import com.example.demo.application.dto.VisitHistoryCursorResponse;
import com.example.demo.application.dto.WaitingCreateRequest;
import com.example.demo.application.dto.WaitingCreateResponse;
import com.example.demo.application.dto.WaitingResponse;
import com.example.demo.application.mapper.WaitingMapper;
import com.example.demo.domain.model.*;
import com.example.demo.domain.port.MemberRepository;
import com.example.demo.domain.port.PopupRepository;
import com.example.demo.domain.port.WaitingRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class WaitingServiceTest {

    @Mock
    private WaitingRepository waitingRepository;

    @Mock
    private PopupRepository popupRepository;

    @Mock
    private MemberRepository memberRepository;

    @Mock
    private WaitingMapper waitingMapper;

    @InjectMocks
    private WaitingService waitingService;

    @Nested
    @DisplayName("createWaiting 테스트")
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

        private final WaitingCreateRequest validRequest = new WaitingCreateRequest(
                1L, 1L, "홍길동", 2, "hong@example.com"
        );

        @Test
        @DisplayName("정상적인 현장 대기 신청 테스트")
        public void test01() {
            // given
            Integer nextWaitingNumber = 5;
            LocalDateTime registeredAt = LocalDateTime.now();
            Waiting savedWaiting = new Waiting(
                    1L, validPopup, "홍길동", validMember,
                    "hong@example.com", 2, nextWaitingNumber,
                    WaitingStatus.RESERVED, registeredAt
            );

            WaitingCreateResponse expectedResponse = new WaitingCreateResponse(
                    1L, "테스트 팝업", "홍길동", 2, "hong@example.com", nextWaitingNumber, registeredAt
            );

            when(popupRepository.findById(1L)).thenReturn(Optional.of(validPopup));
            when(waitingRepository.getNextWaitingNumber(1L)).thenReturn(nextWaitingNumber);
            when(memberRepository.findById(1L)).thenReturn(Optional.of(validMember));
            when(waitingRepository.save(any(Waiting.class))).thenReturn(savedWaiting);
            when(waitingMapper.toCreateResponse(savedWaiting)).thenReturn(expectedResponse);

            // when
            WaitingCreateResponse response = waitingService.createWaiting(validRequest);

            // then
            assertNotNull(response);
            assertEquals(expectedResponse, response);

            // verify
            verify(popupRepository).findById(1L);
            verify(waitingRepository).getNextWaitingNumber(1L);
            verify(memberRepository).findById(1L);
            verify(waitingRepository).save(any(Waiting.class));
            verify(waitingMapper).toCreateResponse(savedWaiting);
        }

        @Test
        @DisplayName("존재하지 않는 팝업으로 대기 신청 시 예외 발생")
        public void test02() {
            // given
            when(popupRepository.findById(999L)).thenReturn(Optional.empty());

            WaitingCreateRequest invalidRequest = new WaitingCreateRequest(
                    999L, 1L, "홍길동", 2, "hong@example.com"
            );

            // when & then
            IllegalArgumentException exception = assertThrows(
                    IllegalArgumentException.class,
                    () -> waitingService.createWaiting(invalidRequest)
            );

            assertEquals("팝업을 찾을 수 없습니다: 999", exception.getMessage());

            // verify
            verify(popupRepository).findById(999L);
            verify(waitingRepository, never()).getNextWaitingNumber(any());
            verify(memberRepository, never()).findById(any());
            verify(waitingRepository, never()).save(any());
            verify(waitingMapper, never()).toCreateResponse(any());
        }

        @Test
        @DisplayName("존재하지 않는 회원으로 대기 신청 시 예외 발생")
        public void test03() {
            // given
            when(popupRepository.findById(1L)).thenReturn(Optional.of(validPopup));
            when(waitingRepository.getNextWaitingNumber(1L)).thenReturn(5);
            when(memberRepository.findById(999L)).thenReturn(Optional.empty());

            WaitingCreateRequest invalidRequest = new WaitingCreateRequest(
                    1L, 999L, "홍길동", 2, "hong@example.com"
            );

            // when & then
            IllegalArgumentException exception = assertThrows(
                    IllegalArgumentException.class,
                    () -> waitingService.createWaiting(invalidRequest)
            );

            assertEquals("회원을 찾을 수 없습니다: 999", exception.getMessage());

            // verify
            verify(popupRepository).findById(1L);
            verify(waitingRepository).getNextWaitingNumber(1L);
            verify(memberRepository).findById(999L);
            verify(waitingRepository, never()).save(any());
            verify(waitingMapper, never()).toCreateResponse(any());
        }

        @Test
        @DisplayName("대기 정보 저장 시 예외 발생")
        public void test04() {
            // given
            when(popupRepository.findById(1L)).thenReturn(Optional.of(validPopup));
            when(waitingRepository.getNextWaitingNumber(1L)).thenReturn(5);
            when(memberRepository.findById(1L)).thenReturn(Optional.of(validMember));
            when(waitingRepository.save(any(Waiting.class))).thenThrow(new RuntimeException("저장 실패"));

            // when & then
            assertThrows(RuntimeException.class, () -> waitingService.createWaiting(validRequest));

            // verify
            verify(popupRepository).findById(1L);
            verify(waitingRepository).getNextWaitingNumber(1L);
            verify(memberRepository).findById(1L);
            verify(waitingRepository).save(any(Waiting.class));
            verify(waitingMapper, never()).toCreateResponse(any());
        }

        @Test
        @DisplayName("대기 번호 조회 시 예외 발생")
        public void test05() {
            // given
            when(popupRepository.findById(1L)).thenReturn(Optional.of(validPopup));
            when(waitingRepository.getNextWaitingNumber(1L)).thenThrow(new RuntimeException("대기 번호 조회 실패"));

            // when & then
            assertThrows(RuntimeException.class, () -> waitingService.createWaiting(validRequest));

            // verify
            verify(popupRepository).findById(1L);
            verify(waitingRepository).getNextWaitingNumber(1L);
            verify(memberRepository, never()).findById(any());
            verify(waitingRepository, never()).save(any());
            verify(waitingMapper, never()).toCreateResponse(any());
        }
    }

    @Nested
    @DisplayName("getVisitHistory 테스트")
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
        @DisplayName("정상적인 방문 내역 조회 테스트 - 첫 페이지")
        public void test01() {
            // given
            Integer size = 10;
            Long lastWaitingId = null;
            String status = null;

            Waiting waiting1 = new Waiting(
                    1L, validPopup, "홍길동", validMember,
                    "hong@example.com", 2, 1,
                    WaitingStatus.RESERVED, LocalDateTime.now()
            );

            Waiting waiting2 = new Waiting(
                    2L, validPopup, "김철수", validMember,
                    "kim@example.com", 3, 2,
                    WaitingStatus.COMPLETED, LocalDateTime.now().minusDays(1))
                    ;

            List<Waiting> waitings = List.of(waiting1, waiting2);

            WaitingResponse waitingResponse1 = new WaitingResponse(
                    1L, 1L, "테스트 팝업", "thumbnail1.jpg", "서울특별시, 강남구",
                    new com.example.demo.application.dto.RatingResponse(4.5, 100),
                    "6월 10일 ~ 6월 20일", 1, "RESERVED"
            );

            WaitingResponse waitingResponse2 = new WaitingResponse(
                    2L, 1L, "테스트 팝업", "thumbnail1.jpg", "서울특별시, 강남구",
                    new com.example.demo.application.dto.RatingResponse(4.5, 100),
                    "6월 10일 ~ 6월 20일", 2, "COMPLETED"
            );

            List<WaitingResponse> waitingResponses = List.of(waitingResponse1, waitingResponse2);

            when(waitingRepository.findByQuery(any(WaitingQuery.class))).thenReturn(waitings);
            when(waitingMapper.toResponse(waiting1)).thenReturn(waitingResponse1);
            when(waitingMapper.toResponse(waiting2)).thenReturn(waitingResponse2);

            // when
            VisitHistoryCursorResponse response = waitingService.getVisitHistory(size, lastWaitingId, status);

            // then
            assertNotNull(response);
            assertEquals(2, response.content().size());
            assertEquals(response.content().getLast().waitingId(), response.lastWaitingId());
            assertFalse(response.hasNext());

            // verify
            verify(waitingRepository).findByQuery(any(WaitingQuery.class));
            verify(waitingMapper).toResponse(waiting1);
            verify(waitingMapper).toResponse(waiting2);
        }

        @Test
        @DisplayName("방문 내역 조회 테스트 - 다음 페이지 존재")
        public void test02() {
            // given
            Integer size = 2;
            Long lastWaitingId = null;
            String status = null;

            Waiting waiting1 = new Waiting(1L, validPopup, "홍길동", validMember, "hong@example.com", 2, 1, WaitingStatus.RESERVED, LocalDateTime.now());
            Waiting waiting2 = new Waiting(2L, validPopup, "김철수", validMember, "kim@example.com", 3, 2, WaitingStatus.COMPLETED, LocalDateTime.now().minusDays(1));

            List<Waiting> waitings = List.of(waiting1, waiting2);

            WaitingResponse waitingResponse1 = new WaitingResponse(1L, 1L, "테스트 팝업", "thumbnail1.jpg", "서울특별시, 강남구", new com.example.demo.application.dto.RatingResponse(4.5, 100), "6월 10일 ~ 6월 20일", 1, "RESERVED");
            WaitingResponse waitingResponse2 = new WaitingResponse(2L, 1L, "테스트 팝업", "thumbnail1.jpg", "서울특별시, 강남구", new com.example.demo.application.dto.RatingResponse(4.5, 100), "6월 10일 ~ 6월 20일", 2, "COMPLETED");

            when(waitingRepository.findByQuery(any(WaitingQuery.class))).thenReturn(waitings);
            when(waitingMapper.toResponse(waiting1)).thenReturn(waitingResponse1);
            when(waitingMapper.toResponse(waiting2)).thenReturn(waitingResponse2);

            // when
            VisitHistoryCursorResponse response = waitingService.getVisitHistory(size, lastWaitingId, status);

            // then
            assertNotNull(response);
            assertEquals(2, response.content().size());
            assertEquals(response.content().getLast().waitingId(), response.lastWaitingId());
            assertTrue(response.hasNext());

            // verify
            verify(waitingRepository).findByQuery(any(WaitingQuery.class));
            verify(waitingMapper).toResponse(waiting1);
            verify(waitingMapper).toResponse(waiting2);
        }

        @Test
        @DisplayName("방문 내역 조회 테스트 - 빈 결과")
        public void test03() {
            // given
            Integer size = 10;
            Long lastWaitingId = null;
            String status = null;

            List<Waiting> emptyWaitings = List.of();

            when(waitingRepository.findByQuery(any(WaitingQuery.class))).thenReturn(emptyWaitings);

            // when
            VisitHistoryCursorResponse response = waitingService.getVisitHistory(size, lastWaitingId, status);

            // then
            assertNotNull(response);
            assertEquals(0, response.content().size());
            assertNull(response.lastWaitingId());
            assertFalse(response.hasNext());

            // verify
            verify(waitingRepository).findByQuery(any(WaitingQuery.class));
            verify(waitingMapper, never()).toResponse(any());
        }

        @Test
        @DisplayName("방문 내역 조회 테스트 - 상태 필터링 (RESERVED)")
        public void test04() {
            // given
            Integer size = 10;
            Long lastWaitingId = null;
            String status = "RESERVED";

            Waiting waiting = new Waiting(
                    1L, validPopup, "홍길동", validMember,
                    "hong@example.com", 2, 1,
                    WaitingStatus.RESERVED, LocalDateTime.now()
            );

            List<Waiting> waitings = List.of(waiting);

            WaitingResponse waitingResponse = new WaitingResponse(
                    1L, 1L, "테스트 팝업", "thumbnail1.jpg", "서울특별시, 강남구",
                    new com.example.demo.application.dto.RatingResponse(4.5, 100),
                    "6월 10일 ~ 6월 20일", 1, "RESERVED"
            );

            when(waitingRepository.findByQuery(any(WaitingQuery.class))).thenReturn(waitings);
            when(waitingMapper.toResponse(waiting)).thenReturn(waitingResponse);

            // when
            VisitHistoryCursorResponse response = waitingService.getVisitHistory(size, lastWaitingId, status);

            // then
            assertNotNull(response);
            assertEquals(1, response.content().size());
            assertEquals("RESERVED", response.content().getFirst().status());

            // verify
            verify(waitingRepository).findByQuery(any(WaitingQuery.class));
            verify(waitingMapper).toResponse(waiting);
        }

        @Test
        @DisplayName("방문 내역 조회 테스트 - 커서 기반 페이지네이션")
        public void test05() {
            // given
            Integer size = 10;
            Long lastWaitingId = 5L;
            String status = null;

            Waiting waiting = new Waiting(
                    6L, validPopup, "홍길동", validMember,
                    "hong@example.com", 2, 6,
                    WaitingStatus.RESERVED, LocalDateTime.now()
            );

            List<Waiting> waitings = List.of(waiting);

            WaitingResponse waitingResponse = new WaitingResponse(
                    6L, 1L, "테스트 팝업", "thumbnail1.jpg", "서울특별시, 강남구",
                    new com.example.demo.application.dto.RatingResponse(4.5, 100),
                    "6월 10일 ~ 6월 20일", 6, "RESERVED"
            );

            when(waitingRepository.findByQuery(any(WaitingQuery.class))).thenReturn(waitings);
            when(waitingMapper.toResponse(waiting)).thenReturn(waitingResponse);

            // when
            VisitHistoryCursorResponse response = waitingService.getVisitHistory(size, lastWaitingId, status);

            // then
            assertNotNull(response);
            assertEquals(1, response.content().size());
            assertEquals(response.content().getLast().waitingId(), response.lastWaitingId());

            // verify
            verify(waitingRepository).findByQuery(any(WaitingQuery.class));
            verify(waitingMapper).toResponse(waiting);
        }

        @Test
        @DisplayName("방문 내역 조회 테스트 - 잘못된 상태 문자열")
        public void test06() {
            // given
            Integer size = 10;
            Long lastWaitingId = null;
            String invalidStatus = "INVALID_STATUS";

            // when & then
            assertThrows(IllegalArgumentException.class, () ->
                    waitingService.getVisitHistory(size, lastWaitingId, invalidStatus)
            );

            // verify
            verify(waitingRepository, never()).findByQuery(any(WaitingQuery.class));
            verify(waitingMapper, never()).toResponse(any());
        }

        @Test
        @DisplayName("방문 내역 조회 테스트 - 썸네일이 없는 팝업")
        public void test07() {
            // given
            Integer size = 10;
            Long lastWaitingId = null;
            String status = null;

            PopupDetail popupWithoutThumbnail = new PopupDetail(
                    2L, "썸네일 없는 팝업", List.of(), 5,
                    new Rating(4.0, 50), new SearchTags("팝업", List.of("테스트")),
                    new Location("서울시 강남구", "서울특별시", "강남구", "역삼동", 127.0012, 37.5665),
                    new Period(LocalDate.now(), LocalDate.now().plusDays(30)),
                    new BrandStory(List.of("brand1.jpg"), List.of()),
                    new PopupDetailInfo(List.of(), List.of("상세 설명"))
            );

            Waiting waiting = new Waiting(
                    1L, popupWithoutThumbnail, "홍길동", validMember,
                    "hong@example.com", 2, 1,
                    WaitingStatus.RESERVED, LocalDateTime.now()
            );

            List<Waiting> waitings = List.of(waiting);

            WaitingResponse waitingResponse = new WaitingResponse(
                    1L, 2L, "썸네일 없는 팝업", null, "서울특별시, 강남구",
                    new com.example.demo.application.dto.RatingResponse(4.0, 50),
                    "6월 10일 ~ 6월 20일", 1, "RESERVED"
            );

            when(waitingRepository.findByQuery(any(WaitingQuery.class))).thenReturn(waitings);
            when(waitingMapper.toResponse(waiting)).thenReturn(waitingResponse);

            // when
            VisitHistoryCursorResponse response = waitingService.getVisitHistory(size, lastWaitingId, status);

            // then
            assertNotNull(response);
            assertEquals(1, response.content().size());
            assertNull(response.content().getFirst().popupImageUrl());

            // verify
            verify(waitingRepository).findByQuery(any(WaitingQuery.class));
            verify(waitingMapper).toResponse(waiting);
        }
    }
}