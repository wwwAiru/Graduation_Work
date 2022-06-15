package com.golikov.bank.utils;

import com.golikov.bank.entity.DepositAccount;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@NoArgsConstructor
@Getter
@Setter
public class ProxyDepositAccount {

    private DepositAccount depositAccount;

    private BigDecimal amount;

}
