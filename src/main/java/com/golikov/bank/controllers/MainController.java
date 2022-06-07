package com.golikov.bank.controllers;

import com.golikov.bank.entity.InvestProduct;
import com.golikov.bank.repository.InvestProdRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.math.BigDecimal;

@Controller
public class MainController {
    @Autowired
    private InvestProdRepository investProdRepository;

    @GetMapping("/")
    public String homepage(Model model) {
        model.addAttribute("title", "Главная страница");
        return "homepage";
    }

    @GetMapping("/deposits")
    public String main(Model model){
        Iterable<InvestProduct> invProducts = investProdRepository.findAll();
        model.addAttribute("invProducts", invProducts);
        return "deposits";
    }

    @PostMapping("/add_inv_product")
    public String addinvProduct(@RequestParam String name,
                                @RequestParam String description,
                                @RequestParam BigDecimal minDeposit,
                                @RequestParam BigDecimal maxDeposit,
                                @RequestParam BigDecimal interestRate,
                                @RequestParam Long depositTerm,
                                @RequestParam boolean isActive){
        InvestProduct invProduct = new InvestProduct(name, description, minDeposit, maxDeposit, interestRate, depositTerm, isActive);
        investProdRepository.save(invProduct);
        return "deposits";
    }
    @PostMapping("/deposit_filter")
    public String productFilter(@RequestParam String name, Model model){

        return "depostits";
    }


}
