package com.golikov.bank.domain.admin;

import com.golikov.bank.domain.client.Client;
import com.golikov.bank.domain.client.Role;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/admin")
@PreAuthorize("hasAuthority('HEAD_MANAGER')")
public class AdminController {

    @Autowired
    AdminService adminService;

    @GetMapping("/")
    public String adminPanel(Model model){
        return "admin-panel/admin";
    }

    @GetMapping("/clients")
    public String clients(Model model){
        model.addAttribute("clients", adminService.findAllClients());
        return "admin-panel/clients";
    }

    @GetMapping("/client/edit/{client}")
    public String clientsEdit(@PathVariable Client client, Model model){
        model.addAttribute("client", client);
        model.addAttribute("roles", Role.values());
        return "admin-panel/client-edit";
    }

    @PostMapping("/client-save")
    public String clientSave(@ModelAttribute Client client){
        adminService.save(client);
        return "redirect:/admin/clients";
    }

    @GetMapping("/disable-products")
    public String productsEdit(Model model){
        return "admin/invest-products-edit";
    }
}
