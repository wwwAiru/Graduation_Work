package com.golikov.bank.service;

import com.golikov.bank.entity.InvestProduct;
import com.golikov.bank.repository.InvestProdRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class InvesttProductServise {

    @Autowired
    InvestProdRepository investProdRepository;

    public void save(InvestProduct investProduct){
        investProdRepository.save(investProduct);
    }


}
