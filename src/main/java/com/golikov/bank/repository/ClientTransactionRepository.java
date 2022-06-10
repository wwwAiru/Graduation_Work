package com.golikov.bank.repository;

import com.golikov.bank.entity.ClientTransaction;
import org.springframework.data.repository.CrudRepository;

public interface ClientTransactionRepository extends CrudRepository<ClientTransaction, Long> {
}
