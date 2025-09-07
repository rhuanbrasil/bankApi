package com.api.bankApi.infra.repository;

import com.api.bankApi.business.dtos.TransactionResponseDto;
import com.api.bankApi.infra.entitys.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface TransactionRepository extends JpaRepository<Transaction,Long> {
    Transaction findByTransactionId(UUID transactionId);
}
