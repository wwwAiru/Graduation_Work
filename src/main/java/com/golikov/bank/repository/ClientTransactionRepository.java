package com.golikov.bank.repository;

import com.golikov.bank.entity.ClientTransaction;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ClientTransactionRepository extends JpaRepository<ClientTransaction, Long> {
}
