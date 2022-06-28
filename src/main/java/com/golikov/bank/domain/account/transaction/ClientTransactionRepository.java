package com.golikov.bank.domain.account.transaction;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ClientTransactionRepository extends JpaRepository<ClientTransaction, Long> {

    @Query("SELECT ct FROM ClientTransaction ct WHERE ct.clientId = :id ")
    public List<ClientTransaction> findAllClientTransactions(@Param("id") Long id);
}
