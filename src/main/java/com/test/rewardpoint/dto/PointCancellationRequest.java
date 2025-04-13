package com.test.rewardpoint.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class PointCancellationRequest {

    private int orderId;
    private int memberId;
    private int cancelAmount;
}
