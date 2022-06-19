package com.golikov.bank.controllers;

import com.golikov.bank.entity.Client;
import com.golikov.bank.entity.DepositAccount;
import com.golikov.bank.entity.InvestProduct;
import com.golikov.bank.repository.InvestProdRepository;
import com.golikov.bank.service.BankService;
import com.golikov.bank.service.InvesttProductServise;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@Controller
public class ProductController {
    @Autowired
    private InvestProdRepository investProdRepository;

    @Autowired
    InvesttProductServise investtProductServise;

    @Autowired
    BankService bankService;


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

    // форма редактирование инвест продукта
    @GetMapping("/product/edit/{investProduct}")
    public String editProduct(Model model, @PathVariable InvestProduct investProduct){
        model.addAttribute("investProduct", investProduct);
        return "deposit-edit";
    }

    //сохранение изменений инвест продукта
    @PostMapping("/product/edit/save")
    public String saveEditedProduct(@ModelAttribute InvestProduct investProduct){
        investtProductServise.save(investProduct);
        return "redirect:/deposits";
    }

    //удаление инвест продукта(сделать неактивным)
    @GetMapping("/product/delete/{investProduct}")
    public String deleteProduct(@PathVariable InvestProduct investProduct){
        investtProductServise.delete(investProduct);
        return "redirect:/deposits";
    }

    @GetMapping("/product/invest/{investProduct}")
    public String invest(Model model,
                         @PathVariable InvestProduct investProduct,
                         @AuthenticationPrincipal Client client){
        List<DepositAccount> accounts = bankService.findValidDepoAccounts(client.getId(),
                                                                          investProduct.getCurrency(),
                                                                          investProduct.getMinDeposit());
        if (accounts.isEmpty()){
            model.addAttribute("NoValidAccountsError","У вас нет инвестиционных счетов для данного продукта.\n" +
                                                                             "Откройте и пополните счёт не менее чем на " +
                                                                             investProduct.getMinDeposit() + " " + investProduct.getCurrency());
            return "invest";
        } else {}
        return "invest";
    }



//    @GetMapping("/deposit_by_rate")
//    public String depositsByRate(Model model){
//        Iterable<InvestProduct> invProducts = investProdRepository.findAllByOrderByInterestRateDesc();
//        model.addAttribute("invProducts", invProducts);
//        return "deposits";
//    }


}
