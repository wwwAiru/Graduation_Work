package com.golikov.bank.domain.client.account.validator;

import com.golikov.bank.domain.client.Client;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.math.BigDecimal;



// кастомный валидатор проверяет на null, отрицательное значение и значение превышающее баланс клиента
@Component
@Getter
@Setter
@NoArgsConstructor
public class ClientBalanceValidator {

    private Client client;
    private RedirectAttributes redirectAttributes;
    private boolean hasErrors;

    public void validate(Object target) {
        this.hasErrors = false;
        BigDecimal inputAmount = (BigDecimal) target;
        if (inputAmount == null){
            redirectAttributes.addFlashAttribute("amountError", "Поле не может быть пустым.");
            this.hasErrors = true;
            return;
        } else if (client.getBalance().compareTo(inputAmount) < 0) {
            redirectAttributes.addFlashAttribute("amountError", "Вы не можете перевести больше чем "+ client.getBalance() + ".");
            this.hasErrors = true;
        } else if (inputAmount.compareTo(BigDecimal.valueOf(0)) < 0) {
            redirectAttributes.addFlashAttribute("amountError", "Значение не может быть отрицательным.");
            this.hasErrors = true;
        }
    }

}
