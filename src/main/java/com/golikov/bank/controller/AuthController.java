package com.golikov.bank.controller;

import com.golikov.bank.entity.Client;
import com.golikov.bank.entity.ClientDTO;
import com.golikov.bank.service.ClientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;

@Controller
public class AuthController {

    @Autowired
    ClientService clientService;

    @RequestMapping("/login")
    public String login() {
        return "login";
    }

    @GetMapping("/registration")
    public String registration(Model model){
        model.addAttribute("client", new ClientDTO());
        return "registration";
    }

    @PostMapping("/registration")
    public String addClient(@ModelAttribute @Valid ClientDTO client,
                            BindingResult result,
                            RedirectAttributes redirectAttributes,
                            Model model){
        Client clientFromDb = clientService.findByEmail(client.getEmail());
        if (result.hasErrors() | clientFromDb != null) {
            model.addAttribute("emailError", "Пользователь с почтой "+client.getEmail()+" уже существует");
            return "registration";
        }
        clientService.createClient(client);
        redirectAttributes.addFlashAttribute("success", "Вы успешно зарегестрировались! Войдите в свою учётную запись");
        return "redirect:/login";
    }

}
