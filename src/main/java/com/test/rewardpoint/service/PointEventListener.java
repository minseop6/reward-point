package com.test.rewardpoint.service;

import com.test.rewardpoint.domain.Point;
import com.test.rewardpoint.repository.PointRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@RequiredArgsConstructor
@Service
public class PointEventListener {

    private final PointRepository pointRepository;

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    @TransactionalEventListener(classes = Point.class, phase = TransactionPhase.AFTER_COMMIT)
    public void handlePointEvent(Point point) {
        pointRepository.save(point);
    }
}
