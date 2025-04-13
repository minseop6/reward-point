package com.test.rewardpoint.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import java.time.LocalDateTime;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity(name = "used_point")
public class UsedPoint extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false)
    private Long id;

    @Column(nullable = false)
    private Long pointId;

    @Column(nullable = false)
    private Integer transactionId;

    @Column(nullable = false)
    private Integer amount;

    @Column
    private LocalDateTime canceledAt;

    @Builder
    public UsedPoint(
            Long pointId,
            Integer transactionId,
            Integer amount
    ) {
        this.pointId = pointId;
        this.transactionId = transactionId;
        this.amount = amount;
    }

    public boolean isCanceled() {
        return canceledAt != null;
    }

    public int cancel(LocalDateTime canceledAt) {
        this.canceledAt = canceledAt;
        return amount;
    }
}
