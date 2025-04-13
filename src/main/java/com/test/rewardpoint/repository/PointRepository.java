package com.test.rewardpoint.repository;

import com.test.rewardpoint.domain.Point;
import java.time.LocalDate;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface PointRepository extends JpaRepository<Point, Long> {

    List<Point> findByMemberIdAndRemainAmountIsGreaterThanAndExpiresDateIsGreaterThanEqualAndCanceledAtIsNull(
            Integer memberId,
            Integer remainAmount,
            LocalDate date
    );

    @Query("SELECT p FROM point p JOIN FETCH p.usedPoints u WHERE u.transactionId = :transactionId AND u.canceledAt IS NULL")
    List<Point> findByTransactionId(int transactionId);
}
