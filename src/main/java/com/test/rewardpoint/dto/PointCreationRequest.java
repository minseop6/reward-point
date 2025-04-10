package com.test.rewardpoint.dto;

import com.test.rewardpoint.domain.GrantBy;
import com.test.rewardpoint.domain.Point;
import java.time.LocalDate;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class PointCreationRequest {

    private int memberId;
    private int amount;
    private String description;
    private LocalDate expiresDate;
    private GrantBy grantBy;

    public Point toPointEntity() {
        return Point.builder()
                .memberId(memberId)
                .amount(amount)
                .description(description)
                .grantBy(grantBy)
                .expiresDate(expiresDate)
                .build();
    }
}
