package com.wis1.bank.repository;

import com.wis1.bank.repository.entity.TransactionLog;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface TransactionLogRepository extends JpaRepository<TransactionLog, UUID> {
}
