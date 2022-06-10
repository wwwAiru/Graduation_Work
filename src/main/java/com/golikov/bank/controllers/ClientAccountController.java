package com.golikov.bank.controllers;


import com.golikov.bank.entity.Client;
import com.golikov.bank.entity.ClientTransaction;
import com.golikov.bank.service.BankService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.math.BigDecimal;
import java.time.LocalDateTime;


// Контроллер для личного кабинета клиента
@Controller
public class ClientAccountController {
    @Autowired
    BankService bankService;

    @RequestMapping("/account")
    public String login(@AuthenticationPrincipal Client client, Model model) {
        model.addAttribute(client);
        return "account";
    }

    @PostMapping("/up_balance")
    public String upBalance(@AuthenticationPrincipal Client client,
                            @RequestParam BigDecimal amount,
                            @RequestParam String ownerName,
                            @RequestParam String cardNumber){
        LocalDateTime localDateTime = LocalDateTime.now();
        ClientTransaction clientTransaction = new ClientTransaction(ownerName,
                                                                    cardNumber,
                                                                    amount,
                                                                    localDateTime, client.getId());
        bankService.cardToBaLance(clientTransaction, client);
        return "redirect:/account";
    }
}
