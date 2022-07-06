package com.golikov.bank.domain.account.dto;

import com.golikov.bank.domain.account.Account;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.DecimalMin;
import java.math.BigDecimal;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class UpAccountBalanceFormDto {

    private Account chosenAccount;

    private List<Account> accounts;

    @DecimalMin(value = "1.00", message = "Вы не можете перевести меньше 1")
    private BigDecimal amount;

    public UpAccountBalanceFormDto(List<Account> accounts) {
        this.accounts = accounts;
    }
}
