package com.test.rewardpoint.domain;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

import com.test.rewardpoint.common.exception.BadRequestException;
import com.test.rewardpoint.mock.PointConfigurationMock;
import com.test.rewardpoint.mock.RandomMock;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

public class PointConfigurationTest {

    @Nested
    public class 요청당_포인트_제한_검증시 {

        @Test
        public void 요청한_포인트가_최소_지급_가능한_포인트보다_작으면_예외를_던진다() {
            // given
            int minimumPointPerGrant = RandomMock.createRandomInteger();
            int requestPoint = RandomMock.createRandomInteger(
                    Integer.MIN_VALUE,
                    minimumPointPerGrant - 1
            );
            PointConfiguration pointConfiguration = PointConfigurationMock.builder()
                    .minimumPointPerGrant(minimumPointPerGrant)
                    .maximumPointPerGrant(Integer.MAX_VALUE)
                    .build();

            // when & then
            assertThrows(BadRequestException.class, () -> pointConfiguration.validatePoint(requestPoint));
        }

        @Test
        public void 요청한_포인트가_최대_지급_가능한_포인트보다_크면_예외를_던진다() {
            // given
            int maximumPointPerGrant = RandomMock.createRandomInteger();
            int requestPoint = RandomMock.createRandomInteger(
                    maximumPointPerGrant + 1,
                    Integer.MAX_VALUE
            );
            PointConfiguration pointConfiguration = PointConfigurationMock.builder()
                    .minimumPointPerGrant(Integer.MIN_VALUE)
                    .maximumPointPerGrant(maximumPointPerGrant)
                    .build();

            // when & then
            assertThrows(BadRequestException.class, () -> pointConfiguration.validatePoint(requestPoint));
        }

        @Test
        public void 요청한_포인트가_지급_가능한_범위라면_예외를_던지지_않는다() {
            // given
            int minimumPointPerGrant = RandomMock.createRandomInteger();
            int maximumPointPerGrant = RandomMock.createRandomInteger(
                    minimumPointPerGrant + 1,
                    Integer.MAX_VALUE
            );
            int requestPoint = RandomMock.createRandomInteger(
                    minimumPointPerGrant,
                    maximumPointPerGrant
            );
            PointConfiguration pointConfiguration = PointConfigurationMock.builder()
                    .minimumPointPerGrant(minimumPointPerGrant)
                    .maximumPointPerGrant(maximumPointPerGrant)
                    .build();

            // when & then
            assertDoesNotThrow(() -> pointConfiguration.validatePoint(requestPoint));
        }
    }
}
