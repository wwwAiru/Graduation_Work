package com.golikov.bank.controllers;


import com.golikov.bank.entity.Client;
import com.golikov.bank.entity.ClientTransaction;
import com.golikov.bank.service.BankService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.time.LocalDateTime;


// Контроллер для личного кабинета клиента
@Controller
public class ClientAccountController {
    @Autowired
    BankService bankService;

    @RequestMapping("/account")
    public String login(@AuthenticationPrincipal Client client, Model model) {
        model.addAttribute("client",client);
        model.addAttribute("clientTransaction", new ClientTransaction());
        return "account";
    }

    @PostMapping("/up_balance")
    public String upBalance(@AuthenticationPrincipal Client client,
                            @ModelAttribute("clientTransaction") ClientTransaction clientTransaction){
        clientTransaction.setDate(LocalDateTime.now());
        clientTransaction.setClientId(client.getId());
        bankService.cardToBaLance(clientTransaction, client);
        return "redirect:/account";
    }
}
