package com.golikov.bank.domain.transaction;

import com.golikov.bank.domain.client.Client;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class TransactionService {

    private ClientTransactionRepository clientTransactionRepository;


    public List<ClientTransaction> getClientTransactions(Client client){
        return clientTransactionRepository.findAllClientTransactions(client.getId());
    }

}
