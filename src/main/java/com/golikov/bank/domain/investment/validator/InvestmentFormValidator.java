package com.golikov.bank.domain.investment.validator;

import com.golikov.bank.domain.investment.dto.InvestmentFormDto;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Component
@NoArgsConstructor
public class InvestmentFormValidator implements Validator {


    @Override
    public boolean supports(Class<?> clazz) {
        return InvestmentFormDto.class.isAssignableFrom(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {

        InvestmentFormDto investment = (InvestmentFormDto) target;
        // валидация наличия инвест-продукта (скрытое поле в форме)
        if (investment.getInvestProduct() == null) {
            errors.rejectValue("investProduct", "Investment.error", "Ошибка продукта");
        }

        // защита от подмены id аккаунта в форме
        if (investment.getChosenAccount() == null || !investment.getAccounts().contains(investment.getChosenAccount())) {
            errors.rejectValue("chosenAccount", "Investment.account.error", "Ошибка аккаунта");
        }

        // валидация суммы инвистиции
        if(investment.getAmount() == null || investment.getAmount().compareTo(investment.getInvestProduct().getMinDeposit())<0){
            errors.rejectValue("amount", "Investment.amountLower.error", "Вы не можете инвестировать меньше чем "+ investment.getInvestProduct().getMinDeposit() + " " + investment.getInvestProduct().getCurrency());
        } else if(investment.getAmount().compareTo(investment.getInvestProduct().getMaxDeposit())>0) {
            errors.rejectValue("amount", "Investment.amountBigger.error", "Вы не можете инвестировать больше чем "+ investment.getInvestProduct().getMaxDeposit() + " " + investment.getInvestProduct().getCurrency());
        }  else if (investment.getAmount().compareTo(investment.getChosenAccount().getBalance()) > 0) {
            errors.rejectValue("amount", "Investment.error", "Вы не можете инвестировать больше чем " + investment.getChosenAccount().getBalance() + " " + investment.getChosenAccount().getCurrency());
        }

        // валидация количества дней для инвестиции
        if(investment.getDays() == null || investment.getDays() < investment.getInvestProduct().getMinDepositTerm()) {
            errors.rejectValue("days", "Investment.days.error", "Срок вклада не может быть меньше чем " + investment.getInvestProduct().getMinDepositTerm());
        } else if(investment.getDays() > investment.getInvestProduct().getMaxDepositTerm()){
            errors.rejectValue("days", "Investment.days.error", "Срок вклада не может быть больше чем "+ investment.getInvestProduct().getMaxDepositTerm());
        }

    }
}
