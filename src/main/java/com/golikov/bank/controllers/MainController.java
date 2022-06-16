package com.golikov.bank.controllers;

import com.golikov.bank.entity.InvestProduct;
import com.golikov.bank.repository.InvestProdRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.math.BigDecimal;

@Controller
public class MainController {
    @Autowired
    private InvestProdRepository investProdRepository;

    @RequestMapping("/login")
    public String login(Model model) {
        return "login";
    }

    @GetMapping("/")
    public String homepage(Model model) {
        return "homepage";
    }

    @GetMapping("/deposits")
    public String deposits(Model model){
        Iterable<InvestProduct> invProducts = investProdRepository.findAll();
        model.addAttribute("invProducts", invProducts);
        model.addAttribute("currentPage", "deposits");
        return "deposits";
    }

    @PostMapping("/add_inv_product")
    public String addinvProduct(@RequestParam String name,
                                @RequestParam String description,
                                @RequestParam String currency,
                                @RequestParam BigDecimal minDeposit,
                                @RequestParam BigDecimal maxDeposit,
                                @RequestParam BigDecimal interestRate,
                                @RequestParam Long depositTerm,
                                @RequestParam boolean isActive){
        InvestProduct invProduct = new InvestProduct(name, description, currency, minDeposit, maxDeposit, interestRate, depositTerm, isActive);
        investProdRepository.save(invProduct);
        return "redirect:/deposits";
    }
    @GetMapping("/deposit_by_rate")
    public String depositsByRate(Model model){
        Iterable<InvestProduct> invProducts = investProdRepository.findAllByOrderByInterestRateDesc();
        model.addAttribute("invProducts", invProducts);
        return "deposits";
    }


}
