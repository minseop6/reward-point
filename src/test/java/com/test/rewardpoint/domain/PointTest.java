package com.test.rewardpoint.domain;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

import com.test.rewardpoint.common.exception.BadRequestException;
import com.test.rewardpoint.mock.PointMock;
import com.test.rewardpoint.mock.RandomMock;
import com.test.rewardpoint.mock.UsedPointMock;
import java.time.LocalDate;
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
    public class 포인트_적립_취소시 {

        @Test
        public void 사용된_이력이_있다면_예외를_발생시킨다() {
            // given
            List<UsedPoint> usedPoints = Stream.generate(() -> UsedPointMock.builder().build())
                    .limit(3).toList();
            Point point = PointMock.builder()
                    .usedPoints(usedPoints)
                    .build();

            // when & then
            assertThrows(BadRequestException.class, point::cancel);
        }

        @Test
        public void 취소_일자를_변경한다() {
            // given
            Point point = PointMock.builder().build();

            // when
            point.cancel();

            // then
            assertThat(point.getCanceledAt()).isNotNull();
        }
    }
}
