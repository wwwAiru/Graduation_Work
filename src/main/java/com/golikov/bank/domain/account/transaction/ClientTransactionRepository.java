package com.golikov.bank.domain.account.transaction;

import org.springframework.data.jpa.repository.JpaRepository;

public interface ClientTransactionRepository extends JpaRepository<ClientTransaction, Long> {
}
