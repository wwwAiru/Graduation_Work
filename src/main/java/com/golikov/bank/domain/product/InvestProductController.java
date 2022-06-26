package com.golikov.bank.domain.product;

import com.golikov.bank.domain.account.Account;
import com.golikov.bank.domain.client.Client;
import com.golikov.bank.domain.investment.ClientInvestProd;
import com.golikov.bank.domain.account.AccountService;
import com.golikov.bank.domain.product.validator.AccountValidator;
import com.golikov.bank.domain.product.validator.DepositDaysValidator;
import com.golikov.bank.domain.investment.validator.InvestmentValidator;
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

@Controller
public class InvestProductController {
    @Autowired
    InvestProductServise investProductServise;

    @Autowired
    AccountService accountService;


    @GetMapping("/deposits")
    public String deposits(Model model){
        Iterable<InvestProduct> invProducts = investProductServise.findAllByActive();
        model.addAttribute("invProducts", invProducts);
        model.addAttribute("currentPage", "deposits");
        return "invest-product/deposits";
    }

    // создание нового инвестиционного продукта
    @GetMapping("/product/add-inv-product")
    public String createInvProduct(Model model){
        model.addAttribute("investProduct", new InvestProduct());
        return "invest-product/add-inv-product";
    }

    // сохранение нового инвестиционного продукта
    @PostMapping("/add-inv-product")
    public String addInvProduct(@ModelAttribute("investProduct") @Valid InvestProduct investProduct,
                                BindingResult result,
                                RedirectAttributes redirectAttributes){
        if (result.hasErrors()){
            return "invest-product/add-inv-product";
        } else {
            redirectAttributes.addFlashAttribute("success", "Инвестиционный продукт успешно добавлен");
            investProductServise.save(investProduct);
        }
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
        List<Account> accounts = accountService.findValidDepoAccounts(client.getId(),
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
    public String saveInvest(@ModelAttribute("investment") @Valid ClientInvestProd clientInvest,
                             BindingResult result,
                             HttpSession session,
                             RedirectAttributes redirectAttributes) {
        List<Account> accounts = (List<Account>) session.getAttribute("accounts");
        InvestProduct investProduct = (InvestProduct) session.getAttribute("investProduct");
//         валидация дней, значение не должно выходить за рамки утановленными инвест продуктом
        DepositDaysValidator depositDaysValidator = new DepositDaysValidator();
        depositDaysValidator.validate(clientInvest.getDays(), investProduct, redirectAttributes);
//         валидация выбранного аккаунта из select option списка формы
        AccountValidator accountValidator = new AccountValidator();
        accountValidator.validate(accounts, clientInvest.getAccount(), redirectAttributes);
//         валидация суммы, значение не должно выходить за рамки утановленными инвест продуктом
        InvestmentValidator investmentValidator = new InvestmentValidator();
        investmentValidator.validate(clientInvest.getBalance(), investProduct, redirectAttributes);
        if (result.hasErrors() |
                depositDaysValidator.hasErrors() |
                                accountValidator.hasErrors() |
                                                investmentValidator.hasErrors()){
            redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.investment", result);
            redirectAttributes.addFlashAttribute("investment", clientInvest);
            redirectAttributes.addFlashAttribute("account", clientInvest.getAccount());
            return "redirect:/product/invest/"+investProduct.getId();
        } else {
            accountService.makeInvest(clientInvest, clientInvest.getAccount(), investProduct);
            redirectAttributes.addFlashAttribute("success", "Вы инвестировали в наш продукт " + investProduct.getName());
        }
        return "redirect:/deposits";
    }



}
