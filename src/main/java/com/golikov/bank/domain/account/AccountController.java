package com.golikov.bank.domain.account;


import com.golikov.bank.domain.client.Client;
import com.golikov.bank.domain.investment.ClientInvestProd;
import com.golikov.bank.domain.account.transaction.ClientTransaction;
import com.golikov.bank.domain.client.ClientService;
import com.golikov.bank.domain.account.validator.ClientBalanceValidator;
import com.golikov.bank.domain.account.validator.TransferBalanceValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.math.BigDecimal;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;


// Контроллер для личного кабинета клиента
@Controller()
public class AccountController {
    @Autowired
    AccountService accountService;

    @Autowired
    AccountRepository accountRepository;

    @Autowired
    ClientBalanceValidator clientBalanceValidator;

    @Autowired
    ClientService clientService;


    @GetMapping("/account")
    public String account(@AuthenticationPrincipal Client client,
                          ClientTransaction clientTransaction,
                          Model model,
                          HttpSession session) {
        model.addAttribute("client", client);
        List<Account> clientAccounts = clientService.findClientAccounts(client.getId());
        // добавил в сессию id всех аккаунтов пользователя для последующих проверок
        Set<Long> clientAccountIds = clientAccounts.stream().map(Account::getId).collect(Collectors.toSet());
        session.setAttribute("clientAccountIds", clientAccountIds);
        model.addAttribute("clientAccounts", clientAccounts);
        if (!model.containsAttribute("transaction")) {
            model.addAttribute("transaction", new ClientTransaction());
        }
        model.addAttribute("newAccount", new Account());
        model.addAttribute("account", new Account());
        return "account/index";
    }

    // пополнения баланса с карты
    @PostMapping("/up-balance")
    public String upBalance(@AuthenticationPrincipal Client client,
                            @ModelAttribute("transaction") @Valid ClientTransaction clientTransaction,
                            BindingResult result,
                            @ModelAttribute Account account,
                            RedirectAttributes redirectAttributes){
        if (result.hasErrors()){
            redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.transaction", result);
            redirectAttributes.addFlashAttribute("transaction", clientTransaction);
            redirectAttributes.addFlashAttribute("upBalanceError", "upBalanceError");
            return "redirect:/account";
        }
        accountService.cardToBaLance(clientTransaction, client);
        return "redirect:/account";
    }

    // открытие ивест счёта
    @PostMapping("/account/create-account")
    public String createAccount(@AuthenticationPrincipal Client client,
                                @ModelAttribute("newAccount") Account account) {
        accountService.createAccount(client, account);
        return "redirect:/account";
    }

    // перевод средств на аккаунт
    @PostMapping("/account/up-balance")
    public String upAccountBalance(@AuthenticationPrincipal Client client,
                                      @ModelAttribute("account") Account account,
                                      @RequestParam(required = false) BigDecimal amount,
                                      HttpSession session,
                                      RedirectAttributes redirectAttributes) {
        //валидация переводимой суммы денег
        clientBalanceValidator.setClient(client);
        clientBalanceValidator.setRedirectAttributes(redirectAttributes);
        clientBalanceValidator.validate(amount);
        // если есть ошибки валидации то редирект с флэш сообщениями из ClientBalanceValidator
        if (clientBalanceValidator.isHasErrors()){
           return  "redirect:/account";
        }
        // защита от подмены id счёта для зачисления, проверяется содержит ли Set id из формы
        if (account == null || !((Set<Long>)session.getAttribute("clientAccountIds")).contains(account.getId())){
            redirectAttributes.addFlashAttribute("accountValidationError", "Попытка подмены id счёта отклонена.");
        } else accountService.upAccountBalance(client, account.getId(), amount);
        session.removeAttribute("clientAccountIds");
        return "redirect:/account";
    }

    // вывод денег с акаунта на общий счёт с пересчётом в рубли
    @PostMapping("/account/withdraw/{account}")
    public String withdraw(@AuthenticationPrincipal Client client,
                           @PathVariable(required = false) Account account,
                           @RequestParam(required = false) String amount,
                           RedirectAttributes redirectAttributes) {
        // защита от попадания строки в поле для цифр
        BigDecimal amountDecimal = null;
        if (amount.matches("\\d+|\\d+\\.\\d+")){
            amountDecimal = new BigDecimal(amount);
        } else {
            redirectAttributes.addFlashAttribute("validationError", "Допускаются только числовые положительные значения");
            return "redirect:/account";
        }
        // защита от подмены id счёта вывода денег
        if (account == null || account.getClient().getId() != client.getId()){
            redirectAttributes.addFlashAttribute("validationError", "Попытка подмены id счёта отклонена.");
            return "redirect:/account";
        }
        // валидация выводимой суммы денег
        TransferBalanceValidator balanceValidator = new TransferBalanceValidator(account, redirectAttributes);
        balanceValidator.validate(amountDecimal);
        // если есть ошибки валидации то редирект с флэш сообщениями из TransferBalanceValidator
        if (balanceValidator.hasErrors()){
            return "redirect:/account";
        }
        accountService.withdrawMoney(client, account, amountDecimal);
        redirectAttributes.addFlashAttribute("success", "Денежные средства успешно выведены");
        return "redirect:/account";
    }

    @PostMapping("/account/investment/close/{investment}")
    public String closeInvestment(@AuthenticationPrincipal Client client,
                                  @PathVariable(required = false) ClientInvestProd investment,
                                  RedirectAttributes redirectAttributes) {
        // защита от подмены id вклада
        List<Long> clientAccounts = client.getAccounts().stream().map(Account::getId).toList();
        if (investment == null || !clientAccounts.contains(investment.getAccount().getId())){
            redirectAttributes.addFlashAttribute("validationError", "Некоррекный идентификатор");
        } else {
            accountService.closeInvestment(investment);
            redirectAttributes.addFlashAttribute("success", investment.getInvestProduct().getName() + " закрыт");
        }
        return "redirect:/account";
    }

}
