package com.test.rewardpoint.domain;

import static org.junit.jupiter.api.Assertions.assertThrows;

import com.test.rewardpoint.common.exception.BadRequestException;
import com.test.rewardpoint.mock.RandomMock;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

public class TransactionTest {

    @Nested
    public class 포인트_사용_취소시 {

        @Test
        public void 취소가능한_금액을_초과하면_예외가_발생한다() {
            // given
            int usedAmount = RandomMock.createRandomInteger(1, 10000);
            int cancelAmount = RandomMock.createRandomInteger(usedAmount + 1, Integer.MAX_VALUE);
            Transaction transaction = Transaction.builder()
                    .orderId(1)
                    .amount(usedAmount)
                    .build();

            // when & then
            assertThrows(
                    BadRequestException.class,
                    () -> transaction.cancel(cancelAmount)
            );
        }
    }
}
