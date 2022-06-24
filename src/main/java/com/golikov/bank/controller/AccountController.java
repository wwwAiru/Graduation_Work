package com.golikov.bank.controller;


import com.golikov.bank.entity.Client;
import com.golikov.bank.entity.ClientTransaction;
import com.golikov.bank.entity.Account;
import com.golikov.bank.repository.AccountRepository;
import com.golikov.bank.service.BankService;
import com.golikov.bank.service.ClientService;
import com.golikov.bank.validator.ClientBalanceValidator;
import com.golikov.bank.validator.TransferBalanceValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpSession;
import java.math.BigDecimal;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;


// Контроллер для личного кабинета клиента
@Controller()
public class AccountController {
    @Autowired
    BankService bankService;

    @Autowired
    AccountRepository accountRepository;

    @Autowired
    ClientBalanceValidator clientBalanceValidator;


    @Autowired
    ClientService clientService;


    @GetMapping("/account")
    public String account(@AuthenticationPrincipal Client client, Model model, HttpSession session) {
        model.addAttribute("client",client);
        List<Account> depoAccList = clientService.findClientAccounts(client.getId());
        // добавил в сессию id всех аккаунтов пользователя для последующих проверок
        Set<Long> clientAccountIds = depoAccList.stream().map(acc -> acc.getId()).collect(Collectors.toSet());
        session.setAttribute("clientAccountIds", clientAccountIds);
        model.addAttribute("depoAccs", depoAccList);
        model.addAttribute("clientTransaction", new ClientTransaction());
        model.addAttribute("newDepoAcc", new Account());
        model.addAttribute("account", new Account());
        return "account";
    }

    // пополнения баланса с карты
    @PostMapping("/up-balance")
    public String upBalance(@AuthenticationPrincipal Client client,
                            @ModelAttribute("clientTransaction") ClientTransaction clientTransaction){
        bankService.cardToBaLance(clientTransaction, client);
        return "redirect:/account";
    }

    // открытие ивест счёта
    @PostMapping("/account/create-account")
    public String createDepoAcc(@AuthenticationPrincipal Client client,
                                @ModelAttribute("newDepoAcc") Account account) {
        bankService.createAccount(client, account);
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
        if (account==null || !((Set<Long>)session.getAttribute("clientAccountIds")).contains(account.getId())){
            redirectAttributes.addFlashAttribute("validationErrror", "Попытка подмены id счёта отклонена.");
        } else bankService.upAccountBalance(client, account.getId(), amount);
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
        if (amount.matches("\\d+\\.\\d+")){
            amountDecimal = new BigDecimal(amount);
        } else {
            redirectAttributes.addFlashAttribute("validationErrror", "Допускаются только числовые положительные значения");
            return "redirect:/account";
        }
        // защита от подмены id счёта вывода денег
        if (account == null || account.getClient().getId() != client.getId()){
            redirectAttributes.addFlashAttribute("validationErrror", "Попытка подмены id счёта отклонена.");
            return  "redirect:/account";
        }
        // валидация выводимой суммы денег
        TransferBalanceValidator balanceValidator = new TransferBalanceValidator(account, redirectAttributes);
        balanceValidator.validate(amountDecimal);
        // если есть ошибки валидации то редирект с флэш сообщениями из WithdrawBalanceValidator
        if (balanceValidator.hasErrors()){
        } else {
            bankService.withdrawMoney(client, account, amountDecimal);
            redirectAttributes.addFlashAttribute("success", "Денежные средства успешно выведены");
        }
        return "redirect:/account";
    }

}
