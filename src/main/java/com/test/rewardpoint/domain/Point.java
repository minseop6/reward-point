package com.test.rewardpoint.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import java.util.List;
import lombok.AccessLevel;
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
    
    @OneToMany
    private List<UsedPoint> usedPoints;
}

enum GrantBy {
    SYSTEM,
    ADMIN,
}