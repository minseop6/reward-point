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
@Entity(name = "point_configuration")
public class PointConfiguration extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false)
    private Integer id;

    @Column(nullable = false)
    private Integer minimumPointPerGrant;

    @Column(nullable = false)
    private Integer maximumPointPerGrant;

    @Column
    private LocalDateTime deletedAt;

    @Builder
    public PointConfiguration(Integer minimumPointPerGrant, Integer maximumPointPerGrant) {
        this.minimumPointPerGrant = minimumPointPerGrant;
        this.maximumPointPerGrant = maximumPointPerGrant;
    }

    public static PointConfiguration getDefaultPointConfiguration() {
        return PointConfiguration.builder()
                .minimumPointPerGrant(1000)
                .maximumPointPerGrant(100000)
                .build();
    }

    public void validatePoint(int point) {
        if (point < minimumPointPerGrant) {
            throw new BadRequestException("지급할 수 있는 최소 포인트는 " + minimumPointPerGrant + "입니다.");
        }
        if (point > maximumPointPerGrant) {
            throw new BadRequestException("지급할 수 있는 최대 포인트는" + maximumPointPerGrant + "입니다.");
        }
    }
}
