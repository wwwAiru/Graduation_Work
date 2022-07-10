package com.golikov.bank.domain.transaction.validator;

import com.golikov.bank.config.security.UserDetailsImpl;
import com.golikov.bank.domain.transaction.ClientTransaction;
import lombok.NoArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import java.math.BigDecimal;

@Component
@NoArgsConstructor
public class ClientTransactionValidator implements Validator {

    @Override
    public boolean supports(Class<?> clazz) {
        return ClientTransaction.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        // получаем пользователя
        UserDetailsImpl userDetails = (UserDetailsImpl) SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getPrincipal();
        // получаем баланс пользователя
        BigDecimal clientBalance = userDetails.getClient().getBalance();

        ClientTransaction transaction = (ClientTransaction) target;


        if (transaction.getAmount() == null || transaction.getAmount().compareTo(BigDecimal.ZERO) <0) {
            errors.rejectValue("amount", "transaction.amount.error", "Поле не может быть пустым, или принимать отрицательные значения");
        } else if ( clientBalance.compareTo(BigDecimal.ZERO) == 0) {
            errors.rejectValue("amount", "client.balance.error", "На Вашем балансе 0");
        } else if (transaction.getAmount().compareTo(clientBalance) > 0)  {
            errors.rejectValue("amount", "transaction.amount.overBalanceError", "Вы не можете вывести больше чем "+ clientBalance);
        }

    }
}
