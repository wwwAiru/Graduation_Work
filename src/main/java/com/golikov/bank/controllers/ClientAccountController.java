package com.golikov.bank.controllers;


import com.golikov.bank.entity.Client;
import com.golikov.bank.entity.ClientTransaction;
import com.golikov.bank.entity.DepositAccount;
import com.golikov.bank.repository.DepositAccountRepository;
import com.golikov.bank.service.BankService;
import com.golikov.bank.service.ClientService;
import com.golikov.bank.utils.ProxyDepositAccount;
import com.golikov.bank.validator.ClientBalanceValidator;
import com.golikov.bank.validator.WithdrawBalanceValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.math.BigDecimal;
import java.util.List;


// Контроллер для личного кабинета клиента
@Controller()
public class ClientAccountController {
    @Autowired
    BankService bankService;

    @Autowired
    DepositAccountRepository depositAccountRepository;

    @Autowired
    ClientBalanceValidator clientBalanceValidator;

    @Autowired
    WithdrawBalanceValidator withdrawBalanceValidator;

    @Autowired
    ClientService clientService;


    @GetMapping("/account")
    public String account(@AuthenticationPrincipal Client client, Model model) {
        model.addAttribute("client",client);
        List<DepositAccount> depoAccList = clientService.findClientAccounts(client.getId());
        model.addAttribute("depoAccs", depoAccList);
        model.addAttribute("clientTransaction", new ClientTransaction());
        model.addAttribute("newDepoAcc", new DepositAccount());
        model.addAttribute("proxyDepositAccount", new ProxyDepositAccount());
        return "account";
    }

    // пополнения баланса с карты
    @PostMapping("/account/up-balance")
    public String upBalance(@AuthenticationPrincipal Client client,
                            @ModelAttribute("clientTransaction") ClientTransaction clientTransaction){
        bankService.cardToBaLance(clientTransaction, client);
        return "redirect:/account";
    }

    // открытие ивест счёта
    @PostMapping("/account/create-deposit-account")
    public String createDepoAcc(@AuthenticationPrincipal Client client, @ModelAttribute("newDepoAcc") DepositAccount depositAccount) {
        bankService.createDepositAccount(client, depositAccount);
        return "redirect:/account";
    }

    // перевод средств на аккаунт
    @PostMapping("/account/up-deposit-balance")
    public String upDepositAccBalance(@AuthenticationPrincipal Client client,
                                      @ModelAttribute("proxyDepositAccount") ProxyDepositAccount proxyDepositAccount,
                                      BindingResult result,
                                      RedirectAttributes redirectAttributes) {
        //валидация переводимой суммы денег
        clientBalanceValidator.setClient(client);
        clientBalanceValidator.setRedirectAttributes(redirectAttributes);
        clientBalanceValidator.validate(proxyDepositAccount.getAmount(), result);
        // если есть ошибки валидации то редирект с флэш сообщениями из ClienBalanceValidator
        if (result.hasErrors()){
           return  "redirect:/account";
        }
        // защита от подмены id счёта для зачисления
        else if (proxyDepositAccount.getDepositAccount()==null || proxyDepositAccount.getDepositAccount().getClient().getId()!=client.getId()){
            redirectAttributes.addFlashAttribute("unaccesableClientId", "Попытка подмены id счёта отклонена.");
        } else bankService.upDepositAccounBalance(client, proxyDepositAccount);
        return "redirect:/account";
    }

    // вывод денег с акаунта на общий счёт с пересчётом в рубли
    @PostMapping("/account/withdraw/{depositAccount}")
    public String withdraw(@AuthenticationPrincipal Client client,
                           @PathVariable(required = false) DepositAccount depositAccount,
                           @RequestParam(required = false) BigDecimal amount,
                           RedirectAttributes redirectAttributes) {

        // защита от подмены id счёта вывода денег
        if (depositAccount == null || depositAccount.getClient().getId() != client.getId()){
            redirectAttributes.addFlashAttribute("unaccesableClientId", "Попытка подмены id счёта отклонена.");
            return  "redirect:/account";
        } else {
        // валидация выводимой суммы денег
        withdrawBalanceValidator.setDepositAccount(depositAccount);
        withdrawBalanceValidator.setRedirectAttributes(redirectAttributes);
        withdrawBalanceValidator.validate(amount);
        }

        // если есть ошибки валидации то редирект с флэш сообщениями из WithdrawBalanceValidator
        if (withdrawBalanceValidator.isHasErrors()){
        } else {
        }
        return "redirect:/account";
    }

}
