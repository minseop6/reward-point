package com.test.rewardpoint.repository;

import com.test.rewardpoint.domain.Point;
import java.time.LocalDate;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PointRepository extends JpaRepository<Point, Long> {

    List<Point> findByMemberIdAndRemainAmountIsGreaterThanAndExpiresDateIsLessThanEqualAndCanceledAtIsNull(
            Integer memberId,
            Integer remainAmount,
            LocalDate date
    );
}
