package com.test.rewardpoint.domain;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.SoftAssertions.assertSoftly;
import static org.junit.jupiter.api.Assertions.assertThrows;

import com.test.rewardpoint.common.exception.BadRequestException;
import com.test.rewardpoint.mock.PointMock;
import com.test.rewardpoint.mock.RandomMock;
import com.test.rewardpoint.mock.UsedPointMock;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Stream;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

public class PointTest {

    @Nested
    public class 포인트_객체_생성시 {

        @Test
        public void 유효기간이_1일_미만인_포인트는_생성할수없다() {
            // given
            LocalDate expiresDate = LocalDate.now();

            // when & then
            assertThrows(BadRequestException.class, () -> {
                new Point(
                        RandomMock.createRandomInteger(),
                        RandomMock.createRandomInteger(),
                        GrantBy.values()[RandomMock.createRandomInteger(1, GrantBy.values().length)],
                        RandomMock.createRandomString(),
                        expiresDate
                );
            });
        }

        @Test
        public void 유효기간이_5년보다_미래인_포인트는_생성할수없다() {
            // given
            LocalDate expiresDate = LocalDate.now().plusYears(5).plusDays(RandomMock.createRandomInteger(1, 100));

            // when & then
            assertThrows(BadRequestException.class, () -> {
                new Point(
                        RandomMock.createRandomInteger(),
                        RandomMock.createRandomInteger(),
                        GrantBy.values()[RandomMock.createRandomInteger(1, GrantBy.values().length)],
                        RandomMock.createRandomString(),
                        expiresDate
                );
            });
        }
    }

    @Nested
    public class 포인트_적립_철회시 {

        @Test
        public void 사용된_이력이_있다면_예외를_발생시킨다() {
            // given
            List<UsedPoint> usedPoints = Stream.generate(() -> UsedPointMock.builder().build())
                    .limit(3).toList();
            Point point = PointMock.builder()
                    .usedPoints(usedPoints)
                    .build();
            LocalDateTime canceledAt = LocalDateTime.now();

            // when & then
            assertThrows(BadRequestException.class, () -> point.withdraw(canceledAt));
        }

        @Test
        public void 취소_일자를_변경한다() {
            // given
            Point point = PointMock.builder().build();
            LocalDateTime canceledAt = LocalDateTime.now();

            // when
            point.withdraw(canceledAt);

            // then
            assertThat(point.getCanceledAt()).isNotNull();
        }
    }

    @Nested
    public class 포인트_사용시 {

        @Test
        public void 사용할_포인트가_잔액보다_크면_예외를_발생시킨다() {
            // given
            int existsPoint = RandomMock.createRandomInteger(1, 1000);
            int usedPoint = RandomMock.createRandomInteger(existsPoint + 1, 2000);
            int transactionId = RandomMock.createRandomInteger();
            Point point = PointMock.builder().amount(existsPoint).build();

            // when & then
            assertThrows(
                    BadRequestException.class,
                    () -> point.use(usedPoint, transactionId)
            );
        }

        @Test
        public void 잔액에서_사용한_포인트만큼_차감된다() {
            // given
            int existsPoint = RandomMock.createRandomInteger(1, 1000);
            int usedPoint = RandomMock.createRandomInteger(1, existsPoint);
            int transactionId = RandomMock.createRandomInteger();
            Point point = PointMock.builder().amount(existsPoint).build();

            // when
            point.use(usedPoint, transactionId);

            // then
            assertSoftly(softly -> {
                softly.assertThat(point.getRemainAmount()).isEqualTo(existsPoint - usedPoint);
                softly.assertThat(point.getUsedPoints().getFirst().getAmount()).isEqualTo(usedPoint);
                softly.assertThat(point.getUsedPoints().getFirst().getTransactionId()).isEqualTo(transactionId);
            });
        }
    }

    @Nested
    public class 포인트_사용_취소시 {

        @Test
        public void 취소할_수_있는_포인트를_초과했다면_예외를_발생시킨다() {
            // given
            int transactionId = RandomMock.createRandomInteger();
            int cancelAmount = RandomMock.createRandomInteger(1, 1000);
            int amount = RandomMock.createRandomInteger(1, cancelAmount);
            Point point = PointMock.builder().amount(amount).build();

            // when & then
            assertThrows(
                    BadRequestException.class,
                    () -> point.cancel(transactionId, cancelAmount, LocalDateTime.now())
            );
        }

        @Test
        public void 부분취소라면_잔액에_추가된다() {
            // given
            int transactionId = RandomMock.createRandomInteger();
            int cancelAmount = RandomMock.createRandomInteger(1, 1000);
            int amount = RandomMock.createRandomInteger(cancelAmount + 1, cancelAmount * 3);
            List<UsedPoint> usedPoints = List.of(UsedPointMock.builder()
                    .transactionId(transactionId)
                    .amount(amount)
                    .build()
            );
            Point point = PointMock.builder()
                    .amount(amount)
                    .remainAmount(0)
                    .usedPoints(usedPoints)
                    .build();

            // when
            point.cancel(transactionId, cancelAmount, LocalDateTime.now());

            // then
            assertSoftly(softly -> {
                softly.assertThat(point.getRemainAmount()).isEqualTo(cancelAmount);
                softly.assertThat(point.getUsedPoints().getLast().getAmount()).isEqualTo(amount - cancelAmount);
            });
        }
    }
}
