package com.test.rewardpoint.domain;

import static org.assertj.core.api.SoftAssertions.assertSoftly;

import com.test.rewardpoint.mock.PointMock;
import com.test.rewardpoint.mock.RandomMock;
import com.test.rewardpoint.mock.TransactionMock;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

public class PointsTest {

    @Nested
    class 포인트_사용시 {

        LocalDate today = LocalDate.now();

        @Test
        public void Transaction의_금액만큼_차감된다() {
            // given
            List<Point> existPoints = List.of(
                    PointMock.builder().grantBy(GrantBy.SYSTEM).amount(1000).expiresDate(today.plusDays(3)).build(),
                    PointMock.builder().grantBy(GrantBy.ADMIN).amount(1000).expiresDate(today.plusDays(3)).build(),
                    PointMock.builder().grantBy(GrantBy.SYSTEM).amount(1000).expiresDate(today.plusDays(1)).build()
            );
            Points points = new Points(existPoints);
            Transaction transaction = TransactionMock.builder().amount(1500).build();

            // when
            points.use(transaction);

            // then
            assertSoftly(softly -> {
                softly.assertThat(points.calculateTotalRemainAmount()).isEqualTo(1500);
                softly.assertThat(existPoints.get(0).getRemainAmount()).isEqualTo(1000);
                softly.assertThat(existPoints.get(1).getRemainAmount()).isEqualTo(0);
                softly.assertThat(existPoints.get(2).getRemainAmount()).isEqualTo(500);
            });
        }
    }

    @Nested
    class 포인트_사용취소시 {

        LocalDate today = LocalDate.now();

        @Test
        public void 취소_금액만큼_차감된다() {
            // given
            List<Point> existPoints = List.of(
                    PointMock.builder()
                            .grantBy(GrantBy.SYSTEM).amount(1000).remainAmount(0).expiresDate(today.plusDays(3))
                            .build(),
                    PointMock.builder()
                            .grantBy(GrantBy.ADMIN).amount(1000).remainAmount(0).expiresDate(today.plusDays(3))
                            .build(),
                    PointMock.builder()
                            .grantBy(GrantBy.SYSTEM).amount(1000).remainAmount(500).expiresDate(today.plusDays(1))
                            .build()
            );
            Points points = new Points(existPoints);
            int transactionId = RandomMock.createRandomInteger();
            int cancelAmount = 2400;
            LocalDateTime canceledAt = LocalDateTime.now();

            // when
            points.cancel(transactionId, cancelAmount, canceledAt);

            // then
            assertSoftly(softly -> {
                softly.assertThat(points.calculateTotalRemainAmount()).isEqualTo(2900);
                softly.assertThat(existPoints.get(0).getRemainAmount()).isEqualTo(900);
                softly.assertThat(existPoints.get(1).getRemainAmount()).isEqualTo(1000);
                softly.assertThat(existPoints.get(2).getRemainAmount()).isEqualTo(1000);
            });
        }
    }
}
