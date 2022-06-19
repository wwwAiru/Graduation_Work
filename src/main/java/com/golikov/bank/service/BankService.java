package com.golikov.bank.service;

import com.golikov.bank.entity.Client;
import com.golikov.bank.entity.ClientTransaction;
import com.golikov.bank.entity.DepositAccount;
import com.golikov.bank.repository.ClientRepository;
import com.golikov.bank.repository.ClientTransactionRepository;
import com.golikov.bank.repository.DepositAccountRepository;
import com.golikov.bank.utils.AccountNumGenerator;
import com.golikov.bank.utils.ProxyDepositAccount;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class BankService {

    @Autowired
    ClientRepository clientRepository;

    @Autowired
    DepositAccountRepository depositAccountRepository;

    @Autowired
    ClientTransactionRepository clientTransactionRepository;

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
        System.out.println(clientTransaction.getCardNumber());
        clientTransactionRepository.save(clientTransaction);
    }

    // метод для создания инвестиционных счетов, в этом методе дополняется сущность депозит акаунта
    // генерируется номер счёта, и устанавливается клиент владелец
    @Transactional
    public void createDepositAccount(Client client, DepositAccount depositAccount){
        depositAccount.setClient(client);
        depositAccount.setAccountNumber(AccountNumGenerator.generate(client.getId()));
        depositAccountRepository.save(depositAccount);
    }

    //переревод денег с баланса на банкововский счёт
    @Transactional
    public void upDepositAccountBalance(Client client, ProxyDepositAccount proxyDepositAccount){
        DepositAccount depositAccount = proxyDepositAccount.getDepositAccount();
        BigDecimal amount = proxyDepositAccount.getAmount();
        // вычитается сумма с баланса клиента
        client.setBalance(client.getBalance().subtract(amount));
        // если валюта не рубль то amount пересчитать по курсу соответствующей валюты
        if (!depositAccount.getCurrency().equals("RUB")){
            amount = amount.divide(currencyService.getCurrencies().get(depositAccount.getCurrency()).getValue(),2,  RoundingMode.HALF_UP);
        }
        // сохранение изменений в базу
        depositAccount.setDepositBalance(depositAccount.getDepositBalance().add(amount));
        clientRepository.save(client);
        depositAccountRepository.save(depositAccount);
    }

    @Transactional
    public List<DepositAccount> findValidDepoAccounts(Long id, String currency, BigDecimal balance){
        return depositAccountRepository.findByClientIdAndCurrencyLikeAndDepositBalanceGreaterThan(id, currency, balance);
    }

    @Transactional
    public void withdrawMoney(Client client, DepositAccount depositAccount, BigDecimal amount){
        // вычитается сумма с баланса аккаунта
        depositAccount.setDepositBalance(depositAccount.getDepositBalance().subtract(amount));
        // если валюта не рубль то amount пересчитать по курсу соответствующей валюты
        if (!depositAccount.getCurrency().equals("RUB")){
            amount = amount.multiply(currencyService.getCurrencies().get(depositAccount.getCurrency()).getValue());
        }
        client.setBalance(client.getBalance().add(amount));
        // сохранение изменений в базу
        clientRepository.save(client);
        depositAccountRepository.save(depositAccount);
    }

}
