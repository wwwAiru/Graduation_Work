package com.golikov.bank.service;

import com.golikov.bank.entity.ClientTransaction;
import com.golikov.bank.repository.ClientTransactionRepository;
import com.golikov.bank.repository.ClientRepository;
import com.golikov.bank.repository.DepositAccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class BankService {

    @Autowired
    ClientRepository clientRepository;

    @Autowired
    DepositAccountRepository depositAccountRepository;

    @Autowired
    ClientTransactionRepository clientTransactionRepository;


    public void cardToBaLance(ClientTransaction clientTransaction){

    }


}
