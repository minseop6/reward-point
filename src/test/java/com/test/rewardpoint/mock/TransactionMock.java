package com.test.rewardpoint.mock;

import com.test.rewardpoint.domain.Transaction;
import lombok.Builder;
import org.springframework.test.util.ReflectionTestUtils;

public class TransactionMock {

    @Builder
    public static Transaction create(
            Integer orderId,
            Integer amount,
            Integer id
    ) {
        Transaction transaction = Transaction.builder()
                .orderId(orderId != null ? orderId : RandomMock.createRandomInteger())
                .amount(amount != null ? amount : RandomMock.createRandomInteger(1, 100000))
                .build();

        ReflectionTestUtils.setField(transaction, "id", id != null ? id : RandomMock.createRandomLong());

        return transaction;
    }
}
