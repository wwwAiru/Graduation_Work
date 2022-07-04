package com.golikov.bank.domain.account;

import org.springframework.data.jpa.repository.JpaRepository;

import java.math.BigDecimal;
import java.util.List;

public interface AccountRepository extends JpaRepository<Account, Long> {

    List<Account> findByClientIdOrderById(Long id);

    List<Account> findByClientIdAndCurrencyLikeAndBalanceGreaterThan(Long id, String currency, BigDecimal balance);
}
