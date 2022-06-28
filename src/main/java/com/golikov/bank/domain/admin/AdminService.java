package com.golikov.bank.domain.admin;

import com.golikov.bank.domain.client.Client;
import com.golikov.bank.domain.client.ClientRepository;
import com.golikov.bank.domain.investment.ClientInvestProd;
import com.golikov.bank.domain.investment.ClientInvestProdRepository;
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

    @Autowired
    ClientInvestProdRepository clientInvestProdRepository;

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

    public List<ClientInvestProd> findAllClientsInvestments(){
        return clientInvestProdRepository.findAll();
    }

    public void delete(InvestProduct investProduct){
        investProductRepository.delete(investProduct);
    }

    public void enableProfitableMode(){
        List<ClientInvestProd> investments = findAllClientsInvestments();
        investments.forEach(investment -> investment.setExpireDate(investment.getExpireDate().minusYears(11)));
        clientInvestProdRepository.saveAll(investments);
    }

    public void disableProfitableMode(){
        List<ClientInvestProd> investments = findAllClientsInvestments();
        investments.forEach(investment -> investment.setExpireDate(investment.getExpireDate().plusYears(11)));
        clientInvestProdRepository.saveAll(investments);
    }

}
