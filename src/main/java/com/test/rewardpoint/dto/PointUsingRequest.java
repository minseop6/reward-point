package com.test.rewardpoint.dto;

import com.test.rewardpoint.domain.Transaction;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class PointUsingRequest {

    private int memberId;
    private int amount;
    private int orderId;

    public Transaction toTransaction() {
        return Transaction.builder()
                .orderId(orderId)
                .amount(amount)
                .build();
    }
}
