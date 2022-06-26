package com.golikov.bank.domain.account;

import com.golikov.bank.domain.client.Client;
import com.golikov.bank.domain.investment.ClientInvestProd;
import com.golikov.bank.domain.product.InvestProduct;
import com.golikov.bank.domain.account.transaction.ClientTransaction;
import com.golikov.bank.domain.investment.ClientInvestProdRepository;
import com.golikov.bank.domain.client.ClientRepository;
import com.golikov.bank.domain.account.transaction.ClientTransactionRepository;
import com.golikov.bank.domain.currency.CurrencyService;
import com.golikov.bank.domain.account.utils.AccountNumGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class AccountService {

    @Autowired
    ClientRepository clientRepository;

    @Autowired
    AccountRepository accountRepository;

    @Autowired
    ClientTransactionRepository clientTransactionRepository;

    @Autowired
    ClientInvestProdRepository clientInvestProdRepository;

    @Autowired
    CurrencyService currencyService;

    // метод для пополнения баланса клиента и запись истории транзакции
    @Transactional
    public void cardToBaLance(ClientTransaction clientTransaction, Client client){
        clientTransaction.setDate(LocalDateTime.now());
        clientTransaction.setClientId(client.getId());
        clientTransaction.setTransactionType("Пополнение баланса");
        BigDecimal currentBalance = client.getBalance();
        BigDecimal amount = clientTransaction.getAmount();
        client.setBalance(currentBalance.add(amount));
        clientRepository.save(client);
        clientTransactionRepository.save(clientTransaction);
    }

    // метод для создания инвестиционных счетов, в этом методе дополняется сущность депозит акаунта
    // генерируется номер счёта, и устанавливается клиент владелец
    @Transactional
    public void createAccount(Client client, Account account){
        account.setClient(client);
        account.setAccountNumber(AccountNumGenerator.generate(client.getId()));
        accountRepository.save(account);
    }

    //переревод денег с баланса на банкововский счёт
    @Transactional
    public void upAccountBalance(Client client, Long id, BigDecimal amount){
        Account account = accountRepository.findById(id).get();
        // вычитается сумма с баланса клиента
        client.setBalance(client.getBalance().subtract(amount));
        // если валюта не рубль то amount пересчитать по курсу соответствующей валюты
        if (!account.getCurrency().equals("RUB")){
            amount = amount.divide(currencyService.getCurrencies().get(account.getCurrency()).getValue(),2,  RoundingMode.HALF_UP);
        }
        account.setBalance(account.getBalance().add(amount));
        clientRepository.save(client);
        accountRepository.save(account);
    }

    @Transactional
    public List<Account> findValidDepoAccounts(Long id, String currency, BigDecimal balance){
        return accountRepository.findByClientIdAndCurrencyLikeAndBalanceGreaterThan(id, currency, balance);
    }

    @Transactional
    public void withdrawMoney(Client client, Account account, BigDecimal amount){
        // вычитается сумма с баланса аккаунта
        account.setBalance(account.getBalance().subtract(amount));
        // если валюта не рубль то amount пересчитать по курсу соответствующей валюты
        if (!account.getCurrency().equals("RUB")){
            amount = amount.multiply(currencyService.getCurrencies().get(account.getCurrency()).getValue());
        }
        client.setBalance(client.getBalance().add(amount));
        clientRepository.save(client);
        accountRepository.save(account);
    }
    @Transactional
    public void makeInvest(ClientInvestProd investment, Account account, InvestProduct investProduct) {
        BigDecimal hundred = BigDecimal.valueOf(100);
        BigDecimal year = BigDecimal.valueOf(365);
        //    расчёт процентов вклада
        BigDecimal profit = investProduct.getInterestRate()
                                .divide(hundred, 2, RoundingMode.HALF_UP)
                                    .divide(year, 16, RoundingMode.HALF_UP)
                                        .multiply(BigDecimal.valueOf(investment.getDays()))
                                            .add(BigDecimal.valueOf(1))
                                                .multiply(investment.getBalance())
                                                    .subtract(investment.getBalance());
        //    операции по перемещению денег
        account.setBalance(account.getBalance().subtract(investment.getBalance()));
        account.addClientInvestProd(investment);
        investment.setInvestProduct(investProduct);
        investment.setBeginDate(LocalDateTime.now());
        investment.setExpireDate(investment.getBeginDate().plusDays(investment.getDays()));
        investment.setProfit(profit);
        accountRepository.save(account);
        clientInvestProdRepository.save(investment);
    }

    // закрытие вклада
    @Transactional
    public void closeInvestment(ClientInvestProd investment){
        Account account = investment.getAccount();
        BigDecimal accountBalance = account.getBalance();
        BigDecimal investmentProfit = investment.getProfit();
        LocalDateTime expireDateTime = investment.getExpireDate();
        // проверка истёк ли срок вклада
        if (LocalDateTime.now().isAfter(expireDateTime)){
            account.setBalance(accountBalance.add(investmentProfit).add(investment.getBalance()));
        } else {
            account.setBalance(accountBalance.add(investment.getBalance()));
        }
        accountRepository.save(account);
        clientInvestProdRepository.delete(investment);
    }

}
