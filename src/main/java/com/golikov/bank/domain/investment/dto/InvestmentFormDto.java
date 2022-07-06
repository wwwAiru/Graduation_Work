package com.golikov.bank.domain.investment.dto;

import com.golikov.bank.domain.account.Account;
import com.golikov.bank.domain.product.InvestProduct;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.List;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class InvestmentFormDto {

    private BigDecimal amount;

    private Integer days;

    private Account chosenAccount;

    private List<Account> accounts;

    private InvestProduct investProduct;

    public InvestmentFormDto(List<Account> accounts, InvestProduct investProduct) {
        this.accounts = accounts;
        this.investProduct = investProduct;
    }
}

