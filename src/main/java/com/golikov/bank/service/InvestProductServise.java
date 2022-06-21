package com.golikov.bank.service;

import com.golikov.bank.entity.InvestProduct;
import com.golikov.bank.repository.InvestProdRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class InvestProductServise {

    @Autowired
    InvestProdRepository investProdRepository;

    public void save(InvestProduct investProduct){
        investProdRepository.save(investProduct);
    }

    public List<InvestProduct> findAllByActive () {
        return investProdRepository.findAllByIsActiveIsTrueOrderByName();
    }

    public void delete(InvestProduct investProduct){
        investProduct.setActive(false);
        investProdRepository.save(investProduct);
    }


    public InvestProduct findById(InvestProduct investProduct){
        return investProdRepository.findById(investProduct.getId()).get();
    }


}
