package com.golikov.bank.domain.account.transaction;

import com.golikov.bank.domain.account.Account;
import com.golikov.bank.domain.account.AccountRepository;
import com.golikov.bank.domain.account.utils.AccountNumGenerator;
import com.golikov.bank.domain.client.Client;
import com.golikov.bank.domain.client.ClientRepository;
import com.golikov.bank.domain.currency.CurrencyService;
import com.golikov.bank.domain.investment.ClientInvestProd;
import com.golikov.bank.domain.investment.ClientInvestProdRepository;
import com.golikov.bank.domain.product.InvestProduct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class TransactionService {

    @Autowired
    ClientTransactionRepository clientTransactionRepository;


    public List<ClientTransaction> getClientTransactions(Client client){
        return clientTransactionRepository.findAllClientTransactions(client.getId());
    }

}
