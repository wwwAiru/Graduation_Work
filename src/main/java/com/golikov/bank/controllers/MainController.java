package com.golikov.bank.controllers;

import com.golikov.bank.entity.InvestProduct;
import com.golikov.bank.repository.InvestProdRepository;
import com.golikov.bank.service.InvesttProductServise;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;

@Controller
public class MainController {
    @Autowired
    private InvestProdRepository investProdRepository;

    @Autowired
    InvesttProductServise investtProductServise;

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
        investtProductServise.save(invProduct);
        return "redirect:/deposits";
    }

    @GetMapping("/product/edit/{investProduct}")
    public String editProduct(Model model, @PathVariable InvestProduct investProduct){
        model.addAttribute("investProduct", investProduct);
        return "deposit-edit";
    }

    @PostMapping("/product/edit/save")
    public String saveEditedProduct(@ModelAttribute InvestProduct investProduct){
        investtProductServise.save(investProduct);
        return "redirect:/deposits";
    }

//    @GetMapping("/deposit_by_rate")
//    public String depositsByRate(Model model){
//        Iterable<InvestProduct> invProducts = investProdRepository.findAllByOrderByInterestRateDesc();
//        model.addAttribute("invProducts", invProducts);
//        return "deposits";
//    }


}
