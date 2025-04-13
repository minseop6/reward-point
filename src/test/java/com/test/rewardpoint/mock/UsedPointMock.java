package com.test.rewardpoint.mock;

import com.test.rewardpoint.domain.UsedPoint;
import lombok.Builder;

public class UsedPointMock {

    @Builder
    public static UsedPoint create(
            Long pointId,
            Integer transactionId,
            Integer amount
    ) {
        return UsedPoint.builder()
                .pointId(pointId != null ? pointId : RandomMock.createRandomLong())
                .transactionId(transactionId != null ? transactionId : RandomMock.createRandomInteger())
                .amount(amount != null ? amount : RandomMock.createRandomInteger(1, 100000))
                .build();
    }

}
