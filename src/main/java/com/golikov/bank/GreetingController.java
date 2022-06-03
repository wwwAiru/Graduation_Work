package com.golikov.bank;

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
public class GreetingController {
    @Autowired
    private InvestProdRepository investProdRepository;

    @GetMapping("/greeting")
    public String greeting(
            @RequestParam(name="name", required=false, defaultValue="World") String name,
            Model model) {
        model.addAttribute("name", name);
        return "greeting";
    }

    @GetMapping
    public String main(Model model){
        Iterable<InvestProduct> invProducts = investProdRepository.findAll();
        model.addAttribute("invProducts", invProducts);
        return "index";
    }
    @PostMapping
    public String addinvProduct(@RequestParam String name,
                                @RequestParam String description,
                                @RequestParam BigDecimal minDeposit,
                                @RequestParam BigDecimal maxDeposit,
                                @RequestParam BigDecimal interestRate,
                                @RequestParam Long depositTerm){
        InvestProduct invProduct = new InvestProduct(name, description, minDeposit, maxDeposit, interestRate, depositTerm);
        investProdRepository.save(invProduct);
        return "index";
    }

}
