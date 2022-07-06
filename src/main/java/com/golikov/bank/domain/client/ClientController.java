package com.golikov.bank.domain.client;

import com.golikov.bank.domain.client.dto.NewClient;
import lombok.AllArgsConstructor;
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
@AllArgsConstructor
public class ClientController {

    private ClientService clientService;

    @RequestMapping("/login")
    public String login() {
        return "login";
    }

    @GetMapping("/registration")
    public String registration(Model model){
        model.addAttribute("client", new NewClient());
        return "registration";
    }

    @PostMapping("/registration")
    public String addClient(@ModelAttribute("client") @Valid NewClient client,
                            BindingResult result,
                            RedirectAttributes redirectAttributes,
                            Model model){
        Client clientFromDb = clientService.findByEmail(client.getEmail());
        if (result.hasErrors()) {
            return "registration";
        } else if (clientFromDb != null) {
            model.addAttribute("emailError", "Пользователь с почтой "+client.getEmail()+" уже существует");
            return "registration";
        }
        clientService.createClient(client);
        redirectAttributes.addFlashAttribute("success", "Вы успешно зарегистрировались! Войдите в свою учётную запись");
        return "redirect:/login";
    }

}
