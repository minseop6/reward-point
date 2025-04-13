package com.test.rewardpoint.service;

import com.test.rewardpoint.domain.Point;
import com.test.rewardpoint.repository.PointRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.event.TransactionalEventListener;

@RequiredArgsConstructor
@Service
public class PointEventListener {

    private final PointRepository pointRepository;

    @TransactionalEventListener(Point.class)
    public void handlePointEvent(Point point) {
        pointRepository.save(point);
    }
}
