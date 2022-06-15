package com.golikov.bank.validator;

import com.golikov.bank.entity.Client;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import java.math.BigDecimal;


public class ClienBalanceValidator implements Validator {

    private Client client;

    private BindingResult bindingResult;

    @Override
    public boolean supports(Class<?> clazz) {
        return Client.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        BigDecimal inputAmount = (BigDecimal) target;
        if (client.getBalance().compareTo(inputAmount) < 0) {
            errors.rejectValue("amount", "error.amount", "Значение не может превышать "+client.getBalance());
            }
        }

    public ClienBalanceValidator(Client client) {
        this.client = client;
    }
}
