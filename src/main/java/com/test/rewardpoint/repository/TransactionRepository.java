package com.test.rewardpoint.repository;

import com.test.rewardpoint.domain.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TransactionRepository extends JpaRepository<Transaction, Long> {

}
