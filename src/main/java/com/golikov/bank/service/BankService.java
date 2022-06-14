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

@Service
public class BankService {

    @Autowired
    ClientRepository clientRepository;

    @Autowired
    DepositAccountRepository depositAccountRepository;

    @Autowired
    ClientTransactionRepository clientTransactionRepository;

    // метод для пополнения баланса клиента и запись истории транзакции
    @Transactional
    public void cardToBaLance(ClientTransaction clientTransaction, Client client){
        BigDecimal currentBalance = client.getBalance();
        BigDecimal amount = clientTransaction.getAmount();
        client.setBalance(currentBalance.add(amount));
        clientRepository.save(client);
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

    public String upDepositAccounBalance(Client client, ProxyDepositAccount proxyDepositAccount){
        BigDecimal clientBalance = client.getBalance();
        BigDecimal amount = proxyDepositAccount.getAmount();
        if (clientBalance.compareTo(amount) < 0) {
            return "Некорректное значение. Перевод отменён.";
        }
        DepositAccount depositAccount = proxyDepositAccount.getDepositAccount();
        BigDecimal currentDepoBalance = depositAccount.getDepositBalance();

        client.setBalance(clientBalance.subtract(amount));
        depositAccount.setDepositBalance(currentDepoBalance.add(amount));
        System.out.println(depositAccount.getDepositBalance());
        depositAccountRepository.save(depositAccount);
        return "Баланс инвестиционного счёта успешно пополнен.";
    }


}
