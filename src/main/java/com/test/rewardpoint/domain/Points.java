package com.test.rewardpoint.domain;

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

    private List<Point> sortPoints() {
        return points.stream().sorted(
                (a, b) -> {
                    if (a.getGrantBy() == GrantBy.ADMIN && b.getGrantBy() != GrantBy.ADMIN) {
                        return -1;
                    } else if (a.getGrantBy() != GrantBy.ADMIN && b.getGrantBy() == GrantBy.ADMIN) {
                        return 1;
                    }

                    if (a.getExpiresDate().isBefore(b.getExpiresDate())) {
                        return -1;
                    } else if (a.getExpiresDate().isAfter(b.getExpiresDate())) {
                        return 1;
                    }

                    return 0;
                }
        ).toList();
    }
}
