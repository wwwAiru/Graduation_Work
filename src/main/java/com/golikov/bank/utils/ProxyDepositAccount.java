package com.golikov.bank.utils;

import com.golikov.bank.entity.DepositAccount;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.math.BigDecimal;

@NoArgsConstructor
@Getter
@Setter
public class ProxyDepositAccount {
    private DepositAccount depositAccount;
    private BigDecimal depositBalance;
}
