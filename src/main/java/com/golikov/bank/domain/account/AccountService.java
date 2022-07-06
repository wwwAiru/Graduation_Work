package com.golikov.bank.domain.account;

import com.golikov.bank.domain.account.transaction.ClientTransaction;
import com.golikov.bank.domain.account.transaction.ClientTransactionRepository;
import com.golikov.bank.domain.account.utils.AccountNumGenerator;
import com.golikov.bank.domain.client.Client;
import com.golikov.bank.domain.client.ClientRepository;
import com.golikov.bank.domain.currency.CurrencyHolder;
import com.golikov.bank.domain.investment.ClientInvestProd;
import com.golikov.bank.domain.investment.ClientInvestProdRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.List;

@Service
@AllArgsConstructor
public class AccountService {

    private ClientRepository clientRepository;

    private AccountRepository accountRepository;

    private ClientTransactionRepository clientTransactionRepository;

    private ClientInvestProdRepository clientInvestProdRepository;

    private CurrencyHolder currencyHolder;

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

    // вывод денег с общего баланса на карту и апись в историю транзакций
    @Transactional
    public void balanceToCard(ClientTransaction clientTransaction, Client client){
        clientTransaction.setDate(LocalDateTime.now());
        clientTransaction.setClientId(client.getId());
        clientTransaction.setTransactionType("Вывод средств");
        BigDecimal currentBalance = client.getBalance();
        BigDecimal amount = clientTransaction.getAmount();
        client.setBalance(currentBalance.subtract(amount));
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

    //переревод денег с баланса на банкововский счёт (аккаунт)
    @Transactional
    public void upAccountBalance(Client client, Account account, BigDecimal amount){
        // вычитается сумма с баланса клиента
        client.setBalance(client.getBalance().subtract(amount));
        // если валюта не рубль то amount пересчитать по курсу соответствующей валюты
        if (!account.getCurrency().equals("RUB")){
            amount = amount.divide(currencyHolder.getCurrencies().get(account.getCurrency()).getValue(),2,  RoundingMode.HALF_UP);
        }
        account.setBalance(account.getBalance().add(amount));
        clientRepository.save(client);
        accountRepository.save(account);
    }

    // найти все аккаунты, которые подходят по параметрам вклада
    // должны совпадать валюта, и дожно быть достаточно денег
    @Transactional
    public List<Account> findValidDepoAccounts(Long id, String currency, BigDecimal balance){
        return accountRepository.findByClientIdAndCurrencyLikeAndBalanceGreaterThan(id, currency, balance);
    }

    // вывод денег с баланса аккауунта
    @Transactional
    public void withdrawMoney(Client client, Account account, BigDecimal amount){
        // вычитается сумма с баланса аккаунта
        account.setBalance(account.getBalance().subtract(amount));
        // если валюта не рубль то amount пересчитать по курсу соответствующей валюты
        if (!account.getCurrency().equals("RUB")){
            amount = amount.multiply(currencyHolder.getCurrencies().get(account.getCurrency()).getValue());
        }
        client.setBalance(client.getBalance().add(amount));
        clientRepository.save(client);
        accountRepository.save(account);
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
