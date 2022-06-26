package com.golikov.bank.domain.product;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class InvestProductServise {

    @Autowired
    InvestProductRepository investProductRepository;

    public void save(InvestProduct investProduct){
        investProductRepository.save(investProduct);
    }

    public List<InvestProduct> findAllByActive () {
        return investProductRepository.findAllByIsActiveIsTrueOrderByName();
    }

    public void delete(InvestProduct investProduct){
        investProduct.setActive(false);
        investProductRepository.save(investProduct);
    }


    public InvestProduct findById(InvestProduct investProduct){
        return investProductRepository.findById(investProduct.getId()).get();
    }


}
