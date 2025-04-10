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
}
