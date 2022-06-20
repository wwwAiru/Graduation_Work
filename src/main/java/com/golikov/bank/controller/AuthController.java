package com.golikov.bank.controller;

import com.golikov.bank.entity.Client;
import com.golikov.bank.entity.Role;
import com.golikov.bank.repository.ClientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import java.util.Collections;


@Controller
public class AuthController {
    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    ClientRepository clientRepository;

    @RequestMapping("/login")
    public String login() {
        return "login";
    }

    @GetMapping("/registration")
    public String registration(){
        return "registration";
    }

    @PostMapping("/registration")
    public String addClient(Client client,
                            @RequestParam String firstName,
                            @RequestParam String lastName,
                            @RequestParam String middleName,
                            @RequestParam String email,
                            @RequestParam String password,
                            Model model){
        Client clientFromDb = clientRepository.findByEmail(client.getEmail());
        if (clientFromDb != null) {
            model.addAttribute("message", "Пользователь с почтой "+client.getEmail()+" уже существует");
            return "registration";
        }
        client.setActive(true);
        client.setRoles(Collections.singleton(Role.USER));
        client.setEmail(email);
        client.setFirstName(firstName);
        client.setLastName(lastName);
        client.setMiddleName(middleName);
        client.setEmail(email);
        client.setPassword(passwordEncoder.encode(password));
        clientRepository.save(client);
        return "redirect:/login";
    }

}
