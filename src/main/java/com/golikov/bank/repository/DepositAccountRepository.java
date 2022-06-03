package com.golikov.bank.repository;

import com.golikov.bank.entity.DepositAccount;
import org.springframework.data.repository.CrudRepository;

public interface DepositAccountRepository extends CrudRepository<DepositAccount, Long> {
}
