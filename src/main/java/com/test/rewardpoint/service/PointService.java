package com.test.rewardpoint.service;

import com.test.rewardpoint.domain.MemberPointConfiguration;
import com.test.rewardpoint.domain.Point;
import com.test.rewardpoint.domain.PointConfiguration;
import com.test.rewardpoint.domain.Points;
import com.test.rewardpoint.dto.PointCreationRequest;
import com.test.rewardpoint.repository.MemberPointConfigurationRepository;
import com.test.rewardpoint.repository.PointConfigurationRepository;
import com.test.rewardpoint.repository.PointRepository;
import java.time.LocalDate;
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

    @Transactional
    public void creatPoint(PointCreationRequest request) {
        LocalDate today = LocalDate.now();
        PointConfiguration pointConfiguration = pointConfigurationRepository.findTopByDeletedAtIsNull()
                .orElse(PointConfiguration.getDefaultPointConfiguration());
        MemberPointConfiguration memberPointConfiguration = memberPointConfigurationRepository.findTopByDeletedAtIsNull()
                .orElse(MemberPointConfiguration.getDefaultMemberPointConfiguration(request.getMemberId()));
        Points memberPoints = new Points(
                pointRepository.findByMemberIdAndRemainAmountIsGreaterThanAndExpiresDateIsLessThanEqual(
                        request.getMemberId(),
                        0,
                        today
                )
        );
        int memberTotalPoint = memberPoints.calculateTotalRemainAmount() + request.getAmount();

        pointConfiguration.validatePoint(request.getAmount());
        memberPointConfiguration.validatePoint(memberTotalPoint);
        Point point = request.toPointEntity();

        pointRepository.save(point);
    }
}
