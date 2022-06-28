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


    @GetMapping("/clients/investments")
    public String clientsInvestments(Model model,
                                     @RequestParam(required = false) String keyword){
        model.addAttribute("investments", adminService.findAllClientsInvestments(keyword));
        return "admin-panel/clients-investments";
    }

    // устанавливает даты окончания всех вкладов -11 лет, для проверки работоспособности расчёта и начисления процентов на счета клиентов
    @GetMapping("/clients/investments/profitable-mode-enable")
    public String profitableModeEnable(){
        adminService.enableProfitableMode();
        return "redirect:/admin/clients/investments";
    }

    // устанавливает даты окончания всех вкладов +11 лет, для того чтобы вернуть даты окончания вкладов к прежним значениям
    @GetMapping("/clients/investments/profitable-mode-disable")
    public String profitableModeDisable(){
        adminService.disableProfitableMode();
        return "redirect:/admin/clients/investments";
    }


    @GetMapping("/product/edit/{id}")
    public String productEdit(@ModelAttribute("id") InvestProduct investProduct,
                              HttpSession session){
        //добавил в сессию значение чтобы по его наличию делать редирект обратно в GetMapping /products/disabled
        session.setAttribute("admin-edit", "1");
        return "redirect:/product/edit/"+investProduct.getId();
    }


    @GetMapping("/product/delete/{id}")
    public String productDelete(@ModelAttribute("id") InvestProduct investProduct){
        adminService.delete(investProduct);
        return "redirect:/admin/products/disabled";
    }


    @GetMapping("/products/disabled")
    public String allDisabledProducts(Model model, HttpSession session){
        model.addAttribute("investProducts", adminService.findAllDisabledProducts());
        return "admin-panel/disabled-invest-products";
    }
}
