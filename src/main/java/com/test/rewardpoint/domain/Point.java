package com.test.rewardpoint.domain;

import com.test.rewardpoint.common.exception.BadRequestException;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity(name = "point")
public class Point extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false)
    private Long id;

    @Column(nullable = false)
    private Integer memberId;

    @Column(nullable = false)
    private Integer amount;

    @Column(nullable = false)
    private Integer remainAmount;

    @Column(nullable = false)
    private GrantBy grantBy;

    @Column(nullable = false)
    private String description;

    @Column(nullable = false)
    private LocalDate expiresDate;

    @OneToMany
    private List<UsedPoint> usedPoints;

    @Builder
    public Point(Integer memberId, Integer amount, GrantBy grantBy, String description,
            LocalDate expiresDate) {
        LocalDate today = LocalDate.now();
        if (expiresDate.isBefore(today.plusDays(1)) || expiresDate.isAfter(today.plusYears(5))) {
            throw new BadRequestException("유효기간은 최소 1일 이상 최대 5년 미만으로 설정 가능합니다.");
        }
        this.memberId = memberId;
        this.amount = amount;
        this.remainAmount = amount;
        this.grantBy = grantBy;
        this.description = description;
        this.expiresDate = expiresDate;
    }
}