package com.example.demo.application.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.example.demo.application.dto.popup.LocationResponse;
import com.example.demo.application.dto.popup.PopupSummaryResponse;
import com.example.demo.application.dto.popup.SearchTagsResponse;
import com.example.demo.application.dto.waiting.VisitHistoryCursorResponse;
import com.example.demo.application.dto.waiting.WaitingCreateRequest;
import com.example.demo.application.dto.waiting.WaitingCreateResponse;
import com.example.demo.application.dto.waiting.WaitingResponse;
import com.example.demo.application.mapper.WaitingDtoMapper;
import com.example.demo.domain.model.DateRange;
import com.example.demo.domain.model.Location;
import com.example.demo.domain.model.Member;
import com.example.demo.domain.model.popup.OpeningHours;
import com.example.demo.domain.model.popup.Popup;
import com.example.demo.domain.model.popup.PopupCategory;
import com.example.demo.domain.model.popup.PopupContent;
import com.example.demo.domain.model.popup.PopupDisplay;
import com.example.demo.domain.model.popup.PopupSchedule;
import com.example.demo.domain.model.popup.PopupStatus;
import com.example.demo.domain.model.popup.PopupType;
import com.example.demo.domain.model.popup.Sns;
import com.example.demo.domain.model.popup.WeeklyOpeningHours;
import com.example.demo.domain.model.waiting.Waiting;
import com.example.demo.domain.model.waiting.WaitingQuery;
import com.example.demo.domain.model.waiting.WaitingStatus;
import com.example.demo.domain.port.MemberPort;
import com.example.demo.domain.port.NotificationPort;
import com.example.demo.domain.port.PopupPort;
import com.example.demo.domain.port.WaitingPort;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class WaitingServiceTest {

    @Mock
    private WaitingPort waitingPort;

    @Mock
    private PopupPort popupPort;

    @Mock
    private MemberPort memberPort;

    @Mock
    private WaitingDtoMapper waitingDtoMapper;

    @Mock
    private WaitingNotificationService waitingNotificationService;

    @Mock
    private NotificationPort notificationPort;

    @InjectMocks
    private WaitingService waitingService;

    @Nested
    @DisplayName("createWaiting 테스트")
    class Test01 {
        // 테스트용 유효한 데이터
        private final Popup validPopup = Popup.builder()
                .id(1L)
                .name("테스트 팝업")
                .location(
                        new Location(
                                "서울시 강남구",
                                "서울특별시",
                                "강남구",
                                "역삼동",
                                127.0012,
                                37.5665
                        )
                )
                .schedule(
                        new PopupSchedule(
                                new DateRange(
                                        LocalDate.now(),
                                        LocalDate.now().plusDays(30)
                                ),
                                new WeeklyOpeningHours(
                                        List.of(
                                                new OpeningHours(
                                                        DayOfWeek.MONDAY,
                                                        LocalTime.now(),
                                                        LocalTime.now().plusHours(1)
                                                )
                                        )
                                )
                        )
                )
                .display(
                        new PopupDisplay(
                                List.of("http://image.com"),
                                new PopupContent("팝업 설명", "팝업 공지"),
                                List.of(new Sns("http://image.com", "http://sns.com"))
                        )
                )
                .type(PopupType.EXHIBITION)
                .popupCategories(
                        List.of(new PopupCategory(1L, "예술"))
                )
                .status(PopupStatus.IN_PROGRESS)
                .build();

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
                    WaitingStatus.WAITING, registeredAt
            );
            Location location = validPopup.getLocation();
            LocationResponse locationResponse = new LocationResponse(
                    location.addressName(),
                    location.region1depthName(),
                    location.region2depthName(),
                    location.region3depthName(),
                    location.longitude(),
                    location.latitude()
            );

            WaitingCreateResponse expectedResponse = new WaitingCreateResponse(
                    1L, "테스트 팝업", "홍길동", 2, "hong@example.com", nextWaitingNumber, registeredAt, locationResponse, validPopup.getDisplay().imageUrls().getFirst()
            );

            when(popupPort.findById(1L)).thenReturn(Optional.of(validPopup));
            when(waitingPort.getNextWaitingNumber(1L)).thenReturn(nextWaitingNumber);
            when(memberPort.findById(1L)).thenReturn(Optional.of(validMember));
            when(waitingPort.save(any(Waiting.class))).thenReturn(savedWaiting);
            when(waitingDtoMapper.toCreateResponse(savedWaiting)).thenReturn(expectedResponse);

            // when
            WaitingCreateResponse response = waitingService.createWaiting(validRequest);

            // then
            assertNotNull(response);
            assertEquals(expectedResponse, response);

            // verify
            verify(popupPort).findById(1L);
            verify(waitingPort).getNextWaitingNumber(1L);
            verify(memberPort).findById(1L);
            verify(waitingPort).save(any(Waiting.class));
            verify(waitingDtoMapper).toCreateResponse(savedWaiting);
        }

        @Test
        @DisplayName("존재하지 않는 팝업으로 대기 신청 시 예외 발생")
        public void test02() {
            // given
            when(popupPort.findById(999L)).thenReturn(Optional.empty());

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
            verify(popupPort).findById(999L);
            verify(waitingPort, never()).getNextWaitingNumber(any());
            verify(memberPort, never()).findById(any());
            verify(waitingPort, never()).save(any());
            verify(waitingDtoMapper, never()).toCreateResponse(any());
        }

        @Test
        @DisplayName("존재하지 않는 회원으로 대기 신청 시 예외 발생")
        @Disabled
        public void test03() {
            // given
            when(popupPort.findById(1L)).thenReturn(Optional.of(validPopup));
            when(waitingPort.getNextWaitingNumber(1L)).thenReturn(5);
            when(memberPort.findById(999L)).thenReturn(Optional.empty());

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
            verify(popupPort).findById(1L);
            verify(waitingPort).getNextWaitingNumber(1L);
            verify(memberPort).findById(999L);
            verify(waitingPort, never()).save(any());
            verify(waitingDtoMapper, never()).toCreateResponse(any());
        }

        @Test
        @DisplayName("대기 정보 저장 시 예외 발생")
        public void test04() {
            // given
            when(popupPort.findById(1L)).thenReturn(Optional.of(validPopup));
            when(waitingPort.getNextWaitingNumber(1L)).thenReturn(5);
            when(memberPort.findById(1L)).thenReturn(Optional.of(validMember));
            when(waitingPort.save(any(Waiting.class))).thenThrow(new RuntimeException("저장 실패"));

            // when & then
            assertThrows(RuntimeException.class, () -> waitingService.createWaiting(validRequest));

            // verify
            verify(popupPort).findById(1L);
            verify(waitingPort).getNextWaitingNumber(1L);
            verify(memberPort).findById(1L);
            verify(waitingPort).save(any(Waiting.class));
            verify(waitingDtoMapper, never()).toCreateResponse(any());
        }

        @Test
        @DisplayName("대기 번호 조회 시 예외 발생")
        public void test05() {
            // given
            when(popupPort.findById(1L)).thenReturn(Optional.of(validPopup));
            when(waitingPort.getNextWaitingNumber(1L)).thenThrow(new RuntimeException("대기 번호 조회 실패"));

            // when & then
            assertThrows(RuntimeException.class, () -> waitingService.createWaiting(validRequest));

            // verify
            verify(popupPort).findById(1L);
            verify(waitingPort).getNextWaitingNumber(1L);
            verify(memberPort, never()).findById(any());
            verify(waitingPort, never()).save(any());
            verify(waitingDtoMapper, never()).toCreateResponse(any());
        }
    }

    @Nested
    @DisplayName("getVisitHistory 테스트")
    class Test02 {
        // 테스트용 유효한 데이터
        private final Popup validPopup = Popup.builder()
                .id(1L)
                .name("테스트 팝업")
                .location(
                        new Location(
                                "서울시 강남구",
                                "서울특별시",
                                "강남구",
                                "역삼동",
                                127.0012,
                                37.5665
                        )
                )
                .schedule(
                        new PopupSchedule(
                                new DateRange(
                                        LocalDate.now(),
                                        LocalDate.now().plusDays(30)
                                ),
                                new WeeklyOpeningHours(
                                        List.of(
                                                new OpeningHours(
                                                        DayOfWeek.MONDAY,
                                                        LocalTime.now(),
                                                        LocalTime.now().plusHours(1)
                                                )
                                        )
                                )
                        )
                )
                .display(
                        new PopupDisplay(
                                List.of("http://image.com"),
                                new PopupContent("팝업 설명", "팝업 공지"),
                                List.of(new Sns("http://image.com", "http://sns.com"))
                        )
                )
                .type(PopupType.EXHIBITION)
                .popupCategories(
                        List.of(new PopupCategory(1L, "예술"))
                )
                .status(PopupStatus.IN_PROGRESS)
                .build();

        private final Member validMember = new Member(1L, "테스트 사용자", "test@example.com");

        @Test
        @DisplayName("정상적인 방문 내역 조회 테스트 - 첫 페이지")
        public void test01() {
            // given
            Integer size = 10;
            Long lastWaitingId = null;
            String status = null;

            LocalDateTime now = LocalDateTime.now();

            Waiting waiting1 = new Waiting(
                    1L, validPopup, "홍길동", validMember,
                    "hong@example.com", 2, 1,
                    WaitingStatus.WAITING, now
            );

            Waiting waiting2 = new Waiting(
                    2L, validPopup, "김철수", validMember,
                    "kim@example.com", 3, 2,
                    WaitingStatus.VISITED, now.minusDays(1));

            List<Waiting> waitings = List.of(waiting1, waiting2);

            PopupSummaryResponse popupDto1 = new PopupSummaryResponse(
                    1L, "테스트 팝업", "thumbnail1.jpg",
                    new LocationResponse("서울시 강남구", "서울특별시", "강남구", "역삼동", 127.0012, 37.5665),
                    5L, "6월 10일 ~ 6월 20일",
                    new SearchTagsResponse("체험형", List.of("패션", "예술"))
            );

            WaitingResponse waitingResponse1 = new WaitingResponse(
                    1L, 1, "RESERVED", "홍길동", 2, "hong@example.com", popupDto1, now
            );

            PopupSummaryResponse popupDto2 = new PopupSummaryResponse(
                    1L, "테스트 팝업", "thumbnail1.jpg",
                    new LocationResponse("서울시 강남구", "서울특별시", "강남구", "역삼동", 127.0012, 37.5665),
                    5L, "6월 10일 ~ 6월 20일",
                    new SearchTagsResponse("체험형", List.of("패션", "예술"))
            );

            WaitingResponse waitingResponse2 = new WaitingResponse(
                    2L, 2, "COMPLETED", "김철수", 3, "kim@example.com", popupDto2, now
            );

            when(waitingPort.findByQuery(any(WaitingQuery.class))).thenReturn(waitings);
            when(waitingDtoMapper.toResponse(waiting1)).thenReturn(waitingResponse1);
            when(waitingDtoMapper.toResponse(waiting2)).thenReturn(waitingResponse2);

            // when
            VisitHistoryCursorResponse response = waitingService.getVisitHistory(validMember.id(), size, lastWaitingId, status, null);

            // then
            assertNotNull(response);
            assertEquals(2, response.content().size());
            assertEquals(response.content().getLast().waitingId(), response.lastWaitingId());
            assertFalse(response.hasNext());

            // verify
            verify(waitingPort).findByQuery(any(WaitingQuery.class));
            verify(waitingDtoMapper).toResponse(waiting1);
            verify(waitingDtoMapper).toResponse(waiting2);
        }

        @Test
        @DisplayName("방문 내역 조회 테스트 - 다음 페이지 존재")
        public void test02() {
            // given
            Integer size = 2;
            Long lastWaitingId = null;
            String status = null;

            LocalDateTime now = LocalDateTime.now();
            Waiting waiting1 = new Waiting(1L, validPopup, "홍길동", validMember, "hong@example.com", 2, 1, WaitingStatus.WAITING, now);
            Waiting waiting2 = new Waiting(2L, validPopup, "김철수", validMember, "kim@example.com", 3, 2, WaitingStatus.VISITED, now.minusDays(1));

            List<Waiting> waitings = List.of(waiting1, waiting2);

            PopupSummaryResponse popupDto1 = new PopupSummaryResponse(
                    1L, "테스트 팝업", "thumbnail1.jpg",
                    new LocationResponse("서울시 강남구", "서울특별시", "강남구", "역삼동", 127.0012, 37.5665),
                    5L, "6월 10일 ~ 6월 20일",
                    new SearchTagsResponse("체험형", List.of("패션", "예술"))
            );
            WaitingResponse waitingResponse1 = new WaitingResponse(1L, 1, "RESERVED", "홍길동", 2, "hong@example.com", popupDto1, now);

            PopupSummaryResponse popupDto2 = new PopupSummaryResponse(
                    1L, "테스트 팝업", "thumbnail1.jpg",
                    new LocationResponse("서울시 강남구", "서울특별시", "강남구", "역삼동", 127.0012, 37.5665),
                    5L, "6월 10일 ~ 6월 20일",
                    new SearchTagsResponse("체험형", List.of("패션", "예술"))
            );
            WaitingResponse waitingResponse2 = new WaitingResponse(2L, 2, "COMPLETED", "김철수", 3, "kim@example.com", popupDto2, now.minusDays(1));

            when(waitingPort.findByQuery(any(WaitingQuery.class))).thenReturn(waitings);
            when(waitingDtoMapper.toResponse(waiting1)).thenReturn(waitingResponse1);
            when(waitingDtoMapper.toResponse(waiting2)).thenReturn(waitingResponse2);

            // when
            VisitHistoryCursorResponse response = waitingService.getVisitHistory(validMember.id(), size, lastWaitingId, status, null);

            // then
            assertNotNull(response);
            assertEquals(2, response.content().size());
            assertEquals(response.content().getLast().waitingId(), response.lastWaitingId());
            assertTrue(response.hasNext());

            // verify
            verify(waitingPort).findByQuery(any(WaitingQuery.class));
            verify(waitingDtoMapper).toResponse(waiting1);
            verify(waitingDtoMapper).toResponse(waiting2);
        }

        @Test
        @DisplayName("방문 내역 조회 테스트 - 빈 결과")
        public void test03() {
            // given
            Integer size = 10;
            Long lastWaitingId = null;
            String status = null;

            List<Waiting> emptyWaitings = List.of();

            when(waitingPort.findByQuery(any(WaitingQuery.class))).thenReturn(emptyWaitings);

            // when
            VisitHistoryCursorResponse response = waitingService.getVisitHistory(validMember.id(), size, lastWaitingId, status, null);

            // then
            assertNotNull(response);
            assertEquals(0, response.content().size());
            assertNull(response.lastWaitingId());
            assertFalse(response.hasNext());

            // verify
            verify(waitingPort).findByQuery(any(WaitingQuery.class));
            verify(waitingDtoMapper, never()).toResponse(any());
        }

        @Test
        @DisplayName("방문 내역 조회 테스트 - 상태 필터링 (RESERVED)")
        public void test04() {
            // given
            Integer size = 10;
            Long lastWaitingId = null;
            String status = "WAITING";

            LocalDateTime now = LocalDateTime.now();
            Waiting waiting = new Waiting(
                    1L, validPopup, "홍길동", validMember,
                    "hong@example.com", 2, 1,
                    WaitingStatus.WAITING, now
            );

            List<Waiting> waitings = List.of(waiting);

            PopupSummaryResponse popupDto = new PopupSummaryResponse(
                    1L, "테스트 팝업", "thumbnail1.jpg",
                    new LocationResponse("서울시 강남구", "서울특별시", "강남구", "역삼동", 127.0012, 37.5665),
                    5L, "6월 10일 ~ 6월 20일",
                    new SearchTagsResponse("체험형", List.of("패션", "예술"))
            );

            WaitingResponse waitingResponse = new WaitingResponse(
                    1L, 1, status, "홍길동", 2, "hong@example.com", popupDto, now
            );

            when(waitingPort.findByQuery(any(WaitingQuery.class))).thenReturn(waitings);
            when(waitingDtoMapper.toResponse(waiting)).thenReturn(waitingResponse);

            // when
            VisitHistoryCursorResponse response = waitingService.getVisitHistory(validMember.id(), size, lastWaitingId, status, null);

            // then
            assertNotNull(response);
            assertEquals(1, response.content().size());
            assertEquals("WAITING", response.content().getFirst().status());

            // verify
            verify(waitingPort).findByQuery(any(WaitingQuery.class));
            verify(waitingDtoMapper).toResponse(waiting);
        }

        @Test
        @DisplayName("방문 내역 조회 테스트 - 커서 기반 페이지네이션")
        public void test05() {
            // given
            Integer size = 10;
            Long lastWaitingId = 5L;
            String status = null;

            LocalDateTime now = LocalDateTime.now();
            Waiting waiting = new Waiting(
                    6L, validPopup, "홍길동", validMember,
                    "hong@example.com", 2, 6,
                    WaitingStatus.WAITING, now
            );

            List<Waiting> waitings = List.of(waiting);

            PopupSummaryResponse popupDto = new PopupSummaryResponse(
                    1L, "테스트 팝업", "thumbnail1.jpg",
                    new LocationResponse("서울시 강남구", "서울특별시", "강남구", "역삼동", 127.0012, 37.5665),
                    5L, "6월 10일 ~ 6월 20일",
                    new SearchTagsResponse("체험형", List.of("패션", "예술"))
            );

            WaitingResponse waitingResponse = new WaitingResponse(
                    6L, 6, "RESERVED", "홍길동", 2, "hong@example.com", popupDto, now
            );

            when(waitingPort.findByQuery(any(WaitingQuery.class))).thenReturn(waitings);
            when(waitingDtoMapper.toResponse(waiting)).thenReturn(waitingResponse);

            // when
            VisitHistoryCursorResponse response = waitingService.getVisitHistory(validMember.id(), size, lastWaitingId, status, null);

            // then
            assertNotNull(response);
            assertEquals(1, response.content().size());
            assertEquals(response.content().getLast().waitingId(), response.lastWaitingId());

            // verify
            verify(waitingPort).findByQuery(any(WaitingQuery.class));
            verify(waitingDtoMapper).toResponse(waiting);
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
                    waitingService.getVisitHistory(validMember.id(), size, lastWaitingId, invalidStatus, null)
            );

            // verify
            verify(waitingPort, never()).findByQuery(any(WaitingQuery.class));
            verify(waitingDtoMapper, never()).toResponse(any());
        }

        @Test
        @DisplayName("대기 단건 조회 테스트")
        public void test07() {
            // given
            Long waitingId = 1L;
            LocalDateTime now = LocalDateTime.now();
            Waiting waiting = new Waiting(
                    waitingId, validPopup, "홍길동", validMember,
                    "hong@example.com", 2, 1,
                    WaitingStatus.WAITING, now
            );

            PopupSummaryResponse popupDto = new PopupSummaryResponse(
                    1L, "테스트 팝업", "thumbnail1.jpg",
                    new LocationResponse("서울시 강남구", "서울특별시", "강남구", "역삼동", 127.0012, 37.5665),
                    5L, "6월 10일 ~ 6월 20일",
                    new SearchTagsResponse("체험형", List.of("패션", "예술"))
            );

            WaitingResponse waitingResponse = new WaitingResponse(
                    waitingId, 1, "WAITING", "홍길동", 2, "hong@example.com", popupDto, now
            );

            when(waitingPort.findByQuery(any(WaitingQuery.class))).thenReturn(List.of(waiting));
            when(waitingDtoMapper.toResponse(waiting)).thenReturn(waitingResponse);

            // when
            VisitHistoryCursorResponse response = waitingService.getVisitHistory(validMember.id(), 10, null, null, waitingId);

            // then
            assertNotNull(response);
            assertEquals(1, response.content().size());
            assertEquals(waitingId, response.content().getFirst().waitingId());
            assertEquals(waitingId, response.lastWaitingId());
            assertFalse(response.hasNext());

            // verify
            verify(waitingPort).findByQuery(new WaitingQuery(waitingId, null, null, null, null, null));
            verify(waitingDtoMapper).toResponse(waiting);
        }

        @Test
        @DisplayName("대기 단건 조회 테스트 - 존재하지 않는 대기")
        public void test08() {
            // given
            Long waitingId = 999L;

            when(waitingPort.findByQuery(any(WaitingQuery.class))).thenReturn(List.of());

            // when & then
            assertThrows(IllegalArgumentException.class, () ->
                    waitingService.getVisitHistory(validMember.id(), 10, null, null, waitingId)
            );

            // verify
            verify(waitingPort).findByQuery(new WaitingQuery(waitingId, null, null, null, null, null));
            verify(waitingDtoMapper, never()).toResponse(any());
        }

        @Test
        @DisplayName("대기 단건 조회 테스트 - 다른 사용자의 대기")
        public void test09() {
            // given
            Long waitingId = 1L;
            Member otherMember = new Member(2L, "다른 사용자", "other@example.com");
            LocalDateTime now = LocalDateTime.now();
            Waiting waiting = new Waiting(
                    waitingId, validPopup, "홍길동", otherMember,
                    "hong@example.com", 2, 1,
                    WaitingStatus.WAITING, now
            );

            when(waitingPort.findByQuery(any(WaitingQuery.class))).thenReturn(List.of(waiting));

            // when & then
            assertThrows(IllegalArgumentException.class, () ->
                    waitingService.getVisitHistory(validMember.id(), 10, null, null, waitingId)
            );

            // verify
            verify(waitingPort).findByQuery(new WaitingQuery(waitingId, null, null, null, null, null));
            verify(waitingDtoMapper, never()).toResponse(any());
        }
    }
}
