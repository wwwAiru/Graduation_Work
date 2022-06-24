package com.golikov.bank.repository;

import com.golikov.bank.entity.Account;
import org.springframework.data.jpa.repository.JpaRepository;

import java.math.BigDecimal;
import java.util.List;

public interface AccountRepository extends JpaRepository<Account, Long> {

    public List<Account> findByClientIdOrderById(Long id);

    public List<Account> findByClientIdAndCurrencyLikeAndBalanceGreaterThan(Long id, String currency, BigDecimal balance);
}
