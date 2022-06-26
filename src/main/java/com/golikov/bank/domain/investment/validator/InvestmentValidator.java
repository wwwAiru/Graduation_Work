package com.golikov.bank.domain.investment.validator;

import com.golikov.bank.domain.product.InvestProduct;
import lombok.NoArgsConstructor;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.math.BigDecimal;


@NoArgsConstructor
public class InvestmentValidator {

    private boolean hasErrors;

    public void validate(BigDecimal inputAmount, InvestProduct investProduct, RedirectAttributes redirectAttributes){
        if (inputAmount == null){
            redirectAttributes.addFlashAttribute("amountError", "Поле не может быть пустым.");
            this.hasErrors = true;
            return;
        } else if (inputAmount.compareTo(investProduct.getMinDeposit()) < 0) {
            redirectAttributes.addFlashAttribute("amountError", "Вы не можете инвестировать меньше чем "+ investProduct.getMinDeposit() + " " + investProduct.getCurrency());
            this.hasErrors = true;
        } else if (inputAmount.compareTo(investProduct.getMaxDeposit()) > 0) {
            redirectAttributes.addFlashAttribute("amountError", "Вы не можете инвестировать больше чем "+ investProduct.getMaxDeposit() + " " + investProduct.getCurrency());
            this.hasErrors = true;
        }
    }

    public boolean hasErrors() {
        return hasErrors;
    }
}
