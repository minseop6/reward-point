package com.test.rewardpoint.domain;

import com.test.rewardpoint.common.exception.BadRequestException;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import java.time.LocalDateTime;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity(name = "member_point_configuration")
public class MemberPointConfiguration extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false)
    private Integer id;

    @Column(nullable = false)
    private Integer memberId;

    @Column(nullable = false)
    private Integer maximumPointLimit;

    @Column
    private LocalDateTime deletedAt;

    @Builder
    public MemberPointConfiguration(Integer memberId, Integer maximumPointLimit) {
        this.memberId = memberId;
        this.maximumPointLimit = maximumPointLimit;
    }

    public static MemberPointConfiguration getDefaultMemberPointConfiguration(Integer memberId) {
        return MemberPointConfiguration.builder()
                .memberId(memberId)
                .maximumPointLimit(10000000)
                .build();
    }

    public void validatePoint(int point, Points memberPoints) {
        if (memberPoints.calculateTotalRemainAmount() + point > maximumPointLimit) {
            throw new BadRequestException("최대 보유할 수 있는 적립금은 " + maximumPointLimit + "입니다.");
        }
    }
}
