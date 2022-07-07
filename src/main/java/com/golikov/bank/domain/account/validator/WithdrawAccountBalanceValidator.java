package com.golikov.bank.domain.account.validator;

import com.golikov.bank.config.security.UserDetailsImpl;
import com.golikov.bank.domain.account.Account;
import com.golikov.bank.domain.account.AccountService;
import com.golikov.bank.domain.account.dto.WithdrawAccountBalanceFormDto;
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
public class WithdrawAccountBalanceValidator implements Validator {
    @Autowired
    private ClientService clientService;

    @Override
    public boolean supports(Class<?> clazz) {
        return WithdrawAccountBalanceFormDto.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {

        WithdrawAccountBalanceFormDto accountForm = (WithdrawAccountBalanceFormDto) target;

        // получаем пользователя
        UserDetailsImpl userDetails = (UserDetailsImpl) SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getPrincipal();
        //получаем аккаунт по id
        Account account = accountForm.getAccount();
        List<Account> accounts = clientService.findClientAccounts(userDetails.getClient().getId());
        // получаем баланс аккаунта
        BigDecimal balance = account.getBalance();

        if (accountForm.getAccount() == null || !accounts.contains(account)) {
            errors.rejectValue("account", "accountWithdrawForm.account.error", "ошибка аккаунта");
        }

        if (accountForm.getAmount() == null || accountForm.getAmount().compareTo(BigDecimal.ZERO) <0) {
            errors.rejectValue("amount", "accountForm.amount.error", "Поле не может быть пустым, или принимать отрицательные значения");
        } else if ( balance.compareTo(BigDecimal.ZERO) == 0) {
            errors.rejectValue("amount", "account.balance.error", "На балансе счёта 0");
        } else if (accountForm.getAmount().compareTo(account.getBalance()) > 0)  {
            errors.rejectValue("amount", "accountForm.amount.overBalanceError", "Вы не можете перевести больше чем "+ account.getBalance());
        }

    }
}
