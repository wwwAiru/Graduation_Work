package com.golikov.bank.domain.account.validator;

import com.golikov.bank.domain.client.Client;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.math.BigDecimal;



// кастомный валидатор проверяет на null, отрицательное значение и значение превышающее баланс клиента

@NoArgsConstructor
public class ClientBalanceValidator {

    private Client client;
    private RedirectAttributes redirectAttributes;
    private boolean hasErrors;

    private String flag;

    public boolean hasErrors() {
        return hasErrors;
    }

    // так как валидатор баланса можно применять в различных ситуациях чтобы кастомизировать ключ ошибки необходимо задать значение flag
    public ClientBalanceValidator(Client client, RedirectAttributes redirectAttributes, String flag) {
        this.client = client;
        this.redirectAttributes = redirectAttributes;
        this.flag = flag;
    }

    public void validate(BigDecimal inputAmount) {
        if (inputAmount == null){
            redirectAttributes.addFlashAttribute(flag + "AmountError", "Поле не может быть пустым.");
            this.hasErrors = true;
            return;
        } else if (client.getBalance().compareTo(inputAmount) < 0) {
            redirectAttributes.addFlashAttribute(flag + "AmountError", "Вы не можете перевести больше чем "+ client.getBalance() + ".");
            this.hasErrors = true;
        } else if (inputAmount.compareTo(BigDecimal.valueOf(0)) < 0) {
            redirectAttributes.addFlashAttribute("amountError", "Значение не может быть отрицательным.");
            this.hasErrors = true;
        }
    }

}
