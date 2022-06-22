package com.golikov.bank.validator;

import com.golikov.bank.entity.DepositAccount;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.math.BigDecimal;


// кастомный валидатор проверяет на null, отрицательное значение и значение превышающее баланс fr
public class TransferBalanceValidator {

    private RedirectAttributes redirectAttributes;
    private DepositAccount depositAccount;
    private boolean hasErrors;

    public TransferBalanceValidator(DepositAccount depositAccount, RedirectAttributes redirectAttributes) {
        this.redirectAttributes = redirectAttributes;
        this.depositAccount = depositAccount;
    }

    public void validate(BigDecimal inputAmount) {
        if (inputAmount == null){
            redirectAttributes.addFlashAttribute("amount", "Поле не может быть пустым.");
            this.hasErrors = true;
            return;
        } else if (depositAccount.getBalance().compareTo(inputAmount) < 0) {
            redirectAttributes.addFlashAttribute("amount", "Вы не можете перевести больше чем "+ depositAccount.getBalance() + ".");
            this.hasErrors = true;
        } else if (inputAmount.compareTo(BigDecimal.valueOf(0)) < 0) {
            redirectAttributes.addFlashAttribute("amount", "Значение не может быть отрицательным.");
            this.hasErrors = true;
        }
    }

    public boolean hasErrors() {
        return hasErrors;
    }
}
