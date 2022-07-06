package com.golikov.bank.domain.account.dto;

import com.golikov.bank.domain.account.Account;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.Min;
import java.math.BigDecimal;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class WithdrawAccountBalanceFormDto {

    private Account account;

    @DecimalMin(value = "0.01", message = "Вы не можете перевести меньше 0.01")
    private BigDecimal amount;

}
