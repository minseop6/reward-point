package com.test.rewardpoint.domain;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

import com.test.rewardpoint.common.exception.BadRequestException;
import com.test.rewardpoint.mock.MemberPointConfigurationMock;
import com.test.rewardpoint.mock.RandomMock;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

public class MemberPointConfigurationTest {

    @Nested
    public class 사용자_포인트_보유량_검증시 {

        @Test
        public void 보유할_수_있는_최대_보유량을_초과하면_예외를_발생시킨다() {
            // given
            int memberId = RandomMock.createRandomInteger();
            int maximumPointLimit = RandomMock.createRandomInteger(1, 100000);
            MemberPointConfiguration memberPointConfiguration = MemberPointConfigurationMock.builder()
                    .memberId(memberId)
                    .maximumPointLimit(maximumPointLimit)
                    .build();
            int adjustedPoint = maximumPointLimit + RandomMock.createRandomInteger(1, 100000);

            // when & then
            assertThrows(BadRequestException.class, () -> memberPointConfiguration.validatePoint(adjustedPoint));
        }

        @Test
        public void 최대_보유량을_초과하지_않으면_예외를_발생시키지_않는다() {
            // given
            int memberId = RandomMock.createRandomInteger();
            int maximumPointLimit = RandomMock.createRandomInteger(1, 100000);
            MemberPointConfiguration memberPointConfiguration = MemberPointConfigurationMock.builder()
                    .memberId(memberId)
                    .maximumPointLimit(maximumPointLimit)
                    .build();

            // when
            int adjustedPoint = maximumPointLimit - RandomMock.createRandomInteger(1, 100000);

            // then
            assertDoesNotThrow(() -> memberPointConfiguration.validatePoint(adjustedPoint));
        }
    }
}
