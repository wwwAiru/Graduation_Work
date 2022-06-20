package com.golikov.bank.controller;

import com.golikov.bank.entity.Client;
import com.golikov.bank.entity.DepositAccount;
import com.golikov.bank.entity.InvestProduct;
import com.golikov.bank.repository.InvestProdRepository;
import com.golikov.bank.service.BankService;
import com.golikov.bank.service.InvestProductServise;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpSession;
import java.util.List;

@Controller
public class ProductController {
    @Autowired
    private InvestProdRepository investProdRepository;

    @Autowired
    InvestProductServise investProductServise;

    @Autowired
    BankService bankService;


    @GetMapping("/deposits")
    public String deposits(Model model){
        Iterable<InvestProduct> invProducts = investProdRepository.findAll();
        model.addAttribute("invProducts", invProducts);
        model.addAttribute("currentPage", "deposits");
        model.addAttribute("investProduct", new InvestProduct());
        return "/deposits";
    }

    @PostMapping("/add-inv-product")
    public String addInvProduct(@ModelAttribute InvestProduct investProduct){
        investProductServise.save(investProduct);
        return "redirect:/deposits";
    }

    // форма редактирование инвест продукта
    @GetMapping("/product/edit/{investProduct}")
    public String editProduct(@PathVariable InvestProduct investProduct, HttpSession session){
        session.setAttribute("securedId", investProduct.getId());
        return "deposit-edit";
    }


    //сохранение изменений инвест продукта
    @PostMapping("/product/edit/save")
    public String saveEditedProduct(@ModelAttribute InvestProduct investProduct,
                                    HttpSession session,
                                    RedirectAttributes redirectAttributes){
        Long securedId = (Long) session.getAttribute("securedId");
        if (securedId!=investProduct.getId()){
            redirectAttributes.addFlashAttribute("error", "Попытка подмены id отклонена");

        } else {
        investProductServise.save(investProduct);
        session.removeAttribute("securedId");}
        return "redirect:/deposits";
    }

    //удаление инвест продукта(сделать неактивным)
    @GetMapping("/product/delete/{investProduct}")
    public String deleteProduct(@PathVariable InvestProduct investProduct){
        investProductServise.delete(investProduct);
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
        } else {

        }
        return "invest";
    }



//    @GetMapping("/deposit_by_rate")
//    public String depositsByRate(Model model){
//        Iterable<InvestProduct> invProducts = investProdRepository.findAllByOrderByInterestRateDesc();
//        model.addAttribute("invProducts", invProducts);
//        return "deposits";
//    }


}
