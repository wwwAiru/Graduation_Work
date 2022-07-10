package com.golikov.bank.domain.account.validator;

import com.golikov.bank.config.security.UserDetailsImpl;
import com.golikov.bank.domain.account.Account;
import com.golikov.bank.domain.account.dto.UpAccountBalanceFormDto;
import com.golikov.bank.domain.client.ClientService;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import java.math.BigDecimal;
import java.util.List;

@Component
@NoArgsConstructor
public class UpAccountBalanceValidator implements Validator {

    @Autowired
    private ClientService clientService;

    @Override
    public boolean supports(Class<?> clazz) {
        return UpAccountBalanceFormDto.class.equals(clazz);
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
        UpAccountBalanceFormDto accountForm = (UpAccountBalanceFormDto) target;
        // получаем аккаунты пользователя
        List<Account> accounts = clientService.findClientAccounts(userDetails.getClient().getId());
        // если в списке аккаунтов нет выбранного аккаунта или аккаунт null то выдать ошибку аккаунта
        if (accountForm.getChosenAccount() == null || !accounts.contains(accountForm.getChosenAccount())) {
            errors.rejectValue("chosenAccount", "accountForm.account.error", "ошибка аккаунта");
        }
        if (accountForm.getAmount() == null || accountForm.getAmount().compareTo(BigDecimal.ZERO) <0) {
            errors.rejectValue("amount", "accountForm.amount.error", "Поле не может быть пустым, или принимать отрицательные значения");
        } else if ( clientBalance.compareTo(BigDecimal.ZERO) == 0) {
            errors.rejectValue("amount", "client.balance.error", "На Вашем балансе 0");
        } else if (accountForm.getAmount().compareTo(clientBalance) > 0)  {
            errors.rejectValue("amount", "accountForm.amount.overBalanceError", "Вы не можете перевести больше чем "+ clientBalance);
        }

    }
}
