package com.test.rewardpoint.domain;

import com.test.rewardpoint.common.configuration.event.EventPublisher;
import com.test.rewardpoint.common.exception.BadRequestException;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.BatchSize;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity(name = "point")
public class Point extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false)
    private Long id;

    @Column(nullable = false)
    private Integer memberId;

    @Column(nullable = false)
    private Integer amount;

    @Column(nullable = false)
    private Integer remainAmount;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, columnDefinition = "ENUM('SYSTEM', 'ADMIN')")
    private GrantBy grantBy;

    @Column(nullable = false)
    private String description;

    @Column(nullable = false)
    private LocalDate expiresDate;

    @Column
    private LocalDateTime canceledAt;

    @OneToMany(cascade = CascadeType.PERSIST)
    @BatchSize(size = 100)
    private final List<UsedPoint> usedPoints = new ArrayList<>();

    @Builder
    public Point(
            Integer memberId,
            Integer amount,
            GrantBy grantBy,
            String description,
            LocalDate expiresDate
    ) {
        LocalDate today = LocalDate.now();
        if (expiresDate.isBefore(today.plusDays(1)) || expiresDate.isAfter(today.plusYears(5))) {
            throw new BadRequestException("유효기간은 최소 1일 이상 최대 5년 미만으로 설정 가능합니다.");
        }
        this.memberId = memberId;
        this.amount = amount;
        this.remainAmount = amount;
        this.grantBy = grantBy;
        this.description = description;
        this.expiresDate = expiresDate;
    }

    public int getCancelableAmount() {
        return amount - remainAmount;
    }

    public void withdraw(LocalDateTime canceledAt) {
        boolean hasUsedPoint = this.usedPoints.stream().anyMatch(usedPoint -> !usedPoint.isCanceled());
        if (hasUsedPoint) {
            throw new BadRequestException("이미 사용된 포인트는 회수할 수 없습니다.");
        }
        this.canceledAt = canceledAt;
    }

    public void use(int amount, int transactionId) {
        if (remainAmount < amount) {
            throw new BadRequestException("포인트가 부족합니다.");
        }
        remainAmount -= amount;
        usedPoints.add(new UsedPoint(id, transactionId, amount));
    }

    public void cancel(int transactionId, int cancelAmount, LocalDateTime canceledAt) {
        if (amount - remainAmount < cancelAmount) {
            throw new BadRequestException("취소할 수 있는 포인트를 초과했습니다.");
        }

        int usedAmount = usedPoints.stream()
                .filter(usedPoint -> usedPoint.getTransactionId() == transactionId)
                .mapToInt(usedPoint -> usedPoint.cancel(canceledAt))
                .sum();

        boolean isPartialCancellation = usedAmount - cancelAmount > 0;
        if (isExpired()) {
            Point point = Point.builder()
                    .amount(cancelAmount)
                    .description(description)
                    .grantBy(grantBy)
                    .expiresDate(LocalDate.now().plusYears(1))
                    .memberId(memberId)
                    .build();
            EventPublisher.raise(point);

            return;
        }

        if (isPartialCancellation) {
            usedPoints.add(new UsedPoint(id, transactionId, usedAmount - cancelAmount));
        }
        remainAmount += cancelAmount;
    }

    private boolean isExpired() {
        return LocalDate.now().isAfter(expiresDate);
    }
}