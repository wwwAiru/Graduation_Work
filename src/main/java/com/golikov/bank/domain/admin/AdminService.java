package com.golikov.bank.domain.admin;

import com.golikov.bank.domain.client.Client;
import com.golikov.bank.domain.client.ClientRepository;
import com.golikov.bank.domain.product.InvestProduct;
import com.golikov.bank.domain.product.InvestProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AdminService {

    @Autowired
    ClientRepository clientRepository;

    @Autowired
    InvestProductRepository investProductRepository;

    public List<Client> findAllClients(){
        return  clientRepository.findAllByOrderById();
    }

    public void save(Client client){
        client.setPassword(client.getPassword());
        clientRepository.save(client);
    }

    public List<InvestProduct> findAllDisabledProducts(){
        return investProductRepository.findAllByIsActiveIsFalseOrderById();
    }
}
