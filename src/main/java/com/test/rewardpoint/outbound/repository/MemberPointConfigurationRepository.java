package com.test.rewardpoint.outbound.repository;

import com.test.rewardpoint.domain.MemberPointConfiguration;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberPointConfigurationRepository extends JpaRepository<MemberPointConfiguration, Long> {

    Optional<MemberPointConfiguration> findTopByDeletedAtIsNull();
}
