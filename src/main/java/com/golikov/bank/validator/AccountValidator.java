package com.golikov.bank.validator;


import com.golikov.bank.entity.DepositAccount;
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

    public void validate(List<DepositAccount> accounts, DepositAccount account, RedirectAttributes redirectAttributes){
        Set<Long> accountsId= accounts.stream().map(DepositAccount::getId).collect(Collectors.toSet());
        if (account==null || !accountsId.contains(account.getId())){
            redirectAttributes.addFlashAttribute("accountError", "Выберите действительный счёт");
            this.hasErrors = true;
        }
    }


}
