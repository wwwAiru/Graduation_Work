package com.golikov.bank.controller;

import com.golikov.bank.entity.Client;
import com.golikov.bank.entity.ClientInvestProd;
import com.golikov.bank.entity.DepositAccount;
import com.golikov.bank.entity.InvestProduct;
import com.golikov.bank.service.BankService;
import com.golikov.bank.service.InvestProductServise;
import com.golikov.bank.validator.AccountValidator;
import com.golikov.bank.validator.DepositDaysValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Controller
public class InvProductController {
    @Autowired
    InvestProductServise investProductServise;

    @Autowired
    BankService bankService;


    @GetMapping("/deposits")
    public String deposits(Model model){
        Iterable<InvestProduct> invProducts = investProductServise.findAllByActive();
        model.addAttribute("invProducts", invProducts);
        model.addAttribute("currentPage", "deposits");
        return "invest-product/deposits";
    }

    // создание нового инвестиционного продукта
    @GetMapping("/product/create-inv-product")
    public String createInvProduct(Model model){
        model.addAttribute("investProduct", new InvestProduct());
        return "invest-product/create-inv-product";
    }

    // сохранение нового инвестиционного продукта
    @PostMapping("/add-inv-product")
    public String addInvProduct(@ModelAttribute InvestProduct investProduct,
                                RedirectAttributes redirectAttributes){
        redirectAttributes.addFlashAttribute("success","Инвестиционный продукт успешно добавлен");
        investProductServise.save(investProduct);

        return "redirect:/deposits";
    }

    // форма редактирование инвест продукта
    @GetMapping("/product/edit/{investProduct}")
    public String editProduct(@PathVariable InvestProduct investProduct, HttpSession session){
        session.setAttribute("securedId", investProduct.getId());
        return "invest-product/deposit-edit";
    }


    // сохранение изменений инвест продукта
    @PostMapping("/product/edit/save")
    public String saveEditedProduct(@ModelAttribute InvestProduct investProduct,
                                    HttpSession session,
                                    RedirectAttributes redirectAttributes){
        Long securedId = (Long) session.getAttribute("securedId");
        session.removeAttribute("securedId");
        if (securedId!=investProduct.getId()){
            redirectAttributes.addFlashAttribute("error", "Попытка подмены id отклонена");

        } else {
        investProductServise.save(investProduct);
        }
        return "redirect:/deposits";
    }

    // удаление инвест продукта(сделать неактивным)
    @GetMapping("/product/delete/{investProduct}")
    public String deleteProduct(@PathVariable InvestProduct investProduct){
        investProductServise.delete(investProduct);
        return "redirect:/deposits";
    }

    // инвестиция в продукт(открытие вклада)
    @GetMapping("/product/invest/{investProduct}")
    public String invest(Model model,
                         @PathVariable InvestProduct investProduct,
                         @AuthenticationPrincipal Client client,
                         HttpSession session){
        List<DepositAccount> accounts = bankService.findValidDepoAccounts(client.getId(),
                                                                          investProduct.getCurrency(),
                                                                          investProduct.getMinDeposit());
        if (accounts.isEmpty()){
            model.addAttribute("error","У вас нет инвестиционных счетов для данного продукта. " +
                                                                             "Откройте и/или пополните счёт не менее чем на " +
                                                                             investProduct.getMinDeposit() + " " + investProduct.getCurrency());
        }
        model.addAttribute("accounts", accounts);
        model.addAttribute("investProduct", investProduct);
        if (!model.containsAttribute("investment")) {
        model.addAttribute("investment", new ClientInvestProd());
        }
        session.setAttribute("accounts", accounts);
        session.setAttribute("investProduct", investProduct);
        return "invest-product/invest";
    }

    @PostMapping("/product/invest/save")
    public String saveInvest(@Valid @ModelAttribute("investment")  ClientInvestProd clientInvest,
                             BindingResult result,
                             HttpSession session,
                             RedirectAttributes redirectAttributes){
        List<DepositAccount> accounts = (List<DepositAccount>) session.getAttribute("accounts");
        InvestProduct investProduct = (InvestProduct) session.getAttribute("investProduct");
//         валидация дней, зачение не должно выходить за рамки утановленными инвест продуктом
        DepositDaysValidator depositDaysValidator = new DepositDaysValidator();
        depositDaysValidator.validate(clientInvest.getDays(), investProduct, redirectAttributes);
//         валидация выбранного аккаунта из select option списка формы
        AccountValidator accountValidator = new AccountValidator();
        accountValidator.validate(accounts, clientInvest.getDepositAccount(), redirectAttributes);
        if (result.hasErrors() | depositDaysValidator.hasErrors() | accountValidator.hasErrors()){
            redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.investment", result);
            redirectAttributes.addFlashAttribute("investment", clientInvest);
            redirectAttributes.addFlashAttribute("account", clientInvest.getDepositAccount());
            return "redirect:/product/invest/"+investProduct.getId();
        } else {
            bankService.makeInvest(clientInvest, clientInvest.getDepositAccount(), investProduct);
            redirectAttributes.addFlashAttribute("success", "Вы инвестировали в наш продукт " + investProduct.getName());
        }
        return "redirect:/deposits";
    }



}
