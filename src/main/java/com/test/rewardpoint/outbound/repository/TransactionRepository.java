package com.test.rewardpoint.outbound.repository;

import com.test.rewardpoint.domain.Transaction;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TransactionRepository extends JpaRepository<Transaction, Long> {

    Optional<Transaction> findByOrderId(int orderId);
}
