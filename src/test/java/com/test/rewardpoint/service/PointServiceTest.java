package com.test.rewardpoint.service;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.BDDMockito.given;

import com.test.rewardpoint.common.exception.NotFoundException;
import com.test.rewardpoint.domain.GrantBy;
import com.test.rewardpoint.domain.MemberPointConfiguration;
import com.test.rewardpoint.domain.Point;
import com.test.rewardpoint.domain.PointConfiguration;
import com.test.rewardpoint.domain.Transaction;
import com.test.rewardpoint.dto.PointCancellationRequest;
import com.test.rewardpoint.dto.PointCreationRequest;
import com.test.rewardpoint.dto.PointUsingRequest;
import com.test.rewardpoint.mock.MemberPointConfigurationMock;
import com.test.rewardpoint.mock.PointConfigurationMock;
import com.test.rewardpoint.mock.PointMock;
import com.test.rewardpoint.mock.RandomMock;
import com.test.rewardpoint.mock.TransactionMock;
import com.test.rewardpoint.outbound.repository.MemberPointConfigurationRepository;
import com.test.rewardpoint.outbound.repository.PointConfigurationRepository;
import com.test.rewardpoint.outbound.repository.PointRepository;
import com.test.rewardpoint.outbound.repository.TransactionRepository;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;
import lombok.Builder;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class PointServiceTest {

    @InjectMocks
    private PointService pointService;

    @Mock
    private PointRepository pointRepository;

    @Mock
    private PointConfigurationRepository pointConfigurationRepository;

    @Mock
    private MemberPointConfigurationRepository memberPointConfigurationRepository;

    @Mock
    private TransactionRepository transactionRepository;

    @Nested
    class 포인트_지급시 {

        @Test
        public void 포인트_지급에_성공한다() {
            // given
            int memberId = RandomMock.createRandomInteger();
            int amount = RandomMock.createRandomInteger(1, 1000);
            int maximumPointLimit = RandomMock.createRandomInteger(1, 1000000);
            PointConfiguration pointConfiguration = PointConfigurationMock.builder()
                    .minimumPointPerGrant(0)
                    .maximumPointPerGrant(maximumPointLimit)
                    .build();
            MemberPointConfiguration memberPointConfiguration = MemberPointConfigurationMock.builder()
                    .memberId(memberId)
                    .maximumPointLimit(maximumPointLimit)
                    .build();
            List<Point> existPoints = Stream.generate(() -> PointMock.builder()
                    .memberId(memberId)
                    .amount(1000)
                    .build()
            ).limit(3).toList();
            PointCreationRequest request = PointCreationRequestMock.builder()
                    .memberId(memberId)
                    .amount(amount)
                    .build();

            given(pointConfigurationRepository.findTopByDeletedAtIsNull()).willReturn(Optional.of(pointConfiguration));
            given(memberPointConfigurationRepository.findTopByDeletedAtIsNull())
                    .willReturn(Optional.of(memberPointConfiguration));
            given(
                    pointRepository.findByMemberIdAndRemainAmountIsGreaterThanAndExpiresDateIsGreaterThanEqualAndCanceledAtIsNull(
                            memberId,
                            0,
                            LocalDate.now()
                    )
            ).willReturn(existPoints);

            // when & then
            assertDoesNotThrow(() -> pointService.creatPoint(request));
        }

        static class PointCreationRequestMock {

            @Builder
            public static PointCreationRequest create(
                    Integer memberId,
                    Integer amount,
                    String description,
                    LocalDate expiresDate,
                    GrantBy grantBy
            ) {
                PointCreationRequest request = new PointCreationRequest();
                request.setMemberId(memberId != null ? memberId : RandomMock.createRandomInteger());
                request.setAmount(amount != null ? amount : RandomMock.createRandomInteger(1, 100000));
                request.setDescription(description != null ? description : RandomMock.createRandomString());
                request.setExpiresDate(expiresDate != null ? expiresDate : LocalDate.now().plusDays(1));
                request.setGrantBy(grantBy != null ? grantBy : GrantBy.ADMIN);
                return request;
            }
        }
    }

    @Nested
    class 적립_취소시 {

        @Test
        public void 포인트_취소에_성공한다() {
            // given
            long pointId = RandomMock.createRandomLong();
            Point point = PointMock.builder().build();

            given(pointRepository.findById(pointId)).willReturn(Optional.of(point));

            // when & then
            assertDoesNotThrow(() -> pointService.withdrawPoint(pointId));
        }

        @Test
        public void 포인트_취소시_포인트가_존재하지_않으면_예외가_발생한다() {
            // given
            long pointId = RandomMock.createRandomLong();

            given(pointRepository.findById(pointId)).willReturn(Optional.empty());

            // when & then
            assertThrows(NotFoundException.class, () -> pointService.withdrawPoint(pointId));
        }
    }

    @Nested
    class 포인트_사용시 {

        @Test
        public void 포인트_사용에_성공한다() {
            // given
            int memberId = RandomMock.createRandomInteger();
            int amount = RandomMock.createRandomInteger(1, 1000);
            Point point = PointMock.builder().memberId(memberId).amount(amount).build();
            List<Point> existPoints = List.of(point);
            PointUsingRequest request = PointUsingRequestMock.builder().memberId(memberId).amount(amount).build();
            Transaction transaction = TransactionMock.builder().build();

            given(
                    pointRepository.findByMemberIdAndRemainAmountIsGreaterThanAndExpiresDateIsGreaterThanEqualAndCanceledAtIsNull(
                            memberId,
                            0,
                            LocalDate.now()
                    )
            ).willReturn(existPoints);
            given(transactionRepository.save(request.toTransaction())).willReturn(transaction);

            // when & then
            assertDoesNotThrow(() -> pointService.usePoint(request));
        }

        static class PointUsingRequestMock {

            @Builder
            public static PointUsingRequest create(
                    Integer memberId,
                    Integer amount,
                    Integer orderId
            ) {
                PointUsingRequest request = new PointUsingRequest();
                request.setMemberId(memberId != null ? memberId : RandomMock.createRandomInteger());
                request.setAmount(amount != null ? amount : RandomMock.createRandomInteger(1, 100000));
                request.setOrderId(orderId != null ? orderId : RandomMock.createRandomInteger());
                return request;
            }
        }
    }

    @Nested
    class 포인트_사용_취소시 {

        @Test
        public void 거래내역을_찾지_못한다면_예외가_발생한다() {
            // given
            int orderId = RandomMock.createRandomInteger();
            PointCancellationRequest request = PointCancellationRequestMock.builder().orderId(orderId).build();

            given(transactionRepository.findByOrderId(orderId)).willReturn(Optional.empty());

            // when & then
            assertThrows(NotFoundException.class, () -> pointService.cancelPoint(request));
        }

        @Test
        public void 포인트_사용_취소에_성공한다() {
            // given
            int orderId = RandomMock.createRandomInteger();
            int transactionId = RandomMock.createRandomInteger();
            int cancelAmount = RandomMock.createRandomInteger(1, 1000);
            Transaction transaction = TransactionMock.builder().id(transactionId).build();
            List<Point> usedPoints = Stream.generate(() -> PointMock.builder().build()).limit(3).toList();
            PointCancellationRequest request = PointCancellationRequestMock.builder()
                    .orderId(orderId)
                    .cancelAmount(cancelAmount)
                    .build();

            given(transactionRepository.findByOrderId(orderId)).willReturn(Optional.of(transaction));
            given(pointRepository.findByTransactionId(transactionId)).willReturn(usedPoints);

            // when & then
            assertDoesNotThrow(() -> pointService.cancelPoint(request));
        }

        static class PointCancellationRequestMock {

            @Builder
            public static PointCancellationRequest create(
                    Integer orderId,
                    Integer cancelAmount
            ) {
                PointCancellationRequest request = new PointCancellationRequest();
                request.setOrderId(orderId != null ? orderId : RandomMock.createRandomInteger());
                request.setCancelAmount(
                        cancelAmount != null ? cancelAmount : RandomMock.createRandomInteger(1, 100000));
                return request;
            }
        }
    }
}
