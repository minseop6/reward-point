package com.test.rewardpoint.service;

import com.test.rewardpoint.common.exception.NotFoundException;
import com.test.rewardpoint.domain.MemberPointConfiguration;
import com.test.rewardpoint.domain.Point;
import com.test.rewardpoint.domain.PointConfiguration;
import com.test.rewardpoint.domain.Points;
import com.test.rewardpoint.domain.Transaction;
import com.test.rewardpoint.dto.PointCancellationRequest;
import com.test.rewardpoint.dto.PointCreationRequest;
import com.test.rewardpoint.dto.PointUsingRequest;
import com.test.rewardpoint.outbound.repository.MemberPointConfigurationRepository;
import com.test.rewardpoint.outbound.repository.PointConfigurationRepository;
import com.test.rewardpoint.outbound.repository.PointRepository;
import com.test.rewardpoint.outbound.repository.TransactionRepository;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PointService {

    private final PointRepository pointRepository;
    private final PointConfigurationRepository pointConfigurationRepository;
    private final MemberPointConfigurationRepository memberPointConfigurationRepository;
    private final TransactionRepository transactionRepository;

    @Transactional
    public void creatPoint(PointCreationRequest request) {
        LocalDate today = LocalDate.now();
        PointConfiguration pointConfiguration = pointConfigurationRepository.findTopByDeletedAtIsNull()
                .orElse(PointConfiguration.getDefaultPointConfiguration());
        MemberPointConfiguration memberPointConfiguration = memberPointConfigurationRepository.findTopByDeletedAtIsNull()
                .orElse(MemberPointConfiguration.getDefaultMemberPointConfiguration(request.getMemberId()));
        List<Point> memberPoints = pointRepository.findByMemberIdAndRemainAmountIsGreaterThanAndExpiresDateIsGreaterThanEqualAndCanceledAtIsNull(
                request.getMemberId(),
                0,
                today
        );
        Points points = new Points(memberPoints);
        int memberTotalPoint = points.calculateTotalRemainAmount() + request.getAmount();

        pointConfiguration.validatePoint(request.getAmount());
        memberPointConfiguration.validatePoint(memberTotalPoint);
        Point point = request.toPointEntity();

        pointRepository.save(point);
    }

    @Transactional
    public void withdrawPoint(long pointId) {
        LocalDateTime canceledAt = LocalDateTime.now();
        Point point = pointRepository.findById(pointId)
                .orElseThrow(() -> new NotFoundException("포인트가 존재하지 않습니다."));
        point.withdraw(canceledAt);
    }

    @Transactional
    public void usePoint(PointUsingRequest request) {
        LocalDate today = LocalDate.now();
        List<Point> memberPoints = pointRepository.findByMemberIdAndRemainAmountIsGreaterThanAndExpiresDateIsGreaterThanEqualAndCanceledAtIsNull(
                request.getMemberId(),
                0,
                today
        );
        Points points = new Points(memberPoints);
        Transaction transaction = transactionRepository.save(request.toTransaction());
        points.use(transaction);
    }

    @Transactional
    public void cancelPoint(PointCancellationRequest request) {
        LocalDateTime canceledAt = LocalDateTime.now();
        Transaction transaction = transactionRepository.findByOrderId(request.getOrderId())
                .orElseThrow(() -> new NotFoundException("포인트 사용 내역이 존재하지 않습니다."));
        List<Point> usedPoints = pointRepository.findByTransactionId(transaction.getId());
        Points points = new Points(usedPoints);

        transaction.cancel(request.getCancelAmount());
        points.cancel(transaction.getId(), request.getCancelAmount(), canceledAt);
    }
}
