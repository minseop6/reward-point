package com.test.rewardpoint.repository;

import com.test.rewardpoint.domain.PointConfiguration;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PointConfigurationRepository extends JpaRepository<PointConfiguration, Integer> {

    Optional<PointConfiguration> findTopByDeletedAtIsNull();
}
