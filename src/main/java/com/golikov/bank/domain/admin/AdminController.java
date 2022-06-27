package com.golikov.bank.domain.admin;

import com.golikov.bank.domain.client.Client;
import com.golikov.bank.domain.client.Role;
import com.golikov.bank.domain.product.InvestProduct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;

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

    @GetMapping("/product/delete/{id}")
    public String productDelete(@ModelAttribute("id") InvestProduct investProduct){
        adminService.delete(investProduct);
        return "redirect:/admin/products/disabled";
    }

    @GetMapping("/products/disabled")
    public String productsEdit(Model model, HttpSession session){
        model.addAttribute("investProducts", adminService.findAllDisabledProducts());
        //добавил в сессию значение чтобы по его наличию делать редирект обратно в этот GetMapping
        session.setAttribute("admin-edit", "1");
        return "admin-panel/disabled-invest-products";
    }
}
