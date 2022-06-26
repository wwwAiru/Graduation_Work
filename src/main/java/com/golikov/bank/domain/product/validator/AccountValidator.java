package com.golikov.bank.domain.product.validator;


import com.golikov.bank.domain.account.Account;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class AccountValidator {

    private boolean hasErrors;

    public AccountValidator() {
    }

    public boolean hasErrors() {
        return hasErrors;
    }

    public void validate(List<Account> accounts, Account account, RedirectAttributes redirectAttributes){
        Set<Long> accountsId= accounts.stream().map(Account::getId).collect(Collectors.toSet());
        if (account==null || !accountsId.contains(account.getId())){
            redirectAttributes.addFlashAttribute("accountError", "Выберите действительный счёт");
            this.hasErrors = true;
        }
    }


}
