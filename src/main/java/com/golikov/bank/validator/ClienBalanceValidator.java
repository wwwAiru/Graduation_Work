package com.golikov.bank.validator;

import com.golikov.bank.entity.Client;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ObjectError;
import org.springframework.validation.Validator;

import java.math.BigDecimal;



// кастомный валидатор проверяет на null, отрицательное значение и значение превышающее баланс клиента
@Component
@Getter
@Setter
@NoArgsConstructor
public class ClienBalanceValidator implements Validator {

    private Client client;

    @Override
    public boolean supports(Class<?> clazz) {
        return Client.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        BigDecimal inputAmount = (BigDecimal) target;
        if (inputAmount == null){
            errors.rejectValue("amount", "Поле не может быть пустым.");
            return;
        } else if (client.getBalance().compareTo(inputAmount) < 0) {
            errors.reject("amount", "Вы не можете перевести больше чем "+inputAmount+".");
        } else if (inputAmount.compareTo(BigDecimal.valueOf(0)) < 0) {
            errors.reject("amount", "Значение не может быть отрицательным.");
            }
        }


}
