package com.golikov.bank.validator;

import com.golikov.bank.entity.InvestProduct;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;



public class DepositDaysValidator {

    private boolean hasErrors;

    public void validate(Integer days, InvestProduct investProduct, RedirectAttributes redirectAttributes){
        if (days == null){
            redirectAttributes.addFlashAttribute("daysError", "Поле не может быть пустым или строкой");
            this.hasErrors = true;
            return;
        }
        else if (days < investProduct.getMinDepositTerm()){
            redirectAttributes.addFlashAttribute("daysError", "Минимальный срок вклада: " + investProduct.getMinDepositTerm());
            this.hasErrors = true;
        }
        else if (days > investProduct.getMaxDepositTerm()) {
            redirectAttributes.addFlashAttribute("daysError", "Максимальный срок вклада: " + investProduct.getMaxDepositTerm());
            this.hasErrors = true;
        }
    }

    public DepositDaysValidator() {
    }

    public boolean hasErrors() {
        return hasErrors;
    }
}
