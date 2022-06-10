package com.golikov.bank.controllers;


import com.golikov.bank.entity.Client;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class ClientAccountController {
    @RequestMapping("/account")
    public String login(@AuthenticationPrincipal Client client, Model model) {
        model.addAttribute(client);
        return "account";
    }
}
