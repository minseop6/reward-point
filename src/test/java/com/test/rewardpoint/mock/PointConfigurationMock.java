package com.test.rewardpoint.mock;

import com.test.rewardpoint.domain.PointConfiguration;
import lombok.Builder;

public class PointConfigurationMock {

    @Builder
    public static PointConfiguration create(Integer minimumPointPerGrant, Integer maximumPointPerGrant) {
        if (minimumPointPerGrant == null) {
            minimumPointPerGrant = RandomMock.createRandomInteger();
        }
        if (maximumPointPerGrant == null) {
            maximumPointPerGrant = RandomMock.createRandomInteger(
                    minimumPointPerGrant + 1,
                    Integer.MAX_VALUE
            );
        }
        return new PointConfiguration(minimumPointPerGrant, maximumPointPerGrant);
    }
}
