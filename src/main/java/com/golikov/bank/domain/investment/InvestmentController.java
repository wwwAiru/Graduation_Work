package com.golikov.bank.domain.investment;

import com.golikov.bank.config.security.UserDetailsImpl;
import com.golikov.bank.domain.account.Account;
import com.golikov.bank.domain.account.AccountService;
import com.golikov.bank.domain.client.Client;
import com.golikov.bank.domain.investment.dto.InvestmentFormDto;
import com.golikov.bank.domain.investment.validator.InvestmentFormValidator;
import com.golikov.bank.domain.product.InvestProduct;
import lombok.AllArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.util.List;


// контроллер для инвестиции в продукт
@SuppressWarnings("unchecked")
@Controller
@AllArgsConstructor
public class InvestmentController {

    private InvestmentService investmentService;

    private AccountService accountService;

    private InvestmentFormValidator investmentFormValidator;

    @InitBinder
    private void initBinder(WebDataBinder binder) {
        binder.setValidator(investmentFormValidator);
    }

    // страница выбора параметров вклада
    @GetMapping("/product/invest/{id}")
    public String invest(Model model,
                         @PathVariable("id") InvestProduct investProduct,
                         @AuthenticationPrincipal UserDetailsImpl userDetails,
                         HttpSession session){
        Client client = userDetails.getClient();
        // список акаунтов клиента доступных для выбранного инвестиционного продукта
        List<Account> accounts = accountService.findValidDepoAccounts(client.getId(),
                                                                      investProduct.getCurrency(),
                                                                      investProduct.getMinDeposit());
        // если список пуст то сообщение об ошибке на странице invest
        if (accounts.isEmpty()){
            model.addAttribute("error",
                    "У вас нет инвестиционных счетов для данного продукта. " +
                               "Откройте и/или пополните счёт не менее чем на " +
                              investProduct.getMinDeposit() + " " + investProduct.getCurrency());
        }
        if (!model.containsAttribute("investment")) {
        model.addAttribute("investment", new InvestmentFormDto(accounts, investProduct));
        }
        // добавление списка акаунтов и инвест-продукт в сессию для защиты от подмены id из формы
        session.setAttribute("accounts", accounts);
        session.setAttribute("investProduct", investProduct);
        return "invest-product/invest";
    }

    // обработка параметров вклада и сохранение
    @PostMapping("/product/invest/save")
    public String saveInvest(@ModelAttribute("investment") @Valid InvestmentFormDto investment,
                             BindingResult bindingResult,
                             HttpSession session,
                             RedirectAttributes redirectAttributes) {
        investment.setInvestProduct((InvestProduct) session.getAttribute("investProduct"));
        investment.setAccounts((List<Account>) session.getAttribute("accounts"));
        // если есть ошибки валидации то вернуть страницу параметров вклада
        if (bindingResult.hasErrors()) {
            return "invest-product/invest";
        }
        session.removeAttribute("investProduct");
        session.removeAttribute("accounts");
        investmentService.makeInvest(investment);
        redirectAttributes.addFlashAttribute("success", "Вы инвестировали в наш продукт " + investment.getInvestProduct().getName());
        return "redirect:/deposits";
        }

}
