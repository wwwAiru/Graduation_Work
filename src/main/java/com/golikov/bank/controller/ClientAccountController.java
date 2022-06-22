package com.golikov.bank.controller;


import com.golikov.bank.entity.Client;
import com.golikov.bank.entity.ClientTransaction;
import com.golikov.bank.entity.DepositAccount;
import com.golikov.bank.repository.DepositAccountRepository;
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
public class ClientAccountController {
    @Autowired
    BankService bankService;

    @Autowired
    DepositAccountRepository depositAccountRepository;

    @Autowired
    ClientBalanceValidator clientBalanceValidator;


    @Autowired
    ClientService clientService;


    @GetMapping("/account")
    public String account(@AuthenticationPrincipal Client client, Model model, HttpSession session) {
        model.addAttribute("client",client);
        List<DepositAccount> depoAccList = clientService.findClientAccounts(client.getId());
        // добавил в сессию id всех аккаунтов пользователя для последующих проверок
        Set<Long> clientAccountIds = depoAccList.stream().map(acc -> acc.getId()).collect(Collectors.toSet());
        session.setAttribute("clientAccountIds", clientAccountIds);
        model.addAttribute("depoAccs", depoAccList);
        model.addAttribute("clientTransaction", new ClientTransaction());
        model.addAttribute("newDepoAcc", new DepositAccount());
        model.addAttribute("depositAccount", new DepositAccount());
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
    public String createDepoAcc(@AuthenticationPrincipal Client client,
                                @ModelAttribute("newDepoAcc") DepositAccount depositAccount) {
        bankService.createDepositAccount(client, depositAccount);
        return "redirect:/account";
    }

    // перевод средств на аккаунт
    @PostMapping("/account/up-deposit-balance")
    public String upDepositAccBalance(@AuthenticationPrincipal Client client,
                                      @ModelAttribute("depositAccount") DepositAccount depositAccount,
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
        if (depositAccount==null || !((Set<Long>)session.getAttribute("clientAccountIds")).contains(depositAccount.getId())){
            redirectAttributes.addFlashAttribute("validationErrror", "Попытка подмены id счёта отклонена.");
        } else bankService.upDepositAccountBalance(client, depositAccount.getId(), amount);
        session.removeAttribute("clientAccountIds");
        return "redirect:/account";
    }

    // вывод денег с акаунта на общий счёт с пересчётом в рубли
    @PostMapping("/account/withdraw/{depositAccount}")
    public String withdraw(@AuthenticationPrincipal Client client,
                           @PathVariable(required = false) DepositAccount depositAccount,
                           @RequestParam(required = false) String amount,
                           RedirectAttributes redirectAttributes) {
        // защита от попадания строки в поле для цифр
        BigDecimal amountDecimal = null;
        if (amount.matches("\\d+")){
            amountDecimal = new BigDecimal(amount);
        } else {
            redirectAttributes.addFlashAttribute("validationErrror", "Допускаются только числовые значения");
            return "redirect:/account";
        }
        // защита от подмены id счёта вывода денег
        if (depositAccount == null || depositAccount.getClient().getId() != client.getId()){
            redirectAttributes.addFlashAttribute("validationErrror", "Попытка подмены id счёта отклонена.");
            return  "redirect:/account";
        }
        // валидация выводимой суммы денег
        TransferBalanceValidator balanceValidator = new TransferBalanceValidator(depositAccount, redirectAttributes);
        balanceValidator.validate(amountDecimal);
        // если есть ошибки валидации то редирект с флэш сообщениями из WithdrawBalanceValidator
        if (balanceValidator.hasErrors()){
        } else {
            bankService.withdrawMoney(client, depositAccount, amountDecimal);
            redirectAttributes.addFlashAttribute("success", "Денежные средства успешно выведены");
        }
        return "redirect:/account";
    }

}
