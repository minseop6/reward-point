package com.test.rewardpoint.mock;

import com.test.rewardpoint.domain.GrantBy;
import com.test.rewardpoint.domain.Point;
import java.time.LocalDate;
import lombok.Builder;

public class PointMock {

    @Builder
    public static Point create(
            Integer memberId,
            Integer amount,
            GrantBy grantBy,
            String description,
            LocalDate expiresDate
    ) {
        if (memberId == null) {
            memberId = RandomMock.createRandomInteger();
        }
        if (amount == null) {
            amount = RandomMock.createRandomInteger(1, 100000);
        }
        if (grantBy == null) {
            grantBy = GrantBy.ADMIN;
        }
        if (description == null) {
            description = RandomMock.createRandomString();
        }
        if (expiresDate == null) {
            expiresDate = LocalDate.now().plusDays(1);
        }
        return Point.builder()
                .memberId(memberId)
                .amount(amount)
                .grantBy(grantBy)
                .description(description)
                .expiresDate(expiresDate)
                .build();
    }

}
