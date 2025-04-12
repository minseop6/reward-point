package com.test.rewardpoint.mock;

import com.test.rewardpoint.domain.MemberPointConfiguration;
import java.time.LocalDateTime;
import lombok.Builder;

public class MemberPointConfigurationMock {

    @Builder
    public static MemberPointConfiguration create(
            Integer memberId,
            Integer maximumPointLimit,
            LocalDateTime deletedAt
    ) {
        if (memberId == null) {
            memberId = RandomMock.createRandomInteger();
        }
        if (maximumPointLimit == null) {
            maximumPointLimit = RandomMock.createRandomInteger();
        }
        return MemberPointConfiguration.builder()
                .memberId(memberId)
                .maximumPointLimit(maximumPointLimit)
                .build();
    }
}
