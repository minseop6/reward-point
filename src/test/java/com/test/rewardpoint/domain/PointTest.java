package com.test.rewardpoint.domain;

import static org.junit.jupiter.api.Assertions.assertThrows;

import com.test.rewardpoint.common.exception.BadRequestException;
import com.test.rewardpoint.mock.RandomMock;
import java.time.LocalDate;
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
}
