package com.golikov.bank.controllers;


import com.golikov.bank.entity.Client;
import com.golikov.bank.entity.ClientTransaction;
import com.golikov.bank.entity.DepositAccount;
import com.golikov.bank.repository.DepositAccountRepository;
import com.golikov.bank.service.BankService;
import com.golikov.bank.utils.ProxyDepositAccount;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.time.LocalDateTime;
import java.util.List;


// Контроллер для личного кабинета клиента
@Controller
public class ClientAccountController {
    @Autowired
    BankService bankService;

    @Autowired
    DepositAccountRepository depositAccountRepository;


    @RequestMapping("/account")
    public String account(@AuthenticationPrincipal Client client, Model model) {
        model.addAttribute("client",client);
        List<DepositAccount> depoAccList = depositAccountRepository.findDepositAccountByClientId(client.getId());
        model.addAttribute("depoAccs", depoAccList);
        model.addAttribute("clientTransaction", new ClientTransaction());
        model.addAttribute("newDepoAcc", new DepositAccount());
        model.addAttribute("proxyDepositAccount", new ProxyDepositAccount());
        return "account";
    }

    @PostMapping("/up-balance")
    public String upBalance(@AuthenticationPrincipal Client client,
                            @ModelAttribute("clientTransaction") ClientTransaction clientTransaction){
        clientTransaction.setDate(LocalDateTime.now());
        clientTransaction.setClientId(client.getId());
        clientTransaction.setTransactionType("Пополнение баланса");
        bankService.cardToBaLance(clientTransaction, client);
        return "redirect:/account";
    }

    @PostMapping("/create-deposit-account")
    public String createDepoAcc(@AuthenticationPrincipal Client client, @ModelAttribute("newDepoAcc") DepositAccount depositAccount) {
        bankService.createDepositAccount(client, depositAccount);
        return "redirect:/account";
    }

    @PostMapping("/up-account-balance")
    public String upDepositAccBalance(@AuthenticationPrincipal Client client, @ModelAttribute("proxyDepositAccount") ProxyDepositAccount proxyDepositAccount) {
        System.out.println(proxyDepositAccount.getDepositAccount().getAccountNumber());
    return "redirect:/account";
    }


}
