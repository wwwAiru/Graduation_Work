package com.golikov.bank.domain.investment;

import com.golikov.bank.domain.account.Account;
import com.golikov.bank.domain.account.AccountRepository;
import com.golikov.bank.domain.investment.dto.InvestmentFormDto;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;

@Service
@AllArgsConstructor
public class InvestmentService {

    private AccountRepository accountRepository;

    private ClientInvestProdRepository clientInvestProdRepository;

    @Transactional
    public void makeInvest(InvestmentFormDto investmentForm) {
        BigDecimal hundred = BigDecimal.valueOf(100);
        BigDecimal year = BigDecimal.valueOf(365);
        Account account = investmentForm.getChosenAccount();
        ClientInvestProd investment = new ClientInvestProd();
        investment.setAccount(account);
        investment.setInvestProduct(investmentForm.getInvestProduct());
        investment.setBalance(investmentForm.getAmount());
        investment.setDays(investmentForm.getDays());
        //    расчёт процентов вклада
        BigDecimal profit = investmentForm.getInvestProduct().getInterestRate()
                                .divide(hundred, 2, RoundingMode.HALF_UP)
                                    .divide(year, 16, RoundingMode.HALF_UP)
                                        .multiply(BigDecimal.valueOf(investmentForm.getDays()))
                                            .add(BigDecimal.valueOf(1))
                                                .multiply(investmentForm.getAmount())
                                                    .subtract(investmentForm.getAmount());
        //    операции по перемещению денег
        account.setBalance(account.getBalance().subtract(investmentForm.getAmount()));
        investment.setBeginDate(LocalDateTime.now());
        investment.setExpireDate(investment.getBeginDate().plusDays(investment.getDays()));
        investment.setProfit(profit);
        account.addClientInvestProd(investment);
        accountRepository.save(account);
        clientInvestProdRepository.save(investment);
    }

}
