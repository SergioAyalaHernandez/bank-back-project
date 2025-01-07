package com.sergio.bank.repository;

import com.sergio.bank.model.TransactionLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface TransactionLogRepository extends JpaRepository<TransactionLog, Long> {
    List<TransactionLog> findBySourceAccountId(Long accountId);
    List<TransactionLog> findByDestinationAccountId(Long accountId);
    List<TransactionLog> findByStatus(String status);
    List<TransactionLog> findByTimestampBetween(LocalDateTime start, LocalDateTime end);
}
