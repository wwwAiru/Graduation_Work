package com.golikov.bank.repository;

import com.golikov.bank.entity.DepositAccount;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface DepositAccountRepository extends CrudRepository<DepositAccount, Long> {

    public List<DepositAccount> findDepositAccountByClientId(Long id);
}
