package com.golikov.bank.validator;

import com.golikov.bank.entity.DepositAccount;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.math.BigDecimal;


// кастомный валидатор проверяет на null, отрицательное значение и значение превышающее баланс fr
@Component
@Getter
@Setter
@NoArgsConstructor
public class WithdrawBalanceValidator {

    private RedirectAttributes redirectAttributes;
    private DepositAccount depositAccount;
    private boolean hasErrors;


    public void validate(BigDecimal inputAmount) {
        this.hasErrors = false;
        if (inputAmount == null){
            redirectAttributes.addFlashAttribute("amount", "Поле не может быть пустым.");
            this.hasErrors = true;
            return;
        } else if (depositAccount.getDepositBalance().compareTo(inputAmount) < 0) {
            redirectAttributes.addFlashAttribute("amount", "Вы не можете перевести больше чем "+ depositAccount.getDepositBalance() + ".");
            this.hasErrors = true;
        } else if (inputAmount.compareTo(BigDecimal.valueOf(0)) < 0) {
            redirectAttributes.addFlashAttribute("amount", "Значение не может быть отрицательным.");
            this.hasErrors = true;
        }
    }

}
