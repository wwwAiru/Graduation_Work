package com.golikov.bank.repository;

import com.golikov.bank.entity.DepositAccount;
import org.springframework.data.jpa.repository.JpaRepository;

import java.math.BigDecimal;
import java.util.List;

public interface DepositAccountRepository extends JpaRepository<DepositAccount, Long> {

    public List<DepositAccount> findByClientIdOrderById(Long id);

    public List<DepositAccount> findByClientIdAndCurrencyLikeAndBalanceGreaterThan(Long id, String currency, BigDecimal balance);
}
