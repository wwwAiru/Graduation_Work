package com.golikov.bank.domain.product;

import lombok.AllArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;

@Controller
@AllArgsConstructor
public class InvestProductController {

    private InvestProductServise investProductServise;

    @GetMapping("/deposits")
    public String deposits(Model model){
        Iterable<InvestProduct> invProducts = investProductServise.findAllByActive();
        model.addAttribute("invProducts", invProducts);
        model.addAttribute("currentPage", "deposits");
        return "invest-product/deposits";
    }

    // создание нового инвестиционного продукта
    @GetMapping("/product/add-inv-product")
    @PreAuthorize("hasAuthority('HEAD_MANAGER')")
    public String createInvProduct(Model model){
        model.addAttribute("investProduct", new InvestProduct());
        return "invest-product/add-inv-product";
    }

    // сохранение нового инвестиционного продукта
    @PostMapping("/save-inv-product")
    @PreAuthorize("hasAuthority('HEAD_MANAGER')")
    public String addInvProduct(@ModelAttribute("investProduct") @Valid InvestProduct investProduct,
                                BindingResult result,
                                RedirectAttributes redirectAttributes){
        if (result.hasErrors()){
            return "invest-product/add-inv-product";
        } else {
            redirectAttributes.addFlashAttribute("success", "Инвестиционный продукт успешно добавлен");
            investProductServise.save(investProduct);
        }
        return "redirect:/deposits";
    }

    // форма редактирование инвест продукта
    @GetMapping("/product/edit/{id}")
    @PreAuthorize("hasAuthority('HEAD_MANAGER')")
    public String editProduct(@PathVariable("id") InvestProduct investProduct,
                              HttpSession session,
                              Model model){
        if (!model.containsAttribute("investProductEdit")) {
            model.addAttribute("investProductEdit", investProduct);
        }
        session.setAttribute("securedId", investProduct.getId());
        return "invest-product/deposit-edit";
    }


    // сохранение изменений инвест продукта
    @PostMapping("/product/edit/save")
    @PreAuthorize("hasAuthority('HEAD_MANAGER')")
    public String saveEditedProduct(@ModelAttribute("investProductEdit") @Valid InvestProduct investProduct,
                                    BindingResult result,
                                    HttpSession session,
                                    RedirectAttributes redirectAttributes){
        Long securedId = (Long) session.getAttribute("securedId");
        session.removeAttribute("securedId");
        // если есть ошибки валидации то редирект в форму
        if (securedId != investProduct.getId() | result.hasErrors()){
            redirectAttributes.addFlashAttribute("error", "Попытка подмены id отклонена");
            redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.investProductEdit", result);
            redirectAttributes.addFlashAttribute("investProductEdit", investProduct);
            return "redirect:/product/edit/"+investProduct.getId();
        }
        investProductServise.save(investProduct);
        String adminEdit = (String) session.getAttribute("admin-edit");
        //  проверка: если редактирование пришло из админки, то вернуть в админку
        if (adminEdit!=null) {
            session.removeAttribute("admin-edit");
            return "redirect:/admin/products/disabled";
        }
        return "redirect:/deposits";
    }

    // удаление инвест продукта(сделать неактивным)
    @GetMapping("/product/delete/{id}")
    @PreAuthorize("hasAuthority('HEAD_MANAGER')")
    public String deleteProduct(@PathVariable("id") InvestProduct investProduct){
        investProductServise.delete(investProduct);
        return "redirect:/deposits";
    }

}
