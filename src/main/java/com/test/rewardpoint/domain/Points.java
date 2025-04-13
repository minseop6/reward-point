package com.test.rewardpoint.domain;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class Points {

    private final List<Point> points;

    public int calculateTotalRemainAmount() {
        return points.stream()
                .mapToInt(Point::getRemainAmount)
                .sum();
    }

    public void use(Transaction transaction) {
        List<Point> sortedPoints = sortPoints();

        int amount = transaction.getAmount();
        for (Point point : sortedPoints) {
            if (amount <= 0) {
                break;
            }

            int usablePoint = Math.min(point.getRemainAmount(), amount);
            point.use(usablePoint, transaction.getOrderId());
            amount -= usablePoint;
        }
    }

    public void cancel(int transactionId, int amount, LocalDateTime canceledAt) {
        List<Point> sortedPoints = sortPoints();
        for (Point point : sortedPoints) {
            if (amount <= 0) {
                break;
            }

            int usablePoint = Math.min(point.getCancelableAmount(), amount);
            point.cancel(transactionId, usablePoint, canceledAt);
            amount -= usablePoint;
        }
    }

    private List<Point> sortPoints() {
        return points.stream().sorted(
                Comparator
                        .comparing(Point::getGrantBy, Comparator.comparing(grantBy -> grantBy == GrantBy.ADMIN ? 0 : 1))
                        .thenComparing(Point::getExpiresDate)
        ).toList();
    }
}
