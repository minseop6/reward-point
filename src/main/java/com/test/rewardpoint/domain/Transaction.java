package com.test.rewardpoint.domain;

import com.test.rewardpoint.common.exception.BadRequestException;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@EqualsAndHashCode(callSuper = false)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity(name = "transaction")
public class Transaction extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false)
    private Integer id;

    @Column(nullable = false)
    private Integer orderId;

    @Column(nullable = false)
    private Integer amount;

    @Builder
    public Transaction(
            Integer orderId,
            Integer amount
    ) {
        this.orderId = orderId;
        this.amount = amount;
    }

    public void cancel(int amount) {
        if (this.amount < amount) {
            throw new BadRequestException("취소할 수 있는 금액을 초과했습니다.");
        }
        this.amount -= amount;
    }
}
