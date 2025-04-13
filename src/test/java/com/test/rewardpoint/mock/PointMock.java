package com.test.rewardpoint.mock;

import com.test.rewardpoint.domain.GrantBy;
import com.test.rewardpoint.domain.Point;
import com.test.rewardpoint.domain.UsedPoint;
import java.time.LocalDate;
import java.util.List;
import lombok.Builder;
import org.springframework.test.util.ReflectionTestUtils;

public class PointMock {

    @Builder
    public static Point create(
            Integer memberId,
            Integer amount,
            GrantBy grantBy,
            String description,
            LocalDate expiresDate,
            List<UsedPoint> usedPoints
    ) {
        Point point = Point.builder()
                .memberId(memberId != null ? memberId : RandomMock.createRandomInteger())
                .amount(amount != null ? amount : RandomMock.createRandomInteger(1, 100000))
                .grantBy(
                        grantBy != null
                                ? grantBy
                                : GrantBy.values()[RandomMock.createRandomInteger(1, GrantBy.values().length)]
                )
                .description(description != null ? description : RandomMock.createRandomString())
                .expiresDate(expiresDate != null ? expiresDate : LocalDate.now().plusDays(1))
                .build();

        ReflectionTestUtils.setField(point, "usedPoints", usedPoints != null ? usedPoints : List.of());

        return point;
    }

}
